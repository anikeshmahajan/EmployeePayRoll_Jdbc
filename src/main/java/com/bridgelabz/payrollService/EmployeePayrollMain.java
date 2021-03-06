package com.bridgelabz.payrollService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.bridgelabz.payrollService.Exception.EmployeePayrollException;
import com.bridgelabz.payrollService.Exception.EmployeePayrollException.ExceptionType;
import com.bridgelabz.payrollService.Models.EmployeePayrollData;
import com.bridgelabz.payrollService.io.EmployeePayrollDBIO;
import com.bridgelabz.payrollService.io.EmployeePayrollDBIO.StatementType;
import com.bridgelabz.payrollService.io.EmployeePayrollFileIO;

public class EmployeePayrollMain {

	public static enum IOCommand
	{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}

	//Declaring global var list of employee data
	public List<EmployeePayrollData> employeeDataList;
	private EmployeePayrollDBIO payrollDBobj;

	/**Setter for list containing Emp Data
	 * @param List containing Emp Data
	 */
	public void setEmployeeDataList(List<EmployeePayrollData> employeeDataList) {
		this.employeeDataList = employeeDataList;
	}

	/**Constructor For Main Class
	 * 
	 */
	public EmployeePayrollMain() {
		payrollDBobj = EmployeePayrollDBIO.getInstance();
	}

	/**Read Emp Data from console <br>
	 * Adds data to Employee Data List
	 */
	public void readEmployeeData() {
		Scanner consoleScanner=new Scanner(System.in);
		System.out.print("Enter Employee ID : ");
		int id = consoleScanner.nextInt();
		System.out.print("Enter Employee name : ");
		String name = consoleScanner.next();
		System.out.print("Enter Employee salary : ");
		double salary = consoleScanner.nextDouble();
		EmployeePayrollData employee=new EmployeePayrollData(id,name,salary);
		System.out.println(employee);
		employeeDataList.add(employee);
		consoleScanner.close();
	}

	/**Write Emp Data to console and file
	 * @param ioType <br> CONSOLE_IO or FILE_IO
	 */
	public void writeEmployeeData(IOCommand ioType) {
		if(ioType.equals(ioType.CONSOLE_IO)) {
			System.out.println("Writing Employee Payroll Data to Console.");
			for (EmployeePayrollData employee:employeeDataList) {
				employee.printDataFileIO();
			}
		}else if (ioType.equals(ioType.FILE_IO)){
			new EmployeePayrollFileIO().writeData(employeeDataList);
			System.out.println("Write in File");
		}
	}

	/**Method to print data to console
	 * 
	 */
	public void printData() {
		new EmployeePayrollFileIO().printData();
	}

	/**Method to count entries in file
	 * @param ioType
	 * @return NoOfEntries
	 */
	public int countEntries(IOCommand ioType) {
		if(ioType.equals(IOCommand.FILE_IO)) return new EmployeePayrollFileIO().countEntries();
		return 0;
	}

	/**Method to read data from file
	 * @return List containing Emp Data
	 */
	public List<EmployeePayrollData> readData(IOCommand ioType) {

		switch(ioType) {		
		case FILE_IO: return new EmployeePayrollFileIO().readData();
		case DB_IO: return new EmployeePayrollDBIO().readData();
		default: System.out.println("Unknown Source!");
		}
		return null;
	}

	public void updateEmployeeSalary(String name, double salary,StatementType type) throws EmployeePayrollException {
		int result = payrollDBobj.updateEmployeeData(name,salary,type);
		EmployeePayrollData employeePayrollData = null;
		if(result == 0)
			throw new EmployeePayrollException(ExceptionType.UPDATE_FAIL, "Update Failed");
		else 
			employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData!=null) {
			employeePayrollData.salary = salary;
		}
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData = this.employeeDataList.stream()
				.filter(employee->employee.name.equals(name))
				.findFirst()
				.orElse(null);
		return employeePayrollData;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<EmployeePayrollData> checkList = payrollDBobj.getEmployeePayrollData(name);
		return checkList.get(0).equals(getEmployeePayrollData(name));
	}

	public List<EmployeePayrollData> getEmployeesInDateRange(String date1, String date2) {
		List<EmployeePayrollData> employeeList = payrollDBobj.getEmployeesInGivenDateRange(date1,date2);
		return employeeList;
	}

	public Map<String, Double> readAverageSalaryByGender(IOCommand ioType) {
		if(ioType.equals(IOCommand.DB_IO)) 
			return payrollDBobj.getAverageSalaryByGender();
		return null;
	}

	public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
		employeeDataList.add(payrollDBobj.addEmployeeToPayroll(name,salary,startDate,gender));
	}

	public void addEmployeeToPayroll(List<EmployeePayrollData> EmpList) {
		for (EmployeePayrollData emp:EmpList) {
			employeeDataList.add(payrollDBobj.addEmployeeToPayroll(emp.name,emp.salary,emp.startDate,"M"));
		}
	}

	//Main Method
	public static void main(String[] args) {
		EmployeePayrollMain employeeFunction = new EmployeePayrollMain();
		employeeFunction.readEmployeeData();
		employeeFunction.writeEmployeeData(IOCommand.CONSOLE_IO);
		employeeFunction.writeEmployeeData(IOCommand.FILE_IO);
		employeeFunction.printData();

		System.out.println("Read from File.");
		for (EmployeePayrollData employee:employeeFunction.readData(IOCommand.FILE_IO)) {
			employee.printDataFileIO();
		}
		System.out.println();
		System.out.println("Read from DB.");
		for (EmployeePayrollData employee:employeeFunction.readData(IOCommand.DB_IO)) {
			employee.printDataDBIO();
		}
	}
}