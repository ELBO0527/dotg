package elbo.dotg.api17.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Aspect
@Order
@Slf4j
//@Component
public class TimeCheckerAspect {

    public TimeCheckerAspect() {}
    public void setTimeCheckerAspoect() {}

    public void init() {}

    @Around("execution(* *test.java.elbo.dotg.api17.**)")
    public Object timeCheckerAnnotation(JoinPoint joinPoint, Object result){
        Signature signature = joinPoint.getSignature();
        log.info("proxy = {}", signature);
        return result;
    }
}