package org.connect4;

/**
 * A Connect 4 játékban egy játékost képvisel.
 * Ez az osztály tárolja a játékos nevét és karaterjét.
 */
public final class Player {
    /**
     * A játékos neve.
     */
    private final String mName;

    /**
     * A játékos által használt token.
     */
    private final char mToken;

    /**
     * Létrehoz egy új játékost a megadott névvel és karakter.
     *
     * @param name A játékos neve.
     * @param token A játékos által használt token.
     */
    public Player(final String name, final char token) {
        this.mName = name;
        this.mToken = token;
    }

    /**
     * Visszaadja a játékos nevét.
     *
     * @return A játékos neve.
     */
    public String getName() {
        return mName;
    }

    /**
     * Visszaadja a játékos által használt karaktert.
     *
     * @return A játékos által használt karakter.
     */
    public char getToken() {
        return mToken;
    }
}
