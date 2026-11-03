package view;

import model.AttendanceRecord;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AttendanceTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Employee ID", "Date", "Status"
    };

    private List<AttendanceRecord> records = new ArrayList<>();

    public void setRecords(List<AttendanceRecord> records) {
        this.records = new ArrayList<>(records);
        fireTableDataChanged();
    }

    public AttendanceRecord getRecordAt(int row) {
        return records.get(row);
    }

    @Override
    public int getRowCount() {
        return records.size();
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
        AttendanceRecord record = records.get(row);

        switch (column) {
            case 0:
                return record.getEmployeeID();
            case 1:
                return record.getDate();
            case 2:
                return record.getStatus();
            default:
                return "";
        }
    }
}