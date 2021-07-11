package com.waracle.cakemgr.model;

public class NoModelException extends ModelException {

	private static final long serialVersionUID = 1L;

	public NoModelException() {
	}

	public NoModelException(String message) {
		super(message);
	}

	public NoModelException(Throwable cause) {
		super(cause);
	}

	public NoModelException(String message, Throwable cause) {
		super(message, cause);
	}

}
