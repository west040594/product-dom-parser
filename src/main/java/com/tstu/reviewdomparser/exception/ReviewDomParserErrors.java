package com.tstu.reviewdomparser.exception;

import com.tstu.commons.exception.ExceptionMessage;
import com.tstu.commons.exception.PrsErrorCode;

import java.util.Arrays;
import java.util.Optional;

public enum  ReviewDomParserErrors implements PrsErrorCode {

    UNSUPPORTED_REVIEW_SYSTEM(1, ExceptionMessage.UNSUPPORTED_REVIEW_SYSTEM_MSG),
    UNEXPECTED_ERROR(2, ExceptionMessage.UNEXPECTED_ERROR_MSG);

    private Integer errorCode;
    private String errorDescription;

    ReviewDomParserErrors(Integer errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public static ReviewDomParserErrors getByDescription(String errorDescription) {
        return Arrays.stream(values())
                .filter(reviewDomParserErrors -> reviewDomParserErrors.errorDescription.equals(errorDescription))
                .findFirst()
                .orElse(UNEXPECTED_ERROR);
    }
}
