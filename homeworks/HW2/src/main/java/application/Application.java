package application;

import game_engine.GameEngine;
import game_engine.GameException;

import java.util.Scanner;


public class Application {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        try {
            do {
                engine.start();
                System.out.println("Do you want to play again? Yes/No");
            } while (new Scanner(System.in).next().equalsIgnoreCase("Yes"));
        } catch (GameException e) {
            e.printStackTrace();
        }
    }
}
