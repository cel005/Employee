package Controller;

import model.AttendanceManager;
import model.AttendanceRecord;
import model.Employee;

import java.io.IOException;
import java.util.List;

public class AttendanceController {

    private final AttendanceManager manager;

    public AttendanceController() {
        manager = new AttendanceManager();
    }

    public List<Employee> getEmployees() {
        return manager.getEmployees();
    }

    public List<AttendanceRecord> getRecords() {
        return manager.getRecords();
    }

    public Employee findEmployee(String employeeID) {
        return manager.findEmployee(employeeID);
    }

    public List<Employee> searchEmployees(String query) {
        return manager.searchEmployees(query);
    }

    public List<AttendanceRecord> getEmployeeHistory(String employeeID) {
        return manager.getEmployeeHistory(employeeID);
    }

    public String getTodayStatus(String employeeID) {
        return manager.getTodayStatus(employeeID);
    }

    public void addEmployee(Employee employee) throws IOException {
        manager.addEmployee(employee);
    }

    public void updateEmployee(String originalID, Employee employee)
            throws IOException {
        manager.updateEmployee(originalID, employee);
    }

    public void deleteEmployee(String employeeID) throws IOException {
        manager.deleteEmployee(employeeID);
    }

    public void markAttendance(String employeeID, String status)
            throws IOException {
        manager.markAttendance(employeeID, status);
    }

    public List<Employee> sortByEmployeeID() {
        return manager.sortByEmployeeID();
    }

    public List<Employee> sortByName() {
        return manager.sortByName();
    }

    public List<Employee> sortByDepartment() {
        return manager.sortByDepartment();
    }

    public List<Employee> sortByAttendanceStatus() {
        return manager.sortByAttendanceStatus();
    }
}