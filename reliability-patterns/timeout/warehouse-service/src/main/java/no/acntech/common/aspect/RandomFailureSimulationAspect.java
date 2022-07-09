package no.acntech.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Random;

@Aspect
@Component
public class RandomFailureSimulationAspect {

    private static final int DELAY_PERCENTAGE_OF_TIMES = 50;
    private final Random random = new Random();

    @Before("within(@org.springframework.stereotype.Service *) && execution(public * *(..)))")
    public void before(JoinPoint joinPoint) throws Throwable {
        if (random.nextInt(100) < DELAY_PERCENTAGE_OF_TIMES) {
            final var sleep = Duration.ofSeconds(random.nextInt(1, 5));
            Thread.sleep(sleep.toMillis());
        }
    }
}
