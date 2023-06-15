package io.whaleops.whaletunnel.benchmark.cli.exception;

public class CommandException extends RuntimeException {

    private CommandException(ExceptionStatus exceptionStatus) {
        super(String.format("CommandException[%s]: %s" + exceptionStatus.getCode(), exceptionStatus.getMessage()));
    }

    private CommandException(ExceptionStatus exceptionStatus, Throwable cause) {
        super(String.format("CommandException[%s]: %s" + exceptionStatus.getCode(), exceptionStatus.getMessage()), cause);
    }

    public static CommandException newCommandException(ExceptionStatus exceptionStatus) {
        return new CommandException(exceptionStatus);
    }

    public static CommandException newCommandException(ExceptionStatus exceptionStatus, Throwable throwable) {
        return new CommandException(exceptionStatus, throwable);
    }
}
