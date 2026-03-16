package hos.srm.srmbusiness.controller;


import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.model.dto.SrmMessageDto;
import hos.srm.srmbusiness.model.entity.Loginuser;
import hos.srm.srmbusiness.service.app.BusinessServcie;
import hos.srm.srmbusiness.service.app.DealMessageService;
import hos.srm.srmbusiness.service.ftp.SrmUploadInfoService;
import hos.srm.srmbusiness.service.login.LoginuserService;
import hos.srm.srmbusiness.service.minio.SynUploadService;
import hos.srm.srmbusiness.utils.ReponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 登录表 前端控制器
 * </p>
 *
 * @author 代码生成器
 * @since 2024-06-22
 */
@RestController
@RequestMapping("/Login")
public class LoginuserController {
    @Autowired
    private LoginuserService loginuserService;
    @Autowired
    private BusinessServcie businessServcie;
    @Autowired
    private DealMessageService dealMessageService;
    @Autowired
    private SrmUploadInfoService srmUploadInfoService;
    @Autowired
    private SynUploadService synUploadService;
    @RequestMapping(value = "/getLogin", method = RequestMethod.POST)
    public ReponseResult Login(@RequestBody Loginuser user){

        ReponseResult res=loginuserService.login(user);
        return res;
    }

    @RequestMapping(value = "/listDept", method = RequestMethod.POST)
    public ReponseResult listDept(@RequestBody SrmBussinessDto srmBussinessDto) {
        return dealMessageService.listDept();
    }
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public ReponseResult getToken(@RequestBody SrmBussinessDto srmBussinessDto) {
        return new ReponseResult<>(200,"",dealMessageService.getAccessToken());
    }
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ReponseResult sendMessage(@RequestBody SrmMessageDto srmMessageDto) {
        return new ReponseResult<>(200,"",dealMessageService.sendMessage(srmMessageDto));
    }
    @RequestMapping(value = "/listUser", method = RequestMethod.POST)
    public ReponseResult listUser(@RequestBody SrmMessageDto srmMessageDto) {
        return dealMessageService.listUser(srmMessageDto);
    }
    @RequestMapping(value = "/listUserDetail", method = RequestMethod.POST)
    public ReponseResult listUserDetail(@RequestBody SrmMessageDto srmMessageDto) {
        return dealMessageService.listUserDetail(srmMessageDto);
    }
    @RequestMapping(value = "/getUserDetailByCode", method = RequestMethod.POST)
    public ReponseResult getUserDetailByCode(@RequestBody SrmMessageDto srmMessageDto) {
        return dealMessageService.getUserDetailByCode(srmMessageDto);
    }
    @RequestMapping(value = "/srmFixPassWord", method = RequestMethod.POST)
    public ReponseResult srmFixPassWord(@RequestParam Map<String,String> mapParams) {

        //String res="housc";
        SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
        srmBussinessDto.setRequestCode(mapParams.get("requestCode"));
        srmBussinessDto.setRequestInput(mapParams.get("requestInput"));

        String rr=businessServcie.sendBusinessData(srmBussinessDto);
        System.out.println(rr);
        //return new ReponseResult<>(200,"成功");
        return new ReponseResult<>(200,"成功",rr);
    }

    @RequestMapping(value = "/srmFixPassWord2", method = RequestMethod.POST)
    public ReponseResult srmFixPassWord2(@RequestBody SrmBussinessDto srmBussinessDto) {
        System.out.println("srmFixPassWord2");
        System.out.println(srmBussinessDto);

        return new ReponseResult<>(200,"成功",businessServcie.sendBusinessData(srmBussinessDto));
    }

    @PostMapping({"/uploadMinio"})
    public ReponseResult uploadMinio(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename, @RequestParam("recId") String recId, @RequestParam("sysNo") String sysNo, @RequestParam("type") String type, @RequestParam("userCode") String userCode, @RequestParam("uploadId") String uploadId, @RequestParam("chunkCount") int chunkCount, @RequestParam("chunkIndex") int chunkIndex) {
        System.out.println(filename);
        FileDto fileDto = new FileDto();
        fileDto.setRecId(recId);
        fileDto.setSysNo(sysNo);
        fileDto.setType(type);
        fileDto.setUserCode(userCode);
        fileDto.setFilename(filename);
        fileDto.setMultipartFile(file);
        fileDto.setUploadId(uploadId);
        fileDto.setChunkCount(chunkCount);
        fileDto.setChunkIndex(chunkIndex);
        //srmUploadInfoService.upload(fileDto);
        return synUploadService.uploadMinio(fileDto);
        //synUploadService

    }
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseBody
    public void download(HttpServletRequest request, @RequestParam("objName") String objName, @RequestParam("fileName") String fileName, HttpServletResponse response) {
        //System.out.println(objName);
        synUploadService.download(request,objName,fileName,response);

    }

    @RequestMapping(value = "/downloadAll", method = RequestMethod.POST)
    @ResponseBody
    public void downloadAll(HttpServletRequest request, @RequestParam("fileList") String fileList, @RequestParam("fileName") String fileName, HttpServletResponse response) {
        //System.out.println(objName);
        synUploadService.downloadAll(request,fileList,fileName,response);

    }
}

