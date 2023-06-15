package io.whaleops.whaletunnel.benchmark.cli.exception;

public enum InitializeEnvCommandExceptionStatus implements ExceptionStatus {
    INITIALIZE_ENV_FILE_EXCEPTION_STATUS("001", "Initialize env file error"),

    ;

    private final String code;
    private final String message;

    InitializeEnvCommandExceptionStatus(String code, String message) {
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
