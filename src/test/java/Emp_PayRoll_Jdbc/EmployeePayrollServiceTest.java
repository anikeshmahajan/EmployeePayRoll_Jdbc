package Emp_PayRoll_Jdbc;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import Emp_PayRoll_Jdbc.exceptions.DBException;
import Emp_PayRoll_Jdbc.modal.EmployeePayrollData;
import Emp_PayRoll_Jdbc.payrollservice.EmployeePayrollService;
import Emp_PayRoll_Jdbc.payrollservice.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmployees = {
                new EmployeePayrollData(1, "Anikesh Mahajan", 800000.0),
                new EmployeePayrollData(2, "Avhilaksh Mahajan", 850000.0),
                new EmployeePayrollData(3, "Rajesh Gupta", 900000.0)};

        EmployeePayrollService payrollServiceObject = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        payrollServiceObject.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        payrollServiceObject.printEmployeePayrollData();

        Assert.assertEquals(3, payrollServiceObject.countEnteries(EmployeePayrollService.IOService.FILE_IO));
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Assert.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }
}
