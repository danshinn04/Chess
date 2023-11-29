import java.util.*;
public interface Piece {
    Color getColor();
    PieceType getType();
    List<Move> getPossibleMoves(Board board, Position position);
    boolean move(Board board, Position newPosition);

    boolean getSymbol();

}
