package Client;

import Common.NetworkAccess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateAccountGUI extends JFrame {

    private final int WIDTH = 200;
    private final int HEIGHT = 400;
    private boolean Visibility;
    FieldPanel x;
    BottomPanel y;

    public CreateAccountGUI() {
        setTitle("Create Account");

        // -- size of the frame: width, height
        setSize(WIDTH, HEIGHT);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(5, 5));
         x = new FieldPanel();
        this.add(x, BorderLayout.CENTER);


        y= new BottomPanel();
        this.add(y, BorderLayout.SOUTH);
        setVisible(true);
        Visibility = true;

    }
    private void SetVis(){
        setVisible(Visibility);
    }

    public class FieldPanel extends JPanel {

        //private JButton loadButton;
        private final JLabel Us;
        private final JTextField User;
        private final JLabel pas;
        private final JTextField Pass;
        private final JLabel em;
        private final JTextField email;
        private final JLabel repas;
        private final JTextField rePass;

        public FieldPanel() {
            setLayout(new FlowLayout(20, 20, 10));


            Us = new JLabel("Username");
            User = new JTextField("", 10);

            em = new JLabel("Email");
            email = new JTextField("", 10);


            pas = new JLabel("Password");
            Pass = new JTextField("", 10);
            repas = new JLabel("Re-enter Password");
            rePass = new JTextField("", 10);


            this.add(Us);
            this.add(User);
            this.add(em);
            this.add(email);
            this.add(pas);
            this.add(Pass);
            this.add(repas);
            this.add(rePass);

        }


    }

    public class BottomPanel extends JPanel {
        private final JButton Close;
        private final JButton Done;
        private LoginGUI L;


        public BottomPanel() {


            Close = new JButton("Exit");
            Done = new JButton("Create");

            this.add(Close);
            this.add(Done);

            PrepareButtons();
        }

        public void PrepareButtons() {

            // Close Button Action
            Close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    Visibility = false;
                   SetVis();
                    L = new LoginGUI();

                }
            });

            Done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    String Email = x.email.getText();
                    String User = x.User.getText();
                    String Password = x.Pass.getText();
                    String  RePassword = x.rePass.getText();

                    String validEmailFormat = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Pattern emailpattern = Pattern.compile(validEmailFormat);


                    Matcher matcher = emailpattern.matcher(Email); // Change to be pulled from GUI
                    if (matcher.find()) {
                        if (Password.equals(RePassword)) { // Change to be pulled from GUI

                        }
                        else
                        {
                            System.out.println("Passwords do not match");
                        }
                    }
                    else
                    {
                        System.out.println("Invalid email address");
                    }
                }
            });

        }
    }

    public static void main(String[] args) {
        new CreateAccountGUI();
    }
}


