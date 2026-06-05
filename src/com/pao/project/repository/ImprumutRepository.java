package com.pao.project.repository;

import com.pao.project.model.Cititor;
import com.pao.project.model.Imprumut;
import com.pao.project.model.Item;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImprumutRepository implements Repository<Imprumut, Integer> {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private final ItemRepository itemRepository = new ItemRepository();
    private final CititorRepository cititorRepository = new CititorRepository();

    @Override
    public void save(Imprumut imprumut) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement psCheck = connection.prepareStatement(
                        "SELECT COUNT(*) FROM imprumut WHERE item_id = ?")) {
                    psCheck.setString(1, imprumut.getItem().getId());
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            throw new SQLException("Item-ul este deja imprumutat: " + imprumut.getItem().getId());
                        }
                    }
                }

                try (PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO imprumut (cititor_cod, item_id, data_imprumut) VALUES (?, ?, ?)")) {
                    ps.setString(1, imprumut.getCititor().getCod());
                    ps.setString(2, imprumut.getItem().getId());
                    ps.setString(3, imprumut.getDataImprumut().toString());
                    ps.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea împrumutului.", e);
        }
    }

    @Override
    public Optional<Imprumut> findById(Integer id) {
        String sql = """
                SELECT imp.id, imp.cititor_cod, imp.item_id, imp.data_imprumut
                FROM imprumut imp
                WHERE imp.id = ?
                """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea împrumutului.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Imprumut> findAll() {
        String sql = """
                SELECT imp.id, imp.cititor_cod, imp.item_id, imp.data_imprumut
                FROM imprumut imp
                ORDER BY imp.data_imprumut DESC
                """;
        List<Imprumut> imprumuturi = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                imprumuturi.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea împrumuturilor.", e);
        }
        return imprumuturi;
    }

    @Override
    public void update(Imprumut imprumut) {
        String sql = """
                UPDATE imprumut
                SET cititor_cod = ?, item_id = ?, data_imprumut = ?
                WHERE id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, imprumut.getCititor().getCod());
            ps.setString(2, imprumut.getItem().getId());
            ps.setString(3, imprumut.getDataImprumut().toString());
            ps.setInt(4, imprumut.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea împrumutului.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM imprumut WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea împrumutului.", e);
        }
    }

    public void deleteByItemId(String itemId) {
        String sql = "DELETE FROM imprumut WHERE item_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la returnarea itemului.", e);
        }
    }

    public boolean isItemImprumutat(String itemId) {
        String sql = "SELECT COUNT(*) FROM imprumut WHERE item_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la verificarea disponibilității.", e);
        }
    }

    public List<Imprumut> findByCititorCod(String codCititor) {
        String sql = """
                SELECT imp.id, imp.cititor_cod, imp.item_id, imp.data_imprumut
                FROM imprumut imp
                WHERE imp.cititor_cod = ?
                ORDER BY imp.data_imprumut DESC
                """;
        List<Imprumut> imprumuturi = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, codCititor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    imprumuturi.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea împrumuturilor cititorului.", e);
        }
        return imprumuturi;
    }

    public List<String> findImprumuturiActiveCuDetalii() {
        String sql = """
                SELECT imp.id, c.cod, p.nume, p.prenume, i.titlu, imp.data_imprumut
                FROM imprumut imp
                JOIN cititor c ON imp.cititor_cod = c.cod
                JOIN persoana p ON c.id = p.id
                JOIN item i ON imp.item_id = i.id
                ORDER BY imp.data_imprumut DESC
                """;
        List<String> rezultate = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add("[" + rs.getInt("id") + "] " +
                        rs.getString("titlu") + " -> " +
                        rs.getString("nume") + " " + rs.getString("prenume") +
                        " (" + rs.getString("cod") + ") la " + rs.getString("data_imprumut"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea împrumuturilor active.", e);
        }
        return rezultate;
    }

    private Imprumut mapRow(ResultSet rs) throws SQLException {
        String codCititor = rs.getString("cititor_cod");
        String itemId = rs.getString("item_id");

        Cititor cititor = cititorRepository.findByCod(codCititor)
                .orElseThrow(() -> new SQLException("Cititor inexistent: " + codCititor));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new SQLException("Item inexistent: " + itemId));

        LocalDate data = LocalDate.parse(rs.getString("data_imprumut"));
        return new Imprumut(rs.getInt("id"), cititor, item, data);
    }
}
