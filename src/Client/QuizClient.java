package Client;

import Server.Database;
import Server.ServerInstruction;

import javax.swing.*;
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

    JFrame frame = new JFrame();
    JTextField textField = new JTextField();
    JTextArea textarea = new JTextArea();
    private final List<JButton> buttonList = new ArrayList<>();
    private final Color buttonBackgroundColor = new Color(186, 179, 179);

    private JButton button1 = new JButton();
    private JButton button2 = new JButton();
    private JButton button3 = new JButton();
    private JButton button4 = new JButton();

    private int portNr;
    private String serverAddress;
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650);
        frame.getContentPane().setBackground(new Color(193, 186, 186));
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setTitle("Quizkampen");


        //Här syns det vilken fråga i ronden man är på.
        textfield.setBounds(0, 0, 650, 50);
        textfield.setBackground(new Color(255, 255, 255));
        textfield.setForeground(new Color(3, 3, 3));
        textfield.setFont(new Font("Geeza Pro", Font.BOLD, 15));
        //textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setHorizontalAlignment(JTextField.CENTER);
        textfield.setEditable(false);
        textfield.setText("Waiting for other player");

        createButton(button1,"", 15, 100);
        createButton(button2,"", 315, 100);
        createButton(button3,"", 15, 350);
        createButton(button4,"", 315, 350);

        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(textfield);
        frame.setVisible(true);
    }

    public void play () throws Exception {
        try {
            System.out.println("Started client");
            String welcomeMessage = readFromServer();
            textfield.setText(welcomeMessage);

            /**
             * First message from server is always the ServerInstruction enum
             */
            while (true) {
                ServerInstruction serverInstruction = ServerInstruction.valueOf(readFromServer());
                System.out.println("Instruction from server: " + serverInstruction.name());

                switch (serverInstruction) {
                    // server requests category from first player
                    case FIRST_PLAYER_ROUND_START -> {
                        textfield.setText(readFromServer());
                        setCategoriesOnButtons();
                    }
                    // server requests second player to initiate a new round
                    case SECOND_PLAYER_ROUND_START -> {
                        textfield.setText(readFromServer());
                        button1.setText("yes");
                        button2.setText("ready");
                        button3.setText("sure thing");
                        button4.setText("no");
                    }
                    // server reports first player score
                    case FIRST_PLAYER_SCORE -> {
                        textfield.setText(readFromServer());
                        setAllBlankButtons();
                    }
                    // server reports second player score and await input to start second player round,
                    // this to not overwrite the textfield displaying the score, before next round starts
                    case SECOND_PLAYER_SCORE -> {
                        textfield.setText(readFromServer());
                        button1.setText("ok");
                        button2.setText("ok");
                        button3.setText("ok");
                        button4.setText("ok");
                    }
                    // the game is over
                    case GAME_ENDED -> {
                        textfield.setText(readFromServer());
                        setAllBlankButtons();
                        System.out.println("gameover");
                        return;
                    }
                    // server sends questions
                    case QUESTION -> {
                        textfield.setText(readFromServer());
                        button1.setText(readFromServer());
                        button2.setText(readFromServer());
                        button3.setText(readFromServer());
                        button4.setText(readFromServer());
                    }
                    case CORRECT_ANSWER -> {
                        String correctAnswer = readFromServer();
                        if (button1.getText().equals(correctAnswer)) {
                            button1.setBackground(Color.GREEN);
                        } else if (button2.getText().equals(correctAnswer)) {
                            button2.setBackground(Color.GREEN);
                        } else if (button3.getText().equals(correctAnswer)) {
                            button3.setBackground(Color.GREEN);
                        } else if (button4.getText().equals(correctAnswer)) {
                            button4.setBackground(Color.GREEN);
                        }
                        Thread.sleep(1000);
                        button1.setBackground(new Color(186, 179, 179));
                        button2.setBackground(new Color(186, 179, 179));
                        button3.setBackground(new Color(186, 179, 179));
                        button4.setBackground(new Color(186, 179, 179));
                        writeToServer("NEXT_QUESTION");
                    }

                    case INCORRECT_ANSWER -> {
                        String incorrectAnswer = readFromServer();
                        if (button1.getText().equals(incorrectAnswer)) {
                            button1.setBackground(Color.RED);
                        } else if (button2.getText().equals(incorrectAnswer)) {
                            button2.setBackground(Color.RED);
                        } else if (button3.getText().equals(incorrectAnswer)) {
                            button3.setBackground(Color.RED);
                        } else if (button4.getText().equals(incorrectAnswer)) {
                            button4.setBackground(Color.RED);
                        }
                        Thread.sleep(1000);
                        button1.setBackground(new Color(186, 179, 179));
                        button2.setBackground(new Color(186, 179, 179));
                        button3.setBackground(new Color(186, 179, 179));
                        button4.setBackground(new Color(186, 179, 179));
                        writeToServer("NEXT_QUESTION");

                    }
                    default -> System.out.println("ERROR! UNKNOWN INSTRUCTION");
                }
            }
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        QuizClient client = new QuizClient(serverAddress, 54448);
        client.play();
    }

    private void sendAnswerToServer(Scanner scanner) {
        String answer = scanner.nextLine();
        writeToServer(answer);
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
            this.socket = new Socket(this.serverAdress, this.portNr);
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

    public String readFromServer() throws IOException {
        return this.socketInput.readLine();
    }

    private void createButton(JButton but, String butText, int x, int y){
        Color backgroundColor = new Color(186, 179, 179);
        but.setBounds(x, y, 300, 250);
        but.setFont(new Font("Geeza Pro", Font.BOLD, 15));
        but.setBackground(backgroundColor);
        but.setFocusable(false);
        but.addActionListener(this);
        but.setText(butText);
        buttonList.add(but);
    }

    private void setAllBlankButtons(){
        button1.setText("");
        button2.setText("");
        button3.setText("");
        button4.setText("");
    }
    private void setCategoriesOnButtons(){
        button1.setText(Database.GameCategory.MUSIC.toString());
        button2.setText(Database.GameCategory.FILM.toString());
        button3.setText(Database.GameCategory.GAMES.toString());
        button4.setText(Database.GameCategory.SPORT.toString());
    }
}