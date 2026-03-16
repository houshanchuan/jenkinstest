package hos.srm.srmbusiness.config;

import hos.srm.srmbusiness.Interceptor.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// 先去掉注解
// @Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        addInterceptor 就是加过滤器
        /*
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/user/test")
                .excludePathPatterns("/user/login")
        ;
        */

    }
}