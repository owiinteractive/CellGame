package game;

import java.util.HashMap;
import java.util.Map;

public class BoardState {

    public static final int MIN = 0;
    public static final int MAX = 7;

    private Map<Point, Cell> cells;

    public BoardState() {
        cells = new HashMap<Point, Cell>();
        for (int r = MIN; r <= MAX; r++) {
            for (int c = MIN; c <= MAX; c++) {
                setCell(new Point(r,c), Cell.EMPTY);
            }
        }
    }

    public BoardState(Map<Point, Cell> copyCells) {
        cells = new HashMap<Point, Cell>();
        cells.putAll(copyCells);
    }

    public Cell getCell(Point point) {
        if (point.getRow() < MIN || point.getColumn() < MIN) return Cell.OFFBOARD;
        if (point.getRow() > MAX || point.getColumn() > MAX) return Cell.OFFBOARD;

        return cells.get(point);
    }

    public Cell getCell(int row, int column) {
        return getCell(new Point(row,column));
    }

    public void setCell(Point point, Cell cell) {
        cells.put(point, cell);
    }

    public void setCell(int row, int column, Cell cell) {
        setCell(new Point(row, column), cell);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardState boardState = (BoardState) o;

        for (int r = MIN; r <= MAX; r++) {
            for (int c = MIN; c < MAX; c++) {
                if (!getCell(new Point(r,c)).equals(boardState.getCell(new Point(r,c)))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return cells != null ? cells.hashCode() : 0;
    }

    public BoardState copy() {
        return new BoardState(cells);
    }
}
