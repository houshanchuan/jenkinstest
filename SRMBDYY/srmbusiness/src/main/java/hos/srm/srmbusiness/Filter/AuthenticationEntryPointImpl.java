package hos.srm.srmbusiness.Filter;

import com.alibaba.fastjson.JSON;
import hos.srm.srmbusiness.utils.ReponseResult;
import hos.srm.srmbusiness.utils.WebUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ReponseResult result=null;

        String authToken = request.getHeader("Admin-Token");
        if(authToken==null){
            // 这要重新修改一下，排除登陆的
            result=new ReponseResult(-200, "token为空");
            String json= JSON.toJSONString(result);
            WebUtil.renderString(response,402,json);
        }else{
            result=new ReponseResult(-200, "token过期,请重新登陆");
            String json= JSON.toJSONString(result);
            WebUtil.renderString(response,401,json);
        }



    }
}
