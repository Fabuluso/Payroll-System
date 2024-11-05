// PayrollService.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PayrollService extends Remote {
    // Employee functionalities
    boolean registerEmployee(String firstName, String lastName, String icPassport) throws RemoteException;
    boolean updateEmployeeDetails(String icPassport, String email, String contact) throws RemoteException;
    double calculateGrossPay(String icPassport) throws RemoteException;
    String viewPayrollReport(String icPassport) throws RemoteException;

    // HR functionalities
    boolean updatePayrollData(String icPassport, double salary) throws RemoteException;
    String generatePayrollReport() throws RemoteException;
    String viewEmployeeDetails(String icPassport) throws RemoteException;
}
