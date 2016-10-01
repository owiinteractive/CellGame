package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import game.BoardState;
import game.Cell;
import game.GameState;
import game.GameStyle;
import game.Point;

public class GameFrame extends JFrame {

    private GameState gameState;
    private JLabel messageLabel;
    private Map<Point, BoardCellPanel> boardCellPanels;

    public GameFrame() {
        super();
        boardCellPanels = new HashMap<Point, BoardCellPanel>();
        initGui();
    }

    public void initGui() {

        // Menu
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
        networkedItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameFrame.this.dispose();
                new NetworkSetupFrame();
            }
        });


        JMenuItem aiItem = new JMenuItem("New AI Game");

        menu.add(twoPlayerItem);
        menu.add(networkedItem);
        menu.add(aiItem);

        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        // Setup for visual board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BoardState.MAX+1, BoardState.MAX+1));
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                BoardCellPanel cellPanel = new BoardCellPanel(r,c);
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                // Do something is board is clicked
                cellPanel.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {}
                    public void mousePressed(MouseEvent e) {}
                    public void mouseEntered(MouseEvent e) {}
                    public void mouseExited(MouseEvent e) {}
                    public void mouseReleased(MouseEvent e) {
                        BoardCellPanel clickedCell = (BoardCellPanel) e.getSource();
                        gameState.doSelect(clickedCell.getRow(), clickedCell.getColumn());
                        gameState.doMove(clickedCell.getRow(), clickedCell.getColumn());
                        refreshBoard();
                    }
                });
                boardCellPanels.put(new Point(r,c), cellPanel);
                boardPanel.add(cellPanel);
            }
        }

        // Finish setting up frame
        messageLabel = new JLabel();
        
        this.setLayout(new BorderLayout());
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(messageLabel, BorderLayout.SOUTH);
        
        this.setSize(new Dimension(500, 600));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void refreshBoard() {
        // Set blue cells blue, red cells red and empty cells white
        for (int r = BoardState.MIN; r <= BoardState.MAX; r++) {
            for (int c = BoardState.MIN; c <= BoardState.MAX; c++) {
                switch (gameState.getTheBoard().getCell(r,c)) {
                    case RED   : boardCellPanels.get(new Point(r,c)).setBackground(new Color(0.5f, 0f, 0f)); break;
                    case BLUE  : boardCellPanels.get(new Point(r,c)).setBackground(new Color(0f, 0f, 0.5f)); break;
                    case EMPTY : boardCellPanels.get(new Point(r,c)).setBackground(Color.WHITE); break;
                }
            }
        }
        // If a cell is selected, show all possible moves
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
            // And show which cell is selected
            if (gameState.getTheBoard().getCell(gameState.getSelectedPoint()).equals(Cell.RED) && gameState.getActivePlayer().getColour().equals(Cell.RED)) {
            	boardCellPanels.get(gameState.getSelectedPoint()).setBackground(Color.RED);
            }
            if (gameState.getTheBoard().getCell(gameState.getSelectedPoint()).equals(Cell.BLUE) && gameState.getActivePlayer().getColour().equals(Cell.BLUE)) {
                boardCellPanels.get(gameState.getSelectedPoint()).setBackground(Color.BLUE);
            }
        }
        // Update the message label
        // Show who's turn it is, or if the game is over
        if (gameState.gameIsOver()) {
        	if (gameState.boardHasRedCells()) {
        		messageLabel.setForeground(Color.RED);
	        	messageLabel.setText("Game Over! The RED player wins!");
        	} else if (gameState.boardHasBlueCells()) {
        		messageLabel.setForeground(Color.BLUE);
	        	messageLabel.setText("Game Over! The BLUE player wins!");
        	} else {
	        	messageLabel.setForeground(Color.BLACK);
	        	messageLabel.setText("Game Over! This game is a draw.");
	        }
        } else {
        	if (gameState.getActivePlayer().getColour().equals(Cell.RED)) {
        		messageLabel.setForeground(Color.RED);
	        	messageLabel.setText("RED player's turn.");
	    	} else {
	    	    messageLabel.setForeground(Color.BLUE);
                messageLabel.setText("BLUE player's turn.");
	    	}
        }
        this.repaint();
    }
}
