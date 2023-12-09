public abstract class Piece {
    protected int color; // 0 for white, 1 for black
    protected int x;
    protected int y;
    protected int type; // Type of the piece, e.g., pawn, knight, etc.
    protected boolean hasMoved;
    public Piece(int color, int x, int y, int type) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.type = type;
        this.hasMoved = false;
    }

    // Getters
    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    // Setters
    public void changePOSITION(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasMoved = true;
    }
    public boolean returnhasMoved() {
        return hasMoved;
    }

    // Check if the move is legal for this piece (to be overridden in subclasses)
    public boolean isLegalMove(int newX, int newY) {
        // Basic implementation, specific rules to be implemented in subclasses
        return newX >= 0 && newX < 8 && newY >= 0 && newY < 8; // Within the bounds of the board
    }

    // String representation of the piece
    @Override
    public String toString() {
        return "Piece{" +
                "color=" + (color == 0 ? "White" : "Black") +
                ", x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }

    public char getSymbol() {
        char s = 'a';
        switch (type) {
            case 1: s = 'P';
                break; // Pawn
            case 2: s = 'N'; break; // Knight
            case 3: s = 'B'; break; // Bishop
            case 4: s = 'R'; break; // Rook
            case 5: s = 'Q'; break; // Queen
            case 6: s = 'K'; break;
        }
        return s;
    }
}
