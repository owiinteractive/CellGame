package game;

public class GameState {

    private GameStyle gameStyle;
    private BoardState theBoard;
    private Player redPlayer;
    private Player bluePlayer;
    private Player activePlayer;
    private Point selectedPoint;

    public GameState(GameStyle gameStyle) {

        this.gameStyle = gameStyle;

        theBoard = new BoardState();
        theBoard.setCell(new Point(BoardState.MIN, BoardState.MIN), Cell.RED);
        theBoard.setCell(new Point(BoardState.MAX, BoardState.MAX), Cell.BLUE);

        redPlayer = new Player(Cell.RED);
        bluePlayer = new Player(Cell.BLUE);
        activePlayer = redPlayer;

        selectedPoint = null;
    }

    public void doSelect(Point point) {
        if (hasActiveSelection() && point.equals(selectedPoint)) {
            selectedPoint = null;
        } else if (isValidSelection(point)) {
            selectedPoint = point;
        }
    }

    public void doSelect(int row, int column) {
        doSelect(new Point(row, column));
    }

    public void doMove(Point point) {
        if (isLegalMove(point)) {
            theBoard.setCell(point, activePlayer.getColour());
            if (Point.getColumnDistance(selectedPoint, point) == 2 || Point.getRowDistance(selectedPoint, point) == 2) {
                theBoard.setCell(selectedPoint, Cell.EMPTY);
            }
            selectedPoint = null;

            for (int r = point.getRow() - 1; r <= point.getRow() + 1; r++) {
                for (int c = point.getColumn() - 1; c <= point.getColumn() + 1; c++) {
                    if (theBoard.getCell(r,c).equals(Cell.RED) || theBoard.getCell(r,c).equals(Cell.BLUE)) {
                        theBoard.setCell(r, c, activePlayer.getColour());
                    }
                }
            }
            switchPlayers();
        }
    }

    public void doMove(int row, int column) {
        doMove(new Point(row, column));
    }

    public void switchPlayers() {
        if (activePlayer == redPlayer) {
            activePlayer = bluePlayer;
        } else {
            activePlayer = redPlayer;
        }
    }

    public boolean hasActiveSelection() {
        return selectedPoint != null;
    }

    public boolean isValidSelection(Point point) {
        return !hasActiveSelection() && theBoard.getCell(point).equals(activePlayer.getColour());
    }

    public boolean isLegalMove(Point point) {
        return hasActiveSelection() && Point.getColumnDistance(selectedPoint, point) <= 2 && Point.getRowDistance(selectedPoint, point) <= 2 && theBoard.getCell(point).equals(Cell.EMPTY);
    }

    public boolean gameIsOver() {
        return !boardHasBlueCells() || !boardHasRedCells() || !boardHasEmptySpace();
    }

    public boolean boardHasBlueCells() {
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                if (theBoard.getCell(r,c).equals(Cell.BLUE)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean boardHasRedCells() {
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                if (theBoard.getCell(r,c).equals(Cell.RED)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean boardHasEmptySpace() {
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                if (theBoard.getCell(r,c).equals(Cell.EMPTY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public BoardState getTheBoard() {
        return theBoard;
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
}
