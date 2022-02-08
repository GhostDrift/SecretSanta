package Common;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Label extends JPanel {
    private JLabel title;
    private String text;


    Label(){
        setLayout(new FlowLayout(1, 10,10));
        Border labelBorder = BorderFactory.createLineBorder(Color.black);
        this.text = "";
        title = new JLabel(text);
        title.setBorder(labelBorder);
        title.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        this.add(title);
    }

    Label(String text){
        super();
        this.setText(text);
    }
    protected void setText(String newText){
        this.title.setText(newText);
        System.out.println("Label new text: " + newText);
    }
    protected String getText(){
        return this.text;
    }
}
