package org.connect4;

import java.util.Scanner;

/**
 * A Connect 4 játék fő belépési pontja.
 * Ez az osztály lehetővé teszi a játékosok számára,
 * hogy válasszanak játékmódokat és elindítsák a játékot.
 */
public final class Main {
    // Privát konstruktor
    private Main() {
        throw new UnsupportedOperationException(
                "A segédosztály példányosítása nem engedélyezett"
        );
    }

    /**
     * A fő metódus amely megviszgálja a játékot.
     *
     * @param args Parancssori argumentumok
     */
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        inditjatek(scanner);
    }

    /**
     * Beállítja és elindítja a játékot a felhasználói inputja alapján.
     *
     * @param scanner A felhasználói inputja - Scanner
     */
    static void inditjatek(final Scanner scanner) {
        DatabaseManager.printHighScores();
        System.out.println("\nVálassz egy lehetőséget:");
        System.out.println("1. Két játékos mód");
        System.out.println("2. Játék az AI ellen");
        System.out.println("3. Betöltés korábbi játékállapotból");
        System.out.print("Írd be a választásod (1, 2 vagy 3): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Game game = Game.setupGame(choice, scanner);
        game.start();

        System.out.println("\nA legjobb pontszámok:");
        DatabaseManager.printHighScores();
    }
}
