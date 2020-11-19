import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:13
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class QuizClient extends JFrame implements ActionListener {



    JFrame frame = new JFrame();
    JTextField textfield = new JTextField();
    JTextArea textarea = new JTextArea();
    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();

    JButton category1 = new JButton();
    JButton category2 = new JButton();
    JButton category3 = new JButton();
    JButton category4 = new JButton();


    JLabel answer_label1 = new JLabel();
    JLabel answer_label2 = new JLabel();
    JLabel answer_label3 = new JLabel();
    JLabel answer_label4 = new JLabel();
    JLabel time_label = new JLabel();
    JLabel seconds_left = new JLabel();
    JTextField number_right = new JTextField();
    int seconds = 15;
    int questionNumber = 1;
    int index;
    int total_questions = 5;
    char answer;

    private int counter = 0;
    QuestionDatabase questionDatabase = new QuestionDatabase();














    int portNr = 54448;
    String host = "127.0.0.1";

    QuizClient() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,650);
        frame.getContentPane().setBackground(new Color(193, 186, 186));
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setTitle("Quizkampen");


        //Här syns det vilken fråga i ronden man är på.
        textfield.setBounds(0,0,650,50);
        textfield.setBackground(new Color(255, 255, 255));
        textfield.setForeground(new Color(3, 3, 3));
        textfield.setFont(new Font("Geeza Pro",Font.BOLD,30));
        //textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setHorizontalAlignment(JTextField.CENTER);
        textfield.setEditable(false);
        textfield.setText("Välj Kategori");


        category1.setBounds(15,100,300,250);
        category1.setFont(new Font("Geeza Pro",Font.BOLD,35));
        category1.setBackground(new Color(186, 179, 179));
        category1.setFocusable(false);
        category1.addActionListener(this);
        category1.setText("Musik");

        category2.setBounds(315,100,300,250);
        category2.setFont(new Font("Geeza Pro",Font.BOLD,35));
        category2.setBackground(new Color(186, 179, 179));
        category2.setFocusable(false);
        category2.addActionListener(this);
        category2.setText("Spel");

        category3.setBounds(15,350,300,250);
        category3.setFont(new Font("Geeza Pro",Font.BOLD,35));
        category3.setBackground(new Color(186, 179, 179));
        category3.setFocusable(false);
        category3.addActionListener(this);
        category3.setText("Film");

        category4.setBounds(315,350,300,250);
        category4.setFont(new Font("Geeza Pro",Font.BOLD,35));
        category4.setBackground(new Color(186, 179, 179));
        category4.setFocusable(false);
        category4.addActionListener(this);
        category4.setText("Sport");



        frame.add(category1);
        frame.add(category2);
        frame.add(category3);
        frame.add(category4);
        frame.add(textfield);
        frame.setVisible(true);







        /*
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,650);
        frame.getContentPane().setBackground(new Color(193, 186, 186));
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setTitle("Quizkampen");


        //Här syns det vilken fråga i ronden man är på.
        textfield.setBounds(0,0,650,50);
        textfield.setBackground(new Color(255, 255, 255));
        textfield.setForeground(new Color(3, 3, 3));
        textfield.setFont(new Font("Geeza Pro",Font.BOLD,30));
        //textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setHorizontalAlignment(JTextField.CENTER);
        textfield.setEditable(false);
        textfield.setText("Fråga nummer: " + questionNumber);

        //Här syns frågan
        textarea.setBounds(0,50,650,50);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setBackground(new Color(255, 255, 255));
        textarea.setForeground(new Color(0, 0, 0));
        textarea.setFont(new Font("Geeza Pro",Font.BOLD,15));
        // textarea.setBorder(BorderFactory.createBevelBorder(1));
        textarea.setEditable(false);
        textarea.setText(questionDatabase.getMusicQuestions()[counter]);
        */


        button1.setBounds(0,100,100,100);
        button1.setFont(new Font("Geeza Pro",Font.BOLD,35));
        button1.setFocusable(false);
        // buttonA.addActionListener(this);
        button1.setText("1");

        button2.setBounds(0,200,100,100);
        button2.setFont(new Font("Geeza Pro",Font.BOLD,35));
        button2.setFocusable(false);
        //buttonB.addActionListener(this);
        button2.setText("2");

        button3.setBounds(0,300,100,100);
        button3.setFont(new Font("Geeza Pro",Font.BOLD,35));
        button3.setFocusable(false);
        // buttonC.addActionListener(this);
        button3.setText("3");

        button4.setBounds(0,400,100,100);
        button4.setFont(new Font("Geeza Pro",Font.BOLD,35));
        button4.setFocusable(false);
        // buttonD.addActionListener(this);
        button4.setText("4");

        answer_label1.setBounds(125,100,500,100);
        answer_label1.setBackground(new Color(50,50,50));
        answer_label1.setForeground(new Color(14, 14, 14));
        answer_label1.setFont(new Font("Geeza Pro",Font.PLAIN,35));

        answer_label2.setBounds(125,200,500,100);
        answer_label2.setBackground(new Color(50,50,50));
        answer_label2.setForeground(new Color(10, 10, 10));
        answer_label2.setFont(new Font("Geeza Pro",Font.PLAIN,35));

        answer_label3.setBounds(125,300,500,100);
        answer_label3.setBackground(new Color(50,50,50));
        answer_label3.setForeground(new Color(7, 7, 7));
        answer_label3.setFont(new Font("Geeza Pro",Font.PLAIN,35));

        answer_label4.setBounds(125,400,500,100);
        answer_label4.setBackground(new Color(50,50,50));
        answer_label4.setForeground(new Color(10, 10, 10));
        answer_label4.setFont(new Font("Geeza Pro",Font.PLAIN,35));

        seconds_left.setBounds(535,510,100,100);
        seconds_left.setBackground(new Color(193, 186, 186));
        seconds_left.setForeground(new Color(255,0,0));
        seconds_left.setFont(new Font("Impact",Font.BOLD,60));
        //seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);
        seconds_left.setHorizontalAlignment(JTextField.CENTER);
        seconds_left.setText(String.valueOf(seconds));


        time_label.setBounds(535,475,100,25);
        time_label.setBackground(new Color(50,50,50));
        time_label.setForeground(new Color(255,0,0));
        //time_label.setFont(new Font("MV Boli",Font.PLAIN,16));
        time_label.setHorizontalAlignment(JTextField.CENTER);

        number_right.setBounds(225,225,200,100);
        number_right.setBackground(new Color(25,25,25));
        number_right.setForeground(new Color(0, 0, 0));
        number_right.setFont(new Font("Impact",Font.BOLD,50));
        number_right.setBorder(BorderFactory.createBevelBorder(1));
        number_right.setHorizontalAlignment(JTextField.CENTER);
        number_right.setEditable(false);

        frame.add(time_label);
        frame.add(seconds_left);
        frame.add(answer_label1);
        frame.add(answer_label2);
        frame.add(answer_label3);
        frame.add(answer_label4);
        frame.add(textarea);
        frame.add(textfield);
        frame.setVisible(true);

        nextQuestion();






        try (
                Socket socketToServer = new Socket(host, portNr);
                PrintWriter writerOut = new PrintWriter(socketToServer.getOutputStream(), true);
                BufferedReader serverIn = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
        ) {

            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            String fromServer;
            String fromUser;

            while ((fromServer = serverIn.readLine()) != null) {
                System.out.println(fromServer);
                fromUser = userIn.readLine();
                if (fromUser != null) {
                    writerOut.println(fromUser);
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == category1 || e.getSource() == category2 ||
                e.getSource() == category3 || e.getSource() == category4) {
            if (e.getSource()== category1) {
                textfield.setText("Kategori: Musik");
            }
            else if (e.getSource() == category2) {
                textfield.setText("Kategori: Spel");
            }
            else if (e.getSource() == category3) {
                textfield.setText("Kategori: Film");
            }
            else if (e.getSource() == category4) {
                textfield.setText("Kategori: Sport");
            }



            textarea.setBounds(0,50,650,50);
            textarea.setLineWrap(true);
            textarea.setWrapStyleWord(true);
            textarea.setBackground(new Color(255, 255, 255));
            textarea.setForeground(new Color(0, 0, 0));
            textarea.setFont(new Font("Geeza Pro",Font.BOLD,15));
            // textarea.setBorder(BorderFactory.createBevelBorder(1));
            textarea.setEditable(false);
            //textarea.setText(questionDatabase.getMusicQuestions()[counter]);
            frame.add(textarea);

            category1.setBounds(0,100,100,100);
            category1.setFont(new Font("Geeza Pro",Font.BOLD,35));
            category1.setFocusable(false);
            // buttonA.addActionListener(this);
            category1.setText("1");

            category2.setBounds(0,200,100,100);
            category2.setFont(new Font("Geeza Pro",Font.BOLD,35));
            category2.setFocusable(false);
            // buttonA.addActionListener(this);
            category2.setText("2");

            category3.setBounds(0,300,100,100);
            category3.setFont(new Font("Geeza Pro",Font.BOLD,35));
            category3.setFocusable(false);
            // buttonA.addActionListener(this);
            category3.setText("3");

            category4.setBounds(0,400,100,100);
            category4.setFont(new Font("Geeza Pro",Font.BOLD,35));
            category4.setFocusable(false);
            // buttonA.addActionListener(this);
            category4.setText("4");

        }

    }

    public void nextQuestion () {
        if (index >= total_questions)
            results();
            else {
                textarea.setText(questionDatabase.getMusicQuestions()[counter]);
                answer_label1.setText(questionDatabase.getMusicAlternatives()[counter++]);
                answer_label2.setText(questionDatabase.getMusicAlternatives()[counter++]);
                answer_label3.setText(questionDatabase.getMusicAlternatives()[counter++]);
                answer_label4.setText(questionDatabase.getMusicAlternatives()[counter++]);
            }

    }

    public void results () {

    }

    public static void main(String[] args) {
        new QuizClient();
    }
}