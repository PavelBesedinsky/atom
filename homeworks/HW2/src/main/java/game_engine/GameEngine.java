package game_engine;

import application.Application;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameEngine implements IGameEngine {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Application.class);

    private ArrayList<String> dictionary;

    public GameEngine() {
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String fileName = dialog.getFile();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            ArrayList<String> dictionary = new ArrayList<>();
            do {
                dictionary.add(scanner.next());
            } while (scanner.hasNext());
            this.dictionary = dictionary;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    private void play(ArrayList<String> dictionary) {
        String randomWord = getRandomWordFromDictionary(dictionary);
        int match = 0;
        boolean result = false;
        do {
            System.out.println("Attempt " + (match + 1));
            System.out.print("> ");
            try {
                result = match(randomWord, new Scanner(System.in).next());
            } catch (GameException e) {
                System.out.println(e.getMessage());
            }
            if (result) break;
            match += 1;
        } while (match < 10);

        if (result)
            System.out.println("You have won!");
        else
            System.out.println("You have lost!");

        log.debug("End of the game");
    }

    private String getRandomWordFromDictionary(ArrayList<String> dictionary) {
        final String word = dictionary.get(
                new Random().nextInt(dictionary.size())
        );
        log.debug("Word '" + word + "' was returned");
        return word;
    }

    @Override
    public void start() throws GameException {
        if (this.dictionary.size() == 0) throw new GameException("Ошибка");
        play(this.dictionary);
    }

    @Override
    public boolean match(@NotNull String word, @NotNull String guessWord) throws GameException {
        final MatchResult match = getCowsAndBullsCount(word, guessWord);
        final boolean result = match.getBulls() == word.length();
        if (result) {
            System.out.println("Bulls: " + match.getBulls());
            System.out.println("Cows: " + match.getCows());
        }
        return result;
    }

    @Override
    public MatchResult getCowsAndBullsCount(@NotNull String word, @NotNull String guessWord) throws GameException {
        if (word.length() != guessWord.length())
            throw new GameException("Try to check the length of the word");

        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < guessWord.length(); i++) {
            if (word.charAt(i) == guessWord.charAt(i)) {
                bulls++;
            } else if (word.indexOf(guessWord.charAt(i)) != -1) {
                cows++;
            }
        }
        return new MatchResult(cows, bulls);
    }
}
