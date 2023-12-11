import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.io.FileWriter;

public class ChessGUI {
    private JFrame frame;
    private JPanel chessBoard;
    private JTextArea moveHistoryArea;
    private static JButton[][] chessBoardSquares;
    private ArrayList<String> movehistory;
    private static ChessBoard gameBoard;
    private Map<String, ImageIcon> pieceImages;
    private ChessEngine chessEngine;
    public ChessGUI() {
        gameBoard = new ChessBoard();
        movehistory = new ArrayList<>();
        loadPieceImages();
        initializeGUI();
        chessEngine = new ChessEngine(gameBoard, 4);
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
        return (piece.getColor() == 0 ? "w" : "b") + Character.toUpperCase(piece.getSymbol());
    }

    private void saveMoveHistoryToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String move : movehistory) {
                writer.write(move + System.lineSeparator());
            }
            System.out.println("Move history saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initializeGUI() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoardSquares = new JButton[8][8];
        JButton saveMoveHistoryButton = new JButton("Save Move History");
        saveMoveHistoryButton.addActionListener(e -> saveMoveHistoryToFile("chess_game_moves.txt"));
        JPanel controlPanel = new JPanel();
        controlPanel.add(saveMoveHistoryButton);
        frame.add(controlPanel, BorderLayout.SOUTH);
        moveHistoryArea = new JTextArea(10, 20);
        moveHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        frame.add(scrollPane, BorderLayout.EAST);

        //Initialize the chess board squares and set pieces
        for (int y = 0; y < chessBoardSquares.length; y++) {
            for (int x = 0; x < chessBoardSquares[y].length; x++) {
                JButton button = new JButton();
                button.setOpaque(true);
                button.setBorderPainted(false);
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
                        if (gameBoard.getCurrentTurn() == 1) { //Black's turn (engine)
                            SwingUtilities.invokeLater(this::executeEngineMove);
                        }
                    }
                });
            }
        }
    }

    private void executeEngineMove() {
        chessEngine.updateChessBoard(gameBoard);
        int[] bestMove = chessEngine.calculateBestMove();
        Square squarefrom = gameBoard.getSquare(bestMove[0],bestMove[1]);
        Square squareto = gameBoard.getSquare(bestMove[2],bestMove[3]);
        System.out.println("Here");
        System.out.println(squarefrom);
        System.out.println(squareto);
        gameBoard.MoveUpdate(squarefrom.getPosX(), squarefrom.getPosY(), squareto.getPosX(), squareto.getPosY());
        System.out.println(gameBoard.getCurrentTurn());
        updateGUI();
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
        gameBoard.update(moveHistoryArea, movehistory);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChessGUI();
            }
        });
    }
}