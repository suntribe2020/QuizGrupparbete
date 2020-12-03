package Client;

import Server.Database;
import Server.ServerInstruction;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class QuizClient extends JFrame implements ActionListener {

    private JTextPane textPane = new JTextPane();
    private final List<JButton> buttonList = new ArrayList<>();
    private final Color buttonBackgroundColor = new Color(186, 179, 179);
    private final Font font = new Font("Geeza Pro", Font.BOLD, 15);

    private final JButton button1 = new JButton();
    private final JButton button2 = new JButton();
    private final JButton button3 = new JButton();
    private final JButton button4 = new JButton();

    private final int portNr;
    private final String serverAddress;
    private Socket socket;
    private BufferedReader socketInput;
    private PrintWriter socketOutput;

    public QuizClient(String serverAddress, int portNr) {
        this.serverAddress = serverAddress;
        this.portNr = portNr;
        setUpGameBoard();
        setUpSocketCommunication();
    }

    private void setUpGameBoard() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 650);
        getContentPane().setBackground(new Color(193, 186, 186));
        setLayout(null);
        setResizable(false);
        setTitle("Quizkampen");

        //Styling för textpane-objekt
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(2, doc.getLength(), center, false);

        //Här syns det vilken fråga i ronden man är på.
        textPane.setBounds(0, 0, 645, 60);
        textPane.setBackground(Color.WHITE);
        textPane.setForeground(Color.BLACK);
        textPane.setFont(font);
        textPane.setEditable(false);
        textPane.setText("Waiting for other player");

        createButton(button1,"", 45, 100);
        createButton(button2,"", 350, 100);
        createButton(button3,"", 45, 350);
        createButton(button4,"", 350, 350);

        for(JButton button: buttonList){
            add(button);
        }
        add(textPane);
        setVisible(true);
    }

    public void play () throws Exception {
        try {
            System.out.println("Started client");
            String welcomeMessage = readFromServer();
            textPane.setText(welcomeMessage);

            /*
             * First message from server is always the ServerInstruction enum
             */
            while (true) {
                ServerInstruction serverInstruction = ServerInstruction.valueOf(readFromServer());
                System.out.println("Instruction from server: " + serverInstruction.name());

                switch (serverInstruction) {
                    // server requests category from first player
                    case FIRST_PLAYER_ROUND_START -> {
                        textPane.setText(readFromServer());
                        setCategoriesOnButtons();
                    }
                    // server requests second player to initiate a new round
                    case SECOND_PLAYER_ROUND_START -> {
                        textPane.setText(readFromServer());
                        button1.setText("Yes");
                        button2.setText("Ready");
                        button3.setText("Sure thing");
                        button4.setText("Let's go!");
                    }
                    // server reports first player score
                    case FIRST_PLAYER_SCORE -> {
                        textPane.setText(readFromServer());
                        setAllButtonsText("");
                    }
                    // server reports second player score and await input to start second player round,
                    // this to not overwrite the textPane displaying the score, before next round starts
                    case SECOND_PLAYER_SCORE -> {
                        textPane.setText(readFromServer());
                        setAllButtonsText("Ok");
                    }
                    // the game is over
                    case GAME_ENDED -> {

                        textPane.setText(readFromServer());
                        setAllButtonsText("");
                        System.out.println("gameover");
                        return;
                    }
                    // server sends questions
                    case QUESTION -> {
                        textPane.setText(readFromServer());
                        for(JButton button: buttonList){
                            button.setText(readFromServer());
                        }
                    }
                    case CORRECT_ANSWER -> {
                        String correctAnswer = readFromServer();
                        JButton correctButton = getButton(correctAnswer);
                        setButtonColor(correctButton, Color.GREEN);
                        Thread.sleep(1000);
                        setButtonColor(correctButton, buttonBackgroundColor);
                        writeToServer("NEXT_QUESTION");
                    }

                    case INCORRECT_ANSWER -> {
                        String incorrectAnswer = readFromServer();
                        JButton incorrectButton = getButton(incorrectAnswer);
                        setButtonColor(incorrectButton, Color.RED);
                        Thread.sleep(1000);
                        setButtonColor(incorrectButton, buttonBackgroundColor);
                        writeToServer("NEXT_QUESTION");
                    }
                    default -> System.out.println("ERROR! UNKNOWN INSTRUCTION");
                }
            }
        } finally {
            socket.close();
        }
    }

    private void setButtonColor(JButton button, Color color) {
        button.setBackground(color);
    }

    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        QuizClient client = new QuizClient(serverAddress, 54448);
        client.play();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

            if (e.getSource() == button1) {
                writeToServer(button1.getText());
            } else if (e.getSource() == button2) {
                writeToServer(button2.getText());
            }  else if (e.getSource() == button3) {
                writeToServer(button3.getText());
            }  else if (e.getSource() == button4) {
                writeToServer(button4.getText());
            }
    }

    private void setUpSocketCommunication() {
        try {
            this.socket = new Socket(this.serverAddress, this.portNr);
            generateSocketReader();
            generateSocketWriter();
        } catch (IOException e) {
            System.out.println("Error when setting up communication links to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateSocketReader() throws IOException {
        this.socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void generateSocketWriter() throws IOException {
        socketOutput = new PrintWriter(socket.getOutputStream(), true);
    }

    public void writeToServer(String message) {
        this.socketOutput.println(message);
    }

    //if message from server contains an explicit newline, it will be replaced with an actual newline.
    public String readFromServer() throws IOException {
        return this.socketInput.readLine().replace("\\n", "\n");
    }

    private void createButton(JButton but, String butText, int x, int y){
        but.setBounds(x, y, 250, 200);
        but.setFont(font);
        but.setBackground(buttonBackgroundColor);
        but.setBorder(new MatteBorder(3,3,3,3,Color.WHITE));
        but.setFocusable(false);
        but.addActionListener(this);
        but.setText(butText);
        buttonList.add(but);
    }

    private void setAllButtonsText(String message){
        for(JButton button: buttonList) {
            button.setText(message);
        }
    }

    private void setCategoriesOnButtons(){
        button1.setText(Database.GameCategory.MUSIC.toString());
        button2.setText(Database.GameCategory.FILM.toString());
        button3.setText(Database.GameCategory.GAMES.toString());
        button4.setText(Database.GameCategory.SPORT.toString());
    }

    private JButton getButton(String answer){
        for(JButton button: buttonList){
            if(button.getText().equalsIgnoreCase(answer)) return button;
        } return null;
    }
}