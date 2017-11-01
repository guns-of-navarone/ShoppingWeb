package com.navarone.exception;

public class FatalShopException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FatalShopException() {
		
	}
	
	public FatalShopException(String code) {
		super(code);
	}

}
