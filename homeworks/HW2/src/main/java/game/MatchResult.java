package game;

public class MatchResult {
    private final int cows;
    private final int bulls;

    public MatchResult(int cows, int bulls) {
        this.cows = cows;
        this.bulls = bulls;
    }

    int getCows() {
        return this.cows;
    }

    int getBulls() {
        return this.bulls;
    }
}
