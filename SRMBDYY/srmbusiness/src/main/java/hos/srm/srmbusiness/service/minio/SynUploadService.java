package hos.srm.srmbusiness.service.minio;

import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.utils.ReponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SynUploadService {
    ReponseResult initMinio(FileDto fileDto);
    ReponseResult uploadMinio(FileDto fileDto);
    ReponseResult completeMinio(FileDto fileDto);
    public boolean createTempBucket(String identify);
    void download(HttpServletRequest request, String objName, String fileName, HttpServletResponse response);
    void downloadAll(HttpServletRequest request, String fileList,String fileName, HttpServletResponse response);


}
