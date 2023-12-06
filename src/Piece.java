import java.awt.image.BufferedImage;
import java.lang.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.*;
public class Piece {
    private final int color;
    private Square currentSquare;
    private int type;

    //0: Nothing
    //1: Pawn
    //2: Knight
    //3: Bishop
    //4: Rook
    //5: Queen
    //6: King
    public Piece(int color, int posX, int posY, int type) {
        this.color = color;
        this.currentSquare = new Square(posX, posY);
        this.type = type;
    }

    public int getColor() {
        return color;
    }



    public void changePOSITION(int posX, int posY) {
        currentSquare.updatePos(posX, posY);
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public int getType() {
        return type;
    }
}