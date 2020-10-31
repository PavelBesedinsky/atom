package game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class GameEngineTest {
    GameEngine game = new GameEngine();

    @Test
    public void equalsWordsTest() throws GameException {
        String word = "test";
        String guessWord = "test";

        MatchResult matchResult = game.getCowsAndBullsCount(guessWord, word);
        assertEquals(matchResult.getBulls(), word.length());
    }

    @Test()
    public void allCowsTest() throws GameException {
        String word = "abcd";
        String guessWord = "dcba";

        MatchResult matchResult = game.getCowsAndBullsCount(guessWord, word);
        assertEquals(new MatchResult(4, 0).getCows(), matchResult.getCows());
        assertEquals(new MatchResult(0, 0).getBulls(), matchResult.getBulls());
    }

    @Test(expected = GameException.class)
    public void differentCountOfLettersTest() throws GameException {
        String word = "one";
        String guessWord = "three";

        MatchResult matchResult = game.getCowsAndBullsCount(guessWord, word);
    }

    @Test()
    public void differentWordsTest() throws GameException {
        String word = "abc";
        String guessWord = "gnt";

        MatchResult matchResult = game.getCowsAndBullsCount(guessWord, word);
        assertEquals(new MatchResult(0, 0).getBulls(), matchResult.getBulls());
        assertEquals(new MatchResult(0, 0).getCows(), matchResult.getCows());
    }

    @Test()
    public void gameWinTest() throws GameException {
        String word = "abc";
        String guessWord = "abc";

        assertTrue(game.match(word, guessWord));
    }

    @Test()
    public void gameFailWithoutCowsTest() throws GameException {
        String word = "abc";
        String guessWord = "gnt";

        assertFalse(game.match(word, guessWord));
    }

    @Test()
    public void gameFailWithCowsTest() throws GameException {
        String word = "abc";
        String guessWord = "cab";

        assertFalse(game.match(word, guessWord));
    }
}