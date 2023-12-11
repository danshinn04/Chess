import java.util.*;

public class ChessEngine {
    private ChessBoard gameboard;
    private int maxDepth;
    private int turn;
    private HashMap<Square, Piece> bestMove;
    private Stack<Piece> capturedPiecesStack;

    public ChessEngine(ChessBoard gameboard, int maxDepth) {
        this.gameboard = gameboard;
        this.maxDepth = maxDepth;
        this.turn = gameboard.getCurrentTurn();
        this.capturedPiecesStack = new Stack<>();
    }

    public void updateBoard() {
        this.turn = gameboard.getCurrentTurn();
    }

    private double evaluateBoard() {
        double[] eval = calculateTotalPositionalValue();
        return turn == 0 ? eval[0] - eval[1] : eval[1] - eval[0];
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
                } else {
                    blackScore += piece.getPositionalValue(square.getPosX(), square.getPosY());
                }
            }
        }
        return new double[]{whiteScore, blackScore};
    }

    private void simulateMove(Square from, Square to) {
        Piece movingPiece = gameboard.getPieceAt(from.getPosX(), from.getPosY());
        Piece capturedPiece = gameboard.getPieceAt(to.getPosX(), to.getPosY());
        gameboard.placePieceAt(movingPiece, to.getPosX(), to.getPosY());
        gameboard.placePieceAt(null, from.getPosX(), from.getPosY());
        capturedPiecesStack.push(capturedPiece);
    }

    private void revertMove(Square from, Square to) {
        Piece movingPiece = gameboard.getPieceAt(to.getPosX(), to.getPosY());
        Piece capturedPiece = capturedPiecesStack.pop();
        gameboard.placePieceAt(movingPiece, from.getPosX(), from.getPosY());
        gameboard.placePieceAt(capturedPiece, to.getPosX(), to.getPosY());
    }

    private double minimax(int depth, double alpha, double beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return evaluateBoard();
        }

        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Map.Entry<Square, List<Square>> entry : gameboard.returnAllPossibleMoves(turn).entrySet()) {
                Square fromSquare = entry.getKey();
                for (Square toSquare : entry.getValue()) {
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

    public HashMap<Square, Piece> calculateBestMove() {
        double bestScore = turn == 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        Square bestFrom = null;
        Square bestTo = null;

        for (Map.Entry<Square, List<Square>> entry : gameboard.returnAllPossibleMoves(turn).entrySet()) {
            for (Square toSquare : entry.getValue()) {
                simulateMove(entry.getKey(), toSquare);
                double score = minimax(maxDepth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, turn != 0);
                revertMove(entry.getKey(), toSquare);

                if ((turn == 0 && score > bestScore) || (turn == 1 && score < bestScore)) {
                    bestScore = score;
                    bestFrom = entry.getKey();
                    bestTo = toSquare;
                }
            }
        }

        if (bestFrom != null && bestTo != null) {
            bestMove = new HashMap<>();
            bestMove.put(bestFrom, gameboard.getPieceAt(bestTo.getPosX(), bestTo.getPosY()));
        }
        return bestMove;
    }
}
