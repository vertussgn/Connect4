package org.connect4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * A Connect 4 játék táblája.
 * Ez az osztály metódusokat biztosít a tábla kezeléséhez,
 * győzelmi feltételek és a játékállapotok
 * mentéséhez és betöltéséhez.
 */
public class Board {
    /** A tábla sorainak száma. */
    private static final int DEFAULT_ROWS = 6;

    /** A tábla oszlopainak száma. */
    private static final int DEFAULT_COLS = 7;

    /** A győzelemhez szükséges összekötendő karakterek száma. */
    private static final int CONNECT_LENGTH = 4;

    /** 2D tömb, amely a táblát reprezentálja. */
    private final char[][] board;

    /**
     * Üres tábla alapértelmezett értékekkel.
     */
    public Board() {
        this.board = new char[DEFAULT_ROWS][DEFAULT_COLS];
        for (char[] row : board) {
            Arrays.fill(row, '.'); // Üres helyekkel inicializálás
        }
    }

    /**
     * Karakter ledobása a megadott oszlopba.
     *
     * @param col   Az oszlop ahova a karaktert dobjuk.
     * @param token A ledobott karakter.
     * @return True, ha a karakter sikeresen le lett dobva,
     *         false, ha az oszlop tele van vagy érvénytelen.
     */
    public boolean dropToken(final int col, final char token) {
        if (col < 0 || col >= DEFAULT_COLS) {
            System.out.println("Érvénytelen oszlop: " + col);
            return false;
        }
        for (int row = DEFAULT_ROWS - 1; row >= 0; row--) {
            if (board[row][col] == '.') {
                board[row][col] = token;
                return true; // Sikeres tokenledobás
            }
        }
        System.out.println("Az oszlop tele van: " + col);
        return false; // Az oszlop tele van
    }

    /**
     * A tábla aktuális állapotának kiírása.
     */
    public void print() {
        for (char[] row : board) {
            System.out.println(new String(row)); // Minden sort kiírunk
        }
    }

    /**
     * Ellenőrzi, hogy a tábla tele van-e.
     *
     * @return True, ha a tábla tele van, különben false.
     */
    public boolean isFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') {
                    return false; // Találtunk egy üres helyet
                }
            }
        }
        return true; // Nincsenek üres helyek
    }

    /**
     * Játékállapot betöltése egy fájlból.
     *
     * @param fileName A fájl neve, amelyből a játékállapotot betöltjük.
     * @return True, ha az állapot sikeresen betöltődött, különben false.
     */
    public boolean loadStateFromFile(final String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for (int r = 0; r < DEFAULT_ROWS; r++) {
                String line = br.readLine();
                if (line == null) {
                    break; // Ha a fájl rövidebb, mint a tábla, akkor leállunk.
                }
                for
                (int c = 0; c < Math.min(line.length(), DEFAULT_COLS); c++) {
                    board[r][c] = line.charAt(c);
                }
            }
            System.out.println(
                    "Játékállapot sikeresen betöltve a(z) "
                            + fileName + " fájlból."
            );
            return true; // Sikeres játékállapot betöltés
        } catch (IOException e) {
            System.out.println("Hiba a(z) " + fileName
                    + " fájl betöltésekor: " + e.getMessage());
            return false; // A játékállapot betöltése nem sikerült
        }
    }

    /**
     * A jelenlegi játékállapot mentése fájlba.
     *
     * @param fileName A fájl neve, ahova a játékállapotot mentjük.
     */
    public void saveStateToFile(final String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (char[] row : board) {
                bw.write(row); // A sort közvetlenül kiírjuk
                bw.newLine(); // Következő sorba lépés a fájlban
            }
            System.out.println("A játékállapot mentésre került a(z) "
                    + fileName + " fájlba.");
        } catch (IOException e) {
            System.out.println("Hiba történt a játékállapot mentésekor: "
                    + e.getMessage());
        }
    }

    /**
     * Vízszintes győzelmi feltétel ellenőrzése.
     *
     * @param player A játékos, akinek a győzelmét vizsgáljuk.
     * @return True, ha a játékos vízszintesen nyert, különben false.
     */
    public boolean checkHorizontalWin(final Player player) {
        final char token = player.getToken();
        for (int row = 0; row < DEFAULT_ROWS; row++) {
            for (int col = 0; col <= DEFAULT_COLS - CONNECT_LENGTH; col++) {
                if (isWinningCombination(row, col, token, 0, 1)) {
                    return true; // Találtunk egy vízszintes győzelmet
                }
            }
        }
        return false; // Nincs vízszintes győzelem
    }

    /**
     * Függőleges győzelmi feltétel ellenőrzése.
     *
     * @param player A játékos, akinek a győzelmét vizsgáljuk.
     * @return True, ha a játékos függőlegesen nyert, különben false.
     */
    public boolean checkVerticalWin(final Player player) {
        final char token = player.getToken();
        for (int col = 0; col < DEFAULT_COLS; col++) {
            for (int row = 0; row <= DEFAULT_ROWS - CONNECT_LENGTH; row++) {
                if (isWinningCombination(row, col, token, 1, 0)) {
                    return true; // Találtunk egy függőleges győzelmet
                }
            }
        }
        return false; // Nincs függőleges győzelem
    }

    /**
     * Átlós győzelmi feltétel.
     *
     * @param player A játékos, akinek a győzelmét vizsgáljuk.
     * @return True, ha a játékos átlósan nyert, különben false.
     */
    public boolean checkDiagonalWin(final Player player) {
        final char token = player.getToken();
        // Pozitív lejtésű átló ellenőrzése (\)
        for (int row = 0; row <= DEFAULT_ROWS - CONNECT_LENGTH; row++) {
            for (int col = 0; col <= DEFAULT_COLS - CONNECT_LENGTH; col++) {
                if (isWinningCombination(row, col, token, 1, 1)) {
                    return true;
                    // Találtunk egy átlós győzelmet (pozitív lejtés)
                }
            }
        }

        // Negatív lejtésű átló ellenőrzése (/)
        for (int row = CONNECT_LENGTH - 1; row < DEFAULT_ROWS; row++) {
            for (int col = 0; col <= DEFAULT_COLS - CONNECT_LENGTH; col++) {
                if
                (isWinningCombination(row, col, token, -1, 1)) {
                    return true;
                    // Találtunk egy átlós győzelmet (negatív lejtés)
                }
            }
        }
        return false; // Nincs átlós győzelem
    }

    /**
     * Segédmetódus a győzelmi kombináció ellenőrzéséhez.
     *
     * @param rowStart   Kezdősor.
     * @param colStart   Kezdőoszlop.
     * @param token      A játékos tokenje.
     * @param rowStep    Soronkénti lépés.
     * @param colStep    Oszloponkénti lépés.
     * @return True, ha a győzelmi kombináció megtalálható, különben false.
     */
    private boolean isWinningCombination(final int rowStart,
                                         final int colStart,
                                         final char token,
                                         final int rowStep,
                                         final int colStep) {
        for
        (int i = 0; i < CONNECT_LENGTH; i++) {
            if
            (board[rowStart + i * rowStep][colStart + i * colStep] != token) {
                return false; // Nincs egyezés
            }
        }
        return true; // Győzelmi kombináció megtalálva
    }
}
