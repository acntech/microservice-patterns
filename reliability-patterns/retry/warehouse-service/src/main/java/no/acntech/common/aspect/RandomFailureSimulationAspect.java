package no.acntech.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Random;

@Aspect
@Component
public class RandomFailureSimulationAspect {

    private static int FAILURE_COUNT = 0;
    private static final int FAIL_PERCENTAGE_OF_TIMES = 50;
    private final Random random = new Random();

    @Before("within(@org.springframework.stereotype.Service *) && execution(public * *(..)))")
    public void before(JoinPoint joinPoint) throws Throwable {
        if (random.nextInt(100) < FAIL_PERCENTAGE_OF_TIMES) {
            throw new RuntimeException("Random failure #" + ++FAILURE_COUNT);
        }
    }
}
