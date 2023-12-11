public class Queen extends Piece {
    public Queen(int color, int x, int y) {
        super(color, x, y, 5, QUEEN_TABLE);
    }

    protected static double[][] QUEEN_TABLE = {
            {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2},
            {-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1},
            {-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
            {-0.05, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.05},
            {0.0, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.05},
            {-0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
            {-0.1, 0.0, 0.05, 0.0, 0.0, 0.0, 0.0, -0.1},
            {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2}
    };
    public double getPositionalValue(int x, int y) {
        return QUEEN_TABLE[x][y];
    }
}