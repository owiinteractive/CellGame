package game;

public class NetworkedGameState extends GameState {

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
            // Move must have been a success, so send the info to remote
            if (!getActivePlayer().getColour().equals(localPlayer.getColour())) {
                communicationLink.sendMove(point);
            }
        }
    }
    
    public void doCommunicationSelect(Point point) {
        super.doSelect(point);
    }

    public void doCommunicationMove(Point point) {
        super.doMove(point);
    }
    
    public boolean isLocalPlayerTurn() {
        return localPlayer.equals(getActivePlayer());
    }

}
