package hos.srm.srmbusiness.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {
    public static String renderString(HttpServletResponse response,Integer code,String data){
        try {
            response.setStatus(code);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(data);
        }
        catch (IOException e){
            e.printStackTrace();

        }
        return null;
    }
}
