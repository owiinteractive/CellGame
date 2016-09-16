package game;

public class Player {

    public Cell colour;

    public Player(Cell colour) {
        this.colour = colour;
    }

    public Cell getColour() {
        return colour;
    }

    public void setColour(Cell colour) {
        this.colour = colour;
    }
}
