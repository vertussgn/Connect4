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

    /** A kilépés választási lehetőség azonosítója. */
    private static final int EXIT_OPTION = 4;

    /**
     * A fő metódus, amely megvizsgálja a játékot.
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
        while (true) {
            DatabaseManager.listPlayers();
            System.out.println("\nVálassz egy lehetőséget:");
            System.out.println("1. Két játékos mód");
            System.out.println("2. Játék az AI ellen");
            System.out.println("3. Betöltés korábbi játékállapotból");
            System.out.println("4. Kilépés");
            System.out.print("Írd be a választásod (1, 2, 3 vagy 4): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            // A kilépéshez tartozó választás ellenőrzése
            if (choice == EXIT_OPTION) {
                System.out.println("A program kilép...");
                break; // Kilépés a while ciklusból, így a program befejeződik
            }

            Game game = Game.setupGame(choice, scanner);
            game.start();

            // A játék befejezése után automatikusan újraindítja a játékot
            System.out.println("\nA játék véget ért. "
                    + "Indulhat a következő játék...");
        }
    }
}
