package game;

import gui.GameFrame;

import java.util.LinkedList;
import java.util.List;

public class AiGameState extends GameState {

    private GameFrame gameFrame;
    private Player localPlayer;
    private Player aiPlayer;
    private Point minimaxSelect;
    private Point minimaxMove;

    public AiGameState(GameStyle gameStyle, Cell localColour) {
        super(gameStyle);
        if (localColour.equals(Cell.RED)) {
            localPlayer = redPlayer;
            aiPlayer = bluePlayer;
        } else {
            localPlayer = bluePlayer;
            aiPlayer = redPlayer;
        }
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    @Override
    public void doSelect(Point point) {
        // The player can only select something on the board if it is their turn
        if (getActivePlayer().getColour().equals(localPlayer.getColour())) {
            super.doSelect(point);
        }
    }

    @Override
    public void doMove(Point point) {
        // The player can only move something on the board if it is their turn
        if (getActivePlayer().getColour().equals(localPlayer.getColour())) {
            super.doMove(point);
            // Move must have been a success, so now it's the AI's turn
            if (getActivePlayer().getColour().equals(aiPlayer.getColour())) {
                runMinimax();
                doAiSelect(minimaxSelect);
                doAiMove(minimaxMove);
            }
        }
    }

    public List<Point> getPossibleSelections() {
        List<Point> possibleSelections = new LinkedList<Point>();
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                if (aiPlayer.getColour().equals(getTheBoard().getCell(r, c))) {
                    possibleSelections.add(new Point(r,c));
                }
            }
        }
        return possibleSelections;
    }

    public List<Point> getPossibleMovesForSelection(Point selection) {
        List<Point> possibleMoves = new LinkedList<Point>();
        for (int r = selection.getRow() - 2; r <= selection.getRow() + 2; r++) {
            for (int c = selection.getColumn() - 2; c <= selection.getColumn() + 2; c++) {
                if (Cell.EMPTY.equals(getTheBoard().getCell(r, c))) {
                    possibleMoves.add(new Point(r,c));
                }
            }
        }
        return possibleMoves;
    }

    public int calculateHeuristicForBoard(BoardState boardState) {
        return boardState.countCells(aiPlayer.getColour()) - boardState.countCells(localPlayer.getColour());
    }

    public void runMinimax() {
        int heuristic = Integer.MIN_VALUE;

        // Create a new board for each possible move
        for (Point selection : getPossibleSelections()) {
            for (Point move : getPossibleMovesForSelection(selection)) {
                BoardState clonedBoard = getTheBoard().copy();
                // Move the cell if the distance is 2 units
                // Or clone if the distance is 1 unit
                clonedBoard.setCell(move, aiPlayer.getColour());
                if (Point.getColumnDistance(selection, move) == 2 || Point.getRowDistance(selection, move) == 2) {
                    clonedBoard.setCell(selection, Cell.EMPTY);
                }
                // After the move, all neighbours change colour
                for (int r = move.getRow() - 1; r <= move.getRow() + 1; r++) {
                    for (int c = move.getColumn() - 1; c <= move.getColumn() + 1; c++) {
                        if (clonedBoard.getCell(r,c).equals(Cell.RED) || clonedBoard.getCell(r,c).equals(Cell.BLUE)) {
                            clonedBoard.setCell(r, c, aiPlayer.getColour());
                        }
                    }
                }
                int cloneHeuristic = calculateHeuristicForBoard(clonedBoard);
                if (cloneHeuristic > heuristic) {
                    minimaxSelect = selection;
                    minimaxMove = move;
                    heuristic = cloneHeuristic;
                }
            }
        }
    }

    public void doAiSelect(Point point) {
        super.doSelect(point);
    }

    public void doAiMove(Point point) {
        super.doMove(point);
        gameFrame.refreshBoard();
    }

    public boolean isLocalPlayerTurn() {
        return localPlayer.equals(getActivePlayer());
    }


}
