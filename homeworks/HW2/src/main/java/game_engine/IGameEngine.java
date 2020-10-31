package game_engine;

import java.util.ArrayList;

public interface IGameEngine {
    void start() throws GameException;

    boolean match(String word, String guessWord) throws GameException;

    MatchResult getCowsAndBullsCount(String word, String guessWord) throws GameException;
}
