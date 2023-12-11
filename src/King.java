public class King extends Piece {
    boolean cancastle;
    protected static double[][] KING_TABLE = {
            {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3},
            {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3},
            {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3},
            {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3},
            {-0.2, -0.3, -0.3, -0.4, -0.4, -0.3, -0.3, -0.2},
            {-0.1, -0.2, -0.2, -0.2, -0.2, -0.2, -0.2, -0.1},
            {0.2, 0.2, 0.0, 0.0, 0.0, 0.0, 0.2, 0.2},
            {0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2}
    };
    public King(int color, int x, int y) {
        super(color, x, y, 6, KING_TABLE);
        cancastle = true;
    }
    public boolean returncancastle() {
        return cancastle;
    }
    public void updatecastling() {
        cancastle = false;
    }
    public double getPositionalValue(int x, int y) {
        return KING_TABLE[x][y];
    }
}
