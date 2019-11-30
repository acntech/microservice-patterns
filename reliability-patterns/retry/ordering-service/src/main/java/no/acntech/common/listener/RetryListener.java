package no.acntech.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class RetryListener extends RetryListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryListener.class);

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        LOGGER.info("Call failed - Retry-Count={}  Error={}", context.getRetryCount(), throwable.getMessage());
        super.onError(context, callback, throwable);
    }

}
