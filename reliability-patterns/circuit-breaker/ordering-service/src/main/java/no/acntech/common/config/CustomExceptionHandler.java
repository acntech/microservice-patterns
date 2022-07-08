package no.acntech.common.config;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx");

    @ExceptionHandler({CallNotPermittedException.class})
    public ResponseEntity<Object> handleServiceUnavailableExceptions(RuntimeException ex, WebRequest request) {
        var status = HttpStatus.SERVICE_UNAVAILABLE;
        var body = new LinkedHashMap<String, Object>();
        body.put("timestamp", resolveTimestamp());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", resolvePath(request));
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    private String resolveTimestamp() {
        return ZonedDateTime.now()
                .withZoneSameInstant(ZoneId.of("UTC"))
                .format(DATE_TIME_FORMATTER);
    }

    private String resolvePath(@Nullable final WebRequest request) {
        if (request instanceof final ServletWebRequest servletWebRequest) {
            final var httpServletRequest = servletWebRequest.getRequest();
            return request.getContextPath() + new UrlPathHelper().getPathWithinApplication(httpServletRequest);
        } else {
            return request != null && StringUtils.hasText(request.getContextPath()) ? request.getContextPath() : "/";
        }
    }
}
