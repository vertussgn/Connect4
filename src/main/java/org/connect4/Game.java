package org.connect4;

import java.util.Random;
import java.util.Scanner;

/**
 * A Connect 4 játékot reprezentálja, beleértve a játékosokat és a játékmenetet.
 */
public final class Game {
    /** A játéktábla oszlopainek száma. */
    private static final int TOTAL_COLUMNS = 7;

    /** AI ellen játszás választása. */
    static final int AI_MODE = 2;

    /** Játék betöltése fájlból választása. */
    private static final int LOAD_FROM_FILE_MODE = 3;

    /** A játéktábla. */
    private final Board board;

    /** Az első játékos. */
    private final Player firstPlayer;

    /** A második játékos. */
    private final Player secondPlayer;

    /** Az éppen aktuális játékos. */
    private Player currentPlayer;

    /** Véletlenszám-generátor az AI lépésekhez. */
    private final Random random;

    /** Megmutatja, hogy a játék AI ellen zajlik-e. */
    private final boolean isPlayingAgainstAI;

    /**
     * Létrehoz egy új Game-t megadott játékosokkal és móddal.
     *
     * @param player1 Az első játékos.
     * @param player2 A második játékos.
     * @param playAgainstAI Megmutatja, hogy a játék AI ellen zajlik-e.
     */
    public Game(
            final Player player1,
            final Player player2,
            final boolean playAgainstAI) {
        this.board = new Board();
        this.firstPlayer = player1;
        this.secondPlayer = player2;
        this.isPlayingAgainstAI = playAgainstAI;
        this.currentPlayer = player1;
        this.random = new Random();
    }

    /**
     * Beállítja a játékot a felhasználói választás alapján.
     *
     * @param choice A felhasználó választása a játékmódhoz.
     * @param scanner Az inputhoz használt scanner.
     * @return Egy új Game példány.
     */
    public static Game setupGame(final int choice, final Scanner scanner) {
        boolean playAgainstAI = false; // AI-e?
        boolean loadFromFile = false;
        String player1Name;
        String player2Name;

        switch (choice) {
            case 1:
                System.out.print("Add meg az 1. játékos nevét: ");
                player1Name = scanner.nextLine();
                System.out.print("Add meg a 2. játékos nevét: ");
                player2Name = scanner.nextLine();
                break;
            case AI_MODE:
                playAgainstAI = true;
                System.out.print("Add meg a játékos nevét: ");
                player1Name = scanner.nextLine();
                player2Name = "AI";
                break;
            case LOAD_FROM_FILE_MODE:
                loadFromFile = true;
                System.out.print("Add meg az 1. játékos nevét: ");
                player1Name = scanner.nextLine();
                System.out.print("Add meg a 2. játékos nevét: ");
                player2Name = scanner.nextLine();
                break;
            default:
                throw new IllegalArgumentException("Érvénytelen választás!");
        }

        // Játékosok pontszámának mentése az adatbázisba
        DatabaseManager.addPlayer(player1Name);  // Adja hozzá az 1. játékost
        DatabaseManager.addPlayer(player2Name);  // Adja hozzá a 2. játékost

        // Új Game objektum létrehozása
        Game game = new Game(
                new Player(player1Name, 'X'),
                new Player(player2Name, 'O'),
                playAgainstAI
        );

        if (loadFromFile) {
            if (!game.loadGameState()) {
                System.out.println("A mentett játékállapot nem található. "
                        + "Új játék indítása.");
            } else {
                System.out.println("Játékállapot betöltve a fájlból.");
            }
        }

        return game;
    }

    /**
     * Elindítja a játék ciklust.
     */
    public void start() {
        boolean gameWon = false;

        while (!gameWon && !board.isFull()) {
            board.print();
            int col = getCurrentPlayerInput();
            if (col == -1) {
                saveGameState();
                continue;
            }

            if (board.dropToken(col, currentPlayer.getToken())) {
                gameWon = checkWin();
                if (!gameWon) {
                    switchPlayer();
                }
            } else {
                System.out.println("A választott oszlop tele van! "
                        + "Válassz másik oszlopot.");
            }
        }

        board.print();
        if (gameWon) {
            System.out.println("A(z) " + currentPlayer.getName()
                    + " nyert!");
            // Nyertes pontszám hozzáadása az adatbázishoz
            DatabaseManager.incrementWins(currentPlayer.getName());
        } else {
            System.out.println("Döntetlen!");
        }
    }


    /**
     * Megkapja az aktuális játékos inputját.
     *
     * @return A kiválasztott oszlop vagy -1 a mentéshez.
     */
    public int getCurrentPlayerInput() {
        if (!isPlayingAgainstAI || currentPlayer.equals(firstPlayer)) {
            return getPlayerInput();
        } else {
            return getRandomAIInput();
        }
    }

    /**
     * Megkérdi a játékost mit szeretne.
     *
     * @return A kiválasztott oszlop.
     */
    public int getPlayerInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print("A(z) "
                    + currentPlayer.getName() + " (" + currentPlayer.getToken()
                    + ") játékos, válassz egy oszlopot (0-6),"
                    + "vagy írd be 's' a mentéshez: ");
            input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("s")) {
                return -1;
            }

            try {
                int col = Integer.parseInt(input);
                if (col >= 0 && col < TOTAL_COLUMNS) {
                    return col;
                }
            } catch (NumberFormatException e) {
                // Érvénytelen bemenet figyelmen kívül hagyása
            }

            System.out.println("Érvénytelen bemenet! "
                    + "Válassz egy oszlopot 0-6 között, "
                    + "vagy írd be az 's' betűt a mentéshez.");
        } while (true);
    }

    /**
     * Véletlenszerű oszlopot ad vissza az AI.
     *
     * @return Egy véletlenszerű oszlopszám returnje.
     */
    int getRandomAIInput() {
        System.out.println("Az AI lép...");
        return random.nextInt(TOTAL_COLUMNS);
    }

    /**
     * Aktuális játékos cseréje.
     */
    void switchPlayer() {
        currentPlayer = (currentPlayer.equals(firstPlayer))
                ? secondPlayer
                : firstPlayer;
    }

    /**
     * Ellenőrzi a nyerési feltételt.
     *
     * @return Igaz, ha az aktuális játékos nyert, ha nem, hamis.
     */
    boolean checkWin() {
        return board.checkHorizontalWin(currentPlayer)
                ||
                board.checkVerticalWin(currentPlayer)
                ||
                board.checkDiagonalWin(currentPlayer);
    }

    /**
     * Betölti a játékállapotot egy fájlból.
     *
     * @return Igaz, ha a sikeresen be lett töltve,
     * hamis ha nem sikerült.
     */
    boolean loadGameState() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Add meg a betöltendő fájl nevét "
                + "(pl. gamestate.txt): ");
        String fileName = scanner.nextLine();

        return board.loadStateFromFile(fileName);
    }

    /**
     * Az aktuális játékállapot mentése egy fájlba.
     * Metódus megkérdezi a felhasználót egy fájlnevről, és elmenti a
     * jelenlegi táblát és játékosinformációkat
     * a megadott fájlba.
     */
    void saveGameState() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Add meg a menteni kívánt fájl nevét: ");
        String fileName = scanner.nextLine();

        board.saveStateToFile(fileName);
        System.out.println("Játék mentve: " + fileName);
    }
}
