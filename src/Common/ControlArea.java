package Common;

import javax.swing.*;
import java.awt.*;

public class ControlArea extends JPanel {
    private JButton left;
    private JButton right;
    private JButton center;
    public ControlArea(){
        setLayout(new BorderLayout(5, 10));
    }
    public void setLeft(JButton newLeft){
        if(this.left != null) {
            this.remove(left);
        }
        this.left = newLeft;
        this.add(newLeft, BorderLayout.WEST);
        this.repaint();
    }
    public void setRight(JButton newRight){
        if(this.right != null){
            this.remove(right);
        }
        this.right = newRight;
        this.add(newRight,BorderLayout.EAST);
        this.repaint();
    }
    public void setCenter(JButton newCenter){
        if(this.center != null){
            this.remove(center);
        }
        this.center = newCenter;
        this.add(newCenter,BorderLayout.CENTER);
        this.repaint();
    }
    protected void delButton(int i){
        if(i == 0){
            this.remove(left);
        }
        else if(i == 1){
            this.remove(center);
        }
        else{
            this.remove(right);
        }
    }

    public void setCenter(JPanel center) {
        this.add(center, BorderLayout.CENTER);
    }
}
