import java.util.*;

public class ChessEngine {
    private ChessBoard gameboard;
    private int maxDepth;
    private int turn;
    private int[] bestMove;
    private Stack<Piece> capturedPiecesStack;
    private double[] valOfPieces;

    public ChessEngine(ChessBoard gameboard, int maxDepth) {
        this.gameboard = gameboard;
        this.maxDepth = maxDepth;
        this.turn = gameboard.getCurrentTurn();
        this.capturedPiecesStack = new Stack<>();
        valOfPieces = new double[]{0.0,1.0,3.0,3.5,5.0,9.0,4000.0};
    }

    public void updateChessBoard(ChessBoard a) {
        gameboard = a;
        capturedPiecesStack.empty();
    }

    public void updateBoard() {
        this.turn = gameboard.getCurrentTurn();
    }

    private double evaluateBoard() {
        double[] eval = calculateTotalPositionalValue();
        System.out.println(eval[0] + " " + eval[1]);
        return turn == 0 ? eval[1] - eval[0] : eval[0] - eval[1];
    }

    private double[] calculateTotalPositionalValue() {
        double whiteScore = 0.0;
        double blackScore = 0.0;
        HashMap<Square, Piece> board = gameboard.getBoardState();

        for (Map.Entry<Square, Piece> entry : board.entrySet()) {
            Piece piece = entry.getValue();
            Square square = entry.getKey();
            if (piece != null) {
                if (piece.getColor() == 0) {
                    whiteScore += piece.getPositionalValue(square.getPosX(), square.getPosY());
                    whiteScore += valOfPieces[piece.getType()];
                } else {
                    blackScore += piece.getPositionalValue(square.getPosX(), square.getPosY());
                    blackScore += valOfPieces[piece.getType()];
                }
            }
        }
        return new double[]{whiteScore, blackScore};
    }

    private void simulateMove(Square from, Square to) {
        Piece movingPiece = gameboard.getPieceAt(from.getPosX(), from.getPosY());
        if (movingPiece != null) {
            Piece capturedPiece = gameboard.getPieceAt(to.getPosX(), to.getPosY());
            gameboard.placePieceAt(movingPiece, to.getPosX(), to.getPosY());
            gameboard.placePieceAt(null, from.getPosX(), from.getPosY());
            capturedPiecesStack.push(capturedPiece);
        }
    }


    private void revertMove(Square from, Square to) {
        Piece movingPiece = gameboard.getPieceAt(to.getPosX(), to.getPosY());
        Piece capturedPiece = capturedPiecesStack.pop();
        gameboard.placePieceAt(movingPiece, from.getPosX(), from.getPosY());
        if (capturedPiece != null) {
            gameboard.placePieceAt(capturedPiece, to.getPosX(), to.getPosY());
        } else {
            gameboard.placePieceAt(null, to.getPosX(), to.getPosY()); // Explicitly remove piece if it was null
        }
    }

    private double minimax(int depth, double alpha, double beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return evaluateBoard();
        }
        if (!maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Map.Entry<Square, List<Square>> entry : gameboard.returnAllPossibleMoves(turn).entrySet()) {
                Square fromSquare = entry.getKey();
                for (Square toSquare : entry.getValue()) {
                    //System.out.println(fromSquare.getPosX() + " " + fromSquare.getPosY() + " " + toSquare.getPosX() + " " + toSquare.getPosY());

                    simulateMove(fromSquare, toSquare);
                    double eval = minimax(depth - 1, alpha, beta, false);
                    revertMove(fromSquare, toSquare);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (Map.Entry<Square, List<Square>> entry : gameboard.returnAllPossibleMoves(turn).entrySet()) {
                Square fromSquare = entry.getKey();
                for (Square toSquare : entry.getValue()) {
                    simulateMove(fromSquare, toSquare);
                    double eval = minimax(depth - 1, alpha, beta, true);
                    revertMove(fromSquare, toSquare);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }

    public int[] calculateBestMove() {
        updateBoard();
        double bestScore = turn == 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        Square bestFrom = null;
        Square bestTo = null;

        for (Map.Entry<Square, List<Square>> entry : gameboard.returnAllPossibleMoves(turn).entrySet()) {
            Square fromSquare = entry.getKey();
            for (Square toSquare : entry.getValue()) {
                simulateMove(fromSquare, toSquare);
                double score = minimax(maxDepth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, turn == 0);
                revertMove(fromSquare, toSquare);

                if ((turn == 0 && score > bestScore) || (turn != 0 && score < bestScore)) {
                    bestScore = score;
                    bestFrom = fromSquare;
                    bestTo = toSquare;
                }
            }
        }
        gameboard.changeTurn();

        if (bestFrom != null && bestTo != null) {
            bestMove = new int[]{bestFrom.getPosX(), bestFrom.getPosY(), bestTo.getPosX(),bestTo.getPosY()};
            System.out.println("Best move calculated: " + bestFrom + " to " + bestTo); // Debug print
        } else {
            System.out.println("No valid move found"); // Debug print for no move scenario
            bestMove = null;
        }
        return bestMove;
    }
}
