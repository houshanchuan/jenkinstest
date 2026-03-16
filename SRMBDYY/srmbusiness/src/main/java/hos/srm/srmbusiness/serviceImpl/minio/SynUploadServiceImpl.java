package hos.srm.srmbusiness.serviceImpl.minio;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import hos.srm.srmbusiness.exception.ErrorException;
import hos.srm.srmbusiness.minio.MinioConfig;
import hos.srm.srmbusiness.minio.MinioUtil;
import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.service.app.BusinessServcie;
import hos.srm.srmbusiness.service.minio.SynUploadService;
import hos.srm.srmbusiness.utils.FileUploader;
import hos.srm.srmbusiness.utils.ReponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SynUploadServiceImpl implements SynUploadService {
    @Autowired
    private BusinessServcie businessServcie;
    @Override
    public ReponseResult initMinio(FileDto fileDto) {
        return null;
    }

    public String setUploadInfo(FileDto fileDto,String realFileName){
        return null;
        /*
        SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
        srmBussinessDto.setRequestCode("File-SaveMinioFile");
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("filename",fileDto.getFilename());
        paramMap.put("rowid",fileDto.getRecId());
        paramMap.put("sysno",fileDto.getSysNo());
        paramMap.put("usercode",fileDto.getUserCode());
        paramMap.put("FileURL",realFileName);
        paramMap.put("type",fileDto.getType());
        srmBussinessDto.setRequestInput(JSON.toJSONString(paramMap));
        String res=businessServcie.sendBusinessData(srmBussinessDto);
        return res;
        */

    }
    @Override
    public ReponseResult uploadMinio(FileDto fileDto){

        //System.out.println(fileDto);
        int chunkLen=fileDto.getChunkCount();
        int chunkIndex=fileDto.getChunkIndex()+1;
        String uploadId=fileDto.getUploadId();
        String res="";
        String filename=fileDto.getFilename();
        int year= LocalDate.now().getYear();
        int month= LocalDate.now().getMonthValue();
        int day= LocalDate.now().getDayOfMonth();
        String uuId= UUID.randomUUID().toString().replaceAll("-", "");
        String fileType=filename.substring(filename.lastIndexOf(".")+1 );
        String newFilename=uuId+"."+fileType;
        //fileDto.setRealFilename(newFilename);
        String realFileName=""+year +'/'+month+'/'+day+'/'+newFilename;
        fileDto.setRealFilename(realFileName);
        //
        if(chunkLen>1){
            //分片上传
            String partFileName= chunkIndex+"."+fileType;
            MultipartFile file = fileDto.getMultipartFile();
            boolean upRes=MinioUtil.uploadPartFile(uploadId,partFileName,file,chunkIndex);
            if(!upRes){
                throw new ErrorException("-200","文件上传失败");
            }
            //System.out.println(chunkLen+","+chunkIndex);
            if(chunkLen==chunkIndex){
                String bucketName = MinioConfig.getBucketName();
                boolean createStatus=MinioUtil.createBucket(bucketName);
                //合并
                MinioUtil.merge(uploadId,bucketName,realFileName);
                //合并完成后，删除临时桶
                MinioUtil.removeBucket(uploadId);
                res=this.setUploadInfo(fileDto,realFileName);
            }
        }else{
            //直接上传
            MinioUtil.upload(fileDto);
            System.out.println("iris:"+realFileName);
            res=this.setUploadInfo(fileDto,realFileName);
        }

        return new ReponseResult(200,"success",res);
    }

    @Override
    public ReponseResult completeMinio(FileDto fileDto) {
        return null;
    }

    @Override
    public boolean createTempBucket(String identify) {
        // 1.校验文件md5是否存在
        //Boolean md5Hava = JedisUtil.exists(identify);
        //if (md5Hava) {
        //    return true;
        //}
        // 2.创建临时桶
        boolean b = MinioUtil.bucketExists(identify);
        if (b) {
            // 存在先删除在创建
            MinioUtil.removeBucket(identify);
        }
        MinioUtil.createBucket(identify);
        // 将MD5存到redis中过期时间为1天，断点续传用到
        //JedisUtil.setJson(identify, String.valueOf(0), 24 * 60 * 60);
        return false;
    }

    @Override
    public void download(HttpServletRequest request, String objName, String fileName, HttpServletResponse response) {
        String buckName=MinioConfig.getBucketName();
        MinioUtil.downLoad(request,buckName,objName,fileName,response);
    }

    @Override
    public void downloadAll(HttpServletRequest request, String fileList,String fileName, HttpServletResponse response) {
        try{
            fileName=fileName+".zip";
            String downloadName=java.net.URLEncoder.encode(fileName, "UTF-8");
            System.out.println(downloadName);
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadName);
            response.setHeader("Access-Control-Allow-Origin", "*");
            ByteArrayOutputStream zipStream = this.createZipStream(fileList);
            OutputStream out = response.getOutputStream();
            zipStream.writeTo(out);
            out.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new ErrorException("-200","下载失败");
        }

    }
    public  void addFileToZip(ZipOutputStream zos, String filePath, String fileNameInZip) throws IOException {
        filePath="/dthealth/dhccftp/materialsftp"+filePath;
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileNameInZip);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
        }
    }

    public  ByteArrayOutputStream createZipStream(String fileList) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            Map<String, String> resMap = JSON.parseObject(fileList, HashMap.class);
            //遍历存放所有key的Set集合
            Set<String> keySet = resMap.keySet();
            Iterator<String> it =keySet.iterator();
            while(it.hasNext()) {
                String key = it.next();
                String item = resMap.get(key);
                Map<String, String> itemMap = JSON.parseObject(item, HashMap.class);
                String fileName = itemMap.get("fileName");
                fileName = key + "-" + fileName;   //确保唯一性
                String fileUrl = itemMap.get("fileUrl");
                System.out.println(fileUrl+","+fileName);
                this.addFileToZip(zos,fileUrl,fileName);
            }

        }
        return baos;
    }
}
