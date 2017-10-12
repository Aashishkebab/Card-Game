/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import static Card_Game.Project3_Bharadwaj.human;
import static Card_Game.Project3_Bharadwaj.truncate;

/**
 *
 * @author Aashish Bharadwaj
 */
public abstract class Interface{ //Where the GUI is stored
    
    static Scene mainScene; //The scene
        static Background background = new Background(new BackgroundImage(
                new Image("file:images/background.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, 
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)); //The background image for the window
        
        static HBox entireWindow = new HBox();  //The entire window
            
            static VBox leftCushion = new VBox();   //Padding on the left for elements
                
            static VBox wholeWindow;    //The rest of the window AFTER the padding
                static CheckBox thinking = new CheckBox("Turn off messages (speeds up gameplay at the expense of user friendliness)");    //Checkbox to turn off the "thinking..." message
                static HBox topPadding = new HBox(11);  //Space from the top of the frame
                static HBox computerUI = new HBox(23);  //All the computer's elements
                    static VBox leftPadding = new VBox(11); //Space from the left of the frame
                    static Label toast = new Label();   //Message that tells user what the computer has done
                        static String toastMessage = "";    //What to display in the toast label
                        
                static HBox cardPiles = new HBox(23);   //Container that contains the drawable decks
                    static VBox pileCushionLeft = new VBox(17);   //Padding from the left for the buttons
                    
                        static ToggleGroup difficulty = new ToggleGroup();  //Group of difficulty buttons
                        
                            /*Difficulty levels*/
                            static RadioButton easy = new RadioButton("Easy");
                            static RadioButton medium = new RadioButton("Medium");
                            static RadioButton hard = new RadioButton("Hard");
                            static RadioButton insane = new RadioButton("Insane (experimental, may break)");
                        
                    static VBox labelCushionLeft = new VBox();  //Padding from the left for the labels underneath the buttons
                    
                    /*Labels that indicate what the stacks above them are*/
                    static Label newPileName = new Label("New cards");
                    static Label oldPileName = new Label("Discard pile");

                static HBox userSeparation = new HBox(51);  //Separation between user buttons and the piles
                
                    /*Buttons that link to the card decks*/
                    static Button newPile = new Button();
                    static Button discardPile = new Button();
                    
                static HBox userUI = new HBox(13);  //Container for all user UI elements
                    static Button[] userButtons =
                    {new Button(), new Button(), new Button(), new Button(), new Button()};   //Array of buttons containing the user's cards
                    
    
    /**
     *  Contains all GUI elements, is abstract
     */
    public Interface(){
        
    }
    
    /**
     *  Creates basic GUI elements
     */
    public static void createUI(){
        Deck.updateDiscards();
        
        entireWindow.getChildren().add(leftCushion);
        
        wholeWindow = new VBox(11);
        entireWindow.getChildren().add(wholeWindow);
            wholeWindow.getChildren().add(thinking);
            wholeWindow.getChildren().add(topPadding);
            wholeWindow.getChildren().add(computerUI);
            wholeWindow.getChildren().add(cardPiles);
            wholeWindow.getChildren().add(userSeparation);
            wholeWindow.getChildren().add(userUI);
            
            entireWindow.setBackground(background);
        mainScene = new Scene(entireWindow, 674, 621);  //Sets what container the scene represents
        
    }
    
    /**
     *  Creates UI elements for the computer
     */
    public static void createComputerUI(){
        computerUI.getChildren().add(leftPadding);
        computerUI.getChildren().add(toast);
        toast.setText("Computer Player");
    }
    
    /**
     *  Creates UI elements for the User
     */
    public static void createUserUI(){
        //human.setUserImages();
        Thread setUserImages = new Thread(new ForkUI("setUserImages"));
        Platform.runLater(setUserImages);   //Displays all images of cards handed out to the User
        
        for(int i = 0; i < userButtons.length; i++){    //Adds the User Buttons array to the container
            userUI.getChildren().add(userButtons[i]);
        }
    }
    
    /**
     *  Creates and messes with non Computer and User elements in the GUi
     */
    public static void createOtherElements(){
        cardPiles.getChildren().add(pileCushionLeft);
        cardPiles.getChildren().add(newPile);
            Card.setBlankImage(newPile);
        
        cardPiles.getChildren().add(discardPile);
        
        userSeparation.getChildren().add(labelCushionLeft);
        userSeparation.getChildren().add(newPileName);
        userSeparation.getChildren().add(oldPileName);
        
        pileCushionLeft.getChildren().add(easy);
        pileCushionLeft.getChildren().add(medium);
        pileCushionLeft.getChildren().add(hard);
        pileCushionLeft.getChildren().add(insane);
        
        easy.setToggleGroup(difficulty);
        medium.setToggleGroup(difficulty);
        hard.setToggleGroup(difficulty);
        insane.setToggleGroup(difficulty);
        
        difficulty.selectToggle(hard);
    }
    
    /**
     *  Resizes all JavaFX elements to make sense
     */
    public static void setSizing(){
        for(int i = 0; i < userButtons.length; i++){
            userButtons[i].setMinSize(111, 131);
            userButtons[i].setMaxSize(111, 131);
        }
        
        topPadding.setMinHeight(17);
        leftPadding.setMinWidth(167);
        
        pileCushionLeft.setMinWidth(157);
        pileCushionLeft.setMinWidth(157);
        
        labelCushionLeft.setMinWidth(193);
        oldPileName.setFont(new Font("", 17));
        newPileName.setFont(new Font("", 17));
        
        toast.setFont(new Font("", 33));
        
        newPile.setMinSize(111, 131);
        newPile.setMaxSize(111, 131);
        
        discardPile.setMinSize(111, 131);
        discardPile.setMaxSize(111, 131);
        
        userSeparation.setMinHeight(99);
        
        computerUI.setMinHeight(111);
        
        leftCushion.setMinWidth(33);
    }
    
    /**
     *  Sets what the buttons on the screen do
     */
    public static void setButtonActions(){
        Thread setUserButtonActions = new Thread(new ForkUI("setUserButtonActions"));
        setUserButtonActions.start();   //Separate thread to set actions of user's buttons
        
        Thread setPileButtonActions = new Thread(new ForkUI("setPileButtonActions"));
        setPileButtonActions.start();   //Separate thread to set actions of decks
        
        Thread setOtherButtonActions = new Thread(new ForkUI("setOtherButtonActions"));
        setOtherButtonActions.start();
    }
    
    /**
     *  Sets what the user's buttons do when clicked
     */
    public static void setUserButtonActions(){
        for(int i = 0; i < userButtons.length; i++){    //Loop through all buttons
            final int u = i;    //Because lambdas are stupid and can only take finals, set final variable 'u' to changing value 'i'
            userButtons[u].setOnAction(userButtonAction ->{
                if(!Computer.makeMove.isAlive()){   //If the computer is not moving
                    human.discardCard(u);   //Discard the card that is clicked on
                }else{  //If user tries to move while computer is still making its move
                    showAlertWait("Illegal move", "It's not your turn!", "Please wait for your turn to move.", "WARNING");
                }
            });
        }
    }

    /**
     *  Sets the click events of the decks
     */
    public static void setPileButtonActions(){
        newPile.setOnAction(drawNewCard ->{ //If fresh cards is clicked on
            if(!Computer.makeMove.isAlive()){
                human.drawNewCard();
            }else{  //If not user's turn
                showAlertWait("Illegal move", "It's not your turn!", "Please wait for your turn to move.", "WARNING");
            }
        });
        discardPile.setOnAction(drawOldCard ->{ //If discarded cards are clicked on
            if(!Computer.makeMove.isAlive()){
                human.drawOldCard();
            }else{  //If not user's turn
                showAlertWait("Illegal move", "It's not your turn!", "Please wait for your turn to move.", "WARNING");
            }
        });
    }
    
    /**
     *  Sets the actions of any other buttons or other clickable elements
     */
    public static void setOtherButtonActions(){
        easy.setOnAction(makeEasy -> {
            Computer.difficulty = 3;
            Computer.isInsane = false;
        });
        medium.setOnAction(makeOkay -> {
            Computer.difficulty = 2;
            Computer.isInsane = false;
        });
        hard.setOnAction(makeHard -> {
            Computer.difficulty = 1;
            Computer.isInsane = false;
        });
        insane.setOnAction(makeInsane -> {
            Computer.difficulty = 1;
            Computer.isInsane = true;
        });
        
        thinking.setOnAction(changeThinking -> {
            if(Computer.thinking == true){
                Computer.thinking = false;
            }else if(Computer.thinking == false){
                Computer.thinking = true;
            }
        });
    }
    
    /**
     *  Display a message on the screen as a Label
     * @param message   The message to be displayed
     */
    public static void toast(String message){
        if(message.equals("thinking...")){  //Center the word "thinking...", and make it big
            leftPadding.setMinWidth(200);
            toast.setFont(new Font("", 37));
        }else{  //Center the long sentences, and use smaller font
            leftPadding.setMinWidth(55);
            toast.setFont(new Font("", 22));
            
//            Toolkit.getDefaultToolkit().beep(); //Beep to alert the user of message
        }
        
	toast.setText(" " + truncate(message, 333));    //Display toasted message
        toastMessage = "";  //Reset what the message is if using the Interface class' built in message string
}

    /**
     *  Show alert box, but continue running code in the background
     * @param title The window title
     * @param header    The heading inside the dialog box
     * @param content   The main content of the box, in a smaller font
     * @param alertType Whether it's an INFORMATION or WARNING style dialog
     */
    public static void showAlert(String title, String header, String content, String alertType){
        alert(title, header, content, alertType, false);
    }

    /**
     *  Show alert box, and pause running code on the thread
     * @param title The window title
     * @param header    The heading inside the dialog box
     * @param content   The main content of the box, in a smaller font
     * @param alertType Whether it's an INFORMATION or WARNING style dialog
     */
    public static void showAlertWait(String title, String header, String content, String alertType){
	alert(title, header, content, alertType, true);
    }
    
    /**
     *  Displays alert dialog box
     * @param title
     * @param header
     * @param content
     * @param alertType
     * @param wait  Whether to pause code execution on this thread
     */
    public static void alert(String title, String header, String content, String alertType, boolean wait){
        Alert alert;
        switch(alertType){
            case "INFORMATION":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case "WARNING":
                alert = new Alert(Alert.AlertType.WARNING);
                break;
            default:
                return;
        }
	
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
        
	Toolkit.getDefaultToolkit().beep();
	
	alert.setResizable(false);
        
        if(wait == true){
            alert.showAndWait();
        }else{
            alert.show();
        }
    }

    /**
     *  Prompt user for text in dialog box
     * @param title Title of window
     * @param header    Header inside dialog
     * @param content   Content information, displayed in smaller font
     * @param filler    What to fill the textbox with by default
     * @return  Returns the result
     */
    public static String promptForText(String title, String header, String content, String filler){
	TextInputDialog dialog = new TextInputDialog(filler);
	dialog.setTitle(title);
	dialog.setHeaderText(header);
	dialog.setContentText(content);
	//dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
	
	Optional<String> result = dialog.showAndWait();
	if (result.isPresent()){
            return result.get();
	}else{
            return null;
	}
    }
    
    /**
     *  Displays dialog with custom buttons that have custom actions
     * @param title Window title
     * @param header    Header within dialog
     * @param content   Smaller text to give details
     * @param buttons   ArrayList of buttons to be displayed, in ButtonType format
     * @return  Returns index of button clicked
     */
    public static int askConfirmation(String title, String header, String content, ArrayList<ButtonType> buttons){
        Toolkit.getDefaultToolkit().beep();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(buttons);
        Optional<ButtonType> result = alert.showAndWait();
        
        return buttons.indexOf(result.get());
    }
    
}
