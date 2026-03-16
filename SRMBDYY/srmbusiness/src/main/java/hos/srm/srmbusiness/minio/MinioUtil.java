package hos.srm.srmbusiness.minio;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import hos.srm.srmbusiness.exception.ErrorException;
import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.utils.FileUploader;
import hos.srm.srmbusiness.utils.ReponseResult;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @author xiaobo
 * @date 2023/5/21
 */
@Component
public class MinioUtil {


    private static MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        MinioUtil.minioClient = minioClient;
    }
    public static boolean uploadPartFile(String tmpBucket,String filName, MultipartFile file,int index) {
        InputStream inputStream = null;
        try {
            inputStream=file.getInputStream();
            // 检查bucket是否存在，不存在则创建
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(tmpBucket).build());
            //System.out.println(index+"存在："+isExist+","+tmpBucket);
            if(index>1&&!isExist){
                return false;
            }
            if (!isExist) {
                System.out.println(isExist+","+tmpBucket);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(tmpBucket).build());
            }

            //System.out.println(bucketName);
            // 执行上传操作
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(tmpBucket)
                            .object(filName)
                            .stream(inputStream, inputStream.available(), -1)
                            .build()
            );

            //System.out.println("文件 '" + filName + "' 已成功上传至存储桶 '" + tmpBucket + "'");
            return true;
        } catch (Exception e) {
            System.err.println("上传失败: " + e.getMessage());
            return false;
        } finally {
            IoUtil.close(inputStream);
        }
    }
    /**
     * description: 文件上传
     *
     * @param bucketName 桶名称
     * @param file       文件
     * @param fileName   文件名
     * @author bo
     * @date 2023/5/21 13:06
     */
    public static boolean uploadSingle(String bucketName, MultipartFile file, String fileName) {
        System.out.println(bucketName+","+fileName);
        InputStream inputStream = null;
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            System.out.println("single");
            if (!isExist) {
                //System.out.println(111);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .build());

            return true;
        } catch (Exception e) {
            System.out.println("错误："+e.getMessage());
            return false;
        }finally {
            IoUtil.close(inputStream);
        }
    }
    public static ReponseResult upload(FileDto fileDto) {
        String res="";
        String realFileName=fileDto.getRealFilename();
        //System.out.println(realFileName);
        InputStream inputStream = null;
        MultipartFile file = fileDto.getMultipartFile();
        try {

            String bucketName=MinioConfig.getBucketName();
            System.out.println("上传："+realFileName);
            boolean upRes=uploadSingle(bucketName,file,realFileName);
            if(!upRes){
                throw new ErrorException("-200","文件上传失败");
            }


        } catch (Exception e) {
            throw new ErrorException("-200",e.getMessage());

        } finally {
            IoUtil.close(inputStream);
        }
        return new ReponseResult(200,"success",res);
    }
    /**
     * description: 文件删除
     *
     * @author bo
     * @date 2023/5/21 11:34
     */
    public static boolean delete(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
                    .object(fileName).build());
            return true;
        } catch (Exception e) {
            throw new ErrorException("Minio文件删除失败", e.getMessage());
        }
    }

    /**
     * description: 删除桶
     *
     * @param bucketName 桶名称
     * @author bo
     * @date 2023/5/21 11:30
     */
    public static boolean removeBucket(String bucketName) {
        try {
            List<Object> folderList = getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (!folderList.isEmpty()) {
                for (Object value : folderList) {
                    Map o = (Map) value;
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            if (!fileNames.isEmpty()) {
                for (String fileName : fileNames) {
                    delete(bucketName, fileName);
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (Exception e) {
            throw new ErrorException("Minio删除桶失败:", e.getMessage());
        }
    }

    /**
     * description: 获取桶下所有文件的文件名+大小
     *
     * @param bucketName 桶名称
     * @author bo
     * @date 2023/5/21 11:39
     */
    public static List<Object> getFolderList(String bucketName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Iterator<Result<Item>> iterator = results.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName':'%s','fileSize':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            //System.out.println(item.objectName());
            items.add(JSON.parse((String.format(format, item.objectName(),
                    formatFileSize(item.size())))));
        }
        return items;
    }

    /**
     * description: 格式化文件大小
     *
     * @param fileS 文件的字节长度
     * @author bo
     * @date 2023/5/21 11:40
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }

    /**
     * 讲快文件合并到新桶   块文件必须满足 名字是 0 1  2 3 5....
     *
     * @param bucketName  存块文件的桶
     * @param bucketName1 存新文件的桶
     * @param fileName1   存到新桶中的文件名称
     * @return boolean
     */
    public static boolean merge(String bucketName, String bucketName1, String fileName1) {
        System.out.println(bucketName+","+bucketName1+","+fileName1);
        try {
            List<ComposeSource> sourceObjectList = new ArrayList<ComposeSource>();
            List<Object> folderList = getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (!folderList.isEmpty()) {
                for (Object value : folderList) {
                    Map o = (Map) value;
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            //System.out.println(fileNames);
            if (!fileNames.isEmpty()) {
                fileNames.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // 提取文件名中的数字部分
                        int dotIndex = o1.indexOf('.');
                        String num1 = o1.substring(0, dotIndex);
                        int dotIndex2 = o2.indexOf('.');
                        String num2 = o2.substring(0, dotIndex2);
                        int num11 = Integer.parseInt(num1);
                        int num22 = Integer.parseInt(num2);

                        // 降序排列
                        return Integer.compare(num11, num22);
                    }
                });

                for (String name : fileNames) {
                    System.out.println("等待合并"+name);
                    sourceObjectList.add(ComposeSource.builder().bucket(bucketName).object(name).build());
                }
            }
            System.out.println(bucketName1+","+fileName1);
            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName1)
                            .object(fileName1)
                            .sources(sourceObjectList)
                            .build());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ErrorException("-200", "Minio合并桶异常");
        }
    }

    /**
     * description: 获取桶列表
     *
     * @author bo
     * @date 2023/5/21 12:06
     */
    public static List<String> getBucketList() {
        List<Bucket> buckets = null;
        try {
            buckets = minioClient.listBuckets();
        } catch (Exception e) {
            throw new ErrorException("-200", "Minio获取桶列表失败");
        }
        List<String> list = new ArrayList<>();
        for (Bucket bucket : buckets) {
            String name = bucket.name();
            list.add(name);
        }
        return list;
    }

    /**
     * description: 创建桶
     *
     * @param bucketName 桶名称
     * @author bo
     * @date 2023/5/21 12:08
     */
    public static boolean createBucket(String bucketName) {
        try {
            boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!b) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return true;
        } catch (Exception e) {
            throw new ErrorException("-200", e.getMessage());
        }
    }

    public static boolean downLoad(HttpServletRequest request,String bucketName, String objectName, String fileName, HttpServletResponse response){

        OutputStream out = null;
        InputStream inputStream=null;
        //objectName="2026/2/1/297b470991ac4c2ab6d04c543f8f9077.pdf";
        //fileName="1.pdf";
        try {
            inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            if (Objects.isNull(inputStream)) {
                throw new ErrorException("-200", "文件下载失败，下载的文件流为空！");
            }
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("application/x-msdownload");
            System.out.println(fileName);
            String downloadName=java.net.URLEncoder.encode(fileName, "UTF-8");
            System.out.println(downloadName);
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadName);
            response.setHeader("Access-Control-Allow-Origin", "*");
            out = response.getOutputStream();

            while((len = inputStream.read(buf)) > 0) {
                //System.out.println(111);
                out.write(buf, 0, len);
            }

        } catch (Exception e) {
            throw new ErrorException("-200", e.getMessage());
        } finally {
            IoUtil.close(out);
            IoUtil.close(inputStream);

        }


        return true;


    }

    /**
     * description: 判断桶是否存在
     *
     * @author bo
     * @date 2023/5/21 12:11
     */
    public static boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new ErrorException("Minio判断桶是否存在出错：", e.getMessage());
        }
    }


}