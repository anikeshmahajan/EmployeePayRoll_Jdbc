package Emp_PayRoll_Jdbc;

import Emp_PayRoll_Jdbc.modal.EmployeePayrollData;
import Emp_PayRoll_Jdbc.payrollservice.EmployeePayrollService;
import Emp_PayRoll_Jdbc.payrollservice.EmployeePayrollService.IOService;

import org.junit.Assert;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmployees = {
                new EmployeePayrollData(1, "Anikesh Mahajan", 1000000.0),
                new EmployeePayrollData(2, "Abhilaksh Mahajan", 850000.0),
                new EmployeePayrollData(3, "Rajesh Gupta", 2000000.0) };

        EmployeePayrollService payrollServiceObject = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        payrollServiceObject.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        payrollServiceObject.printEmployeePayrollData();

        Assert.assertEquals(3, payrollServiceObject.countEnteries(EmployeePayrollService.IOService.FILE_IO));
    }

    @Test
    public void given3EmployeesWhenReadFromFileShouldMatchNumberOfEmployeeEntries() {

        EmployeePayrollService payrollServiceObject = new EmployeePayrollService();
        payrollServiceObject.readEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        int countOfEntriesRead = payrollServiceObject.sizeOfEmployeeList();
        Assert.assertEquals(3, countOfEntriesRead);
    }
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Assert.assertEquals(5, employeePayrollData.size());
    }
}
