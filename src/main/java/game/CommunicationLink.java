package game;

import gui.GameFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationLink extends Thread {

    private NetworkedGameState gameState;
    private String role;
    private String hostName;
    private int portNum;
    private Point selectionToSend;
    private Point moveToSend;

    private DataInputStream in;
    private DataOutputStream out;

    public CommunicationLink(String role, String hostName, int portNum) throws IOException {
        this.role = role;
        this.hostName = hostName;
        this.portNum = portNum;
    }
    
    public void setNetworkedGameState(NetworkedGameState networkedGameState) {
        this.gameState = networkedGameState;
    }
    
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = null;
            Socket socket = null;
            
            if (role.equals("Host")) {
                serverSocket = new ServerSocket(portNum);
                socket = serverSocket.accept();
            } else {
                socket = new Socket(hostName, portNum);
            }

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            boolean sendingMode = gameState.isLocalPlayerTurn();

            while (!gameState.gameIsOver())  {
                if (sendingMode) {
                    if (moveToSend == null) {
                        sleep(1000L);
                    } else {
                        String pointString = selectionToSend.toString() + " " + moveToSend.toString();
                        out.writeUTF(pointString);
                        System.out.println("Write " + pointString);
                        selectionToSend = null;
                        moveToSend = null;
                        sendingMode = false;
                    }
                } else {
                    String pointString = in.readUTF();
                    System.out.println("Read " + pointString);
                    String[] points = pointString.split(" ");
                    Point selectionPoint = new Point(points[0]);
                    Point movePoint = new Point(points[1]);
                    receiveSelectionAndMove(selectionPoint, movePoint);
                    sendingMode = true;
                }
            }

            // Send the final move if necessary
            if (sendingMode && moveToSend != null) {
                String pointString = selectionToSend.toString() + " " + moveToSend.toString();
                out.writeUTF(pointString);
                System.out.println("Write " + pointString);
                selectionToSend = null;
                moveToSend = null;
            }
            
            in.close();
            out.close();
            socket.close();
            if (serverSocket != null) {
                serverSocket.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendSelectionAndMove(Point selectionPoint, Point movePoint) {
        // Sends the move point as a string to the remote player
        selectionToSend = selectionPoint;
        moveToSend = movePoint;
    }

    public void receiveSelectionAndMove(Point selectionPoint, Point movePoint) {
        // Receives the move point as a string from the remote player
        gameState.doCommunicationSelect(selectionPoint);
        gameState.doCommunicationMove(movePoint);
    }

}
