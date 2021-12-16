package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class InteractiveGUI extends JFrame {
    private PasswordChangeGUI x;
    private boolean Visiblity;
    InteractiveGUI() {

        setTitle("Interactive Screen");

        // -- size of the frame: width, height
        setSize(600, 600);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(5, 5));
        FieldPanel con = new FieldPanel();
        this.add(con, BorderLayout.CENTER);


        BottomPanel low = new BottomPanel();
        this.add(low, BorderLayout.SOUTH);
        this.setVisible(true);

    }
    private void SetVis(){
        setVisible(Visiblity);
    }

    public class FieldPanel extends JPanel {

        private JTextArea Query;
        private JTextField SubQ;

        FieldPanel(){
            Query = new JTextArea(25,25);
            SubQ= new JTextField("",25);
            this.add(SubQ);
            this.add(Query);

        }



    }

    public class BottomPanel extends JPanel {
        //private JButton ;
        private JButton Submit;
        private JButton ChangeP;
        private JButton Close;



        public BottomPanel() {


            Close = new JButton("Close");
            ChangeP= new JButton("Change Password");
            Submit = new JButton("Submit");
            //this.add(Connect);
            this.add(Submit);
            this.add(ChangeP);
            //this.add(Connect);
            this.add(Close);

            PrepareButtons();
        }

        public void PrepareButtons() {
            //Action Handlers here

            ChangeP.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Visiblity = false;
                    SetVis();
                    x = new PasswordChangeGUI();
                }

            });

        }
    }


    public static void main(String[] args) {
        new InteractiveGUI();
    }
}