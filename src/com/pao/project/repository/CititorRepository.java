package com.pao.project.repository;

import com.pao.project.model.Cititor;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CititorRepository implements Repository<Cititor, Integer> {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @Override
    public void save(Cititor cititor) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int persoanaId;
                try (PreparedStatement psPersoana = connection.prepareStatement(
                        "INSERT INTO persoana (nume, prenume, an_nastere) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    psPersoana.setString(1, cititor.getNume());
                    psPersoana.setString(2, cititor.getPrenume());
                    psPersoana.setInt(3, cititor.getAnNastere());
                    psPersoana.executeUpdate();

                    try (ResultSet keys = psPersoana.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Nu s-a putut genera id-ul persoanei.");
                        }
                        persoanaId = keys.getInt(1);
                    }
                }

                try (PreparedStatement psCititor = connection.prepareStatement(
                        "INSERT INTO cititor (id, cod) VALUES (?, ?)")) {
                    psCititor.setInt(1, persoanaId);
                    psCititor.setString(2, cititor.getCod());
                    psCititor.executeUpdate();
                }

                cititor.setId(persoanaId);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cititorului.", e);
        }
    }

    @Override
    public Optional<Cititor> findById(Integer id) {
        String sql = """
                SELECT p.id, p.nume, p.prenume, p.an_nastere, c.cod
                FROM cititor c
                JOIN persoana p ON c.id = p.id
                WHERE c.id = ?
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
            throw new RuntimeException("Eroare la căutarea cititorului.", e);
        }
        return Optional.empty();
    }

    public Optional<Cititor> findByCod(String cod) {
        String sql = """
                SELECT p.id, p.nume, p.prenume, p.an_nastere, c.cod
                FROM cititor c
                JOIN persoana p ON c.id = p.id
                WHERE c.cod = ?
                """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea cititorului după cod.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cititor> findAll() {
        String sql = """
                SELECT p.id, p.nume, p.prenume, p.an_nastere, c.cod
                FROM cititor c
                JOIN persoana p ON c.id = p.id
                ORDER BY p.nume, p.prenume
                """;
        List<Cititor> cititori = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cititori.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cititorilor.", e);
        }
        return cititori;
    }

    @Override
    public void update(Cititor cititor) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement psPersoana = connection.prepareStatement(
                        "UPDATE persoana SET nume = ?, prenume = ?, an_nastere = ? WHERE id = ?")) {
                    psPersoana.setString(1, cititor.getNume());
                    psPersoana.setString(2, cititor.getPrenume());
                    psPersoana.setInt(3, cititor.getAnNastere());
                    psPersoana.setInt(4, cititor.getId());
                    psPersoana.executeUpdate();
                }

                try (PreparedStatement psCititor = connection.prepareStatement(
                        "UPDATE cititor SET cod = ? WHERE id = ?")) {
                    psCititor.setString(1, cititor.getCod());
                    psCititor.setInt(2, cititor.getId());
                    psCititor.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cititorului.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM persoana WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea cititorului.", e);
        }
    }

    public void deleteByCod(String cod) {
        findByCod(cod).ifPresent(cititor -> delete(cititor.getId()));
    }

    public List<String> findCititoriCuNumarImprumuturi() {
        String sql = """
                SELECT c.cod, p.nume, p.prenume, COUNT(i.id) AS nr_imprumuturi
                FROM cititor c
                JOIN persoana p ON c.id = p.id
                LEFT JOIN imprumut i ON c.cod = i.cititor_cod
                GROUP BY c.cod, p.nume, p.prenume
                ORDER BY nr_imprumuturi DESC, p.nume
                """;
        List<String> rezultate = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(rs.getString("cod") + " - " +
                        rs.getString("nume") + " " + rs.getString("prenume") +
                        ": " + rs.getInt("nr_imprumuturi") + " împrumuturi active");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea cititorilor cu împrumuturi.", e);
        }
        return rezultate;
    }

    private Cititor mapRow(ResultSet rs) throws SQLException {
        return new Cititor(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getInt("an_nastere"),
                rs.getString("cod")
        );
    }
}
