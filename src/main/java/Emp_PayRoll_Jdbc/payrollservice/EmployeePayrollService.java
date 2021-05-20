package Emp_PayRoll_Jdbc.payrollservice;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Emp_PayRoll_Jdbc.exceptions.DBException;
import Emp_PayRoll_Jdbc.exceptions.SQLUpdateFailedException;
import Emp_PayRoll_Jdbc.ioservice.EmployeePayrollDBService;
import Emp_PayRoll_Jdbc.ioservice.FileIOService;
import Emp_PayRoll_Jdbc.modal.EmployeePayrollData;

public class EmployeePayrollService {
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    public static final Scanner SC = new Scanner(System.in);
    private List<EmployeePayrollData> employeePayrollDataList;

    public EmployeePayrollService() {
        this.employeePayrollDataList = new ArrayList<EmployeePayrollData>();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeeList) {
        this.employeePayrollDataList = employeeList;
    }

    public int sizeOfEmployeeList() {
        return this.employeePayrollDataList.size();
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollDataList = new EmployeePayrollDBService().readData();
        return this.employeePayrollDataList;
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result=new EmployeePayrollDBService().updateEmployeeData(name,salary);
        if(result==0) {
            try {
                throw new SQLUpdateFailedException("Query is failed.");
            } catch (SQLUpdateFailedException e) {
                e.printStackTrace();
            }
        }
        EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
        if (employeePayrollData!=null) employeePayrollData.salary=salary;

    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollDataList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.employeeName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) throws DBException {
        List<EmployeePayrollData> employeePayrollDataList=new EmployeePayrollDBService().getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }


    public void readEmployeeData(IOService ioType) {
        if (ioType.equals(IOService.CONSOLE_IO)) {
            System.out.println("Enter employee id:");
            int employeeId = SC.nextInt();
            System.out.println("Enter employee name:");
            SC.nextLine();
            String employeeName = SC.nextLine();
            System.out.println("Enter employee salary:");
            double employeeSalary = SC.nextDouble();
            EmployeePayrollData newEmployee = new EmployeePayrollData(employeeId, employeeName, employeeSalary);
            employeePayrollDataList.add(newEmployee);
        } else if (ioType.equals(IOService.FILE_IO)) {
            this.employeePayrollDataList = new FileIOService().readData();
        }
    }

    public void writeEmployeeData(IOService ioType) {
        if (ioType.equals(IOService.CONSOLE_IO)) {
            for (EmployeePayrollData o : employeePayrollDataList)
                System.out.println(o.toString());
        } else if (ioType.equals(IOService.FILE_IO)) {
            new FileIOService().writeData(employeePayrollDataList);
        }
    }

    public long countEnteries(IOService ioType) {
        if (ioType.equals(IOService.FILE_IO))
            return new FileIOService().countEntries();
        return 0;
    }

    public void printEmployeePayrollData() {
        new FileIOService().printEmployeePayrolls();
    }
}