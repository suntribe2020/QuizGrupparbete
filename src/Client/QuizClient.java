package Client;

import Server.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class QuizClient extends JFrame implements ActionListener {

    private static final Font GEEZA_PRO = new Font("Geeza Pro", Font.BOLD, 15);
    JTextField textfield = new JTextField();
    JTextArea textarea = new JTextArea();

    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();

    /*JLabel answer_label1 = new JLabel();
    JLabel answer_label2 = new JLabel();
    JLabel answer_label3 = new JLabel();
    JLabel answer_label4 = new JLabel();
    JLabel time_label = new JLabel();
    JLabel seconds_left = new JLabel();
    JTextField number_right = new JTextField();
    */

    private int counter = 0;
    int portNr;
    String serverAdress;
    private Socket socket;
    private BufferedReader socketInput;
    private PrintWriter socketOutput;
    private int questionIndex = 0;

    public QuizClient(String serverAdress, int portNr) {
        this.serverAdress = serverAdress;
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
        textfield.setBackground(Color.WHITE);
        textfield.setForeground(Color.BLACK);
        textfield.setFont(GEEZA_PRO);
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

        Scanner scanner = new Scanner(System.in);
        String message;
        try {
            System.out.println("Started client");
            String welcomeMessage = readFromServer();
            String currentMessage;
            textfield.setText(welcomeMessage);
            while (true) {
                String firstMessage = readFromServer();
                System.out.println(firstMessage);
                textfield.setText(firstMessage);

                //returns category if prompted for category
                if (firstMessage.contains("choose a category")){
                    setCategoriesOnButtons();
                } else if(firstMessage.startsWith("Chosen")){
                    button1.setText("yes");
                    button2.setText("ready");
                    button3.setText("sure thing");
                    button4.setText("no");
                }

                //checks if game is over
                if (firstMessage.startsWith("The game has ended")){
                    textfield.setText(firstMessage);
                    System.out.println("gameover");
                    return;
                }

                //1 round of questions
                for(int i = 0; i<3; i++) {
                    textfield.setText(readFromServer());
                    if (textfield.getText().startsWith("Score this round")){
                        setAllBlankButtons();
                        break;
                    }
                    button1.setText(readFromServer());
                    button2.setText(readFromServer());
                    button3.setText(readFromServer());
                    button4.setText(readFromServer());
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
        but.setFont(GEEZA_PRO);
        but.setBackground(backgroundColor);
        but.setFocusable(false);
        but.addActionListener(this);
        but.setText(butText);
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