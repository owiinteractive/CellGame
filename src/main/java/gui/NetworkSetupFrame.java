package gui;

import game.Cell;
import game.CommunicationLink;
import game.GameStyle;
import game.NetworkedGameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NetworkSetupFrame extends JFrame {

    public static final int PORTNUM = 9933;

    private JComboBox<String> comboBox;
    private JLabel outputLabel;
    private JLabel hostLabel;
    private JTextField hostName;
    private JButton startButton;

    public NetworkSetupFrame() {
        super();
        initGui();
    }

    public void initGui() {
        this.setLayout(new GridLayout(5,1));

        comboBox = new JComboBox<String>();
        comboBox.addItem("Host");
        comboBox.addItem("Join");

        hostLabel = new JLabel("Host Name:");
        hostName = new JTextField();
        
        hostLabel.setVisible(false);
        hostName.setVisible(false);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem().equals("Join")) {
                    hostLabel.setVisible(true);
                    hostName.setVisible(true);
                } else {
                    hostLabel.setVisible(false);
                    hostName.setVisible(false);
                }
            }
        });

        outputLabel = new JLabel("");

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem().equals("Host")) {
                    outputLabel.setText("Hosting on port " + PORTNUM);
                    // The host gets to play red.
                    NetworkedGameState gameState = new NetworkedGameState(GameStyle.NETWORKED, Cell.RED);
                    try {
                        CommunicationLink communicationLink = new CommunicationLink(comboBox.getSelectedItem().toString(), hostName.getText(), PORTNUM);
                        communicationLink.setNetworkedGameState(gameState);
                        gameState.setCommunicationLink(communicationLink);
                        communicationLink.start();
                        GameFrame gameFrame = new GameFrame();
                        gameFrame.setGameState(gameState);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    outputLabel.setText("Connecting to " + hostName.getText() + " " + PORTNUM);
                    // The client gets to play blue.
                    NetworkedGameState gameState = new NetworkedGameState(GameStyle.NETWORKED, Cell.BLUE);
                    try {
                        CommunicationLink communicationLink = new CommunicationLink(comboBox.getSelectedItem().toString(), hostName.getText(), PORTNUM);
                        communicationLink.setNetworkedGameState(gameState);
                        gameState.setCommunicationLink(communicationLink);
                        communicationLink.start();
                        GameFrame gameFrame = new GameFrame();
                        gameFrame.setGameState(gameState);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.add(comboBox);
        this.add(hostLabel);
        this.add(hostName);
        this.add(startButton);
        this.add(outputLabel);

        this.setSize(new Dimension(500, 600));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
