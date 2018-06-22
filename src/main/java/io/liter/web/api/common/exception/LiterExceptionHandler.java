package io.liter.web.api.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
public class LiterExceptionHandler implements WebExceptionHandler {

    private ObjectMapper objectMapper;

    public LiterExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof LiterException) {
            LiterException literException = (LiterException) ex;
            log.debug("]-----] UserExceptionHandler::handle httpStatus [-----[ {}", literException.getHttpStatus());
            exchange.getResponse().setStatusCode(literException.getHttpStatus());
            LiterErrors errors = new LiterErrors(literException.getErrorMessagerCode().getCode(), literException.getErrorMessagerCode().getResponseValue());
            if (literException.getErrors() != null) {
                if (literException.getErrors().size() > 0) {
                    literException.getErrors().forEach(e -> errors.add(e.getPath(), e.getCode(), e.getMessage()));
                }
            }
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            try {
                DataBuffer db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errors));
                // marks the response as complete and forbids writing to it
                return exchange.getResponse().writeWith(Mono.just(db));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Mono.empty();
            }
        }
        return Mono.error(ex);
    }
}
