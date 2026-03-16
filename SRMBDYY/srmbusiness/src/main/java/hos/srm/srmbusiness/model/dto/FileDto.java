package hos.srm.srmbusiness.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDto {
    private MultipartFile multipartFile;
    private String sysNo;
    private String id;
    private String userCode;
    private String recId;
    private String type;
    private String filename;
    private String filepath;
    private String realFilename;
    private String yyMmDd;
    private String uploadId;
    private Long fileSize;
    private int chunkCount;
    private int chunkIndex;

}
