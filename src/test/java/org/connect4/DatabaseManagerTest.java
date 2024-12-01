package org.connect4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    private Connection conn;

    @BeforeEach
    public void setUp() {
        // Kapcsolódunk az SQLite memória adatbázishoz és létrehozzuk a táblát
        conn = DatabaseManager.connect();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    public void testAddPlayer() throws SQLException {
        String playerName = "Player1";

        // Player hozzáadása az adatbázishoz
        DatabaseManager.addPlayer(playerName);

        // Ellenőrizzük, hogy a player hozzá lett adva
        String selectSQL = "SELECT name FROM Players WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(playerName, rs.getString("name"));
        }
    }

    @Test
    public void testAddPlayerIfExists() throws SQLException {
        String playerName = "Player2";

        // Player hozzáadása először
        DatabaseManager.addPlayer(playerName);
        // Újra megpróbáljuk hozzáadni ugyanazt
        DatabaseManager.addPlayer(playerName);

        // Ellenőrizzük, hogy csak egyszer lett hozzáadva
        String selectSQL = "SELECT count(*) FROM Players WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));  // Csak egyszer szerepelhet
        }
    }
}
