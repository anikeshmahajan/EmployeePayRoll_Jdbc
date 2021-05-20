package com.bridgelabz.payrollService;

import static org.junit.Assert.*;

import java.time.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

import com.bridgelabz.payrollService.EmployeePayrollMain.IOCommand;
import com.bridgelabz.payrollService.Exception.EmployeePayrollException;
import com.bridgelabz.payrollService.Models.EmployeePayrollData;
import com.bridgelabz.payrollService.io.EmployeePayrollDBIO.StatementType;

public class EmployeePayrollTest {
	
	EmployeePayrollMain employeeFunction;
	List<EmployeePayrollData> employeePayrollData;
	
	@Before
	public void init() {
		employeeFunction=new EmployeePayrollMain();
		employeePayrollData = employeeFunction.readData(IOCommand.DB_IO);
	}

	@Test
	public void given3Employees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmp = {
				new EmployeePayrollData(1,"Jeff Bezos",100000.0),
				new EmployeePayrollData(2, "Bill Gates",200000.0),
				new EmployeePayrollData(3, "Mark Zuckerberg",300000.0)
		};
		employeeFunction.setEmployeeDataList(Arrays.asList(arrayOfEmp));
		employeeFunction.writeEmployeeData(IOCommand.FILE_IO);
		long entries = employeeFunction.countEntries(IOCommand.FILE_IO);
		employeeFunction.printData();
		List<EmployeePayrollData> employeeList = employeeFunction.readData(IOCommand.FILE_IO);
		System.out.println(employeeList);
		assertEquals(3, entries);
	}
	
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() throws EmployeePayrollException {
		employeeFunction.updateEmployeeSalary("Terisa",3000000.00,StatementType.STATEMENT);
		assertTrue(employeeFunction.checkEmployeePayrollInSyncWithDB("Terisa"));
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDatabase() throws EmployeePayrollException {
		employeeFunction.updateEmployeeSalary("Terisa",3000000.00,StatementType.PREPARED_STATEMENT);
		assertTrue(employeeFunction.checkEmployeePayrollInSyncWithDB("Terisa"));
	}
	
	@Test
	public void givenDateRangeForEmployee_WhenRetrievedUsingStatement_ShouldReturnProperData() throws EmployeePayrollException {
<<<<<<< HEAD
		List<EmployeePayrollData> employeeDataInGivenDateRange = employeeFunction.getEmployeesInDateRange("2018-01-01","2021-05-20");
=======
		List<EmployeePayrollData> employeeDataInGivenDateRange = employeeFunction.getEmployeesInDateRange("2018-01-01","2019-11-15");
>>>>>>> Uc2
		assertEquals(2, employeeDataInGivenDateRange.size());
	}
	
	@Test
	public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
		Map<String,Double> averageSalaryByGender  = employeeFunction.readAverageSalaryByGender(IOCommand.DB_IO);
		System.out.println(averageSalaryByGender);
		assertTrue(averageSalaryByGender.get("M").equals(2000000.00)&&
				averageSalaryByGender.get("F").equals(3000000.00));
	}
	
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
		employeeFunction.addEmployeeToPayroll("Mark",50000000.00,LocalDate.now(),"M");
		assertTrue(employeeFunction.checkEmployeePayrollInSyncWithDB("Mark"));
	}
	
	public void givenEmployeeData_ShouldPrintInstanceTime_ToConsole() {
		EmployeePayrollData[] arrayOfEmp = {
				new EmployeePayrollData(1, "Jeff Bezos", 100000.0, LocalDate.now()),
				new EmployeePayrollData(2, "Bill Gates", 200000.0, LocalDate.now()),
				new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0, LocalDate.now()),
				new EmployeePayrollData(4, "Sundar", 600000.0, LocalDate.now()),
				new EmployeePayrollData(5, "Mukesh", 500000.0, LocalDate.now()),
				new EmployeePayrollData(6, "Anil", 300000.0, LocalDate.now())
		};
		Instant start = Instant.now();
		employeeFunction.addEmployeeToPayroll(Arrays.asList(arrayOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: "+java.time.Duration.between(start, end));
	}
	
	public void givenEmployeeData_ShouldPrintInstanceTime_ToConsole_UsingThreads() {
		EmployeePayrollData[] arrayOfEmp = {
				new EmployeePayrollData(1, "Jeff Bezos", 100000.0, LocalDate.now()),
				new EmployeePayrollData(2, "Bill Gates", 200000.0, LocalDate.now()),
				new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0, LocalDate.now()),
				new EmployeePayrollData(4, "Sundar", 600000.0, LocalDate.now()),
				new EmployeePayrollData(5, "Mukesh", 500000.0, LocalDate.now()),
				new EmployeePayrollData(6, "Anil", 300000.0, LocalDate.now())
		};
		Instant start = Instant.now();
		employeeFunction.addEmployeesToPayrollUsingThreads(Arrays.asList(arrayOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: "+java.time.Duration.between(start, end));
	}
}