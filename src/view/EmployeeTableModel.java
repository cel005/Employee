package view;

import model.Employee;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Employee ID", "Full Name", "Department", "Position"
    };

    private List<Employee> employees = new ArrayList<>();

    public void setEmployees(List<Employee> employees) {
        this.employees = new ArrayList<>(employees);
        fireTableDataChanged();
    }

    public Employee getEmployeeAt(int row) {
        return employees.get(row);
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Employee employee = employees.get(row);

        switch (column) {
            case 0:
                return employee.getEmployeeID();
            case 1:
                return employee.getFullName();
            case 2:
                return employee.getDepartment();
            case 3:
                return employee.getPosition();
            default:
                return "";
        }
    }
}