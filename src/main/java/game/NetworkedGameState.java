package game;

public class NetworkedGameState extends GameState {

    private CommunicationLink communicationLink;
    private Player localPlayer;

    public NetworkedGameState(GameStyle gameStyle, Player localPlayer, CommunicationLink communicationLink) {
        super(gameStyle);
        this.localPlayer = localPlayer;
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

}
