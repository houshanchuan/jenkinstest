package hos.srm.srmbusiness.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       // WebMvcConfigurer.super.addCorsMappings(registry);
       //设置运行跨越请求域名
        //System.out.println(111111111);
        registry.addMapping("/**")
                //设置允许跨域请求域名
                .allowedOriginPatterns("*")
                //设置允许cookie
                .allowCredentials(true)
                //设置允许方法
                .allowedMethods("GET","POST","DELETE","PUT")
                //设置允许的header属性
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
