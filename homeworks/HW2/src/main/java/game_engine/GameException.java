package game_engine;

import application.Application;
import org.slf4j.LoggerFactory;

public class GameException extends Exception {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Application.class);

    public GameException(String message) {
        super(message);
        log.debug(message);
    }
}
