package db;

import model.glass.Glass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GlassDAO {

    public List<Glass> getAllGlasses() {
        List<Glass> sticle = new ArrayList<>();
        String query = "SELECT * FROM sticle ORDER BY id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sticle.add(new Glass(
                        rs.getInt("id"),
                        rs.getString("tip_sticla"),
                        rs.getString("grosime_mm"),
                        rs.getDouble("simpla_debitata"),
                        rs.getDouble("securizata_calita"),
                        rs.getDouble("manopera_slefuire"),
                        rs.getDouble("manopera_gaurire_4_20"),
                        rs.getDouble("manopera_gaurire_21_30"),
                        rs.getDouble("manopera_gaurire_31_60_cnc"),
                        rs.getDouble("adaos_forma_proc"),
                        rs.getDouble("adaos_sablon_proc"),
                        rs.getDouble("manopera_decupe_feron")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Eroare la citirea sticlelor: " + e.getMessage());
        }

        return sticle;
    }
}