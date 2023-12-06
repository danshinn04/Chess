import java.util.Objects;

public class Square {
    private int posX;
    private int posY;
    public Square(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public void updatePos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return posX == square.posX && posY == square.posY;
    }
    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }

}
