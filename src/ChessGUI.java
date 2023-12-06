import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChessGUI {
    private JFrame frame;
    private JPanel chessBoard;
    private JButton[][] chessBoardSquares;
    private static ChessBoard gameBoard;
    private Square selectedSquare = null;
    public ChessGUI() {
        gameBoard = new ChessBoard();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoardSquares = new JButton[8][8];

        // Initialize the chess board squares and set pieces
        for (int y = 0; y < chessBoardSquares.length; y++) {
            for (int x = 0; x < chessBoardSquares[y].length; x++) {
                JButton button = new JButton();
                button.setOpaque(true);
                button.setBackground((x + y) % 2 == 0 ? Color.WHITE : Color.GRAY);

                // Invert the y-axis for chessboard
                Piece piece = gameBoard.getPieceAt(x, y);
                if (piece != null) {
                    button.setText(getPieceSymbol(piece));
                }

                chessBoardSquares[y][x] = button;
                chessBoard.add(chessBoardSquares[y][x]);
                int finalX = x;
                int finalY = y;
                chessBoardSquares[y][x].addActionListener(e -> showPossibleMoves(finalX, finalY));
            }
        }

        frame.add(chessBoard, BorderLayout.CENTER);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private void showPossibleMoves(int x, int y) {
        clearHighlights(); // Clear existing highlights

        Piece selectedPiece = gameBoard.getPieceAt(x, y);
        if (selectedPiece != null) {
            // Assuming it's the correct turn for this piece's color
            List<Square> possibleMoves = gameBoard.getPotentialMovesForPiece(x, y);
            for (Square move : possibleMoves) {
                highlightSquare(move.getPosX(), move.getPosY());
            }
        }
    }

    private void clearHighlights() {
        for (int y = 0; y < chessBoardSquares.length; y++) {
            for (int x = 0; x < chessBoardSquares[y].length; x++) {
                JButton squareButton = chessBoardSquares[y][x];
                Color originalColor = (x + y) % 2 == 0 ? Color.WHITE : Color.GRAY;
                squareButton.setBackground(originalColor);
            }
        }
    }

    private void highlightSquare(int x, int y) {
        JButton squareButton = chessBoardSquares[y][x];
        Color highlightColor = Color.YELLOW; // Choose a color that stands out
        squareButton.setBackground(highlightColor);
    }

    // Method to get the symbol for a piece
    private String getPieceSymbol(Piece piece) {
        String color = piece.getColor() == 0 ? "W" : "B";
        String type = "";
        switch (piece.getType()) {
            case 1: type = "P"; break; // Pawn
            case 2: type = "N"; break; // Knight
            case 3: type = "B"; break; // Bishop
            case 4: type = "R"; break; // Rook
            case 5: type = "Q"; break; // Queen
            case 6: type = "K"; break; // King
        }
        return color + type;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChessGUI();
                System.out.println(gameBoard.toString());
            }
        });
    }
}
