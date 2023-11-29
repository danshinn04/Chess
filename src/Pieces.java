import java.util.*;
public interface Pieces {
    Color getColor();
    PieceType getType();
    List<Move> getPossibleMoves(Board board, Position position);
    boolean move(Board board, Position newPosition);
}

