/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Aashish Bharadwaj
 */
public abstract class Card implements Comparable<Card>{

    private int cardValue;  //The integer value of the card
    
    /**
     *  Creates a card, class is abstract
     */
    public Card(){
        
    }
    
    /**
     *  Returns a copy of the card fed into the method
     * @return  Returns the copied card
     */
    public Card createCopy(){
        switch (getType()) {
            case "Spade":
                return new Spade(this.cardValue);
            case "Diamond":
                return new Diamond(this.cardValue);
            case "Club":
                return new Club(this.cardValue);
            case "Heart":
                return new Heart(this.cardValue);
            default:
                System.out.println("Oh no");
                return null;
        }
    }
    
    /**
     *  Gets the type of card (Spade, Diamond, Heart, or Club
     * @return  Returns a string of the card type
     */
    public abstract String getType();
    
    /**
     *  Get the image file to display corresponding to the card
     * @return
     */
    public abstract File getImage();
    
    /**
     *  Sets the image of a button the its card
     * @param button    The button to be changed
     */
    public void setImage(Button button){
        button.setGraphic(new ImageView(new Image(getImage().toURI().toString())));
    }
    
    /**
     *  Set image of back of card
     * @param button    The button to be changed
     */
    public static void setBlankImage(Button button){
        button.setGraphic(new ImageView(new Image("file:images/cards/no_card.png")));
    }
    
    /**
     *  Set the integer value of the card
     * @param value
     */
    public final void setValue(int value){
        this.cardValue = value;
    }
    
    /**
     *  Gets the value of a card in English
     * @return  Returns a string with correct English definite article followed by the value in English
     */
    public String getValue(){
        switch(this.cardValue){
            case 1:
                return "an Ace";
            case 2:
                return "a Two";
            case 3:
                return "a Three";
            case 4:
                return "a Four";
            case 5:
                return "a Five";
            case 6:
                return "a Six";
            case 7:
                return "a Seven";
            case 8:
                return "an Eight";
            case 9:
                return "a Nine";
            case 10:
                return "a Ten";
            case 11:
                return "a Jack";
            case 12:
                return "a Queen";
            case 13:
                return "a King";
            default:
                return "a nonexistent number";
        }
    }
    
    /**
     *  Gets the numerical representation of a card as a whole number
     * @return  Returns card value
     */
    public int getIntegerValue(){
        return this.cardValue;
    }
    
    /**
     *  Generates a random card
     * @return  Returns the random card generated
     */
    public static Card generateCard(){
        int random;
        random = (int)(Math.random() * 4) + 1;
        
        switch(random){
            case 1:
                return new Spade();
            case 2:
                return new Heart();
            case 3:
                return new Diamond();
            case 4:
                return new Club();
            default:
                Interface.showAlertWait("Oh crap", "We should not be here. The program will now exit.", "A card has been generated that is neither spades nor hearts nor diamonds nor clubs", "INFORMATION");
                System.exit(0);
        }
        
        return null;
    }
    
    private static int randomizeValue(){    //Sets the card's value to a random number
        return (int)(Math.random() * 13) + 1;
    }
    
    /**
     *  Creates a random card, with random type and value
     * @return  Returns random card
     */
    public Card randomizeCard(){
        Card randomCard = generateCard();
        randomCard.cardValue = randomizeValue();
        return randomCard;
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public int compareTo(Card card){
        if(this.cardValue == 0){
            return 1;
        }
        return Integer.compare(card.getIntegerValue(), this.cardValue);
    }
    
}

class Spade extends Card{   //This is a type of card
    
    public Spade(){
        
    }
    public Spade(int cardValue){
        super.setValue(cardValue);
    }
    
    @Override
    public File getImage(){ //Gets the image at the location of where all of its type are (in the corresponding folder)
        return new File("images/cards/spades/" + super.getIntegerValue() + ".png");
    }

    @Override
    public String toString(){   //Returns a string of the card in plain English
        return super.getValue() + " of Spades";
    }

    @Override
    public String getType(){
        return "Spade";
    }

}

class Heart extends Card{
    
    public Heart(){
        
    }
    public Heart(int cardValue){
        super.setValue(cardValue);
    }

    @Override
    public File getImage() {
        return new File("images/cards/hearts/" + super.getIntegerValue() + ".png");
    }

    @Override
    public String toString() {
        return super.getValue() + " of Hearts";
    }
    
    @Override
    public String getType() {
        return "Heart";
    }
    
}

class Diamond extends Card{
    
    public Diamond(){
        
    }
    public Diamond(int cardValue){
        super.setValue(cardValue);
    }
    
    @Override
    public File getImage() {
        return new File("images/cards/diamonds/" + super.getIntegerValue() + ".png");
    }

    @Override
    public String toString() {
        return super.getValue() + " of Diamonds";
    }
    
    @Override
    public String getType() {
        return "Diamond";
    }
    
}

class Club extends Card{
    
    public Club(){
        
    }
    public Club(int cardValue){
        super.setValue(cardValue);
    }

    @Override
    public File getImage() {
        return new File("images/cards/clubs/" + super.getIntegerValue() + ".png");
    }

    @Override
    public String toString(){
        return super.getValue() + " of Clubs";
    }
    
    @Override
    public String getType() {
        return "Club";
    }
    
}

class Blank extends Card{
    
    public Blank(){ //Automatically sets this card to 0, as it has no value
        setValue(0);
    }

    @Override
    public String getType() {
        return "Blank";
    }

    @Override
    public File getImage() {
        return new File("images/cards/no_card.png");
    }

    @Override
    public String toString() {
        return "No card";
    }
    
}
