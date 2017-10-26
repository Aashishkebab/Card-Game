/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import java.util.Arrays;
import javafx.application.Platform;
import static Card_Game.CardGame.discardedCards;
import static Card_Game.CardGame.freshCards;
import static Card_Game.CardGame.reshuffleDiscards;
import static Card_Game.CardGame.robot;


/**
 *
 * @author Aashish Bharadwaj
 */
public class User extends Player{   //Human player
    
    @Override
    public void discardCard(int cardNumber){        
        if(cards[4].getType().equals("Blank")){ //If a blank card is clicked on
            Interface.showAlert("Illegal move", "You cannot discard another card.", "You must first draw a new card.", "WARNING");
            return;
        }
        
        discardedCards.addCard(cards[cardNumber]);  //Sends the selected card to the discard pile
        cards[cardNumber] = new Blank();    //Sets the discarded card to a new blank one

        Thread setUserImages = new Thread(new ForkUI("setUserImages"));
        Platform.runLater(setUserImages);   //Updates list of cards

        Thread updateDiscards = new Thread(new ForkUI("updateDiscards"));
        Platform.runLater(updateDiscards);  //Updates discard pile face image

        Computer.makeMove = new Thread(new Fork("makeMove"));
        Computer.makeMove.start();  //Have the computer make its turn
    }
    
    @Override
    public void setUserImages(){
        try{
            Arrays.sort(cards); //Sort the cards by value
        }catch(NullPointerException exception){
            System.out.println("Wat iz dis?");
        }
        
        int i = 0;
        try{
            for(i = 0; i < 5; i++){ //Set the images of all the cards
                cards[i].setImage(Interface.userButtons[i]);
            }
        }catch(NullPointerException exception){
            System.out.println("Error oh noe aaaahahahahahahh");
        }
    }

    @Override
    boolean isWinner(){ //Returns boolean of whether User has won
        int occurrences = 0;
        
        for(int i = 0; i < cards.length; i++){
            try{
                if(cards[i].getIntegerValue() == cards[i - 1].getIntegerValue()){
                    occurrences++;
                }else{
                    occurrences = 0;
                }
                if(occurrences >= 3){   //If there are 4 cards with the same value
                    return true;
                }
            }catch(ArrayIndexOutOfBoundsException exception){}
        }
        
        return false;
    }

    @Override
    void iWon(){    //What to do if User has won
        Thread userWon = new Thread(new Fork("userWon"));
        Platform.runLater(userWon); //Display User has won alert
    }

    @Override
    public void drawOldCard(){
        if(cards[4].getType().equals("Blank")){ //If there is room to draw a new card
            cards[4] = discardedCards.drawCard();   //Draw card and add it to user's cards
            
            robot.addKnownUserCard(this.cards[4]);
            
            Thread updateDiscards = new Thread(new ForkUI("updateDiscards"));
            Platform.runLater(updateDiscards);
            
            Thread setUserImages = new Thread(new ForkUI("setUserImages"));
            Platform.runLater(setUserImages);   //Sort user's cards and set their images
        }else{  //If card cannot be added/drawn
            Interface.showAlert("Illegal move", "Your stack is full.", "You must discard a card first.", "WARNING");
        }
        
        Thread checkWinner = new Thread(new Fork("checkWinner"));
        checkWinner.start();    //See if anyone has won
    }

    @Override
    public void drawNewCard(){
        try{
            if(cards[4].getType().equals("Blank")){ //If space to draw
                try{
                    cards[4] = freshCards.drawCard();
                }catch(Exception noCards){
                    reshuffleDiscards();    //If the pile of fresh cards runs out, transfer discards over to fresh cards deck
                }

                //setUserImages();
                Thread setUserImages = new Thread(new ForkUI("setUserImages"));
                Platform.runLater(setUserImages);
            }else{  //If no space to draw
                Interface.showAlert("Illegal move", "Your stack is full.", "You must discard a card first.", "WARNING");
            }
        }catch(NullPointerException exception){
            System.out.println("Last card is null.");
        }
        
        Thread checkWinner = new Thread(new Fork("checkWinner"));
        checkWinner.start();    //See if anyone has won
    }
    
}