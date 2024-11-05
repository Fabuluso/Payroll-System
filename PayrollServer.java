// PayrollServiceImpl.java
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class PayrollServiceImpl extends UnicastRemoteObject implements PayrollService {
    private Map<String, Employee> employeeDatabase;

    public PayrollServiceImpl() throws RemoteException {
        super();
        employeeDatabase = new HashMap<>(); // In-memory storage for demonstration
    }

    @Override
    public boolean registerEmployee(String firstName, String lastName, String icPassport) throws RemoteException {
        if (employeeDatabase.containsKey(icPassport)) {
            return false; // Username already exists
        }
        employeeDatabase.put(icPassport, new Employee(firstName, lastName, icPassport));
        return true; // Registration successful
    }

    @Override
    public boolean updateEmployeeDetails(String icPassport, String email, String contact) throws RemoteException {
        Employee employee = employeeDatabase.get(icPassport);
        if (employee != null) {
            employee.setEmail(email);
            employee.setContact(contact);
            return true;
        }
        return false;
    }

    @Override
    public double calculateGrossPay(String icPassport) throws RemoteException {
        Employee employee = employeeDatabase.get(icPassport);
        if (employee != null) {
            // Basic calculation for demonstration
            return employee.getBaseSalary() + (employee.getOvertimeHours() * 1.5 * (employee.getBaseSalary() / 160));
        }
        return 0.0;
    }

    @Override
    public String viewPayrollReport(String icPassport) throws RemoteException {
        Employee employee = employeeDatabase.get(icPassport);
        if (employee != null) {
            return employee.generatePayrollReport();
        }
        return "Employee not found.";
    }

    @Override
    public boolean updatePayrollData(String icPassport, double salary) throws RemoteException {
        Employee employee = employeeDatabase.get(icPassport);
        if (employee != null) {
            employee.setBaseSalary(salary);
            return true;
        }
        return false;
    }

    @Override
    public String generatePayrollReport() throws RemoteException {
        StringBuilder report = new StringBuilder();
        for (Employee emp : employeeDatabase.values()) {
            report.append(emp.generatePayslip()).append("\n");
        }
        return report.toString();
    }

    @Override
    public String viewEmployeeDetails(String icPassport) throws RemoteException {
        Employee employee = employeeDatabase.get(icPassport);
        if (employee != null) {
            return employee.toString();
        }
        return "Employee not found.";
    }
}

