package hos.srm.srmbusiness.MyAnnotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";  //模块名
    String operation() default ""; //操作名
}