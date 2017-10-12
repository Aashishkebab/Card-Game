/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_Game;

import static Card_Game.Card_Game.*;

/**
 *
 * @author Aashish Bharadwaj
 */
public class ForkUI extends Fork{   //Does the same thing as Fork, but here for organization
    
    public ForkUI(String whatToRun){
        super(whatToRun);
    }
    
    @Override
    public void run(){
        if(whatToRun.equals("createComputerUI")){
            Interface.createComputerUI();
        }
        if(whatToRun.equals("createUserUI")){
            Interface.createUserUI();
        }
        if(whatToRun.equals("createOtherElements")){
            Interface.createOtherElements();
        }
        if(whatToRun.equals("setSizing")){
            Interface.setSizing();
        }
        if(whatToRun.equals("setButtonActions")){
            Interface.setButtonActions();
        }
        if(whatToRun.equals("setUserButtonActions")){
            Interface.setUserButtonActions();
        }
        if(whatToRun.equals("setPileButtonActions")){
            Interface.setPileButtonActions();
        }
        if(whatToRun.equals("setOtherButtonActions")){
            Interface.setOtherButtonActions();
        }
        if(whatToRun.equals("setUserImages")){
            human.setUserImages();
        }
        if(whatToRun.equals("updateDiscards")){
            Deck.updateDiscards();
        }
        if(whatToRun.equals("createUI")){
            Interface.createUI();
        }
        if(whatToRun.equals("toast")){
            Interface.toast(Interface.toastMessage);
        }
        if(whatToRun.equals("notifyReshuffled")){
            notifyReshuffle();
        }
    }
}
