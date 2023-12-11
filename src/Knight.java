public class Knight extends Piece {
    protected static double[][] KNIGHT_TABLE = {
            {-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5},
            {-0.4, -0.2, 0.0, 0.0, 0.0, 0.0, -0.2, -0.4},
            {-0.3, 0.0, 0.1, 0.15, 0.15, 0.1, 0.0, -0.3},
            {-0.3, 0.05, 0.15, 0.2, 0.2, 0.15, 0.05, -0.3},
            {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
            {-0.3, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.3},
            {-0.4, -0.2, 0.0, 0.05, 0.05, 0.0, -0.2, -0.4},
            {-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5}
    };
    public Knight(int color, int x, int y) {
        super(color, x, y, 2, KNIGHT_TABLE);
    }
    public double getPositionalValue(int x, int y) {
        return KNIGHT_TABLE[x][y];
    }
}