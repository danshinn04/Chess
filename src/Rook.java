public class Rook extends Piece {
    public Rook(int color, int x, int y) {
        super(color, x, y, 4, ROOK_TABLE);
    }

    protected static double[][] ROOK_TABLE = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05},
            {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
            {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
            {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
            {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
            {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
            {0.0, 0.0, 0.0, 0.05, 0.05, 0.0, 0.0, 0.0}
    };

    public double getPositionalValue(int x, int y) {
        return ROOK_TABLE[x][y];
    }
}
