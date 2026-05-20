package gds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library {
    private final List<Game> games;

    public Library() {
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) {
        if (!games.contains(game)) {
            games.add(game);
        }
    }

    public List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    public boolean contains(Game game) {
        return games.contains(game);
    }
}
