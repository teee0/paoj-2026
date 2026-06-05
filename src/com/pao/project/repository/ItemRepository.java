package com.pao.project.repository;

import com.pao.project.model.Autor;
import com.pao.project.model.Film;
import com.pao.project.model.Item;
import com.pao.project.model.ItemMuzica;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemRepository implements Repository<Item, String> {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @Override
    public void save(Item item) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer autorId = item.getAutor() != null && item.getAutor().getId() > 0
                        ? item.getAutor().getId()
                        : null;

                try (PreparedStatement psItem = connection.prepareStatement(
                        "INSERT INTO item (id, titlu, an_aparitie, autor_id) VALUES (?, ?, ?, ?)")) {
                    psItem.setString(1, item.getId());
                    psItem.setString(2, item.getTitlu());
                    psItem.setInt(3, item.getAnAparitie());
                    if (autorId == null) {
                        psItem.setNull(4, java.sql.Types.INTEGER);
                    } else {
                        psItem.setInt(4, autorId);
                    }
                    psItem.executeUpdate();
                }

                if (item instanceof Film film) {
                    try (PreparedStatement psFilm = connection.prepareStatement(
                            "INSERT INTO film (id, durata_minute) VALUES (?, ?)")) {
                        psFilm.setString(1, film.getId());
                        psFilm.setInt(2, film.getDurataMinute());
                        psFilm.executeUpdate();
                    }
                } else if (item instanceof ItemMuzica muzica) {
                    try (PreparedStatement psMuzica = connection.prepareStatement(
                            "INSERT INTO muzica (id, durata_minute) VALUES (?, ?)")) {
                        psMuzica.setString(1, muzica.getId());
                        psMuzica.setInt(2, muzica.getDurataMinute());
                        psMuzica.executeUpdate();
                    }
                } else {
                    throw new IllegalArgumentException("Tip de item nesuportat: " + item.getClass().getSimpleName());
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea itemului.", e);
        }
    }

    @Override
    public Optional<Item> findById(String id) {
        String sql = """
                SELECT i.id, i.titlu, i.an_aparitie, i.autor_id,
                       f.durata_minute AS durata_film,
                       m.durata_minute AS durata_muzica,
                       p.nume AS autor_nume, p.prenume AS autor_prenume,
                       p.an_nastere AS autor_an, a.domeniu
                FROM item i
                LEFT JOIN film f ON i.id = f.id
                LEFT JOIN muzica m ON i.id = m.id
                LEFT JOIN autor a ON i.autor_id = a.id
                LEFT JOIN persoana p ON a.id = p.id
                WHERE i.id = ?
                """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea itemului.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Item> findAll() {
        String sql = """
                SELECT i.id, i.titlu, i.an_aparitie, i.autor_id,
                       f.durata_minute AS durata_film,
                       m.durata_minute AS durata_muzica,
                       p.nume AS autor_nume, p.prenume AS autor_prenume,
                       p.an_nastere AS autor_an, a.domeniu
                FROM item i
                LEFT JOIN film f ON i.id = f.id
                LEFT JOIN muzica m ON i.id = m.id
                LEFT JOIN autor a ON i.autor_id = a.id
                LEFT JOIN persoana p ON a.id = p.id
                ORDER BY i.titlu
                """;
        List<Item> items = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea itemilor.", e);
        }
        return items;
    }

    @Override
    public void update(Item item) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer autorId = item.getAutor() != null && item.getAutor().getId() > 0
                        ? item.getAutor().getId()
                        : null;

                try (PreparedStatement psItem = connection.prepareStatement(
                        "UPDATE item SET titlu = ?, an_aparitie = ?, autor_id = ? WHERE id = ?")) {
                    psItem.setString(1, item.getTitlu());
                    psItem.setInt(2, item.getAnAparitie());
                    if (autorId == null) {
                        psItem.setNull(3, java.sql.Types.INTEGER);
                    } else {
                        psItem.setInt(3, autorId);
                    }
                    psItem.setString(4, item.getId());
                    psItem.executeUpdate();
                }

                if (item instanceof Film film) {
                    try (PreparedStatement psFilm = connection.prepareStatement(
                            "UPDATE film SET durata_minute = ? WHERE id = ?")) {
                        psFilm.setInt(1, film.getDurataMinute());
                        psFilm.setString(2, film.getId());
                        psFilm.executeUpdate();
                    }
                } else if (item instanceof ItemMuzica muzica) {
                    try (PreparedStatement psMuzica = connection.prepareStatement(
                            "UPDATE muzica SET durata_minute = ? WHERE id = ?")) {
                        psMuzica.setInt(1, muzica.getDurataMinute());
                        psMuzica.setString(2, muzica.getId());
                        psMuzica.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea itemului.", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM item WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea itemului.", e);
        }
    }

    public Optional<Item> findByTitlu(String titlu) {
        String sql = """
                SELECT i.id, i.titlu, i.an_aparitie, i.autor_id,
                       f.durata_minute AS durata_film,
                       m.durata_minute AS durata_muzica,
                       p.nume AS autor_nume, p.prenume AS autor_prenume,
                       p.an_nastere AS autor_an, a.domeniu
                FROM item i
                LEFT JOIN film f ON i.id = f.id
                LEFT JOIN muzica m ON i.id = m.id
                LEFT JOIN autor a ON i.autor_id = a.id
                LEFT JOIN persoana p ON a.id = p.id
                WHERE LOWER(i.titlu) = LOWER(?)
                """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titlu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea itemului după titlu.", e);
        }
        return Optional.empty();
    }

    public List<String> findTopItemeImprumutate(int limit) {
        String sql = """
                SELECT i.titlu, p.nume, p.prenume, COUNT(imp.id) AS nr_imprumuturi
                FROM item i
                LEFT JOIN imprumut imp ON i.id = imp.item_id
                LEFT JOIN autor a ON i.autor_id = a.id
                LEFT JOIN persoana p ON a.id = p.id
                GROUP BY i.id, i.titlu, p.nume, p.prenume
                ORDER BY nr_imprumuturi DESC, i.titlu
                LIMIT ?
                """;
        List<String> rezultate = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String autor = rs.getString("nume") != null
                            ? rs.getString("nume") + " " + rs.getString("prenume")
                            : "necunoscut";
                    rezultate.add(rs.getString("titlu") + " (" + autor + "): " +
                            rs.getInt("nr_imprumuturi") + " împrumuturi");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea top iteme împrumutate.", e);
        }
        return rezultate;
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        Autor autor = null;
        int autorId = rs.getInt("autor_id");
        if (!rs.wasNull()) {
            autor = new Autor(
                    autorId,
                    rs.getString("autor_nume"),
                    rs.getString("autor_prenume"),
                    rs.getInt("autor_an"),
                    rs.getString("domeniu")
            );
        }

        String id = rs.getString("id");
        String titlu = rs.getString("titlu");
        int anAparitie = rs.getInt("an_aparitie");

        int durataFilm = rs.getInt("durata_film");
        if (!rs.wasNull()) {
            return new Film(id, titlu, anAparitie, durataFilm, autor);
        }

        int durataMuzica = rs.getInt("durata_muzica");
        if (!rs.wasNull()) {
            return new ItemMuzica(id, titlu, anAparitie, durataMuzica, autor);
        }

        throw new SQLException("Itemul " + id + " nu este film sau muzică.");
    }
}
