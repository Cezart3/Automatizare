package db;

import model.product.Product;
import model.glass.Glass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static db.DatabaseManager.getConnection;

public class ProductDAO {

    public List<Product> getProductsFromTable(String tableName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT CodProdus, Denumire FROM `" + tableName + "` GROUP BY CodProdus, Denumire";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("CodProdus"),
                        rs.getString("Denumire")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Eroare la citirea din tabelul " + tableName + ": " + e.getMessage());
        }

        return products;
    }

    public String getProductImage(String tableName, String productCode) {
        String sql = "SELECT image_url FROM `" + tableName + "` WHERE CodProdus = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("image_url");
                }
            }

        } catch (SQLException e) {
            System.err.println("Eroare la citirea imaginii pentru " + productCode + " din tabelul " + tableName + ": " + e.getMessage());
        }

        return null;
    }

    public double getPrice(String tableName, String codProdus, int materialId, int finishId) {
        String sql = "SELECT Price FROM `" + tableName + "` WHERE CodProdus = ? AND Material_Id = ? AND Finish_Id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codProdus);
            stmt.setInt(2, materialId);
            stmt.setInt(3, finishId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Price");
                }
            }

        } catch (SQLException e) {
            System.err.println("Eroare la citirea pretului pentru " + codProdus + " din tabelul " + tableName + ": " + e.getMessage());
        }

        return 0.0;
    }

    public boolean updatePrice(String tableName, String codProdus, int materialId, int finishId, double newPrice) {
        String sql = "UPDATE `" + tableName + "` SET Price = ? WHERE CodProdus = ? AND Material_Id = ? AND Finish_Id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setString(2, codProdus);
            stmt.setInt(3, materialId);
            stmt.setInt(4, finishId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Eroare la update pret: " + e.getMessage());
            return false;
        }
    }

    public List<String> getMaterials(String tableName, String codProdus) {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT Material_Id FROM `" + tableName + "` WHERE CodProdus = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ids.add(String.valueOf(rs.getInt("Material_Id")));
            }
        } catch (SQLException e) {
            System.err.println("Eroare la getMaterials: " + e.getMessage());
        }
        return ids;
    }

    public List<String> getFinishes(String tableName, String codProdus) {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT Finish_Id FROM `" + tableName + "` WHERE CodProdus = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ids.add(String.valueOf(rs.getInt("Finish_Id")));
            }
        } catch (SQLException e) {
            System.err.println("Eroare la getFinishes: " + e.getMessage());
        }
        return ids;
    }

    // METODĂ NOUĂ: inserează produs cu material, finisaj și image_url
    public boolean insertProduct(String tableName, String codProdus, String denumire, int materialId, int finishId, double price) {
        // Construim image_url automat bazat pe codul produsului
        String imageUrl = "Pictures/" + codProdus + ".jpg";

        String sql = "INSERT INTO `" + tableName + "` (CodProdus, Denumire, Material_Id, Finish_Id, Price, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            stmt.setString(2, denumire);
            stmt.setInt(3, materialId);
            stmt.setInt(4, finishId);
            stmt.setDouble(5, price);
            stmt.setString(6, imageUrl);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la insert produs: " + e.getMessage());
            return false;
        }
    }

    // METODĂ NOUĂ: șterge produs specific cu material și finisaj
    public boolean deleteProduct(String tableName, String codProdus, int materialId, int finishId) {
        String sql = "DELETE FROM `" + tableName + "` WHERE CodProdus = ? AND Material_Id = ? AND Finish_Id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            stmt.setInt(2, materialId);
            stmt.setInt(3, finishId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la delete produs: " + e.getMessage());
            return false;
        }
    }

    // Metodă pentru a șterge toate variantele unui produs (cu toate materialele și finisajele)
    public boolean deleteProductCompletely(String tableName, String codProdus) {
        String sql = "DELETE FROM `" + tableName + "` WHERE CodProdus = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la ștergerea completă a produsului: " + e.getMessage());
            return false;
        }
    }

    // Metode vechi păstrate pentru compatibilitate
    public boolean insertProduct(String tableName, String codProdus, String denumire) {
        return insertProduct(tableName, codProdus, denumire, 5, 10, 0.0); // Valori default
    }

    public boolean deleteProduct(String tableName, String codProdus) {
        return deleteProductCompletely(tableName, codProdus);
    }

    public List<Glass> getAllGlass() {
        GlassDAO dao = new GlassDAO();
        return dao.getAllGlasses();
    }
    // Adaugă aceste metode în ProductDAO.java

    public boolean updateGlass(Glass glass) {
        String sql = "UPDATE sticle SET simpla_debitata = ?, securizata_calita = ?, " +
                "manopera_slefuire = ?, manopera_gaurire_4_20 = ?, manopera_gaurire_21_30 = ?, " +
                "manopera_gaurire_31_60_cnc = ?, adaos_forma_proc = ?, adaos_sablon_proc = ?, " +
                "manopera_decupe_feron = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, glass.getSimplaDebitata());
            stmt.setDouble(2, glass.getSecurizataCalita());
            stmt.setDouble(3, glass.getManoperaSlefuire());
            stmt.setDouble(4, glass.getManoperaGaurire4_20());
            stmt.setDouble(5, glass.getManoperaGaurire21_30());
            stmt.setDouble(6, glass.getManoperaGaurire31_60_cnc());
            stmt.setDouble(7, glass.getAdaosFormaProc());
            stmt.setDouble(8, glass.getAdaosSablonProc());
            stmt.setDouble(9, glass.getManoperaDecupeFeron());
            stmt.setInt(10, glass.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Eroare la update sticlă: " + e.getMessage());
            return false;
        }
    }
    public boolean insertGlass(Glass glass) {
        String sql = "INSERT INTO sticle (id, tip_sticla, grosime_mm, simpla_debitata, securizata_calita, " +
                "manopera_slefuire, manopera_gaurire_4_20, manopera_gaurire_21_30, manopera_gaurire_31_60_cnc, " +
                "adaos_forma_proc, adaos_sablon_proc, manopera_decupe_feron) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, glass.getId());
            stmt.setString(2, glass.getTipSticla());
            stmt.setString(3, glass.getGrosimeMm());
            stmt.setDouble(4, glass.getSimplaDebitata());
            stmt.setDouble(5, glass.getSecurizataCalita());
            stmt.setDouble(6, glass.getManoperaSlefuire());
            stmt.setDouble(7, glass.getManoperaGaurire4_20());
            stmt.setDouble(8, glass.getManoperaGaurire21_30());
            stmt.setDouble(9, glass.getManoperaGaurire31_60_cnc());
            stmt.setDouble(10, glass.getAdaosFormaProc());
            stmt.setDouble(11, glass.getAdaosSablonProc());
            stmt.setDouble(12, glass.getManoperaDecupeFeron());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Eroare la inserarea sticlei: " + e.getMessage());
            return false;
        }
    }

    // Metodă pentru ștergerea sticlei
    public boolean deleteGlass(int glassId) {
        String sql = "DELETE FROM sticle WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, glassId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Eroare la ștergerea sticlei: " + e.getMessage());
            return false;
        }
    }


    public List<String> getAllTableNames() {
        List<String> tables = new ArrayList<>();
        String sql = "SHOW TABLES";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea tabelelor: " + e.getMessage());
        }
        return tables;
    }

    public List<String> getMaterialNames(String tableName, String codProdus) {
        List<String> materials = new ArrayList<>();
        String sql = "SELECT DISTINCT m.IDMaterial, m.Nume " +
                "FROM " + tableName + " t " +
                "JOIN materiale m ON t.MaterialID = m.IDMaterial " +
                "WHERE t.CodProdus = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                materials.add(rs.getString("Nume")); // Sau coloana corectă
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea denumirilor materialelor pentru " + codProdus + " din " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return materials;
    }

    public List<String> getFinishNames(String tableName, String codProdus) {
        List<String> finishes = new ArrayList<>();
        String sql = "SELECT DISTINCT f.IDFinisaj, f.Nume " +
                "FROM " + tableName + " t " +
                "JOIN finisaje f ON t.FinisajID = f.IDFinisaj " +
                "WHERE t.CodProdus = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                finishes.add(rs.getString("Nume")); // Sau coloana corectă
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea denumirilor finisajelor pentru " + codProdus + " din " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return finishes;
    }

    public int getMaterialIdByName(String tableName, String codProdus, String materialName) {
        String sql = "SELECT m.ID " +
                "FROM `" + tableName + "` p " +
                "JOIN materials m ON p.Material_Id = m.ID " +
                "WHERE p.CodProdus = ? AND m.Denumire = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            stmt.setString(2, materialName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la getMaterialIdByName: " + e.getMessage());
        }
        return 0;
    }

    public int getFinishIdByName(String tableName, String codProdus, String finishName) {
        String sql = "SELECT f.ID " +
                "FROM `" + tableName + "` p " +
                "JOIN finishes f ON p.Finish_Id = f.ID " +
                "WHERE p.CodProdus = ? AND f.Denumire = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codProdus);
            stmt.setString(2, finishName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la getFinishIdByName: " + e.getMessage());
        }
        return 0;
    }
}