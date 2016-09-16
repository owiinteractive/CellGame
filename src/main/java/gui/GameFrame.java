package gui;

import game.*;
import game.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class GameFrame extends JFrame {

    private GameState gameState;
    private Map<Point, BoardCellPanel> boardCellPanels;

    public GameFrame() {
        super();
        boardCellPanels = new HashMap<Point, BoardCellPanel>();
        initGui();
    }

    public void initGui() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("New Game...");

        JMenuItem twoPlayerItem = new JMenuItem("New Two-Player Game");
        twoPlayerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameState = new GameState(GameStyle.TWO_PLAYER);
                refreshBoard();
            }
        });

        JMenuItem networkedItem = new JMenuItem("New Networked Game");
        JMenuItem aiItem = new JMenuItem("New AI Game");

        menu.add(twoPlayerItem);
        menu.add(networkedItem);
        menu.add(aiItem);

        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BoardState.MAX+1, BoardState.MAX+1));
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                BoardCellPanel cellPanel = new BoardCellPanel(r,c);
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                cellPanel.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {}
                    public void mousePressed(MouseEvent e) {}
                    public void mouseEntered(MouseEvent e) {}
                    public void mouseExited(MouseEvent e) {}
                    public void mouseReleased(MouseEvent e) {
                        BoardCellPanel clickedCell = (BoardCellPanel) e.getSource();
                        if (gameState.hasActiveSelection()) {
                            gameState.doMove(clickedCell.getRow(), clickedCell.getColumn());
                        } else {
                            gameState.doSelect(clickedCell.getRow(), clickedCell.getColumn());
                        }
                        refreshBoard();
                    }
                });
                boardCellPanels.put(new Point(r,c), cellPanel);
                boardPanel.add(cellPanel);
            }
        }

        this.add(boardPanel);

        this.setSize(new Dimension(500, 500));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void refreshBoard() {
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                switch (gameState.getTheBoard().getCell(r,c)) {
                    case RED   : boardCellPanels.get(new Point(r,c)).setBackground(Color.RED); break;
                    case BLUE  : boardCellPanels.get(new Point(r,c)).setBackground(Color.BLUE); break;
                    case EMPTY : boardCellPanels.get(new Point(r,c)).setBackground(Color.WHITE); break;
                }
            }
        }
        if (gameState.hasActiveSelection()) {
            for (int r = gameState.getSelectedPoint().getRow() - 2; r <= gameState.getSelectedPoint().getRow() + 2; r++) {
                for (int c = gameState.getSelectedPoint().getColumn() - 2; c <= gameState.getSelectedPoint().getColumn() + 2; c++) {
                    if (gameState.getActivePlayer().colour.equals(Cell.RED) && gameState.getTheBoard().getCell(r,c).equals(Cell.EMPTY)) {
                        boardCellPanels.get(new Point(r,c)).setBackground(new Color(1.0f, 0.8f, 0.8f));
                    } else if (gameState.getActivePlayer().colour.equals(Cell.BLUE) && gameState.getTheBoard().getCell(r,c).equals(Cell.EMPTY)) {
                        boardCellPanels.get(new Point(r,c)).setBackground(new Color(0.8f, 0.8f, 1.0f));
                    }
                }
            }
            if (gameState.getTheBoard().getCell(gameState.getSelectedPoint()).equals(Cell.RED) && gameState.getActivePlayer().getColour().equals(Cell.RED)) {
                boardCellPanels.get(gameState.getSelectedPoint()).setBackground(new Color(0.5f, 0f, 0f));
            }
            if (gameState.getTheBoard().getCell(gameState.getSelectedPoint()).equals(Cell.BLUE) && gameState.getActivePlayer().getColour().equals(Cell.BLUE)) {
                boardCellPanels.get(gameState.getSelectedPoint()).setBackground(new Color(0f, 0f, 0.5f));
            }
        }
    }
}
