package com.pineone.icbms.sda.comm.conf;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 예외상황 처리 핸들러
 */
public class ExceptionHelper {
    private static Boolean supportsNestedThrowable = null;
    
    private ExceptionHelper() {
    }

    /**
     * cause 설정
     * @param exception
     * @param cause
     * @return Throwable
     */
    public static synchronized Throwable setCause(Throwable exception, Throwable cause) {
        if (exception != null) {
            if (supportsNestedThrowable()) {
                try {
                    Method initCauseMethod = 
                        exception.getClass().getMethod("initCause", new Class[] {Throwable.class});
                    initCauseMethod.invoke(exception, new Object[] {cause});
                } catch (Exception e) {
                    getLog().warn(
                        "Unable to invoke initCause() method on class: " + 
                        exception.getClass().getName(), e);
                }
            }
        }        
        return exception;
    }
    
    /**
     * cause 가져오기
     * @param exception
     * @return Throwable
     */
    public static synchronized Throwable getCause(Throwable exception) {
        if (supportsNestedThrowable()) {
            try {
                Method getCauseMethod = 
                    exception.getClass().getMethod("getCause", (Class[])null);
                return (Throwable)getCauseMethod.invoke(exception, (Object[])null);
            } catch (Exception e) {
                getLog().warn(
                    "Unable to invoke getCause() method on class: " + 
                    exception.getClass().getName(), e);
            }
        }
        
        return null;
    }
    
    /**
     * Nested Throwable 지원여부
     * @return
     * @return boolean
     */
    public static synchronized boolean supportsNestedThrowable() {
        if (supportsNestedThrowable == null) {
            try {
                Throwable.class.getMethod("initCause", new Class[] {Throwable.class});
                Throwable.class.getMethod("getCause", (Class[])null);
                supportsNestedThrowable = Boolean.TRUE;
                getLog().debug("Detected JDK support for nested exceptions.");
            } catch (NoSuchMethodException e) {
                supportsNestedThrowable = Boolean.FALSE;
                getLog().debug("Nested exceptions are not supported by this JDK.");
            }
        }
        
        return supportsNestedThrowable.booleanValue();
    }
    
    /**
     * 로거 가져오기
     * @return
     * @return Logger
     */
    private static Logger getLog() {
        return LoggerFactory.getLogger(ExceptionHelper.class);
    }
}
