/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import java.util.ArrayList;
import javafx.scene.control.ButtonType;
import static Card_Game.Interface.askConfirmation;
import static Card_Game.Card_Game.*;

/**
 *
 * @author Aashish Bharadwaj
 */
public class Fork implements Runnable{  //Class that contains commands to run threads, rather than putting each thread in a separate class
    
    String whatToRun;
    
    /**
     *  Creates an instance with a specific string to run a thread that corresponds
     * @param whatToRun The name of the command to be run
     */
    public Fork(String whatToRun){
        this.whatToRun = whatToRun;
    }

    @Override
    public void run(){  //If the string whatToRun equals a certain value, then run the actions corresponding to it
        if(whatToRun.equals("createUI")){
            Thread createComputerUI = new Thread(new ForkUI("createComputerUI"));
            createComputerUI.start();

            Thread createUserUI = new Thread(new ForkUI("createUserUI"));
            createUserUI.start();

            Thread createOtherElements = new Thread(new ForkUI("createOtherElements"));
            createOtherElements.start();
            
            Thread setSizing = new Thread(new ForkUI("setSizing"));
            setSizing.start();
            
            Thread setButtonActions = new Thread(new ForkUI("setButtonActions"));
            setButtonActions.start();
            
            Thread createUI = new Thread(new ForkUI("createUI"));
            createUI.start();
            
            try{
                createComputerUI.join();
                createUserUI.join();
                createOtherElements.join();
                setSizing.join();
                setButtonActions.join();
                createUI.join();
            }catch(InterruptedException except){}
            
            initialRun = false;
        }
        
        if(whatToRun.equals("newDeck")){
            freshCards.newDeck();
        }
        if(whatToRun.equals("shufflefreshcards")){
            freshCards.shuffle();
        }
        if(whatToRun.equals("makeMove")){
            robot.makeMove();
        }
        if(whatToRun.equals("checkWinner")){
            Player.checkWinner();
        }
        if(whatToRun.equals("warnCheating")){
            Interface.showAlert("Bad!", "You are attempting to cheat!", "You cannot view this deck!", "WARNING");
        }
        if(whatToRun.equals("computerWon")){
            ArrayList<ButtonType> options = new ArrayList<>();
            options.add(new ButtonType("Play again"));
            options.add(new ButtonType("I'm done with this"));
            
            endGameDecision(askConfirmation("Winner!", "The AI has won the game!", null, options));
        }
        if(whatToRun.equals("userWon")){
            ArrayList<ButtonType> options = new ArrayList<>();
            options.add(new ButtonType("Play again"));
            options.add(new ButtonType("I'm done with this"));

            endGameDecision(askConfirmation("Winner!", "You have won the game!", null, options));
        }
        if(whatToRun.equals("bothWon")){
            ArrayList<ButtonType> options = new ArrayList<>();
            options.add(new ButtonType("Play again"));
            options.add(new ButtonType("I'm done with this"));

            endGameDecision(askConfirmation("Winners!", "Both players have won the game!", null, options));
        }
        if(whatToRun.equals("clearDiscards")){
            clearDiscards();
        }
        if(whatToRun.equals("recreateFreshCards")){
            createFreshCards();
        }
        if(whatToRun.equals("initialMessage")){
            initialMessage();
        }
        if(this.whatToRun.equals("test")){
            //Test code here
        }
    }

    
}
