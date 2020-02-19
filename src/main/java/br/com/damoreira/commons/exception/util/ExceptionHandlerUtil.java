package br.com.damoreira.commons.exception.util;

import br.com.damoreira.commons.exception.ExceptionWrapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.concurrent.CompletionException;

@UtilityClass
public class ExceptionHandlerUtil {

    /**
     * Deserialize the {@link Throwable}'s message to a original Exception.
     * <p>
     * If the {@link Throwable}'s message is not a serialized json, then build from the {@link Throwable} sent by parameter.
     *
     * @param throwable Exception that can have a serialized {@link ExceptionWrapper} object on message.
     */
    public static Throwable getOriginalException(Throwable throwable) {
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }
        try {
            ExceptionWrapper exceptionWrapper = ExceptionWrapper.fromJson(throwable.getMessage());
            return exceptionWrapper.getOriginalException();
        } catch (IOException e) {
            return throwable;
        }
    }

}
