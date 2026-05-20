package gds;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Shop {
    private final DataStore dataStore;

    public Shop(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<Game> getGameList() {
        List<Game> games = dataStore.getAllGames();
        games.sort(Comparator.comparing(Game::getGameId));
        return games;
    }

    public List<Game> searchGames(String keyword) {
        List<Game> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Game game : getGameList()) {
            if (containsIgnoreCase(game.getGameId(), lowerKeyword)
                    || containsIgnoreCase(game.getTitle(), lowerKeyword)
                    || containsIgnoreCase(game.getDeveloperName(), lowerKeyword)
                    || containsIgnoreCase(game.getGenre(), lowerKeyword)
                    || containsIgnoreCase(game.getDetail(), lowerKeyword)) {
                result.add(game);
            }
        }

        return result;
    }

    public Game findGame(String gameId) {
        return dataStore.findGame(gameId);
    }

    public void showGameList() {
        System.out.println(GConstant.Console.GAME_LIST_TITLE);
        for (Game game : getGameList()) {
            showGameSummary(game);
        }
    }

    public void showSearchResult(String keyword) {
        List<Game> games = searchGames(keyword);

        System.out.println(GConstant.Console.SEARCH_RESULT_TITLE);
        if (games.isEmpty()) {
            System.out.println(GConstant.Message.SEARCH_RESULT_EMPTY);
            return;
        }

        for (Game game : games) {
            showGameSummary(game);
        }
    }

    public void showGameDetail(Game game) {
        System.out.println(GConstant.Console.GAME_DETAIL_TITLE);
        System.out.println(GConstant.Console.GAME_ID_LABEL + game.getGameId());
        System.out.println(GConstant.Console.GAME_TITLE_LABEL + game.getTitle());
        System.out.println(GConstant.Console.GAME_DEVELOPER_LABEL + game.getDeveloperName());
        System.out.println(GConstant.Console.GAME_PRICE_LABEL + game.getPrice());
        System.out.println(GConstant.Console.GAME_GENRE_LABEL + game.getGenre());
        System.out.println(GConstant.Console.GAME_DEMO_AVAILABLE_LABEL + game.isDemoAvailable());
        System.out.println(GConstant.Console.GAME_DETAIL_LABEL + game.getDetail());
        System.out.println(GConstant.Console.GAME_FILE_LABEL + game.getGameFile().getFileName());
        System.out.println(
                GConstant.Console.GAME_FILE_SIZE_LABEL
                        + game.getGameFile().getFileSizeMB()
                        + GConstant.Console.FILE_SIZE_UNIT_MB
        );
        System.out.println(GConstant.Console.GAME_DISTRIBUTION_STATUS_LABEL + game.getDistributionStatus().getStatusName());
    }

    public void addGameToCart(MemberGamer gamer, Game game) {
        if (gamer.getLibrary().contains(game)) {
            System.out.println(GConstant.Message.GAME_ALREADY_IN_LIBRARY);
            return;
        }

        if (gamer.getCart().contains(game)) {
            System.out.println(GConstant.Message.GAME_ALREADY_IN_CART);
            return;
        }

        gamer.addGameToCart(game);
        System.out.println(GConstant.Message.ADDED_TO_CART_PREFIX + game.getTitle());
    }

    public void showGameSummary(Game game) {
        System.out.println(
                game.getGameId()
                        + GConstant.Console.DISPLAY_SEPARATOR
                        + game.getTitle()
                        + GConstant.Console.DISPLAY_SEPARATOR
                        + game.getPrice()
        );
    }

    private boolean containsIgnoreCase(String source, String lowerKeyword) {
        return source != null && source.toLowerCase().contains(lowerKeyword);
    }
}
