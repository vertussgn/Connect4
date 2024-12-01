package org.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    private Player player1;
    private Board mockBoard;
    private Game game;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice", 'X');
        Player player2 = new Player("Bob", 'O');
        mockBoard = mock(Board.class);
        game = new Game(player1, player2, false);
    }


    @Test
    void testCheckWin_NoWinCondition() {
        when(mockBoard.checkHorizontalWin(player1)).thenReturn(false);
        when(mockBoard.checkVerticalWin(player1)).thenReturn(false);
        when(mockBoard.checkDiagonalWin(player1)).thenReturn(false);
        assertFalse(game.checkWin());
    }

}
