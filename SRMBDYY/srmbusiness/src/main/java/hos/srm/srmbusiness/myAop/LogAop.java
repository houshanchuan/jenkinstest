package hos.srm.srmbusiness.myAop;

import hos.srm.srmbusiness.MyAnnotation.LogAnnotation;
import hos.srm.srmbusiness.utils.MyLogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LogAop {
    //定义切点
    @Pointcut("@annotation(hos.srm.srmbusiness.MyAnnotation.LogAnnotation)")
    public void logPointCut() {
    }
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        recordLog(point, time);
        return result;
    }
    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Logger logger= MyLogManager.getLogger();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        logger.info("=====================log start================================");
        logger.info("module:{}",logAnnotation.module());
        logger.info("operation:{}",logAnnotation.operation());
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        logger.info("request method:{}",className + "." + methodName + "()");
        //请求的参数
        Object[] args = joinPoint.getArgs();
        //String params = JSON.toJSONString(args[0]);
        //String params= JsonChange.toJson(args[0]);
        //logger.info("params:{}",params);
        //获取request 设置IP地址
        logger.info("excute time : {} ms",time);
        logger.info("=====================log end================================");
    }
}
