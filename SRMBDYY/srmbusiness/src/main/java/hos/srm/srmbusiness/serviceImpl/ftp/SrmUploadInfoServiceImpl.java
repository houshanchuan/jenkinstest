package hos.srm.srmbusiness.serviceImpl.ftp;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;

import cn.hutool.extra.ssh.JschUtil;
import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.*;

import hos.srm.srmbusiness.exception.ErrorException;
import hos.srm.srmbusiness.ftp.SftpUserInfo;
import hos.srm.srmbusiness.minio.MinioConfig;
import hos.srm.srmbusiness.model.BaseResponse;
import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.service.app.BusinessServcie;
import hos.srm.srmbusiness.service.ftp.SrmUploadInfoService;
import hos.srm.srmbusiness.utils.FileUploader;
import hos.srm.srmbusiness.utils.ReponseResult;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 上传信息表 服务实现类
 * </p>
 *
 * @author 代码生成器
 * @since 2024-09-06
 */
@Service
@Slf4j
public class SrmUploadInfoServiceImpl implements SrmUploadInfoService {
    @Value("${ftpinfo.ip}")
    private String ftpIp;
    @Value("${ftpinfo.port}")
    private int ftpPort;
    @Value("${ftpinfo.ftpUser}")
    private String ftpUser;
    @Value("${ftpinfo.passWord}")
    private String passWord;
    @Value("${ftpinfo.path}")
    private String ftpPath;
    @Value("${ftpinfo.mainPath}")
    private String mainPath;

