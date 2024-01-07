package no.acntech.common.error;

import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import no.acntech.error.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@Component
public class GrpcErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcErrorHandler.class);

    public StatusRuntimeException onError(final Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);

        final var httpStatus = this.resolveHttpStatus(throwable);
        final var errorResponse = buildErrorResponse(throwable, httpStatus);
        final var metadata = buildMetadata(errorResponse);

        switch (httpStatus) {
            case BAD_REQUEST -> {
                return Status.INVALID_ARGUMENT
                        .withDescription(throwable.getMessage())
                        .withCause(throwable)
                        .asRuntimeException(metadata);
            }
            case NOT_FOUND -> {
                return Status.NOT_FOUND
                        .withDescription(throwable.getMessage())
                        .withCause(throwable)
                        .asRuntimeException(metadata);
            }
            default -> {
                return Status.UNKNOWN
                        .withDescription(throwable.getMessage())
                        .withCause(throwable)
                        .asRuntimeException(metadata);
            }
        }
    }

    private HttpStatus resolveHttpStatus(final Throwable throwable) {
        final var responseStatusAnnotation = MergedAnnotations
                .from(throwable.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .get(ResponseStatus.class);
        return responseStatusAnnotation.getValue("code", HttpStatus.class)
                .orElseGet(() -> resolveHttpStatusFromException(throwable));
    }

    private HttpStatus resolveHttpStatusFromException(final Throwable throwable) {
        if (IllegalArgumentException.class.isAssignableFrom(throwable.getClass())) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private Metadata buildMetadata(final ErrorResponse errorResponse) {
        final var errorResponseKey = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance());
        final var metadata = new Metadata();
        metadata.put(errorResponseKey, errorResponse);
        return metadata;
    }

    private ErrorResponse buildErrorResponse(final Throwable throwable,
                                             final HttpStatus httpStatus) {
        final var timestamp = Instant.now();
        return ErrorResponse.newBuilder()
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(timestamp.getEpochSecond())
                        .setNanos(timestamp.getNano())
                        .build())
                .setStatus(UInt32Value.of(httpStatus.value()))
                .setError(StringValue.of(httpStatus.getReasonPhrase()))
                .setMessage(StringValue.of(throwable.getMessage()))
                .build();
    }
}
