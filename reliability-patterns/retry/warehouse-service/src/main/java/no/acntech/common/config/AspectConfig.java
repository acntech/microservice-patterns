package no.acntech.common.config;

import java.util.Random;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AspectConfig {

    @Before("execution(public * createReservation(*))")
    public void randomExceptionAdvice() {
        Random random = new Random();
        if (random.nextInt(100) > 20) { // Will fail 80% of the time
            throw new IllegalStateException("Random error occurred");
        }
    }
}
