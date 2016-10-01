package game;

public class CommunicationLink {

    private NetworkedGameState gameState;

    public CommunicationLink(NetworkedGameState gameState) {
        this.gameState = gameState;
    }

    public void sendMove(Point point) {
        // Sends the move point as a string to the remote player
    }

    public void receiveMove(Point point) {
        // Receives the move point as a string from the remote player
        gameState.doMove(point);
    }

}
