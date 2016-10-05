package game;

import gui.GameFrame;

public class NetworkedGameState extends GameState {

    private GameFrame gameFrame;
    private CommunicationLink communicationLink;
    private Player localPlayer;

    public NetworkedGameState(GameStyle gameStyle, Cell localColour) {
        super(gameStyle);
        if (localColour.equals(Cell.RED)) {
            localPlayer = redPlayer;
        } else {
            localPlayer = bluePlayer;
        }
    }
    
    public void setCommunicationLink(CommunicationLink communicationLink) {
        this.communicationLink = communicationLink;
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
            // Save the selected point in case the move is successful
            Point currentSelection = new Point(getSelectedPoint().toString());
            super.doMove(point);
            // Move must have been a success, so send the info to remote
            if (!getActivePlayer().getColour().equals(localPlayer.getColour())) {
                communicationLink.sendSelectionAndMove(currentSelection, point);
            }
        }
    }

    public void doCommunicationSelect(Point point) {
        super.doSelect(point);
        gameFrame.refreshBoard();
        try {
            Thread.sleep(500L);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doCommunicationMove(Point point) {
        super.doMove(point);
        gameFrame.refreshBoard();
    }
    
    public boolean isLocalPlayerTurn() {
        return localPlayer.equals(getActivePlayer());
    }

}
