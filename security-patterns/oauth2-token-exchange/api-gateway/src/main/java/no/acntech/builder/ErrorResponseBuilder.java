package no.acntech.builder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;

import java.net.URI;
import java.time.Instant;

public final class ErrorResponseBuilder {

    private URI type = URI.create("about:blank");
    private final HttpStatus httpStatus;
    private String detail;
    private URI instance;

    private ErrorResponseBuilder(final HttpStatus httpStatus) {
        Assert.notNull(httpStatus, "HttpStatus is null");
        this.httpStatus = httpStatus;
    }

    public ErrorResponseBuilder type(URI type) {
        Assert.notNull(type, "Type is null");
        this.type = type;
        return this;
    }

    public ErrorResponseBuilder detail(String detail) {
        Assert.hasText(detail, "Detail is null");
        this.detail = detail;
        return this;
    }

    public ErrorResponseBuilder instance(URI instance) {
        Assert.notNull(instance, "Instance is null");
        this.instance = instance;
        return this;
    }

    public ProblemDetail build() {
        final var target = ProblemDetail.forStatus(httpStatus);
        target.setType(type);
        target.setTitle(httpStatus.getReasonPhrase());
        target.setDetail(detail);
        target.setInstance(instance);
        target.setProperty("timestamp", Instant.now());
        return target;
    }

    public static ErrorResponseBuilder with(final HttpStatus httpStatus) {
        return new ErrorResponseBuilder(httpStatus);
    }
}
