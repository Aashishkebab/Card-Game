/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import javafx.application.Platform;
import static Card_Game.Card_Game.freshCards;
import static Card_Game.Card_Game.human;
import static Card_Game.Card_Game.robot;

/**
 *
 * @author Aashish Bharadwaj
 */
public abstract class Player{   //Abstract class containing different types of players
    Card[] cards;   //Cards that the player owns
    
    /**
     *  Called when creating instance of Player subclass, creates array of cards
     */
    public Player(){
        this.cards = new Card[5];
        
        for(int i = 0; i < 4; i++){
            cards[i] = freshCards.drawCard();   //Draw cards from the fresh stack
        }
        cards[4] = new Blank(); //Last card is blank
    }
    
    /**
     *  Computerized players make a move
     */
    public void makeMove(){}
    
    /**
     *  Discard the chosen card
     * @param cardNumber    Index of card in array of Player's cards to discard
     */
    public abstract void discardCard(int cardNumber);
    
    /**
     *  Draw a card from fresh stack
     */
    public abstract void drawNewCard();
    
    /**
     *  Draw card from discard pile
     */
    public abstract void drawOldCard();
    
    /**
     *  Sets the images for a user's cards, must be overriden to work
     */
    public void setUserImages(){}
    
    abstract boolean isWinner();
    
    abstract void iWon();
    
    public void addKnownUserCard(Card cardToAdd){}
    public void removeKnownUserCard(Card cardToRemove){}
    
    
    /**
     *  Check if someone has won
     */
    public static void checkWinner(){
        int result;
        
        if(human.isWinner() && robot.isWinner()){   //If both players have won
            Player.bothWon();
        }else if(human.isWinner()){
            human.iWon();
        }else if(robot.isWinner()){
            robot.iWon();
        }else{
        }
    }
    
    /**
     *  What to do if both players have won
     */
    public static void bothWon(){
        Thread bothWon = new Thread(new Fork("bothWon"));
        Platform.runLater(bothWon); //Start new thread displaying that both players have won
    }
}
