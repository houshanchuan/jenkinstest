package hos.srm.srmbusiness.serviceImpl.ftp;

import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.service.ftp.SrmMinioUploadService;
import hos.srm.srmbusiness.utils.FileUploader;
import hos.srm.srmbusiness.utils.ReponseResult;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SrmMinioUploadServiceImpl implements SrmMinioUploadService {

    @Override
    public ReponseResult initMinio(FileDto fileDto) {
        return null;
    }



    @Override
    public ReponseResult uploadMinio(FileDto fileDto) {
        return null;
    }

    @Override
    public ReponseResult completeMinio(FileDto fileDto) {
        return null;
    }




}
