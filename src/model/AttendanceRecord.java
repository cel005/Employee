package model;

import java.io.Serializable;
import java.time.LocalDate;

public class AttendanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String employeeID;
    private LocalDate date;
    private String status;

    public AttendanceRecord(String employeeID,
                            LocalDate date,
                            String status) {
        this.employeeID = employeeID;
        this.date = date;
        this.status = status;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return date + " - " + status;
    }
}