/*
 * Aashish Bharadwaj
 * Dr. Cheatham
 * CS1181
 * Project 3
 */
package Card_Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *
 * @author Aashish Bharadwaj
 */

public class Card_Game extends Application{    
    static Deck freshCards = new Deck(false);   //New pile, with visibility false
    static Deck discardedCards = new Deck(true);    //New pile, with visibility true
    
    static Player human;
    static Player robot;
    
    static boolean initialRun = true;   //If this is the first playthrough in THIS launch of the game
    
    public static void main(String[] args){
        Application.launch(args);   //Start application
    }
    
    @Override
    public void start(Stage primaryStage){        
        initialMessage();   //Show initial message
        startNewGame(); //Launch method to initialize and load game and GUI components
        
        /*Set Window sizing*/
        primaryStage.setMinWidth(674);
        //primaryStage.setMaxWidth(675);
        primaryStage.setMinHeight(621);
        //primaryStage.setMaxHeight(637);
        
        primaryStage.setScene(Interface.mainScene);
        primaryStage.show();
    }
    
    /**
     *  Displays initial message to grader if it is the first time the program has been run
     */
    public static void initialMessage(){
        try{
            new Scanner(new File("initial_run"));   //Check if the program has been run by checking a file created upon first run
        }catch(FileNotFoundException firstRun){
            Interface.showAlertWait("Please Read...", "This project meets all extra credit requirements.",  //Display information to grader
                    "The program has a smart computer player (AI).\nThe program uses a GUI", "INFORMATION");
            
            try{
                new PrintWriter(new File("initial_run")).println("First run complete"); //Create initial_run file (the printed text won't save
            }catch(FileNotFoundException ex){}
        }
    }
    
    /**
     *  Does an action based on what user clicks in End Game dialog
     * @param answer    The button index returned by End Game dialog: 0 is "play again", 1 is "exit program"
     */
    public static void endGameDecision(int answer){
        switch(answer){
            case 0: //If the user wants to "Play Again"
                startNewGame();
                break;
            case 1: //If the user wants to exit
                endGame();
                break;
            default:
                System.out.println("Something is wrong.");
                break;
        }
    }
    
    /**
     *  Clears and updates the discard pile of all cards
     */
    public static void clearDiscards(){
        discardedCards.clearDeck();
        Deck.updateDiscards();
    }
    
    /**
     *  Recreates a new deck of new cards, and shuffles them.
     */
    public static void createFreshCards(){
        freshCards.newDeck();
        freshCards.shuffle();
    }
    
    /**
     *  Does all necessary things to start a new game
     */
    public static void startNewGame(){
        createFreshCards();   //Creates new deck
        
        Thread clearDiscards = new Thread(new Fork("clearDiscards"));
        Platform.runLater(clearDiscards);   //Clears discard pile on separate thread for speed
        
        human = new User();
        robot = new Computer();
        
        if(initialRun == true){ //If it's the first round
            Thread createUI = new Thread(new Fork("createUI"));
            createUI.start();   //Create all the basic UI elements
            
            try{    //Wait till element creation is complete
                createUI.join();
                clearDiscards.join();
            }catch(InterruptedException ex){}
        }else{
            human.setUserImages();
        }
        
        Player.checkWinner();   //See if the first dealing has any winners, human is given priority
    }
    
    /**
     *  Exits the program, but uses a separate thread to do it, just because... (or maybe not anymore)
     */
    public static void endGame(){
        Platform.exit();    //Closes the JavaFX application
        System.exit(0); //Kill the program and all its hundreds of threads
        
//        new Thread(new Exit()).start();
    }
    
    /**
     *  Tells the user that the deck has been refilled due to it being empty
     */
    public static void notifyReshuffle(){
        Interface.showAlert("Deck created...", "The fresh cards deck was empty.", "It has been refilled by the discard pile.", "INFORMATION");
    }
    
    /**
     *  Transfers all discarded cards into the New Card pile, then shuffles them
     */
    public static void reshuffleDiscards(){
        try{
            while(true){    //Transfer all discards to fresh pile
                freshCards.addCard(discardedCards.drawCard());
            }
        }catch(Exception allDone){  //When there are no discarded cards left to transfer
            Thread notifyReshuffled = new Thread(new ForkUI("notifyReshuffled"));
            Platform.runLater(notifyReshuffled);    //Notify the user of what has happened
        }
        
        Thread updateDiscards = new Thread(new ForkUI("updateDiscards"));
        Thread shuffle = new Thread(new Fork("shufflefreshcards"));
        
        shuffle.start();    //Shuffle the deck
        Platform.runLater(updateDiscards);  //Update the discard pile image to be blank
        
        
        try{    //Wait till shuffling and updating has completed
            shuffle.join();
            updateDiscards.join();
        }catch(InterruptedException ex){}
    }
    
    /**
     *  Truncates string to specified length, and then adds three periods to denote truncation
     * @param string    The string to be truncated
     * @param length    How many maximum characters the string should be allowed to contain
     * @return  Returns the truncated string
     */
    public static String truncate(String string, int length){
	if(string.length() > (length + 1)){ //If the string needs to be truncated because it is too long
		string = string.substring(0, length) + "...";   //Shorten the string to specified length, then add three dots at the end
	}
	return string;
    }
    
}


class Exit implements Runnable{ //Class whose sole purpose is to exit the program

    @Override
    public void run(){
        try{
            Platform.exit();    //Closes the JavaFX application
            System.exit(0); //Kill the program and all its hundreds of threads
        }catch(Exception exception){}
    }

}
