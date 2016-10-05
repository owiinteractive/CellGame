package game;

public class Point {

    private int row;
    private int column;

    public Point(int row, int column) {
        this.row = row;
        this.column = column;
    }
    
    public Point(String pointString) {
        String[] values = pointString.split(";");
        this.row = Integer.parseInt(values[0]);
        this.column = Integer.parseInt(values[1]);
    }

    public Point copy() {
        return new Point(row, column);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public static int getRowDistance(Point point1, Point point2) {
        return Math.abs(point1.getRow() - point2.getRow());
    }

    public static int getColumnDistance(Point point1, Point point2) {
        return Math.abs(point1.getColumn() - point2.getColumn());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return row == point.row && column == point.column;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
    
    @Override
    public String toString() {
        return row + ";" + column;
    }
}
