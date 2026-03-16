package hos.srm.srmbusiness.service.ftp;

import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.utils.ReponseResult;

public interface SrmMinioUploadService {
    ReponseResult initMinio(FileDto fileDto);
    ReponseResult uploadMinio(FileDto fileDto);
    ReponseResult completeMinio(FileDto fileDto);
}
