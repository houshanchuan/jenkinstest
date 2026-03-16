package hos.srm.srmbusiness.Filter;

import com.alibaba.fastjson.JSON;
import hos.srm.srmbusiness.utils.ReponseResult;
import hos.srm.srmbusiness.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ReponseResult result=new ReponseResult(HttpStatus.UNAUTHORIZED.value(), "授权未通过");

        String json= JSON.toJSONString(result);
        WebUtil.renderString(response,200,json);
    }
}
