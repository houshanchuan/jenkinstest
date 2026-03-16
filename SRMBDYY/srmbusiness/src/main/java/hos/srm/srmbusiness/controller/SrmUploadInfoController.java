package hos.srm.srmbusiness.controller;


import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.service.ftp.SrmUploadInfoService;
import hos.srm.srmbusiness.utils.ReponseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 上传信息表
 * </p>
 *
 * @author 代码生成器
 * @since 2024-09-06
 */
@RestController
@RequestMapping("/uploadInfo")
@Api(tags = "上传信息表")
public class SrmUploadInfoController {
    @Autowired
    private SrmUploadInfoService srmUploadInfoService;

    @PostMapping({"/upload"})
    public ReponseResult upload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName, @RequestParam("recId") String recId, @RequestParam("sysNo") String sysNo, @RequestParam("type") String type, @RequestParam("userCode") String userCode) {
        System.out.println(fileName);
        FileDto fileDto = new FileDto();
        fileDto.setRecId(recId);
        fileDto.setSysNo(sysNo);
        fileDto.setType(type);
        fileDto.setUserCode(userCode);
        fileDto.setFilename(fileName);
        fileDto.setMultipartFile(file);
        return srmUploadInfoService.upload(fileDto);

    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public void download(HttpServletRequest request, @RequestParam("path") String filepath, @RequestParam("filename") String filename, HttpServletResponse response) {
        System.out.println(filepath);
        srmUploadInfoService.download(request,filepath,filename,response);

    }


    @PostMapping({"/uploadMinio"})
    public ReponseResult uploadMinio(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename, @RequestParam("recId") String recId, @RequestParam("sysNo") String sysNo, @RequestParam("type") String type, @RequestParam("userCode") String userCode) {
        System.out.println(filename);
        FileDto fileDto = new FileDto();
        fileDto.setRecId(recId);
        fileDto.setSysNo(sysNo);
        fileDto.setType(type);
        fileDto.setUserCode(userCode);
        fileDto.setFilename(filename);
        fileDto.setMultipartFile(file);
        //srmUploadInfoService.upload(fileDto);
        return srmUploadInfoService.uploadMinio(fileDto);

    }
}
