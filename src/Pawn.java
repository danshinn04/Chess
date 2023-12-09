public class Pawn extends Piece {
    private boolean justMovedTwoSquares;
    public Pawn(int color, int x, int y) {
        super(color, x, y, 1);
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
}