package br.com.damoreira.commons.exception.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@UtilityClass
public class StacktraceUtil {

    public static String getStacktrace(Throwable throwable) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
