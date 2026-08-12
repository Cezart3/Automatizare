package test;

import db.DatabaseManager;
import java.sql.*;

public class TestSQLiteStructure {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseManager.getConnection();

            // Verifică toate tabelele
            System.out.println("=== TABELE ÎN BAZA DE DATE ===");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Tabel: " + tableName);

                // Verifică coloanele pentru fiecare tabel
                ResultSet columns = meta.getColumns(null, null, tableName, null);
                System.out.println("  Coloane:");
                while (columns.next()) {
                    System.out.println("    - " + columns.getString("COLUMN_NAME") +
                            " (" + columns.getString("TYPE_NAME") + ")");
                }
                columns.close();

                // Verifică primele 3 înregistrări
                System.out.println("  Date exemplu:");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 3")) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.println("      " + rsmd.getColumnName(i) + ": " + rs.getString(i));
                        }
                        System.out.println("      ---");
                    }
                } catch (SQLException e) {
                    System.out.println("    Nu s-au putut citi datele: " + e.getMessage());
                }
            }
            tables.close();

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}