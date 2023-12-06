import java.util.*;
public class ChessBoard {
    private HashMap<Square, Piece> TrackOfPieces;
    private Square[][] squares;
    private int currentTurn = 0;
    public ChessBoard() {
        TrackOfPieces = new HashMap<>();
        squares = new Square[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares[x][y] = new Square(x, y);
            }
        }

        // Black pieces
        TrackOfPieces.put(squares[0][0],new Piece(1, 0, 0, 4)); //Black Rook at a8
        TrackOfPieces.put(squares[1][0],new Piece(1, 1, 0, 2)); //Black Knight at b8
        TrackOfPieces.put(squares[2][0],new Piece(1, 2, 0, 3)); //Black Bishop at c8
        TrackOfPieces.put(squares[3][0],new Piece(1, 3, 0, 5)); //Black Queen at d8
        TrackOfPieces.put(squares[4][0],new Piece(1, 4, 0, 6)); //Black King at e8
        TrackOfPieces.put(squares[5][0],new Piece(1, 5, 0, 3)); //Black Bishop at f8
        TrackOfPieces.put(squares[6][0],new Piece(1, 6, 0, 2)); //Black Knight at g8
        TrackOfPieces.put(squares[7][0],new Piece(1, 7, 0, 4)); //Black Rook at h8
        for (int i = 0; i < 8; i++) {
            TrackOfPieces.put(squares[i][1], new Piece(1, i, 1, 1)); //Black Pawns // Black Pawns
        }

