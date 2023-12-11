import java.util.*;
import javax.swing.*;

public class ChessBoard {
    private HashMap<Square, Piece> TrackOfPieces;
    private Square[][] squares;
    private int currentTurn = 0;
    private ArrayList<String> moveHistory;
    public ChessBoard() {
        TrackOfPieces = new HashMap<>();
        squares = new Square[8][8];
        moveHistory = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares[x][y] = new Square(x, y);
            }
        }

        // Black pieces
        TrackOfPieces.put(squares[0][0],new Rook(1, 0, 0)); //Black Rook at a8
        TrackOfPieces.put(squares[1][0],new Knight(1, 1, 0)); //Black Knight at b8
        TrackOfPieces.put(squares[2][0],new Bishop(1, 2, 0)); //Black Bishop at c8
        TrackOfPieces.put(squares[3][0],new Queen(1, 3, 0)); //Black Queen at d8
        TrackOfPieces.put(squares[4][0],new King(1, 4, 0)); //Black King at e8
        TrackOfPieces.put(squares[5][0],new Bishop(1, 5, 0)); //Black Bishop at f8
        TrackOfPieces.put(squares[6][0],new Knight(1, 6, 0)); //Black Knight at g8
        TrackOfPieces.put(squares[7][0],new Rook(1, 7, 0)); //Black Rook at h8
        for (int i = 0; i < 8; i++) {
            TrackOfPieces.put(squares[i][1], new Pawn(1, i, 1)); //Black Pawns
        }

        //White pieces
        TrackOfPieces.put(squares[0][7],new Rook(0, 0, 7)); //White Rook at a1
        TrackOfPieces.put(squares[1][7],new Knight(0, 1, 7)); //White Knight at b1
        TrackOfPieces.put(squares[2][7],new Bishop(0, 2, 7)); //White Bishop at c1
        TrackOfPieces.put(squares[3][7],new Queen(0, 3, 7)); //White Queen at d1
        TrackOfPieces.put(squares[4][7],new King(0, 4, 7)); //White King at e1
        TrackOfPieces.put(squares[5][7],new Bishop(0, 5, 7)); //White Bishop at f1
        TrackOfPieces.put(squares[6][7],new Knight(0, 6, 7)); //White Knight at g1
        TrackOfPieces.put(squares[7][7],new Rook(0, 7, 7)); //White Rook at h1
        for (int i = 0; i < 8; i++) {
            TrackOfPieces.put(squares[i][6], new Pawn(0, i, 6));  //White Pawns
        }
    }
    public void changeTurn() {
        currentTurn = (currentTurn == 0) ? 1 : 0; //Move parity {0: White to Move, 1: Black to Move}
    }


    public boolean movePiece(Square fromSquare, Square toSquare) {

        Piece piece = getPieceAt(fromSquare.getPosX(), fromSquare.getPosY());
        if (piece != null && piece.getColor() == currentTurn) {
            List<Square> possibleMoves = getPotentialMovesForPiece(fromSquare.getPosX(), fromSquare.getPosY());

            //Check if the move is in the list of possible moves
            if (possibleMoves.contains(toSquare)) {
                if (piece instanceof Pawn && isEnPassantMove(piece, fromSquare, toSquare)) {
                    handleEnPassantCapture(piece, toSquare);
                }
                if (piece instanceof Pawn) {
                    if ((piece.getColor() == 0 && toSquare.getPosY() == 0) || // White pawn reaches the last row
                            (piece.getColor() == 1 && toSquare.getPosY() == 7)) { // Black pawn reaches the last row
                        handlePawnPromotion(piece, toSquare);
                    }
                }

                MoveUpdate(fromSquare.getPosX(), fromSquare.getPosY(), toSquare.getPosX(), toSquare.getPosY());

                //Check for castling move (king moving two squares)
                if (piece instanceof King && Math.abs(fromSquare.getPosX() - toSquare.getPosX()) == 2) {
                    performCastling(fromSquare, toSquare);
                }
                resetJustMovedTwoSquares();

                changeTurn();
                if (piece instanceof Pawn && Math.abs(fromSquare.getPosY() - toSquare.getPosY()) == 2) {
                    ((Pawn) piece).setJustMovedTwoSquares(true);
                }
                return true;
            }
        }
        return false;
    }

    private void handlePawnPromotion(Piece piece, Square square) {
        String[] options = new String[]{"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose piece for promotion",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        promotePawn(piece, square, options[choice]);
    }
    public void placePieceAt(Piece piece, int x, int y) {
        Square square = squares[x][y];
        if (piece != null) {
            piece.changePOSITION(x, y); // Only change position if piece is not null
        }
        TrackOfPieces.put(square, piece); // This can handle null (removing a piece)
    }
    private void promotePawn(Piece piece, Square square, String chosenPiece) {
        int color = piece.getColor();
        Piece newPiece;

        switch (chosenPiece) {
            case "Queen":
                newPiece = new Queen(color, square.getPosX(), square.getPosY());
                System.out.println("YE");
                break;
            case "Rook":
                newPiece = new Rook(color, square.getPosX(), square.getPosY());
                break;
            case "Bishop":
                newPiece = new Bishop(color, square.getPosX(), square.getPosY());
                break;
            case "Knight":
                newPiece = new Knight(color, square.getPosX(), square.getPosY());
                break;
            default:
                throw new IllegalArgumentException("Invalid piece type for promotion");
        }
        placePieceAt(newPiece, piece.getX(), piece.getY());
    }


    private boolean isEnPassantMove(Piece pawn, Square fromSquare, Square toSquare) {
        int dy = Math.abs(toSquare.getPosY() - fromSquare.getPosY());
        int dx = Math.abs(toSquare.getPosX() - fromSquare.getPosX());
        return dx == 1 && dy == 1 && isSquareEmpty(toSquare);
    }

    private void handleEnPassantCapture(Piece pawn, Square toSquare) {
        int capturedPawnY = pawn.getColor() == 0 ? toSquare.getPosY() + 1 : toSquare.getPosY() - 1;
        Square capturedPawnSquare = new Square(toSquare.getPosX(), capturedPawnY);
        TrackOfPieces.remove(capturedPawnSquare); // Remove the captured pawn
    }

    private void resetJustMovedTwoSquares() {
        for (Piece piece : TrackOfPieces.values()) {
            if (piece instanceof Pawn) {
                ((Pawn) piece).setJustMovedTwoSquares(false);
            }
        }
    }

    private void performCastling(Square kingFrom, Square kingTo) {
        int row = kingFrom.getPosY();
        int rookFromX = kingTo.getPosX() == 6 ? 7 : 0; // 7 for king-side, 0 for queen-side
        int rookToX = kingTo.getPosX() == 6 ? 5 : 3; // Next to the king's final position

        Square rookFrom = new Square(rookFromX, row);
        Square rookTo = new Square(rookToX, row);
        Piece rook = getPieceAt(rookFromX, row);

        MoveUpdate(rookFrom.getPosX(), rookFrom.getPosY(), rookTo.getPosX(), rookTo.getPosY());
    }
    public Square getSquares(int x, int y) {
        return squares[x][y];
    }


    public void MoveUpdate(int prevX, int prevY, int newX, int newY) {
        Square prevSquare = squares[prevX][prevY];
        Square newSquare = squares[newX][newY];
        Piece movingPiece = TrackOfPieces.get(prevSquare);
        if (movingPiece != null) {
            //Remove the moving piece from its current square
            TrackOfPieces.remove(prevSquare);
            //Check if there is a piece at the new square
            Piece capturedPiece = TrackOfPieces.get(newSquare);
            if (capturedPiece != null && capturedPiece.getColor() != movingPiece.getColor()) {
                //Capture logic: Remove the captured piece if it's an opponent's piece
                TrackOfPieces.remove(newSquare);
            }
            //Move the piece to the new square and update its internal position
            movingPiece.changePOSITION(newX, newY);
            TrackOfPieces.put(newSquare, movingPiece);
            recordMove(movingPiece, newSquare);
        }

    }




    public void recordMove(Piece piece, Square toSquare) {
        String moveNotation = moveToString(piece, toSquare);
        moveHistory.add(moveNotation);
    }

    public String moveToString(Piece piece, Square destination) {
        String pieceNotation = "";
        if (piece instanceof Knight) {
            pieceNotation = "N";
        } else if (piece instanceof Bishop) {
            pieceNotation = "B";
        } else if (piece instanceof Rook) {
            pieceNotation = "R";
        } else if (piece instanceof Queen) {
            pieceNotation = "Q";
        } else if (piece instanceof King) {
            pieceNotation = "K";
        }

        char file = (char) ('a' + destination.getPosX());
        int rank = 8 - destination.getPosY();

        return pieceNotation + file + rank;
    }

    public Square getSquare(int x, int y){
        return squares[x][y];
    }

    public HashMap<Square, Piece> returnBoard(){
        return TrackOfPieces;
    }
    public HashMap<Square, List<Square>> returnAllPossibleMoves(int colorToMove) {
        HashMap<Square, List<Square>> possibleMoves = new HashMap<>();
        for (Map.Entry<Square, Piece> entry : TrackOfPieces.entrySet()) {
            Piece piece = entry.getValue();
            Square currentSquare = entry.getKey();
            if (piece != null && piece.getColor() == colorToMove) {
                List<Square> movesForThisPiece = calculateMovesForPiece(piece, currentSquare);

                possibleMoves.put(currentSquare, movesForThisPiece);
            }
        }
        return possibleMoves;
    }

    private List<Square> calculateMovesForPiece(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        if(piece.getColor()==currentTurn) {
            switch (piece.getType()) {
                case 1: //Pawn
                    moves.addAll(calculatePawnMoves(piece, currentSquare));
                    break;
                case 2: //Knight
                    moves.addAll(calculateKnightMoves(piece, currentSquare));
                    break;
                case 3: //Bishop
                    moves.addAll(calculateBishopMoves(piece, currentSquare));
                    break;
                case 4: //Rook
                    moves.addAll(calculateRookMoves(piece, currentSquare));
                    break;
                case 5: //Queen
                    moves.addAll(calculateQueenMoves(piece, currentSquare));
                    break;
                case 6: //King
                    moves.addAll(calculateKingMoves(piece, currentSquare));
                    break;
            }
        }

        return moves;
    }

    private List<Square> calculateTheoreticalMovesForPiece(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        if(piece.getColor()!=currentTurn) {
            switch (piece.getType()) {
                case 1: //Pawn
                    moves.addAll(calculatePawnMoves(piece, currentSquare));
                    break;
                case 2: //Knight
                    moves.addAll(calculateKnightMoves(piece, currentSquare));
                    break;
                case 3: //Bishop
                    moves.addAll(calculateBishopMoves(piece, currentSquare));
                    break;
                case 4: //Rook
                    moves.addAll(calculateRookMoves(piece, currentSquare));
                    break;
                case 5: //Queen
                    moves.addAll(calculateQueenMoves(piece, currentSquare));
                    break;
                case 6: //King
                    moves.addAll(calculateKingMoves(piece, currentSquare));
                    break;
            }
        }

        return moves;
    }

    private List<Square> Thiccy(Piece piece, Square currentSquare, int[][] directions) {
        List<Square> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int nextX = currentSquare.getPosX() + direction[0];
            int nextY = currentSquare.getPosY() + direction[1];
            Square potentialMove = new Square(nextX, nextY);
            while (isSquareWithinBoard(potentialMove)) {
                if (isSquareEmpty(potentialMove)) {
                    moves.add(potentialMove);
                } else {
                    if (isSquareOccupiedByOpponent(potentialMove, piece.getColor())) {
                        moves.add(potentialMove); //Add capturing move
                    }
                    break; //Stop if any piece is encountered
                }

                //Update the potentialMove for the next iteration
                nextX += direction[0];
                nextY += direction[1];
                potentialMove = new Square(nextX, nextY);
            }
        }

        return moves;
    }

    private void addEnPassantMoves(Piece piece, List<Square> moves, Square currentSquare, int xOffset) {
        int x = currentSquare.getPosX() + xOffset;
        if (x >= 0 && x < 8) {
            Piece adjacentPiece = getPieceAt(x, currentSquare.getPosY());
            if (adjacentPiece instanceof Pawn && ((Pawn)adjacentPiece).isJustMovedTwoSquares()) {
                System.out.println("Yes");
                int yTarget = piece.getColor() == 0 ? currentSquare.getPosY() - 1 : currentSquare.getPosY() + 1;
                moves.add(new Square(x, yTarget));
            }
        }
    }

    private List<Square> calculatePawnMoves(Piece piece, Square currentSquare) {
        List<Square> moves = new ArrayList<>();
        int direction = piece.getColor() == 0 ? -1 : 1; //Assuming 0 is White, 1 is Black
        int startRow = piece.getColor() == 0 ? 6 : 1; //Starting row for White and Black
        //Forward move
        Square oneStepForward = new Square(currentSquare.getPosX(), currentSquare.getPosY() + direction);
        if (isSquareEmpty(oneStepForward) && isSquareWithinBoard(oneStepForward)) {
            moves.add(oneStepForward);
            //Two-step move from start position
            if (currentSquare.getPosY() == startRow) {
                Square twoStepsForward = new Square(currentSquare.getPosX(), currentSquare.getPosY() + 2 * direction);
                if (isSquareEmpty(twoStepsForward)) {
                    moves.add(twoStepsForward);
                }
            }
        }

        //Capture moves
        Square[] diagonalSquares = {
                new Square(currentSquare.getPosX() - 1, currentSquare.getPosY() + direction),
                new Square(currentSquare.getPosX() + 1, currentSquare.getPosY() + direction)
        };

        for (Square diagonalSquare : diagonalSquares) {
            if (!isSquareEmpty(diagonalSquare)){
                if (isSquareWithinBoard(diagonalSquare) && isSquareOccupiedByOpponent(diagonalSquare, piece.getColor())) {
                    moves.add(diagonalSquare);
                }
            }
        }
        // En-passant logic for white pawns
        if (piece.getColor() == 0 && currentSquare.getPosY() == 3) {
            addEnPassantMoves(piece, moves, currentSquare, -1); // Check left
            addEnPassantMoves(piece, moves, currentSquare, 1);  // Check right
        }

        // En-passant logic for black pawns
        if (piece.getColor() == 1 && currentSquare.getPosY() == 4) {
            addEnPassantMoves(piece, moves, currentSquare, -1); // Check left
            addEnPassantMoves(piece, moves, currentSquare, 1);  // Check right
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

        //All possible L-shaped moves for a knight
        int[][] knightMoves = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            Square potentialMove = new Square(currentSquare.getPosX() + move[0], currentSquare.getPosY() + move[1]);
            if (isSquareWithinBoard(potentialMove) && !isSquareOccupiedByAlly(potentialMove, piece.getColor())) {
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
            if (isSquareWithinBoard(potentialMove) && !isSquareOccupiedByAlly(potentialMove, piece.getColor())) {
                moves.add(potentialMove);
            }
        }

        if (!piece.returnhasMoved()) {
            // Check king-side and queen-side castling
            if (isCastlingPossible(currentSquare.getPosX(), currentSquare.getPosY(), 7, currentSquare.getPosY())) {
                moves.add(new Square(6, currentSquare.getPosY())); // King-side castling

            }
            if (isCastlingPossible(currentSquare.getPosX(), currentSquare.getPosY(), 0, currentSquare.getPosY())) {
                moves.add(new Square(2, currentSquare.getPosY())); // Queen-side castling
            }
        }

        return moves;
    }

    public Piece getPieceAt(int x, int y) {
        return TrackOfPieces.get(squares[x][y]);
    }

    public HashMap<Square, Piece> getBoardState() {
        return new HashMap<>(TrackOfPieces);
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
        HashMap<Square, List<Square>> possibleMoves = returnAllPossibleMoves(currentTurn);
        List<Square> moves = possibleMoves.get(squares[x][y]);

        return filterMovesThatLeaveKingInCheck(moves, squares[x][y]);

    }

    private List<Square> filterMovesThatLeaveKingInCheck(List<Square> moves, Square currentSquare) {
        Piece piece = getPieceAt(currentSquare.getPosX(), currentSquare.getPosY());
        List<Square> safeMoves = new ArrayList<>();

        for (Square move : moves) {
            // Temporarily make the move
            makeTemporaryMove(currentSquare, move, piece);

            // Check if the king is still in check after this move
            if (!isKingInCheck(piece.getColor())) {
                safeMoves.add(move);
            }

            // Undo the temporary move
            undoTemporaryMove(currentSquare, move, piece);
        }

        return safeMoves;
    }
    private Piece tempCapturedPiece;

    private void makeTemporaryMove(Square from, Square to, Piece piece) {
        tempCapturedPiece = TrackOfPieces.get(to);
        TrackOfPieces.remove(from);
        TrackOfPieces.put(to, piece);
    }


    private void undoTemporaryMove(Square from, Square to, Piece piece) {
        TrackOfPieces.remove(to);
        TrackOfPieces.put(from, piece);
        if (tempCapturedPiece != null) {
            TrackOfPieces.put(to, tempCapturedPiece);
            tempCapturedPiece = null;
        }
    }


    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean isCheckmate(int playerColor) {
        if (!isKingInCheck(playerColor)) {
            return false;
        }

        return !hasLegalMoves(playerColor);
    }

    private boolean checkAnnounced = false;

    public boolean isKingInCheck(int kingColor) {
        Square kingSquare = findKing(kingColor);
        boolean isUnderAttack = isSquareUnderAttack(kingSquare, kingColor);

        if (isUnderAttack && !checkAnnounced) {
            System.out.println("Check");
            JOptionPane.showMessageDialog(null, "Check!");
            checkAnnounced = true; //Set flag to true after announcing
        } else if (!isUnderAttack) {
            checkAnnounced = false; //Reset flag if king is no longer in check
        }

        return isUnderAttack;
    }

    /*String[] options = new String[]{"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose piece for promotion",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        promotePawn(piece, square, options[choice]);*/

    private Square findKing(int kingColor) {
        for (Map.Entry<Square, Piece> entry : TrackOfPieces.entrySet()) {
            Piece piece = entry.getValue();
            if (piece != null && piece.getColor() == kingColor && piece instanceof King) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean isSquareUnderAttack(Square square, int kingColor) {
        for (Map.Entry<Square, Piece> entry : TrackOfPieces.entrySet()) {
            Piece piece = entry.getValue();
            if (piece != null && piece.getColor() != kingColor) {
                List<Square> attackingMoves = calculateTheoreticalMovesForPiece(piece, entry.getKey());
                if (attackingMoves.contains(square)) {
                    return true;
                }
            }
        }
        return false;
    }


    public GameState checkGameState() {
        boolean kingInCheck = isKingInCheck(currentTurn);
        boolean hasLegalMoves = hasLegalMoves(currentTurn);

        if (kingInCheck) {
            return hasLegalMoves ? GameState.CHECK : GameState.CHECKMATE;
        } else {
            return hasLegalMoves ? GameState.ONGOING : GameState.STALEMATE;
        }
    }

    private boolean hasLegalMoves(int playerColor) {
        for (Map.Entry<Square, Piece> entry : TrackOfPieces.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getColor() == playerColor) {
                List<Square> legalMoves = getPotentialMovesForPiece(entry.getKey().getPosX(), entry.getKey().getPosY());
                if (!legalMoves.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCastlingPossible(int kingX, int kingY, int rookX, int rookY) {
        Piece king = getPieceAt(kingX, kingY);
        Piece rook = getPieceAt(rookX, rookY);

        //Check if pieces are King and Rook and have not moved
        if (!(rook instanceof Rook) || king.returnhasMoved() || rook.returnhasMoved()) {
            return false;
        }

        //Determine the row and range for castling
        int row = kingY;
        int startCol = Math.min(kingX, rookX) + 1;
        int endCol = Math.max(kingX, rookX) - 1;

        //Check if squares between king and rook are empty
        for (int col = startCol; col <= endCol; col++) {
            if (!isSquareEmpty(new Square(col, row))) {
                return false;
            }
        }
        boolean yeet = false;
        if (king.getColor() == currentTurn) {
            yeet = !isKingInCheck(currentTurn);
        }

        //Check if the king is in check or the castling squares are under attack
        return yeet && !areCastlingSquaresUnderAttack(kingX, row, endCol, king.getColor());
    }

    private boolean areCastlingSquaresUnderAttack(int startCol, int row, int endCol, int playerColor) {
        int opponentColor = 1 - playerColor;
        for (int col = startCol; col <= endCol; col++) {
            Square squareToCheck = new Square(col, row);
            if (isSquareUnderAttackByOpponentPieces(squareToCheck, opponentColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSquareUnderAttackByOpponentPieces(Square square, int opponentColor) {
        for (Map.Entry<Square, Piece> entry : TrackOfPieces.entrySet()) {
            Piece piece = entry.getValue();
            if (piece != null && piece.getColor() == opponentColor) {
                List<Square> attackingMoves = calculateMovesForPiece(piece, entry.getKey());
                if (attackingMoves.contains(square)) {
                    return true;
                }
            }
        }
        return false;
    }


    public void update(JTextArea moveHistoryArea, ArrayList<String> movehistory) {
        for (String s :moveHistory){
            moveHistoryArea.append(s + "\n");
            movehistory.add(s);
        }
        moveHistory.clear();
    }

    public HashMap<Square, Piece> getTrackOfPieces() {
        return TrackOfPieces;
    }
}

enum GameState {
    ONGOING, CHECK, CHECKMATE, STALEMATE
}
