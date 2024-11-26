package org.connect4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    public void testGetName() {
        // Arrange: Játékos példány létrehozása névvel és tokennel
        Player player = new Player("Játékos1", 'X');

        // Act: A getName metódus hívása
        String actualName = player.getName();

        // Assert: Ellenőrizzük, hogy a visszaadott név megegyezik az elvárt névvel
        assertEquals("Játékos1", actualName, "A játékos neve nem egyezik.");
    }
}
