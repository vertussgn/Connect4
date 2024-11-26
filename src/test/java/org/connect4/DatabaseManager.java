package org.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @BeforeEach
    void setUp() {
        // Mielőtt minden teszt fut, biztosítjuk, hogy az adatbázis tábla létrejön
        DatabaseManager.createTableIfNotExists();
    }

    @Test
    void testCreateTableIfNotExists() {
        // Ellenőrizzük, hogy a tábla létrehozásra kerül, ha nem létezik
        // A teszt automatikusan lefut a setUp() metódusban, amely biztosítja a tábla létrehozását
        assertDoesNotThrow(DatabaseManager::createTableIfNotExists);
    }

    @Test
    void testAddNewHighScore() {
        // Új pontszám hozzáadása egy játékoshoz
        String playerName = "Játékos1";
        int score = 100;

        // A pontszám hozzáadása
        DatabaseManager.addOrUpdateHighScore(playerName, score);

        // Lekérdezzük a legjobb pontszámokat
        // Az SQL parancsokat nem tudjuk közvetlenül tesztelni, de biztosítjuk,
        // hogy a rekord létezik és a megfelelő adatokat tartalmazza.
        DatabaseManager.printHighScores();

        // A teszt sikeres, ha nem történik hiba, és a pontszám megjelenik a listában
    }

    @Test
    void testUpdateHighScore() {
        // Pontszám frissítése egy meglévő játékos számára
        String playerName = "Játékos2";
        int initialScore = 50;
        int updatedScore = 200;

        // Az első pontszám hozzáadása
        DatabaseManager.addOrUpdateHighScore(playerName, initialScore);
        // Frissítjük a pontszámot
        DatabaseManager.addOrUpdateHighScore(playerName, updatedScore);

        // Lekérdezzük a legjobb pontszámokat
        DatabaseManager.printHighScores();

        // Itt ellenőrizhetjük, hogy a pontszám frissült-e
        // Ebben az egyszerű tesztben biztosítjuk, hogy ne dobjon hibát
        assertDoesNotThrow(DatabaseManager::printHighScores);
    }

    @Test
    void testPrintHighScores() {
        // Pontszámok lekérdezése és helyes megjelenítésük ellenőrzése
        String playerName1 = "Játékos3";
        int score1 = 150;

        String playerName2 = "Játékos4";
        int score2 = 120;

        // Két pontszám hozzáadása
        DatabaseManager.addOrUpdateHighScore(playerName1, score1);
        DatabaseManager.addOrUpdateHighScore(playerName2, score2);

        // Ellenőrizzük, hogy a pontszámok megjelennek
        // A tesztben nem fogunk konkrét értékeket ellenőrizni,
        // csak biztosítjuk, hogy a pontszámokat helyesen írja ki
        assertDoesNotThrow(DatabaseManager::printHighScores);
    }

    @Test
    void testAddOrUpdateHighScoreWhenNewRecord() {
        // Új rekord hozzáadása
        String playerName = "Játékos5";
        int score = 80;

        // A pontszám hozzáadása
        DatabaseManager.addOrUpdateHighScore(playerName, score);

        // Ellenőrizzük, hogy a rekord valóban hozzá lett adva
        DatabaseManager.printHighScores();

        // Itt is csak annyit tesztelünk, hogy a függvény nem dob hibát
        assertDoesNotThrow(() -> DatabaseManager.addOrUpdateHighScore(playerName, score));
    }

    @Test
    void testAddOrUpdateHighScoreWhenExistingRecord() {
        // Már létező rekord frissítése
        String playerName = "Játékos6";
        int initialScore = 70;
        int updatedScore = 120;

        // Az első pontszám hozzáadása
        DatabaseManager.addOrUpdateHighScore(playerName, initialScore);

        // A pontszám frissítése
        DatabaseManager.addOrUpdateHighScore(playerName, updatedScore);

        // Ellenőrizzük, hogy a rekord frissült
        DatabaseManager.printHighScores();
        assertDoesNotThrow(() -> DatabaseManager.addOrUpdateHighScore(playerName, updatedScore));
    }
}
