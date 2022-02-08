package Common;

import javax.swing.*;

public abstract class displayPanel extends JPanel {
    private  String panelName;
    private  String spaces = "                                                                                                              ";

    public void setPanelName(String panelName){
        this.panelName = panelName;
    }
    public void setSpaces(String spaces){
        this.spaces = spaces;
    }
    public String getLabel(){
        return spaces + panelName + spaces;
    }


}
