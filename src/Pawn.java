public class Pawn extends Piece {
    private boolean justMovedTwoSquares;

    protected static double[][] PAWN_TABLE = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35},
            {0.1, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, 0.1},
            {0.05, 0.05, 0.1, 0.25, 0.25, 0.1, 0.05, 0.05},
            {0.0, 0.0, 0.0, 0.2, 0.2, 0.0, 0.0, 0.0},
            {0.05, -0.05, -0.1, 0.0, 0.0, -0.1, -0.05, 0.05},
            {0.05, 0.1, 0.1, -0.2, -0.2, 0.1, 0.1, 0.05},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
    };
    public Pawn(int color, int x, int y) {
        super(color, x, y, 1, PAWN_TABLE);
        justMovedTwoSquares = false;
    }
    public boolean isJustMovedTwoSquares() {
        return justMovedTwoSquares;
    }

    public void setJustMovedTwoSquares(boolean justMovedTwoSquares){
        this.justMovedTwoSquares = justMovedTwoSquares;
    }

    @Override
    public void changePOSITION(int x, int y) {
        super.changePOSITION(x, y);
        justMovedTwoSquares = false; // Reset the flag when the pawn moves
    }
    public double getPositionalValue(int x, int y) {
        return PAWN_TABLE[x][y];
    }
}