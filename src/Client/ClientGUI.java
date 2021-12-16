package Client;


//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import javafx.stage.WindowEvent;

//public class ClientGUI extends Application
//{
//	Client client = null;
//
//    int WIDTH = 256;
//    int HEIGHT = 256;
//
//    private Scene mainScene;
//
//    // -- Main container
//    private BorderPane pane;
//
//    // -- Controls container
//    private ControlBoxInner controlBox;
//
//    @Override
//    public void start(Stage mainStage)
//    {
//    	System.out.println("start");
//    	// -- Application title
//        mainStage.setTitle("JavaFX Graphics Application");
//        mainStage.setWidth(WIDTH);
//
//    	// -- construct the controls
//    	controlBox = new ControlBoxInner();
//
//        // -- create the primary window structure
//        pane = new BorderPane();
//
//    	// -- add the graphics canvas and the control box to the Border Pane
//        pane.setLeft(controlBox);
//
//        mainScene = new Scene(pane);
//        mainStage.setScene(mainScene);
//
//        // -- clean up on close
//        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent e) {
//            	// -- if client is connected to the server, disconnect
//            	//    before shutting down
//            	if (client != null) {
//            		client.disconnect();
//            		client = null;
//            	}
//            	Platform.exit();
//            	System.exit(0);
//            }
//          });
//
//        // -- display the application window
//        mainStage.show();
//
//        // -- set keyboard focus to the pane
//        pane.requestFocus();
//
//    }
//
//
//    // -- launch the application
//    public void launchApp(String[] args)
//    {
//    	System.out.println("launch");
//        launch(args);
//    }
//
//
//    // -- Inner class for Controls
//    public class ControlBoxInner extends VBox {
//
//        private Button disconnectbutton = new Button("Disconnect");
//        private Button connectbutton = new Button("Client.Connect");
//        private Button messagebutton = new Button("Send Message");
//
//        private TextField messagefield = new TextField();
//        private TextArea responsearea = new TextArea();
//
//        public ControlBoxInner()
//        {
//        	super();
//
//        	// -- create button handlers
//            messagebutton.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//
//            		// -- send message to server and receive reply.
//            		String commandString;
//            		String replyString;
//
//           			// -- send a String to the server and wait for the response
//           			commandString = messagefield.getText();
//           			System.out.println("CLIENT send:  " + commandString);
//           			replyString = client.getNetworkAccess().sendString(commandString, true);
//           			responsearea.appendText("CLIENT receive: " + replyString + "\n");
//                }
//            });
//
//            connectbutton.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                	// -- do not allow a client to connect multiple times
//                	if (client == null) {
//	            		String host = "127.0.0.1";// "192.168.1.8";//"127.0.0.1";
//	            		int port = 8000;
//	            		// -- instantiate a Client object
//	            		//    the constructor will attempt to connect to the server
//	            		client = new Client(host, port);
//                	}
//                }
//            });
//
//            disconnectbutton.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//            		// -- disconnect from the server
//            		client.disconnect();
//            		client = null;
//                	pane.requestFocus();
//                }
//            });
//
//            // -- build a simple GUI
//            int maxwidth = 100;
//            messagefield.setMaxWidth(maxwidth);
//            responsearea.setMaxWidth(maxwidth);
//            responsearea.setMaxHeight(200);
//    		this.getChildren().add(connectbutton);
//            this.getChildren().add(disconnectbutton);
//    		this.getChildren().add(messagebutton);
//            this.getChildren().add(new Label("Message"));
//    		this.getChildren().add(messagefield);
//            this.getChildren().add(new Label("Response"));
//    		this.getChildren().add(responsearea);
//
//        }
//     }
//}
