package model;

import utils.FileDBHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AttendanceManager {

    private List<Employee> employees;
    private List<AttendanceRecord> records;

    public AttendanceManager() {
        try {
            employees = FileDBHelper.load(
                    "employees.dat",
                    new ArrayList<Employee>()
            );

            records = FileDBHelper.load(
                    "attendance.dat",
                    new ArrayList<AttendanceRecord>()
            );

        } catch (IOException | ClassNotFoundException e) {
            employees = new ArrayList<>();
            records = new ArrayList<>();
        }
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public List<AttendanceRecord> getRecords() {
        return new ArrayList<>(records);
    }

    public Employee findEmployee(String employeeID) {
        if (employeeID == null) {
            return null;
        }

        for (Employee employee : employees) {
            if (employee.getEmployeeID().equalsIgnoreCase(
                    employeeID.trim())) {
                return employee;
            }
        }

        return null;
    }

    public void addEmployee(Employee employee) throws IOException {
        validateEmployee(employee);

        if (findEmployee(employee.getEmployeeID()) != null) {
            throw new IllegalArgumentException(
                    "Employee ID already exists."
            );
        }

        employees.add(employee);
        saveData();
    }

    public void updateEmployee(String originalID,
                               Employee updated)
            throws IOException {

        validateEmployee(updated);

        Employee existing = findEmployee(originalID);

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Employee not found."
            );
        }

        String newID = updated.getEmployeeID().trim();

        Employee duplicate = findEmployee(newID);

        if (duplicate != null && duplicate != existing) {
            throw new IllegalArgumentException(
                    "Employee ID already exists."
            );
        }

        // Keep the original ID if attendance records already
        // refer to it. This preserves the attendance history.
        boolean hasHistory = false;

        for (AttendanceRecord record : records) {
            if (record.getEmployeeID().equalsIgnoreCase(originalID)) {
                hasHistory = true;
                break;
            }
        }

        if (hasHistory && !existing.getEmployeeID().equalsIgnoreCase(newID)) {
            throw new IllegalArgumentException(
                    "Employee ID cannot be changed because attendance "
                    + "records already exist for this employee."
            );
        }

        existing.setEmployeeID(newID);
        existing.setFullName(updated.getFullName().trim());
        existing.setDepartment(updated.getDepartment().trim());
        existing.setPosition(updated.getPosition().trim());

        saveData();
    }

    public void deleteEmployee(String employeeID) throws IOException {
        Employee employee = findEmployee(employeeID);

        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee not found."
            );
        }

        employees.remove(employee);

        // Historical attendance records are intentionally retained.
        saveData();
    }

    public List<Employee> searchEmployees(String query) {
        List<Employee> result = new ArrayList<>();

        String text = query == null
                ? ""
                : query.trim().toLowerCase(Locale.ROOT);

        for (Employee employee : employees) {
            if (employee.getEmployeeID().toLowerCase(Locale.ROOT)
                    .contains(text)
                    || employee.getFullName().toLowerCase(Locale.ROOT)
                    .contains(text)
                    || employee.getDepartment().toLowerCase(Locale.ROOT)
                    .contains(text)
                    || employee.getPosition().toLowerCase(Locale.ROOT)
                    .contains(text)) {

                result.add(employee);
            }
        }

        return result;
    }

    public void markAttendance(String employeeID, String status)
            throws IOException {

        Employee employee = findEmployee(employeeID);

        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee not found."
            );
        }

        if (status == null
                || !(status.equalsIgnoreCase("Present")
                || status.equalsIgnoreCase("Absent")
                || status.equalsIgnoreCase("Late"))) {

            throw new IllegalArgumentException(
                    "Status must be Present, Absent, or Late."
            );
        }

        String normalizedStatus =
                status.substring(0, 1).toUpperCase(Locale.ROOT)
                + status.substring(1).toLowerCase(Locale.ROOT);

        LocalDate today = LocalDate.now();

        for (AttendanceRecord record : records) {
            if (record.getEmployeeID().equalsIgnoreCase(employeeID)
                    && record.getDate().equals(today)) {

                record.setStatus(normalizedStatus);
                saveData();
                return;
            }
        }

        records.add(new AttendanceRecord(
                employee.getEmployeeID(),
                today,
                normalizedStatus
        ));

        saveData();
    }

    public String getTodayStatus(String employeeID) {
        LocalDate today = LocalDate.now();

        for (AttendanceRecord record : records) {
            if (record.getEmployeeID().equalsIgnoreCase(employeeID)
                    && record.getDate().equals(today)) {
                return record.getStatus();
            }
        }

        return "Not Marked";
    }

    public List<AttendanceRecord> getEmployeeHistory(String employeeID) {
        List<AttendanceRecord> history = new ArrayList<>();

        for (AttendanceRecord record : records) {
            if (record.getEmployeeID().equalsIgnoreCase(employeeID)) {
                history.add(record);
            }
        }

        Collections.sort(history,
                new Comparator<AttendanceRecord>() {
                    @Override
                    public int compare(AttendanceRecord a,
                                       AttendanceRecord b) {
                        return b.getDate().compareTo(a.getDate());
                    }
                });

        return history;
    }

    // SORTING METHODS

    public List<Employee> sortByEmployeeID() {
        List<Employee> sorted = getEmployees();

        Collections.sort(sorted,
                Comparator.comparing(
                        Employee::getEmployeeID,
                        String.CASE_INSENSITIVE_ORDER
                ));

        return sorted;
    }

    public List<Employee> sortByName() {
        List<Employee> sorted = getEmployees();

        Collections.sort(sorted,
                Comparator.comparing(
                        Employee::getFullName,
                        String.CASE_INSENSITIVE_ORDER
                ));

        return sorted;
    }

    public List<Employee> sortByDepartment() {
        List<Employee> sorted = getEmployees();

        Collections.sort(sorted,
                Comparator.comparing(
                        Employee::getDepartment,
                        String.CASE_INSENSITIVE_ORDER
                ));

        return sorted;
    }

    public List<Employee> sortByAttendanceStatus() {
        List<Employee> sorted = getEmployees();

        Collections.sort(sorted,
                Comparator.comparing(
                        (Employee e) -> getTodayStatus(e.getEmployeeID()),
                        String.CASE_INSENSITIVE_ORDER
                ).thenComparing(
                        Employee::getFullName,
                        String.CASE_INSENSITIVE_ORDER
                ));

        return sorted;
    }

    public void saveData() throws IOException {
        FileDBHelper.save(employees, "employees.dat");
        FileDBHelper.save(records, "attendance.dat");
    }

    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee information is required."
            );
        }

        if (isBlank(employee.getEmployeeID())
                || isBlank(employee.getFullName())
                || isBlank(employee.getDepartment())
                || isBlank(employee.getPosition())) {

            throw new IllegalArgumentException(
                    "All employee fields are required."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}