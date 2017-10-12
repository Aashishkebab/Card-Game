/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import static Card_Game.Project3_Bharadwaj.discardedCards;
import static Card_Game.Project3_Bharadwaj.freshCards;
import static Card_Game.Project3_Bharadwaj.reshuffleDiscards;

/**
 *
 * @author Aashish Bharadwaj
 */
public class Computer extends Player{
    
    public static Thread makeMove = new Thread();   //Universal thread to start and check if alive to prevent others from moving
    
    public static int difficulty = 1;   //How smart the computer is
    public static boolean thinking = true;  //Whether computer displays toast on every turn (setting false saves time)
    public static boolean isInsane = false;
    
    public ArrayList<Card> knownUserCards = new ArrayList<>();
    
    @Override
    public void makeMove(){
        think();    //Pause a little
        
        drawCard(); //Draw new card
        
        discardCard(chooseDiscard());   //Discard old card
        
        Thread toast = new Thread(new ForkUI("toast"));
        Platform.runLater(toast);   //Notify user of actions that have been taken
    }
    
    private void showCards(String string){   //Print out cards in deck to console, used for testing purposes
        System.out.println(string); System.out.println("");
        
        for(int i = 0; i < 5; i++){
            System.out.println(cards[i].toString());
        }
        System.out.println(""); System.out.println("");
    }
    
    private int chooseDiscard(){    //Choose which card to discard based on current cards
        ArrayList<Integer> occurrences = getOccurrences(getCardValues());   //Get an ArrayList of the number of times each value occurrs
        
        return findSmallest(occurrences);   //Choose to discard whichever card has the least number of occurrences in hand
    }
    
    @Override
    public void discardCard(int cardNumber){
        if(this.cards[cardNumber].getIntegerValue() != 0){
            discardedCards.addCard(cards[cardNumber]);
            
            Interface.toastMessage += "\n I discarded " + discardedCards.viewTopCard() + " to the discard pile.";
            
            cards[cardNumber] = new Blank();    //Replace discarded card with blank one
            
            Thread updateDiscards = new Thread(new ForkUI("updateDiscards"));
            Platform.runLater(updateDiscards);
        }
    }
    
    @Override
    public void drawNewCard(){
        try{
            if(cards[4].getType().equals("Blank")){
                try{
                    Interface.toastMessage = "I drew something from the new pile.";
                    cards[4] = freshCards.drawCard();
                }catch(Exception noCards){
                    reshuffleDiscards();
                    try{
                        cards[4] = freshCards.drawCard();
                    }catch(Exception crap){
                        Interface.showAlertWait("Fatal error", "There has been an error.", "After reshuffling, there were still no cards", "WARNING");
                        System.exit(0);
                    }
                }
            }
        }catch(NullPointerException noCardsLeft){
            System.out.println("Last card is null");
        }
        
        Thread checkWinner = new Thread(new Fork("checkWinner"));
        checkWinner.start();    //See if anyone has won
    }

    @Override
    public void drawOldCard() {
        if(cards[4].getType().equals("Blank")){
            
            cards[4] = discardedCards.drawCard();
            Interface.toastMessage = "I drew " + cards[4].toString() + " from the discard pile.";

            Thread updateDiscards = new Thread(new ForkUI("updateDiscards"));
            Platform.runLater(updateDiscards);
        }
        
        Thread checkWinner = new Thread(new Fork("checkWinner"));
        checkWinner.start();    //See if anyone has won
    }

    @Override
    boolean isWinner(){
        int occurrences = 0;
        
        for(int i = 0; i < cards.length; i++){
            try{
                if(cards[i].getIntegerValue() == cards[i - 1].getIntegerValue()){
                    occurrences++;
                }else{
                    occurrences = 0;
                }
                if(occurrences >= 3){
                    return true;
                }
            }catch(ArrayIndexOutOfBoundsException exception){}
        }
        
        return false;
    }

    @Override
    void iWon(){
        Thread computerWon = new Thread(new Fork("computerWon"));
        Platform.runLater(computerWon); //Alert that computer has won
    }
    
    private ArrayList<Integer> getOccurrences(int[] cardValues){    //Return ArrayList of the number of times each card value appears
        ArrayList<Integer> occurrences = new ArrayList<>();
        
        for(int i = 0; i < cardValues.length; i++){ //Loop through until every card has been counted
            int individualOccurrences = 0;
            for(int j = 0; j < cardValues.length; j++){ //Go through and count the occurrences of current card
                if(cardValues[i] == cardValues[j]){ //If the card value is the same, it has another occurrence
                    individualOccurrences++;
                }
            }
            occurrences.add(individualOccurrences); //Add the number of occurrences to ArrayList
        }
        
        return occurrences;
    }

