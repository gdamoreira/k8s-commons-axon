package br.com.damoreira.commons.axon.interceptor;

import br.com.damoreira.commons.exception.CommonException;
import br.com.damoreira.commons.exception.ExceptionWrapper;
import br.com.damoreira.commons.exception.ValidationException;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.axonframework.messaging.unitofwork.UnitOfWork;

/**
 * Catches all exceptions on message handling process, serializes as a json and throws back to Axon as a exception message.
 */
public class ExceptionHandlerInterceptor implements MessageHandlerInterceptor<Message<?>> {

    @Override
    public Object handle(UnitOfWork<? extends Message<?>> unitOfWork, InterceptorChain interceptorChain) {
        try {
            return interceptorChain.proceed();
        } catch (JSR303ViolationException e) {
            throw new RuntimeException(new ExceptionWrapper(new ValidationException(e.getViolations())).toJson());
        } catch (CommonException e) {
            throw new RuntimeException(new ExceptionWrapper(e).toJson());
        } catch (Throwable e) {
            throw new RuntimeException(new ExceptionWrapper(e).toJson());
        }
    }

}