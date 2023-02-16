package main.java.example.loggerinterface;

import burp.api.montoya.http.handler.HttpResponseReceived;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel extends AbstractTableModel {
    private final List<HttpResponseReceived> responseLog = new ArrayList<>();

    @Override
    public synchronized int getRowCount() {
        return responseLog.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column){
            case 0 -> "tool";
            case 1 -> "URL";
            default -> "";
        };
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex) {
        HttpResponseReceived responseReceived = responseLog.get(rowIndex);
        return switch (columnIndex)
                {
                    case 0 -> responseReceived.toolSource().toolType();
                    case 1 -> responseReceived.initiatingRequest().url();
                    default -> "";
                };
    }

    public synchronized void add(HttpResponseReceived responseReceived){
        int index = responseLog.size();
        responseLog.add(responseReceived);
        fireTableRowsInserted(index,index); // what is this?
    }

    public synchronized HttpResponseReceived get(int rowIndex){
        return responseLog.get(rowIndex);
    }
}
