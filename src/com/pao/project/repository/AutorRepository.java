package com.pao.project.repository;

import com.pao.project.model.Autor;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutorRepository implements Repository<Autor, Integer> {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @Override
    public void save(Autor autor) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int persoanaId;
                try (PreparedStatement psPersoana = connection.prepareStatement(
                        "INSERT INTO persoana (nume, prenume, an_nastere) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    psPersoana.setString(1, autor.getNume());
                    psPersoana.setString(2, autor.getPrenume());
                    psPersoana.setInt(3, autor.getAnNastere());
                    psPersoana.executeUpdate();

                    try (ResultSet keys = psPersoana.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Nu s-a putut genera id-ul persoanei.");
                        }
                        persoanaId = keys.getInt(1);
                    }
                }

                try (PreparedStatement psAutor = connection.prepareStatement(
                        "INSERT INTO autor (id, domeniu) VALUES (?, ?)")) {
                    psAutor.setInt(1, persoanaId);
                    psAutor.setString(2, autor.getDomeniu());
                    psAutor.executeUpdate();
                }

                autor.setId(persoanaId);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea autorului.", e);
        }
    }

    @Override
    public Optional<Autor> findById(Integer id) {
        String sql = """
                SELECT p.id, p.nume, p.prenume, p.an_nastere, a.domeniu
                FROM autor a
                JOIN persoana p ON a.id = p.id
                WHERE a.id = ?
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
            throw new RuntimeException("Eroare la căutarea autorului.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Autor> findAll() {
        String sql = """
                SELECT p.id, p.nume, p.prenume, p.an_nastere, a.domeniu
                FROM autor a
                JOIN persoana p ON a.id = p.id
                ORDER BY p.nume, p.prenume
                """;
        List<Autor> autori = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                autori.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea autorilor.", e);
        }
        return autori;
    }

    @Override
    public void update(Autor autor) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement psPersoana = connection.prepareStatement(
                        "UPDATE persoana SET nume = ?, prenume = ?, an_nastere = ? WHERE id = ?")) {
                    psPersoana.setString(1, autor.getNume());
                    psPersoana.setString(2, autor.getPrenume());
                    psPersoana.setInt(3, autor.getAnNastere());
                    psPersoana.setInt(4, autor.getId());
                    psPersoana.executeUpdate();
                }

                try (PreparedStatement psAutor = connection.prepareStatement(
                        "UPDATE autor SET domeniu = ? WHERE id = ?")) {
                    psAutor.setString(1, autor.getDomeniu());
                    psAutor.setInt(2, autor.getId());
                    psAutor.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea autorului.", e);
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
            throw new RuntimeException("Eroare la ștergerea autorului.", e);
        }
    }

    public Optional<Autor> findByNume(String nume) {
        String sql = """
                SELECT p.id, p.nume, p.prenume, p.an_nastere, a.domeniu
                FROM autor a
                JOIN persoana p ON a.id = p.id
                WHERE LOWER(p.nume) = LOWER(?)
                """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nume);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea autorului după nume.", e);
        }
        return Optional.empty();
    }

    private Autor mapRow(ResultSet rs) throws SQLException {
        return new Autor(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getInt("an_nastere"),
                rs.getString("domeniu")
        );
    }
}
