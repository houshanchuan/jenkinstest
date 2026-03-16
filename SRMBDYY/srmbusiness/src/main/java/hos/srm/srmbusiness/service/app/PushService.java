package hos.srm.srmbusiness.service.app;

import hos.srm.srmbusiness.model.dto.app.PushNoticeDto;
import hos.srm.srmbusiness.model.vo.app.PushNoticeVo;
import hos.srm.srmbusiness.service.login.LoginuserService;
import hos.srm.srmbusiness.serviceImpl.app.PushImpl;

public interface PushService {
    public PushNoticeVo pushMessageToSingleCid(PushNoticeDto bean);
}
