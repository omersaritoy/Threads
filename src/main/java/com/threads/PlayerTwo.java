package com.threads;

public class PlayerTwo implements Runnable{
    private Game game;

    public PlayerTwo(Game game){
        this.game=game;
    }

    @Override
    public void run() {
        for(int i=0;i<=100;i++){
            this.game.scor++;
            System.out.println("p1...:"+this.game.scor);
        }
    }
}
