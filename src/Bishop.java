public class Bishop extends Piece {
    public Bishop(int color, int x, int y) {
        super(color, x, y, 3, BISHOP_TABLE);
    }
    protected static double[][] BISHOP_TABLE = {
            {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2},
            {-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1},
            {-0.1, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.1},
            {-0.1, 0.05, 0.05, 0.1, 0.1, 0.05, 0.05, -0.1},
            {-0.1, 0.0, 0.1, 0.1, 0.1, 0.1, 0.0, -0.1},
            {-0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, -0.1},
            {-0.1, 0.05, 0.0, 0.0, 0.0, 0.0, 0.05, -0.1},
            {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2}
    };
    public double getPositionalValue(int x, int y) {
        return BISHOP_TABLE[x][y];
    }
}
