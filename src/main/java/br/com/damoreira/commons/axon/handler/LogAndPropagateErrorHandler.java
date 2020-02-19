package br.com.damoreira.commons.axon.handler;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.*;

import static java.lang.String.format;

@Slf4j
public class LogAndPropagateErrorHandler implements ErrorHandler, ListenerInvocationErrorHandler {

    @Override
    public void onError(Exception exception, EventMessage<?> event, EventMessageHandler eventHandler) throws Exception {
        log.error(format("EventListener [%s] failed to handle event [%s] (%s). payload=%s", eventHandler.getTargetType().getSimpleName(), event.getIdentifier(), event.getPayloadType().getName(), event.getPayload()), exception);

        throw exception;
    }

    @Override
    public void handleError(ErrorContext errorContext) throws Exception {
        throw (Exception) errorContext.error();
    }
}
