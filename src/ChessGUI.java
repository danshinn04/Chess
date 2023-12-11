import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ChessGUI {
    private JFrame frame;
    private JPanel chessBoard;
    private JTextArea moveHistoryArea;
    private static JButton[][] chessBoardSquares;
    private static ChessBoard gameBoard;
    private Map<String, ImageIcon> pieceImages;
    private ChessEngine chessEngine;
    public ChessGUI() {
        gameBoard = new ChessBoard();
        loadPieceImages();
        initializeGUI();
        
    }
    private ImageIcon loadScaledImage(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
    private void loadPieceImages() {
        int imageSize = 1000/8;
        pieceImages = new HashMap<>();
        pieceImages.put("bK", loadScaledImage("C:/Users/Dan/Downloads/Chess/bk.png", imageSize, imageSize));
        pieceImages.put("bQ", loadScaledImage("C:/Users/Dan/Downloads/Chess/bq.png", imageSize, imageSize));
        pieceImages.put("bN", loadScaledImage("C:/Users/Dan/Downloads/Chess/bn.png", imageSize, imageSize));
        pieceImages.put("bR", loadScaledImage("C:/Users/Dan/Downloads/Chess/br.png", imageSize, imageSize));
        pieceImages.put("bB", loadScaledImage("C:/Users/Dan/Downloads/Chess/bb.png", imageSize, imageSize));
        pieceImages.put("bP", loadScaledImage("C:/Users/Dan/Downloads/Chess/bp.png", imageSize, imageSize));
        pieceImages.put("wK", loadScaledImage("C:/Users/Dan/Downloads/Chess/wk.png", imageSize, imageSize));
        pieceImages.put("wQ", loadScaledImage("C:/Users/Dan/Downloads/Chess/wq.png", imageSize, imageSize));
        pieceImages.put("wN", loadScaledImage("C:/Users/Dan/Downloads/Chess/wn.png", imageSize, imageSize));
        pieceImages.put("wR", loadScaledImage("C:/Users/Dan/Downloads/Chess/wr.png", imageSize, imageSize));
        pieceImages.put("wB", loadScaledImage("C:/Users/Dan/Downloads/Chess/wb.png", imageSize, imageSize));
        pieceImages.put("wP", loadScaledImage("C:/Users/Dan/Downloads/Chess/wp.png", imageSize, imageSize));
    }
    private String getPieceKey(Piece piece) {
        // Construct the key used in the pieceImages map
        return (piece.getColor() == 0 ? "w" : "b") + Character.toUpperCase(piece.getSymbol());
    }


    private void initializeGUI() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoardSquares = new JButton[8][8];


        moveHistoryArea = new JTextArea(10, 20);
        moveHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        frame.add(scrollPane, BorderLayout.EAST);

        // Initialize the chess board squares and set pieces
        for (int y = 0; y < chessBoardSquares.length; y++) {
            for (int x = 0; x < chessBoardSquares[y].length; x++) {
                JButton button = new JButton();
                button.setOpaque(true);
                button.setBorderPainted(false); // Optional: to remove button borders
                button.setBackground((x + y) % 2 == 0 ? Color.WHITE : Color.GRAY);

                Piece piece = gameBoard.getPieceAt(x, y);
                if (piece != null) {
                    String pieceKey = getPieceKey(piece);
                    button.setIcon(pieceImages.get(pieceKey));
                }

                chessBoardSquares[y][x] = button;
                chessBoard.add(chessBoardSquares[y][x]);
                int finalX = x;
                int finalY = y;
                chessBoardSquares[y][x].addActionListener(e -> showPossibleMoves(finalX, finalY));
            }
        }

        frame.add(chessBoard, BorderLayout.CENTER);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
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
        Color highlightColor = Color.GREEN; //Color
        squareButton.setBackground(highlightColor);
    }


    private void showPossibleMoves(int x, int y) {
        clearHighlights();
        Piece selectedPiece = gameBoard.getPieceAt(x, y);
        if (selectedPiece != null && selectedPiece.getColor() == gameBoard.getCurrentTurn()) {
            List<Square> possibleMoves = gameBoard.getPotentialMovesForPiece(x, y);
            for (Square move : possibleMoves) {
                highlightSquare(move.getPosX(), move.getPosY());
                JButton moveButton = chessBoardSquares[move.getPosY()][move.getPosX()];
                moveButton.addActionListener(e -> {
                    if (gameBoard.movePiece(new Square(x, y), move)) {
                        updateGUI();
                    }
                });
            }
        }

    }
    public void updateGUI() {
        for (int y = 0; y < chessBoardSquares.length; y++) {
            for (int x = 0; x < chessBoardSquares[y].length; x++) {
                JButton squareButton = chessBoardSquares[y][x];
                Piece piece = gameBoard.getPieceAt(x, y);
                if (piece != null) {
                    String pieceKey = getPieceKey(piece);
                    squareButton.setIcon(pieceImages.get(pieceKey));
                } else {
                    squareButton.setIcon(null);
                }
                squareButton.setBackground((x + y) % 2 == 0 ? Color.WHITE : Color.GRAY);

            }
        }
        gameBoard.update(moveHistoryArea);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChessGUI();
            }
        });
    }
}