        // White pieces
        TrackOfPieces.put(squares[0][7],new Piece(0, 0, 7, 4)); //White Rook at a1
        TrackOfPieces.put(squares[1][7],new Piece(0, 1, 7, 2)); //White Knight at b1
        TrackOfPieces.put(squares[2][7],new Piece(0, 2, 7, 3)); //White Bishop at c1
        TrackOfPieces.put(squares[3][7],new Piece(0, 3, 7, 5)); //White Queen at d1
        TrackOfPieces.put(squares[4][7],new Piece(0, 4, 7, 6)); //White King at e1
        TrackOfPieces.put(squares[5][7],new Piece(0, 5, 7, 3)); //White Bishop at f1
        TrackOfPieces.put(squares[6][7],new Piece(0, 6, 7, 2)); //White Knight at g1
        TrackOfPieces.put(squares[7][7],new Piece(0, 7, 7, 4)); //White Rook at h1
        for (int i = 0; i < 8; i++) {
            TrackOfPieces.put(squares[i][6], new Piece(0, i, 6, 1));  //White Pawns
        }
    }

    public void changeTurn() {
        currentTurn = (currentTurn == 0) ? 1 : 0; //Move parity {0: White to Move, 1: Black to Move}
    }

    public boolean movePiece(Square fromSquare, Square toSquare) {
        Piece piece = getPieceAt(fromSquare.getPosX(), fromSquare.getPosY());
        if (piece != null && piece.getColor() == currentTurn) {
            // Check if the move is valid based on the piece's possible moves
            // If valid, perform the move, remove/capture the piece if needed, and change turn
            MoveUpdate(fromSquare.getPosX(), fromSquare.getPosY(), toSquare.getPosX(), toSquare.getPosY());
            return true; // Return true if move is successful
        }
        return false;
    }

    public void MoveUpdate(int prevX, int prevY, int newX, int newY) {
        Square prevSquare = squares[prevX][prevY];
        Square newSquare = squares[newX][newY];
        Piece squareOrig = TrackOfPieces.get(prevSquare);
        Piece squareNEW = TrackOfPieces.get(newSquare);
        if (squareNEW != null){
            if (squareOrig.getColor() != squareNEW.getColor()) {
                //Can't have same color eat same
                TrackOfPieces.remove(newSquare);
                TrackOfPieces.remove(prevSquare);
                squareOrig.changePOSITION(newSquare.getPosX(), newSquare.getPosY());
                TrackOfPieces.put(squareOrig.getCurrentSquare(), squareOrig);
            }
        }
    }

    public HashMap<Square, List<Square>> returnAllPossibleMoves(int colorToMove) {
        HashMap<Square, List<Square>> possibleMoves = new HashMap<>();

        for (Map.Entry<Square, Piece> entry : TrackOfPieces.entrySet()) {
            Piece piece = entry.getValue();
            Square currentSquare = entry.getKey();

            if (piece.getColor() == colorToMove) {
                List<Square> movesForThisPiece = calculateMovesForPiece(piece, currentSquare);
                possibleMoves.put(currentSquare, movesForThisPiece);
            }
        }

        return possibleMoves;
    }

    private List<Square> calculateMovesForPiece(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();

        switch (piece.getType()) {
            case 1: // Pawn
                moves.addAll(calculatePawnMoves(piece, currentSquare));
                break;
            case 2: // Knight
                moves.addAll(calculateKnightMoves(piece, currentSquare));
                break;
            case 3: // Bishop
                moves.addAll(calculateBishopMoves(piece, currentSquare));
                break;
            case 4: // Rook
                moves.addAll(calculateRookMoves(piece, currentSquare));
                break;
            case 5: // Queen
                moves.addAll(calculateQueenMoves(piece, currentSquare));
                break;
            case 6: // King
                moves.addAll(calculateKingMoves(piece, currentSquare));
                break;
        }

        return moves;
    }
    private List<Square> Thiccy(Piece piece, Square currentSquare, int[][] directions) {
        List<Square> moves = new ArrayList<>();

        for (int[] direction : directions) {
            Square potentialMove = new Square(currentSquare.getPosX() + direction[0], currentSquare.getPosY() + direction[1]);

            while (isSquareWithinBoard(potentialMove)) {
                if (isSquareEmpty(potentialMove)) {
                    moves.add(potentialMove);
                } else {
                    if (isSquareOccupiedByOpponent(potentialMove, piece.getColor())) {
                        moves.add(potentialMove);
                    }
                    break; // Stop if any piece is encountered
                }
                potentialMove = new Square(potentialMove.getPosX() + direction[0], potentialMove.getPosY() + direction[1]);
            }
        }

        return moves;
    }
    // Implement specific movement logic for each piece type
    private List<Square> calculatePawnMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        int direction = piece.getColor() == 0 ? -1 : 1; // Assuming 0 is White, 1 is Black
        int startRow = piece.getColor() == 0 ? 6 : 1; // Starting row for White and Black

        // Forward move
        Square oneStepForward = new Square(currentSquare.getPosX(), currentSquare.getPosY() + direction);
        if (isSquareEmpty(oneStepForward)) {
            moves.add(oneStepForward);

            // Two-step move from start position
            if (currentSquare.getPosY() == startRow) {
                Square twoStepsForward = new Square(currentSquare.getPosX(), currentSquare.getPosY() + 2 * direction);
                if (isSquareEmpty(twoStepsForward)) {
                    moves.add(twoStepsForward);
                }
            }
        }

        // Capture moves
        Square[] diagonalSquares = {
                new Square(currentSquare.getPosX() - 1, currentSquare.getPosY() + direction),
                new Square(currentSquare.getPosX() + 1, currentSquare.getPosY() + direction)
        };

        for (Square diagonalSquare : diagonalSquares) {
            if (isSquareWithinBoard(diagonalSquare) && isSquareOccupiedByOpponent(diagonalSquare, piece.getColor())) {
                moves.add(diagonalSquare);
            }
        }

        return moves;
    }

    private boolean isSquareEmpty(Square square) {
        return TrackOfPieces.get(square) == null;
    }

    private boolean isSquareWithinBoard(Square square) {
        return square.getPosX() >= 0 && square.getPosX() < 8 && square.getPosY() >= 0 && square.getPosY() < 8;
    }

    private boolean isSquareOccupiedByOpponent(Square square, int myColor) {
        Piece piece = TrackOfPieces.get(square);
        return piece != null && piece.getColor() != myColor;
    }

    private boolean isSquareOccupiedByAlly(Square square, int myColor) {
        Piece piece = TrackOfPieces.get(square);
        return piece != null && piece.getColor() == myColor;
    }


    private List<Square> calculateKnightMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();

        // All possible L-shaped moves for a knight
        int[][] knightMoves = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            Square potentialMove = new Square(currentSquare.getPosX() + move[0], currentSquare.getPosY() + move[1]);
            if (isSquareWithinBoard(potentialMove) && isSquareOccupiedByOpponent(potentialMove, piece.getColor())) {
                moves.add(potentialMove);
            }
        }

        return moves;
    }

    private List<Square> calculateBishopMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        //top-left, top-right, bottom-left, bottom-right
        int[][] directions = {{-1, 1}, {1, 1}, {-1, -1}, {1, -1}};
        return Thiccy(piece, currentSquare, directions);
    }

    private List<Square> calculateRookMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        return Thiccy(piece, currentSquare, directions);

    }

    private List<Square> calculateQueenMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, -1}, {1, -1}};
        return Thiccy(piece, currentSquare, directions);
    }

    private List<Square> calculateKingMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        int[][] kingMoves = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, -1}, {1, -1}
        };

        for (int[] move : kingMoves) {
            Square potentialMove = new Square(currentSquare.getPosX() + move[0], currentSquare.getPosY() + move[1]);
            if (isSquareWithinBoard(potentialMove) && isSquareOccupiedByOpponent(potentialMove, piece.getColor())) {
                moves.add(potentialMove);
            }
        }

        return moves;
    }

    public Piece getPieceAt(int x, int y) {
        return TrackOfPieces.get(new Square(x,y));
    }

    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = getPieceAt(x, y);
                if (piece != null) {
                    boardString.append(getPieceSymbol(piece)).append(" ");
                } else {
                    boardString.append(" . "); //empty square
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }


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

    public List<Square> getPotentialMovesForPiece(int x, int y) {
        HashMap<Square, List<Square>> possibleMoves = returnAllPossibleMoves(0);
        return possibleMoves.get(squares[x][y]);


    }
}
