public class Board {
    private Piece[][] gameboard;

    public Board() {
        gameboard = new Piece[8][8];
        // Initialize the board with pieces at their starting positions
        setupBoard();
    }

    private void setupBoard() {
        // Initialize pieces on the board
        // For example:
        gameboard[0][0] = new Rook(Color.BLACK);
        gameboard[0][1] = new Knight(Color.BLACK);

    }

    public Piece getPieceAt(Position position) {
        return gameboard[position.getX()][position.getY()];
    }

    public void setPieceAt(Position position, Piece piece) {
        gameboard[position.getX()][position.getY()] = piece;
    }

    public boolean isSquareOccupied(Position position) {
        return getPieceAt(position) != null;
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        setPieceAt(to, piece);
        setPieceAt(from, null);
    }
    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = gameboard[i][j];
                System.out.print(piece == null ? " . " : piece.getSymbol() + " ");
            }
            System.out.println();
        }
    }
}



