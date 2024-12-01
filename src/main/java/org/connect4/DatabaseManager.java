package org.connect4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Az adatbázis-kezelésért felelős utility osztály.
 */
public final class DatabaseManager {

    /**
     * Az adatbázis URL-je.
     */
    private static final String DATABASE_URL =
            "jdbc:sqlite:connect4.db";

    private DatabaseManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Létrehozza az adatbázis-kapcsolatot
     * és biztosítja, hogy a szükséges táblák létezzenek.
     *
     * @return A kapcsolódó Connection objektum, vagy null, ha hiba történt.
     */
    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(DATABASE_URL);
            createTablesIfNotExist(conn); // Ellenőrizzük a táblák létét
            return conn;
        } catch (SQLException e) {
            System.out.println(
                    "Hiba az adatbázishoz való csatlakozáskor: "
                            + e.getMessage()
            );
            return null;
        }
    }

    /**
     * Ellenőrzi, hogy a táblák léteznek-e, és ha nem, létrehozza őket.
     *
     * @param conn Az adatbázis kapcsolat.
     */
     private static void createTablesIfNotExist(final Connection conn) {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    wins INTEGER DEFAULT 0
                );
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            System.out.println(
                    "Hiba a táblák létrehozása során: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Játékos hozzáadása az adatbázishoz.
     *
     * @param playerName A játékos neve.
     */
    public static void addPlayer(final String playerName) {
        String insertPlayerSQL = """
                INSERT OR IGNORE INTO Players (name, wins) VALUES (?, 0);
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertPlayerSQL)) {
            pstmt.setString(1, playerName);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(playerName + " hozzáadva az adatbázishoz.");
            } else {
                System.out.println(playerName + " már létezik.");
            }
        } catch (SQLException e) {
            System.out.println(
                    "Hiba a játékos hozzáadásakor: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Játékos nyeréseinek növelése.
     *
     * @param playerName A játékos neve.
     */
    public static void incrementWins(final String playerName) {
        String updateWinsSQL = """
                UPDATE Players SET wins = wins + 1 WHERE name = ?;
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(updateWinsSQL)) {
            pstmt.setString(1, playerName);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(playerName
                        + " nyeréseinek száma növelve.");
            } else {
                System.out.println(playerName
                        + " nem található az adatbázisban.");
            }
        } catch (SQLException e) {
            System.out.println(
                    "Hiba a nyerések frissítésekor: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Játékosok és nyeréseik lekérdezése az adatbázisból.
     */
    public static void listPlayers() {
        String selectPlayersSQL = """
                SELECT name, wins FROM Players ORDER BY wins DESC;
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectPlayersSQL)) {

            System.out.println("Játékosok és nyerések:");
            while (rs.next()) {
                System.out.println(
                        "Név: "
                                + rs.getString("name")
                                + ", Nyerések: "
                                + rs.getInt("wins")
                );
            }
        } catch (SQLException e) {
            System.out.println(
                    "Hiba az adatok lekérdezésekor: "
                            + e.getMessage()
            );
        }
    }

}
