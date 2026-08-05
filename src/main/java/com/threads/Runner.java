package com.threads;

public class Runner {
    public static void main(String[] args) {
        Game game=new Game();
        game.scor=500;
        PlayerOne playerOne=new PlayerOne(game);
        PlayerTwo playerTwo=new PlayerTwo(game);

        playerOne.start();//ayrı thread

        new Thread(playerTwo).start(); // ayrı bir thread

        System.out.println("Uygulama sona erdi");


    }

}
