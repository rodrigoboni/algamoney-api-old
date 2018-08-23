package com.algaworks.algamoney.api.exceptionHandler;

public class ErrorMessage {
	private String message;
	private String exceptionCause;
	private String exceptionMessage;
	private StackTraceElement[] stackTrace;

	public ErrorMessage(String message, Exception exception) {
		this.message = message;
		this.exceptionCause = exception.getCause().toString();
		this.exceptionMessage = exception.getMessage();
		stackTrace = exception.getStackTrace();
	}

	public String getMessage() {
		return message;
	}

	public String getExceptionCause() {
		return exceptionCause;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
}
