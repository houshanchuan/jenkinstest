package hos.srm.srmbusiness.service.app;

import hos.srm.srmbusiness.model.dto.SrmMessageDto;
import hos.srm.srmbusiness.utils.ReponseResult;

public interface DealMessageService {
    ReponseResult listDept();
    String sendMessage(SrmMessageDto srmMessageDto);
    String getAccessToken();
    ReponseResult listUser(SrmMessageDto srmMessageDto);
    ReponseResult listUserDetail(SrmMessageDto srmMessageDto);
    ReponseResult getUserDetailByCode(SrmMessageDto srmMessageDto);
}
