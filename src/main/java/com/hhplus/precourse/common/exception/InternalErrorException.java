package com.hhplus.precourse.common.exception;

import com.hhplus.precourse.common.support.Status;

public class InternalErrorException extends ApplicationException {
    public InternalErrorException(Status status) {
        super(status);
    }

    public InternalErrorException(Status status, String message) {
        super(status, message);
    }

    public InternalErrorException(Status status, Throwable cause) {
        super(status, cause);
    }

    public InternalErrorException(Status status, String message, Throwable cause) {
        super(status, message, cause);
    }
}
