package org.connect4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A DatabaseManager osztály kezeli az adatbázissal való műveleteket,
 * beleértve a pontszámok mentését, frissítését és lekérdezését.
 */
public final class DatabaseManager {
    /**
     * Az adatbázis fájl elérési útja.
     * Az SQLite adatbázis fájlt használjuk
     * a Connect 4 játék pontszámainak tárolására.
     */
    private static final String DB_URL = "jdbc:sqlite:connect4.db";
    // Az adatbázis fájl elérési útja

    // A konstruktor priváttá tétele,
    // hogy megakadályozza az osztály példányosítását
    private DatabaseManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Ellenőrzi, hogy létezik-e a szükséges tábla, ha nem, akkor létrehozza.
     */
    static void createTableIfNotExists() {
        // Az SQL parancs a tábla létrehozására, ha nem létezik
        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS high_scores ("
                        + "name TEXT PRIMARY KEY,"
                        + "score INTEGER"
                        + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Hiba történt a tábla létrehozása során: "
                    + e.getMessage());
        }
    }

    /**
     * Pontszám hozzáadása vagy frissítése
     * a játékos adatainak az adatbázisban.
     * Ha a játékos már létezik, frissíti a pontszámot,
     * különben új rekordot ad hozzá.
     *
     * @param playerName A játékos neve.
     * @param score A játékos pontszáma.
     */
    public static void addOrUpdateHighScore(final String playerName,
                                            final int score) {
        // Először ellenőrizzük, hogy a tábla létezik-e, ha nem, létrehozzuk
        createTableIfNotExists();

        // Az SQL parancs a pontszám ellenőrzésére
        String checkSql = "SELECT score FROM high_scores WHERE name = ?";

        // Az SQL parancs az új rekord beszúrására
        String insertSql =
                "INSERT INTO high_scores (name, score) VALUES (?, ?)";

        // Az SQL parancs a pontszám frissítésére
        String updateSql = "UPDATE high_scores SET score = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Ellenőrizzük, hogy a játékos létezik-e már
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, playerName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Ha létezik, frissítjük a pontszámot, ha szükséges
                    int existingScore = rs.getInt("score");
                    if (score > existingScore) {
                        try (PreparedStatement updateStmt =
                                     conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, score);
                            updateStmt.setString(2, playerName);
                            updateStmt.executeUpdate();
                        }
                    }
                } else {
                    // Ha nem létezik, új rekordot adunk hozzá
                    try (PreparedStatement insertStmt =
                                 conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, playerName);
                        insertStmt.setInt(2, score);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Hiba történt a pontszám mentése során: "
                    + e.getMessage());
        }
    }

    /**
     * A legjobb pontszámokat lekérdezi az adatbázisból.
     */
    public static void printHighScores() {
        // Az SQL parancs a legjobb pontszámok lekérdezésére
        String sql =
                "SELECT name, score "
                        + "FROM high_scores "
                        + "ORDER BY score DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nLegjobb pontszámok:");
            while (rs.next()) {
                // Játékos neve és pontszámának lekérdezése
                String playerName = rs.getString("name"); // Játékos neve
                int score = rs.getInt("score"); // Játékos pontszáma
                System.out.println(playerName + ": " + score);
            }
        } catch (SQLException e) {
            System.out.println("Hiba történt! " + e.getMessage());
        }
    }
}