    private int[] getCardValues(){  //Return Array of integers for values of cards, to make for better code readability, rather than use Card.getIntegerValue() each time
        int[] cardValues = new int[5];
        
        for(int i = 0; i < 5; i++){
            cardValues[i] = cards[i].getIntegerValue();
        }
        
        return cardValues;
    }

    private int findSmallest(ArrayList<Integer> occurrences){   //Finds the smallest value in an ArrayList
        int smallestValue = 6; //Sets the default smallest value to higher than the number of cards there possibly can be
        int smallestValueIndex = -1;
        int largestValue = 0;
        int largestValueIndex = -1;
        
        boolean inUser = false; //If the item exists within known user items
        boolean hasThree = false;
        boolean userHasTwo = false; int userMostWanted = 0;
        
        if(isInsane){   //If it's insane difficulty and the user has two or more of one type of card
            int[] knownCardsOfUser = new int[knownUserCards.size()];
            for(int i = 0; i < knownUserCards.size(); i++){ //Get occurrences of all user's cards
                knownCardsOfUser[i] = knownUserCards.get(i).getIntegerValue();
            }
            ArrayList<Integer> userOccurrences = getOccurrences(knownCardsOfUser);
            
            for(int i = 0; i < userOccurrences.size(); i++){    //Check if user has two of one type
                if(userOccurrences.get(i) >= 2){
                    userHasTwo = true;
                    userMostWanted = knownUserCards.get(userOccurrences.get(i)).getIntegerValue();
                }
            }
        }
        
        for(int i = 0; i < occurrences.size(); i++){    //Go throuugh and see if ArrayList value is smaller than int smallestValue
            if(occurrences.get(i) > largestValue){  //Sees what the largest value of occurrences is
                largestValue = occurrences.get(i);
                largestValueIndex = i;
                if(largestValue >= 3){
//                    showCards("Cards are...");
                    hasThree = true;    //If there are three of the same value, ignore what the user has later on
                }
            }
            
            if(occurrences.get(i) < smallestValue){ //If new value is smaller than old smallest value, store the new one and its index
                if(!isInsane || (hasThree && !userHasTwo)){
                    smallestValue = occurrences.get(i);
                    smallestValueIndex = i;
                }else{  //If insane difficulty
                    for(int k = 0; k < knownUserCards.size(); k++){ //Check if the card is inside known user pile
                        if(knownUserCards.get(k).getIntegerValue() == cards[i].getIntegerValue()){  //If it is, make sure the computer knows
                            inUser = true;
                            break;
                        }
                    }
                    if(!inUser){
                        smallestValue = occurrences.get(i);
                        smallestValueIndex = i;
                    }
                    inUser = false;
                }
            }
        }
        
        return smallestValueIndex;  //Return where the smallest value is
    }

    private void drawCard(){    //Determines which card to draw
        Arrays.sort(cards);
        
        if(isWorthDraw(discardedCards.viewTopCard().getIntegerValue())){    //If it's worth drawing from the discards
            drawOldCard();
        }else{
            drawNewCard();
        }
    }

    private boolean isWorthDraw(int comparedCard){  //Check whether it is worth drawing from the discard pile, by seeing the top card matches a number in the Copmuter's hand
        int occurrences = 0;
        
        for(int i = 0; i < 5; i++){ //See how many times the top card of the discard pile occurrs 
            if(comparedCard == cards[i].getIntegerValue()){
                occurrences++;
            }
        }
        
        return occurrences >= Computer.difficulty;    //If there is at least certain number in the hand that matches
    }
    
    /**
     *  Pauses and waits so the user can see their own move before the computer moves
     */
    public static void think(){
        Thread toast = new Thread(new ForkUI("toast"));
        Interface.toastMessage = "thinking...";
        Platform.runLater(toast);
        
        if(thinking){
            try{    //Sleep for a random amount of time no less than 333 and no greater than 777 milliseconds
                Thread.sleep((int)((Math.random() * 444) + 333));
            }catch(InterruptedException ex){}
        }
    }

    @Override
    public void removeKnownUserCard(Card cardToRemove){
        for(int i = 0; i < knownUserCards.size(); i++){
            if(knownUserCards.get(i).equals(cardToRemove)){
                knownUserCards.remove(i);
                return;
            }
        }
    }
    
    @Override
    public void addKnownUserCard(Card cardToAdd){
        knownUserCards.add(cardToAdd);
    }

    public void showKnownUserCards(){
        System.out.println("Known user cards"); System.out.println("");
        for(int i = 0; i < knownUserCards.size(); i++){
            System.out.println(knownUserCards.get(i).toString());
        }
    }
    
}
