package com.bridgelabz.payrollService.Exception;

public class EmployeePayrollException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3298605493968795430L;

	public enum ExceptionType
	{UPDATE_FAIL}

	public ExceptionType type;

	public EmployeePayrollException(ExceptionType type,String message) {
		super(message);
		this.type = type;
	}
}