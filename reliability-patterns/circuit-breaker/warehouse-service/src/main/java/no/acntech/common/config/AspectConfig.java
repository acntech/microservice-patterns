package no.acntech.common.config;

import java.time.Duration;
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
        int percent = random.nextInt(100);
        if (percent < 20) {
            throw new IllegalStateException("Random error occurred");
        } else if (percent > 80) {
            try {
                Thread.sleep(Duration.ofSeconds(10).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
