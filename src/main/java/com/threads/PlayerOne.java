package com.threads;

public class PlayerOne extends Thread {


    Game game;

    public PlayerOne(Game game){
        this.game=game;
    }

    public void run(){
        for(int i=0;i<100;i++){
            this.game.scor--;
            System.out.println("p1...:"+this.game.scor);
        }
    }
}
