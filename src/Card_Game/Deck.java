/*To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Platform;
import static Card_Game.Project3_Bharadwaj.*;

/**
 *
 * @author Aashish Bharadwaj
 */
public class Deck{
    
    private ArrayDeque<Card> deck = new ArrayDeque<>(); //The deck itself, represented as a stack
    public boolean visibility; //Whether you are allowed to view the deck
    
    /**
     *  Creates a deck, sets its visibility as false by default
     */
    public Deck(){
        this.visibility = false;
    }
    
    /**
     *  Creates a deck with custom visibility
     * @param visibility    Whether the players can see the deck
     */
    public Deck(boolean visibility){
        this.visibility = visibility;
    }
    
    /**
     *  Shuffle the deck
     */
    public void shuffle(){
        ArrayList<Card> shuffleHolder = new ArrayList<>();
        
        shuffleHolder.addAll(this.deck);    //Move stack to ArrayList
        
        Collections.shuffle(shuffleHolder); //Shuffle the ArrayList of cards
        
        this.deck.clear();  //Remove current deck
        this.deck.addAll(shuffleHolder);    //Add the shuffled ArrayList to the stack
    }
    
    /**
     *  Creates a new standard deck of 52 cards, with four cards per numerical card value, one spade, one heart, one diamond, one club
     */
    public void newDeck(){
        clearDeck();
        
        for(int i = 0; i < (52/4); i++){
            deck.push(new Spade(i + 1));
            deck.push(new Heart(i + 1));
            deck.push(new Diamond(i + 1));
            deck.push(new Club(i + 1));
        }
    }
    
    /**
     *  Clears the deck of all cards
     */
    public void clearDeck(){
        deck.clear();
    }
    
    /**
     *  Draws a card from the deck
     * @return  Returns the card drawn
     */
    public Card drawCard(){
        return this.deck.pop();
    }
    
    /**
     *  Peek at the top card in the deck
     * @return  Returns the Card peeked at
     */
    public Card viewTopCard(){
        if(this.visibility == true){
            return this.deck.peek();
        }else{
            Thread warnCheating = new Thread(new Fork("warnCheating"));
            Platform.runLater(warnCheating);    //Warn that the deck is invisible
            return new Blank();
        }
    }

    /**
     *  Add a card to the deck
     * @param card  The card to be added
     */
    public void addCard(Card card){
        this.deck.push(card);
    }
    
    /**
     *  Update the image on the discard pile
     */
    public static void updateDiscards(){
        try{
            robot.removeKnownUserCard(discardedCards.viewTopCard());    //Update computer's index of known user cards
            
            if(discardedCards.viewTopCard().getIntegerValue() != 0){
                discardedCards.viewTopCard().setImage(Interface.discardPile);
            }else{  //If there is a blank card
                Card.setBlankImage(Interface.discardPile);
            }
        }catch(Exception noCards){  //If there is no card there
            Card.setBlankImage(Interface.discardPile);
        }
    }
}