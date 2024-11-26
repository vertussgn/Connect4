package org.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        // Inicializáljuk a táblát és a játékosokat
        board = new Board();
        player1 = new Player("Player 1", 'X');
        player2 = new Player("Player 2", 'O');
    }

    @Test
    void testDropToken_Success() {
        // Játékos 1 token-t helyez el a 3. oszlopban
        assertTrue(board.dropToken(3, player1.getToken()));
    }

    @Test
    void testDropToken_FullColumn() {
        // Töltsük fel az oszlopot
        for (int i = 0; i < 6; i++) {
            board.dropToken(3, player1.getToken());
        }
        // Próbáljunk meg újabb token-t lehelyezni a teljes oszlopba
        assertFalse(board.dropToken(3, player2.getToken()));
    }

    @Test
    void testDropToken_InvalidColumn() {
        // Próbáljunk meg token-t lehelyezni érvénytelen oszlopba (-1)
        assertFalse(board.dropToken(-1, player1.getToken()));
        // Próbáljunk meg token-t lehelyezni érvénytelen oszlopba (7)
        assertFalse(board.dropToken(7, player2.getToken()));
    }

    @Test
    void testIsFull() {
        // Helyezzünk el token-eket a táblán, hogy megteljen
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board.dropToken(j, player1.getToken());
            }
        }
        assertTrue(board.isFull());
    }

    @Test
    void testHorizontalWin() {
        // Helyezzünk el token-eket, hogy horiszontálisan nyerjen a Játékos 1
        board.dropToken(0, player1.getToken());
        board.dropToken(1, player1.getToken());
        board.dropToken(2, player1.getToken());
        board.dropToken(3, player1.getToken());

        assertTrue(board.checkHorizontalWin(player1));
    }

    @Test
    void testVerticalWin() {
        // Helyezzünk el token-eket, hogy vertikálisan nyerjen a Játékos 1
        board.dropToken(0, player1.getToken());
        board.dropToken(0, player1.getToken());
        board.dropToken(0, player1.getToken());
        board.dropToken(0, player1.getToken());

        assertTrue(board.checkVerticalWin(player1));
    }

    @Test
    void testLoadStateFromFile() {
        // Szimuláljuk a játék állapotának betöltését egy fájlból
        // Feltételezve, hogy a fájl tartalmaz egy érvényes Connect 4 tábla ábrázolást
        String fileName = "gameState.txt"; // Cseréld le valós fájl elérési útra egy valódi tesztben
        assertTrue(board.loadStateFromFile(fileName));
    }

    @Test
    void testSaveStateToFile() {
        // Mentse el az aktuális állapotot egy fájlba
        String fileName = "gameState.txt"; // Cseréld le valós fájl elérési útra egy valódi tesztben
        board.saveStateToFile(fileName);
        // Ellenőrizzük, hogy a fájl létrejött-e és helyes tartalommal rendelkezik
        // (Ez kézzel tesztelhető vagy a fájl olvasásával és a tartalom ellenőrzésével)
    }
}
