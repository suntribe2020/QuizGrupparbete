package Client;

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

    String currentState = "";

    JFrame frame = new JFrame();
    JTextField textfield = new JTextField();
    JTextArea textarea = new JTextArea();

    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();

    JLabel answer_label1 = new JLabel();
    JLabel answer_label2 = new JLabel();
    JLabel answer_label3 = new JLabel();
    JLabel answer_label4 = new JLabel();
    JLabel time_label = new JLabel();
    JLabel seconds_left = new JLabel();
    JTextField number_right = new JTextField();

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
        textfield.setBackground(new Color(255, 255, 255));
        textfield.setForeground(new Color(3, 3, 3));
        textfield.setFont(new Font("Geeza Pro", Font.BOLD, 30));
        //textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setHorizontalAlignment(JTextField.CENTER);
        textfield.setEditable(false);
        textfield.setText("Välj Kategori");


        button1.setBounds(15, 100, 300, 250);
        button1.setFont(new Font("Geeza Pro", Font.BOLD, 35));
        button1.setBackground(new Color(186, 179, 179));
        button1.setFocusable(false);
        button1.addActionListener(this);
        //button1.setText("Musik");

        button2.setBounds(315, 100, 300, 250);
        button2.setFont(new Font("Geeza Pro", Font.BOLD, 35));
        button2.setBackground(new Color(186, 179, 179));
        button2.setFocusable(false);
        button2.addActionListener(this);
        //button2.setText("Spel");

        button3.setBounds(15, 350, 300, 250);
        button3.setFont(new Font("Geeza Pro", Font.BOLD, 35));
        button3.setBackground(new Color(186, 179, 179));
        button3.setFocusable(false);
        button3.addActionListener(this);
        //button3.setText("Film");

        button4.setBounds(315, 350, 300, 250);
        button4.setFont(new Font("Geeza Pro", Font.BOLD, 35));
        button4.setBackground(new Color(186, 179, 179));
        button4.setFocusable(false);
        button4.addActionListener(this);
        //button4.setText("Sport");
        //displayCategories();

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
            if (readFromServer().equals("Welcome player1 You are now waiting for another player")){
                textfield.setText("waiting for player 2");
            }
            if (readFromServer().equals("CAT")){
                currentState = "CAT";
                displayCategories();
            }
            if (readFromServer().equals("QUE")){
                currentState = "QUE";
                textfield.setText(readFromServer());
                button1.setText(readFromServer());
                button2.setText(readFromServer());
                button3.setText(readFromServer());
                button4.setText(readFromServer());
            }

            System.out.println(readFromServer());

            System.out.println("Started client");
            String welcomeMessage = readFromServer();
            System.out.println(welcomeMessage);
            while (true) {
                String firstMessage = readFromServer();
                System.out.println(firstMessage);


                sendAnswerToServer(scanner);
                message = readFromServer();
                System.out.println("Message after first answer is:  " + message);
                sendAnswerToServer(scanner);
                message = readFromServer();
                System.out.println("Message after second answer is:  " + message);
                sendAnswerToServer(scanner);
                message = readFromServer();
                System.out.println("Message after third answer is:  " + message);
                sendAnswerToServer(scanner);
                message = readFromServer();
                System.out.println("Message after fourth answer is:  " + message);
                sendAnswerToServer(scanner);

                String lastMessage = readFromServer();
                System.out.println("This was the last message for this round:  " + lastMessage);

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
        //if (currentState.equals("CAT")){
            if (e.getSource() == button1){writeToServer(button1.getText());}
            if (e.getSource() == button2){writeToServer(button2.getText());}
            if (e.getSource() == button3){writeToServer(button3.getText());}
            if (e.getSource() == button4){writeToServer(button4.getText());}
            //}
        /*else if(currentState.equals("QUE")){
            if (e.getSource() == button1){writeToServer(button1.getText());}
            if (e.getSource() == button2){writeToServer(button2.getText());}
            if (e.getSource() == button3){writeToServer(button3.getText());}
            if (e.getSource() == button4){writeToServer(button4.getText());}
            }
        */

        /*if (e.getSource() == button1 || e.getSource() == button2 ||
                e.getSource() == button3 || e.getSource() == button4) {
            if (e.getSource() == button1) {
                textfield.setText("Kategori: Musik");
                socketOutput.println(button1.getText() + questionIndex);
                this.questionIndex += 1;
            } else if (e.getSource() == button2) {
                textfield.setText("Kategori: Spel");
                socketOutput.println(button2.getText() + questionIndex);
                this.questionIndex += 1;
            } else if (e.getSource() == button3) {
                textfield.setText("Kategori: Film");
                socketOutput.println(button3.getText() + questionIndex);
                this.questionIndex += 1;
            } else if (e.getSource() == button4) {
                textfield.setText("Kategori: Sport");
                socketOutput.println(button4.getText() + questionIndex);
                this.questionIndex += 1;
            }*/

            /*
            textarea.setBounds(0, 50, 650, 50);
            textarea.setLineWrap(true);
            textarea.setWrapStyleWord(true);
            textarea.setBackground(new Color(255, 255, 255));
            textarea.setForeground(new Color(0, 0, 0));
            textarea.setFont(new Font("Geeza Pro", Font.BOLD, 15));
            // textarea.setBorder(BorderFactory.createBevelBorder(1));
            textarea.setEditable(false);
            //textarea.setText(questionDatabase.getMusicQuestions()[counter]);
            frame.add(textarea);

            button1.setBounds(0, 100, 100, 100);
            button1.setFont(new Font("Geeza Pro", Font.BOLD, 35));
            button1.setFocusable(false);
            // buttonA.addActionListener(this);
            button1.setText("1");

            button2.setBounds(0, 200, 100, 100);
            button2.setFont(new Font("Geeza Pro", Font.BOLD, 35));
            button2.setFocusable(false);
            // buttonA.addActionListener(this);
            button2.setText("2");

            button3.setBounds(0, 300, 100, 100);
            button3.setFont(new Font("Geeza Pro", Font.BOLD, 35));
            button3.setFocusable(false);
            // buttonA.addActionListener(this);
            button3.setText("3");

            button4.setBounds(0, 400, 100, 100);
            button4.setFont(new Font("Geeza Pro", Font.BOLD, 35));
            button4.setFocusable(false);
            // buttonA.addActionListener(this);
            button4.setText("4");

            */



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

    public void displayCategories(){
        button1.setText("Musik");
        button2.setText("Spel");
        button3.setText("Film");
        button4.setText("Sport");
    }
}