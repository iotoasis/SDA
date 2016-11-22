package com.pineone.icbms.sda.comm.conf;

import java.io.PrintStream;
import java.io.PrintWriter;


public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public static final int ERR_UNSPECIFIED = 0;
    public static final int ERR_BAD_CONFIGURATION = 50;
    public static final int ERR_TIME_BROKER_FAILURE = 70;
    public static final int ERR_CLIENT_ERROR = 100;
    public static final int ERR_COMMUNICATION_FAILURE = 200;
    public static final int ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION = 210;
    public static final int ERR_PERSISTENCE = 400;
    public static final int ERR_PERSISTENCE_JOB_DOES_NOT_EXIST = 410;
    public static final int ERR_PERSISTENCE_CALENDAR_DOES_NOT_EXIST = 420;
    public static final int ERR_PERSISTENCE_TRIGGER_DOES_NOT_EXIST = 430;
    public static final int ERR_PERSISTENCE_CRITICAL_FAILURE = 499;
    public static final int ERR_THREAD_POOL = 500;
    public static final int ERR_THREAD_POOL_EXHAUSTED = 510;
    public static final int ERR_THREAD_POOL_CRITICAL_FAILURE = 599;
    public static final int ERR_JOB_LISTENER = 600;
    public static final int ERR_JOB_LISTENER_NOT_FOUND = 610;
    public static final int ERR_TRIGGER_LISTENER = 700;
    public static final int ERR_TRIGGER_LISTENER_NOT_FOUND = 710;
    public static final int ERR_JOB_EXECUTION_THREW_EXCEPTION = 800;
    public static final int ERR_TRIGGER_THREW_EXCEPTION = 850;

    private Throwable cause;

    private int errorCode = ERR_UNSPECIFIED;

    public ConfigException() {
        super();
    }

    public ConfigException(String msg) {
        super(msg);
    }

    public ConfigException(String msg, int errorCode) {
        super(msg);
        setErrorCode(errorCode);
    }

    public ConfigException(Throwable cause) {
        super(cause.toString());
        setCause(cause);
    }

    public ConfigException(String msg, Throwable cause) {
        super(msg);
        setCause(cause);
    }

    public ConfigException(String msg, Throwable cause, int errorCode) {
        super(msg);
        setCause(cause);
        setErrorCode(errorCode);
    }

    private void setCause(Throwable cause) {
        if (ExceptionHelper.supportsNestedThrowable()) {
            ExceptionHelper.setCause(this, cause);
        } else {
            this.cause = cause;
        }
    }

    public Throwable getUnderlyingException() {
        return (ExceptionHelper.supportsNestedThrowable()) ?
            ExceptionHelper.getCause(this) : cause;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPersistenceError() {
        return (errorCode >= ERR_PERSISTENCE && errorCode <= ERR_PERSISTENCE + 99);
    }

    public boolean isThreadPoolError() {
        return (errorCode >= ERR_THREAD_POOL && errorCode <= ERR_THREAD_POOL + 99);
    }

    public boolean isClientError() {
        return (errorCode >= ERR_CLIENT_ERROR && errorCode <= ERR_CLIENT_ERROR + 99);
    }

    public boolean isConfigurationError() {
        return (errorCode >= ERR_BAD_CONFIGURATION && errorCode <= ERR_BAD_CONFIGURATION + 49);
    }

    public String toString() {
        Throwable cause = getUnderlyingException(); 
        if (cause == null || cause == this) {
            return super.toString();
        } else {
            return super.toString() + " [See nested exception: " + cause + "]";
        }
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        
        if (cause != null) {
            synchronized (out) {
                out.println("* Nested Exception (Underlying Cause) ---------------");
                cause.printStackTrace(out);
            }
        }
    }

    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        
        if (cause != null) {
            synchronized (out) {
                out.println("* Nested Exception (Underlying Cause) ---------------");
                cause.printStackTrace(out);
            }
        }
    }

}
