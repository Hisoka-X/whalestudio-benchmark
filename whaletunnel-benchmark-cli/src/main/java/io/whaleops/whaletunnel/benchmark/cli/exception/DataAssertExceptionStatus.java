package io.whaleops.whaletunnel.benchmark.cli.exception;

public enum DataAssertExceptionStatus implements ExceptionStatus {
    ROW_COUNT_DOES_NOT_MATCH("DataAssertException-001", "row count does not match"),
    ;

    private final String code;
    private final String message;

    DataAssertExceptionStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
