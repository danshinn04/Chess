public class King extends Piece {
    boolean cancastle;
    public King(int color, int x, int y) {
        super(color, x, y, 6);
        cancastle = true;
    }
    public boolean returncancastle() {
        return cancastle;
    }
    public void updatecastling() {
        cancastle = false;
    }
}