    @Autowired
    private BusinessServcie businessServcie;
    public Session getSession(String host, int port, String user, String password) {
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            System.out.println(user+","+host+","+port+","+password);
            SftpUserInfo sftpUserInfo = new SftpUserInfo();
            session.setUserInfo((UserInfo)sftpUserInfo);
            return session;
        } catch (Exception e) {
            JschUtil.close(session);
            throw new ErrorException("-200",e.getMessage());
            //throw new BaseBusinessException(FileExceptionEnum.CREATE_SFTP_CLIENT_ERROR.getCode(), FileExceptionEnum.CREATE_SFTP_CLIENT_ERROR.getMsg(), e);
        }
    }

    public ReponseResult uploadNew(FileDto fileDto){
        String res="";
        String sysNo=fileDto.getSysNo();
        String recId=fileDto.getRecId();
        String type=fileDto.getType();
        String filename=fileDto.getFilename();
        String userCode=fileDto.getUserCode();
        int year= LocalDate.now().getYear();
        int month= LocalDate.now().getMonthValue();
        int day= LocalDate.now().getDayOfMonth();
        String uuId= UUID.randomUUID().toString().replaceAll("-", "");
        String fileType=filename.substring(filename.lastIndexOf(".")+1 );
        String newFilename=uuId+"."+fileType;
        fileDto.setRealFilename(newFilename);
        String realFileName='/'+ftpPath+'/'+ year +'/'+month+'/'+day+'/'+newFilename;
        String fileUrl='/'+ftpPath+'/'+ year +'/'+month+'/'+day;
        //fileUrl='/'+ftpPath+'/'+ year +'/'+month+'/'+day+ "222";
        String generateFileName=fileDto.getRealFilename();
        MultipartFile file = fileDto.getMultipartFile();
        FTPSClient ftpsClient = new FTPSClient("TLS");
        //FTPSClient ftpsClient = new FTPSClient(false);
        try {
            // 连接到FTPS服务器
            ftpsClient.connect(ftpIp, ftpPort);
            int reply = ftpsClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpsClient.disconnect();

                throw new ErrorException("-200","FTPS服务器拒绝连接");
            }
            // 登录认证
            boolean loginSuccess = ftpsClient.login(ftpUser, passWord);
            if (!loginSuccess) {
                // 获取服务器回复以了解失败原因
                String[] replies = ftpsClient.getReplyStrings();
                if (replies != null) {
                    for (String reply2 : replies) {
                        System.out.println("服务器回复: " + reply2);
                    }
                }
                throw new ErrorException("-200","FTPS登录失败");
            }
            // 关键配置：设置编码和传输模式
            //ftpsClient.setControlEncoding("UTF-8");
            ftpsClient.enterLocalPassiveMode();
            ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 处理中文文件名编码问题
            String newFilename2 = new String(newFilename.getBytes("UTF-8"), "ISO-8859-1");
            // 执行文件上传
            InputStream inputStream = file.getInputStream();
            String currentDirectory = ftpsClient.printWorkingDirectory();
            System.out.println("当前路径: " + currentDirectory);
            Boolean changeDirFlag=createDirectory(ftpsClient,fileUrl);
            if(!changeDirFlag){
                throw new ErrorException("-200","创建目录失败");
            }
            // 安全退出
            ftpsClient.logout();
            // 报错文件


            //boolean uploadSuccess = ftpsClient.storeFile(newFilename2, inputStream);
            inputStream.close();
            /*
            if (uploadSuccess) {
                System.out.println("文件上传成功");
            } else {
                String[] replies = ftpsClient.getReplyStrings();
                if (replies != null) {
                    for (String reply2 : replies) {
                        System.out.println("服务器回复: " + reply2);
                    }
                }

                throw new ErrorException("-200","文件上传失败");
            }
            */


            SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
            srmBussinessDto.setRequestCode("File-SaveFile");
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("filename",filename);
            paramMap.put("rowid",recId);
            paramMap.put("sysno",sysNo);
            paramMap.put("usercode",userCode);
            paramMap.put("FileURL",realFileName);
            paramMap.put("type",type);
            srmBussinessDto.setRequestInput(JSON.toJSONString(paramMap));
            res=businessServcie.sendBusinessData(srmBussinessDto);

        } catch (Exception e) {
            throw new ErrorException("-200",e.getMessage());

        } finally {
            // 确保连接被关闭
            if (ftpsClient.isConnected()) {
                try {
                    ftpsClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ReponseResult(200,"success",res);
    }
    @Override
    public ReponseResult upload(FileDto fileDto){
        String res="";
        String sysNo=fileDto.getSysNo();
        String recId=fileDto.getRecId();
        String type=fileDto.getType();
        String filename=fileDto.getFilename();
        String userCode=fileDto.getUserCode();
        int year= LocalDate.now().getYear();
        int month= LocalDate.now().getMonthValue();
        int day= LocalDate.now().getDayOfMonth();
        String uuId= UUID.randomUUID().toString().replaceAll("-", "");
        String fileType=filename.substring(filename.lastIndexOf(".")+1 );
        String newFilename=uuId+"."+fileType;
        fileDto.setRealFilename(newFilename);
        String realFileName='/'+mainPath+'/'+ftpPath+'/'+ year +'/'+month+'/'+day+'/'+newFilename;
        String realFileName2='/'+ftpPath+'/'+ year +'/'+month+'/'+day+'/'+newFilename;

        String fileUrl='/'+ftpPath+'/'+ year +'/'+month+'/'+day;
        //fileUrl='/'+ftpPath+'/'+ year +'/'+month+'/'+day+ "222";
        String generateFileName=fileDto.getRealFilename();
        MultipartFile file = fileDto.getMultipartFile();
        FTPSClient ftpsClient = new FTPSClient("TLS");
        //FTPSClient ftpsClient = new FTPSClient(false);
        try {
            // 连接到FTPS服务器
            ftpsClient.connect(ftpIp, ftpPort);
            int reply = ftpsClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpsClient.disconnect();

                throw new ErrorException("-200","FTPS服务器拒绝连接");
            }
            // 登录认证
            boolean loginSuccess = ftpsClient.login(ftpUser, passWord);
            if (!loginSuccess) {
                // 获取服务器回复以了解失败原因
                String[] replies = ftpsClient.getReplyStrings();
                if (replies != null) {
                    for (String reply2 : replies) {
                        System.out.println("服务器回复: " + reply2);
                    }
                }
                throw new ErrorException("-200","FTPS登录失败");
            }
            // 关键配置：设置编码和传输模式
            ftpsClient.enterLocalPassiveMode();
            ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream inputStream = file.getInputStream();
            String currentDirectory = ftpsClient.printWorkingDirectory();
            System.out.println("当前路径: " + currentDirectory);
            Boolean changeDirFlag=createDirectory(ftpsClient,fileUrl);
            if(!changeDirFlag){
                throw new ErrorException("-200","创建目录失败");
            }
            // 安全退出
            ftpsClient.logout();
            // 报错文件

            File convFile = new File(realFileName);
            FileOutputStream fos = new FileOutputStream(convFile);
            int bytesRead = 0;
            byte[] buffer = new byte[3072];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                System.out.println(bytesRead);
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            inputStream.close();
            SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
            srmBussinessDto.setRequestCode("File-SaveFile");
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("filename",filename);
            paramMap.put("rowid",recId);
            paramMap.put("sysno",sysNo);
            paramMap.put("usercode",userCode);
            paramMap.put("FileURL",realFileName2);
            paramMap.put("type",type);
            srmBussinessDto.setRequestInput(JSON.toJSONString(paramMap));
            res=businessServcie.sendBusinessData(srmBussinessDto);

        } catch (Exception e) {
            throw new ErrorException("-200",e.getMessage());

        } finally {
            // 确保连接被关闭
            if (ftpsClient.isConnected()) {
                try {
                    ftpsClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ReponseResult(200,"success",res);
    }
    // 创建目录的方法
    private boolean createDirectory(FTPSClient ftpsClient, String path) throws IOException {
        String[] directories = path.split("/");
        StringBuilder currentPath = new StringBuilder();

        for (String dir : directories) {
            if (!dir.isEmpty()) {
                currentPath.append("/").append(dir);
                // 检查目录是否存在
                if (!ftpsClient.changeWorkingDirectory(currentPath.toString())) {
                    // 目录不存在，尝试创建
                    boolean created = ftpsClient.makeDirectory(currentPath.toString());
                    if (!created) {
                        System.out.println("创建目录失败: " + currentPath.toString());
                        return false;
                    }else{
                        ftpsClient.changeWorkingDirectory(currentPath.toString());
                    }
                }else{
                    System.out.println("切换目录: " + currentPath.toString());
                }
            }
        }
        return true;
    }


    public ReponseResult uploadSftp(FileDto fileDto) {
        String res="";
        String sysNo=fileDto.getSysNo();
        String recId=fileDto.getRecId();
        String type=fileDto.getType();
        String filename=fileDto.getFilename();
        String userCode=fileDto.getUserCode();
        int year= LocalDate.now().getYear();
        int month= LocalDate.now().getMonthValue();
        int day= LocalDate.now().getDayOfMonth();
        String uuId= UUID.randomUUID().toString().replaceAll("-", "");
        String fileType=filename.substring(filename.lastIndexOf(".")+1 );
        String newFilename=uuId+"."+fileType;
        fileDto.setRealFilename(newFilename);
        String fileUrl='/'+ftpPath+'/'+ year +'/'+month+'/'+day+'/'+newFilename;
        String generateFileName=fileDto.getRealFilename();
        MultipartFile file = fileDto.getMultipartFile();
        InputStream inputStream = null;
        Session session = null;
        try {

            session = getSession(ftpIp, ftpPort, ftpUser, passWord);
            inputStream = file.getInputStream();
            String uploadPath=ftpPath+"/"+year+"/"+month+"/"+day;
            System.out.println("path:"+uploadPath);
            ReponseResult result=uploadFile(session, inputStream, uploadPath, generateFileName);
            int code=result.getCode();
            if(code!=200){
                throw new ErrorException("-200","上传失败");
            }
            System.out.println("res:"+code);
            // 下面要处理附件业务表
            SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
            srmBussinessDto.setRequestCode("File-SaveFile");
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("filename",filename);
            paramMap.put("rowid",recId);
            paramMap.put("sysno",sysNo);
            paramMap.put("usercode",userCode);
            paramMap.put("FileURL",fileUrl);
            paramMap.put("type",type);
            srmBussinessDto.setRequestInput(JSON.toJSONString(paramMap));
            res=businessServcie.sendBusinessData(srmBussinessDto);

        } catch (Exception e) {
            //FileUtil.del(fileInfoDTO.getFilePath());
            throw new ErrorException("-200",e.getMessage());
        } finally {
            IoUtil.close(inputStream);
        }
        return new ReponseResult(200,"success",res);
    }
    public ReponseResult uploadFile(Session session, InputStream inputStream, String remoteOffersFilePath, String fileName) throws Exception {
        Channel channel = null;
        ChannelSftp c = null;
        try {
            session.setTimeout(3000);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            c = (ChannelSftp)channel;
            System.out.println(remoteOffersFilePath);
            c.cd(mainPath);
            mkdirs(c, remoteOffersFilePath);
            c.put(inputStream, fileName, 0);


        }catch (Exception e){
            throw new ErrorException("-200",e.getMessage());
        }finally {
            if (c != null)
                try {
                    c.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (channel != null)
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (session != null)
                try {
                    session.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return new ReponseResult(200,"成功");
    }
    private void mkdirs(ChannelSftp sftp, String dir) throws SftpException {
        String[] folders = dir.split("/");
        for (String folder : folders) {
            if (!folder.isEmpty())
                try {
                    sftp.cd(folder);
                }catch(Exception exception){
                    sftp.mkdir(folder);//创建目录
                    sftp.cd(folder);  //进入目录
                }

        }
    }

    public void download(HttpServletRequest request,String filepath,String orginalFileName, HttpServletResponse response) {

        String[] dirs=filepath.split("/");
        String downloadName=orginalFileName;

        System.out.println(filepath);
        int lena=dirs.length;
        if(lena>0){
            downloadName=dirs[lena-1];
        }
        String agent = request.getHeader("USER-AGENT");
        try {
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                System.out.println("-----非苹果下载-----");
                downloadName = java.net.URLEncoder.encode(orginalFileName, "UTF-8");
            } else if (agent.contains("MAC OS")) {
                System.out.println("-----苹果下载-----");
                //String headerValue = "attachment;";
                //headerValue += " filename=\"" + URLUtil.encode(downloadName) + "\";";
                //headerValue += " filename*=utf-8''" + URLUtil.encode(downloadName);
                //response.setHeader("Content-Disposition", headerValue);
            } else {
                System.out.println("-----非苹果下载-----");
                downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
            }

            String newfilepath=mainPath+filepath;

            InputStream inStream = new FileInputStream(newfilepath);// 文件的存放路径
            //response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + downloadName + "\"");
            // 循环取出流中的数据
            byte[] b = new byte[3072];
            int len;
            while ((len = inStream.read(b)) > 0) {

                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
        } catch (Exception e) {
            //throw new FileDownloadFailException();
        }
    }


    @Override
    public ReponseResult uploadMinio(FileDto fileDto){
        String res="";
        String sysNo=fileDto.getSysNo();
        String recId=fileDto.getRecId();
        String type=fileDto.getType();
        String filename=fileDto.getFilename();
        String userCode=fileDto.getUserCode();
        int year= LocalDate.now().getYear();
        int month= LocalDate.now().getMonthValue();
        int day= LocalDate.now().getDayOfMonth();
        String uuId= UUID.randomUUID().toString().replaceAll("-", "");
        String fileType=filename.substring(filename.lastIndexOf(".")+1 );
        String newFilename=uuId+"."+fileType;
        fileDto.setRealFilename(newFilename);
        System.out.println("y:"+year);
        System.out.println("month:"+month);
        String realFileName=""+year +'/'+month+'/'+day+'/'+newFilename;
        System.out.println(realFileName);
        InputStream inputStream = null;
        MultipartFile file = fileDto.getMultipartFile();
        try {
            inputStream = file.getInputStream();
            FileUploader fileUploader=new FileUploader();
            fileUploader.uploadFile(realFileName,inputStream,this.getContentType(realFileName));
            SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
            srmBussinessDto.setRequestCode("File-SaveFile");
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("filename",filename);
            paramMap.put("rowid",recId);
            paramMap.put("sysno",sysNo);
            paramMap.put("usercode",userCode);
            paramMap.put("FileURL",realFileName);
            paramMap.put("type",type);
            srmBussinessDto.setRequestInput(JSON.toJSONString(paramMap));
            //res=businessServcie.sendBusinessData(srmBussinessDto);

        } catch (Exception e) {
            throw new ErrorException("-200",e.getMessage());

        } finally {
            IoUtil.close(inputStream);
        }
        return new ReponseResult(200,"success",res);
    }
    private  String getContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }
}
