package hos.srm.srmbusiness.serviceImpl.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hos.srm.srmbusiness.model.dto.MyLoginUser;
import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.model.entity.Loginuser;
import hos.srm.srmbusiness.exception.ErrorException;
import hos.srm.srmbusiness.service.app.BusinessServcie;
import hos.srm.srmbusiness.service.login.LoginuserService;
import hos.srm.srmbusiness.utils.JwtTokenUtils;
import hos.srm.srmbusiness.utils.ReponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 登录表 服务实现类
 * </p>
 *
 * @author 代码生成器
 * @since 2024-06-22
 */
@Service
public class LoginuserServiceImpl  implements LoginuserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private BusinessServcie businessServcie;
    @Override
    public ReponseResult login(Loginuser user) {
        SrmBussinessDto srmBussinessDto=new SrmBussinessDto();
        srmBussinessDto.setRequestCode("Login-CheckUser");
        Map<String,String> parmaMap=new HashMap<>();
        parmaMap.put("account",user.getUserName());
        parmaMap.put("password",user.getPassword());
        parmaMap.put("wxCode", user.getWxCode());
        String params= JSON.toJSONString(parmaMap);
        srmBussinessDto.setRequestInput(params);
        System.out.println(srmBussinessDto);
        String res=businessServcie.sendBusinessData(srmBussinessDto);
        System.out.println(res);
        JSONObject jsonObject = JSON.parseObject(res);
        String success=jsonObject.getString("success");
        // 移除UTF-8 BOM头
        if (res.startsWith("\uFEFF")) {
            res = res.substring(1);
        }
        // 移除其他非法开头字符
        res = res.trim();

        Map<String,String> map=new HashMap<>();
        try {
            Map<String, Object> resMap = JSON.parseObject(res, HashMap.class);
            if(!success.equals("0")){
                throw new ErrorException("-200",String.valueOf(resMap.get("msg")));
            }

            String userName=String.valueOf(resMap.get("userName"));
            String HosptialId=String.valueOf(resMap.get("HosptialId"));
            String HosptialName=String.valueOf(resMap.get("HosptialName"));
            String userId=String.valueOf(resMap.get("userId"));
            String Account=String.valueOf(resMap.get("Account"));

            String token=jwtTokenUtils.createtoken(userName);
            String refreshToken=jwtTokenUtils.refreshToken(token);

            map.put("token",token);
            map.put("refreshToken",refreshToken);
            map.put("code","200");
            map.put("userId",userId);
            map.put("userName",userName);
            map.put("HosptialId",HosptialId);
            map.put("HosptialName",HosptialName);
            map.put("Account",Account);
        }catch (Exception e){
            throw new ErrorException("-200","登录失败");
        }

        return  new ReponseResult(200,"success",map);
    }
}
