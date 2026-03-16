package hos.srm.srmbusiness.service.ftp;


import hos.srm.srmbusiness.model.dto.FileDto;
import hos.srm.srmbusiness.utils.ReponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 上传信息表 服务类
 * </p>
 *
 * @author 代码生成器
 * @since 2024-09-06
 */
public interface SrmUploadInfoService {
    ReponseResult upload(FileDto fileDto);
    ReponseResult uploadMinio(FileDto fileDto);
    void download(HttpServletRequest request, String fileUrl, String orginalFileName, HttpServletResponse response);
}
