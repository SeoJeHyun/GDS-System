package gds;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private final Map<String, MemberGamer> memberGamers;
    private final Map<String, Developer> developers;
    private final Map<String, Administrator> administrators;
    private final Map<String, Game> games;
    private final List<String> paymentMethods;

    public DataStore() {
        this.memberGamers = new HashMap<>();
        this.developers = new HashMap<>();
        this.administrators = new HashMap<>();
        this.games = new HashMap<>();
        this.paymentMethods = new ArrayList<>();
    }

    public void load() {
        Path dataDirectory = Paths.get(GConstant.DataPath.DATA_DIRECTORY);
        loadMemberGamers(dataDirectory.resolve(GConstant.DataPath.USER_ACCOUNTS_FILE));
        loadDevelopers(dataDirectory.resolve(GConstant.DataPath.DEVELOPER_ACCOUNTS_FILE));
        loadAdministrators(dataDirectory.resolve(GConstant.DataPath.ADMIN_ACCOUNTS_FILE));
        loadGames(dataDirectory.resolve(GConstant.DataPath.GAMES_FILE));
        loadPaymentMethods(dataDirectory.resolve(GConstant.DataPath.PAYMENT_METHODS_FILE));
    }

    public MemberGamer findMemberGamer(String userId) {
        return memberGamers.get(userId);
    }

    public Developer findDeveloper(String userId) {
        return developers.get(userId);
    }

    public Administrator findAdministrator(String userId) {
        return administrators.get(userId);
    }

    public List<Game> getAllGames() {
        return new ArrayList<>(games.values());
    }

    public Game findGame(String gameId) {
        return games.get(gameId);
    }

    public String getDefaultPaymentMethod() {
        if (paymentMethods.isEmpty()) {
            return GConstant.DataFormat.DEFAULT_PAYMENT_METHOD;
        }
        return paymentMethods.get(GConstant.DataFormat.FIRST_ITEM_INDEX);
    }

    private void loadMemberGamers(Path path) {
        for (String line : readDataLines(path)) {
            String[] values = split(line, GConstant.DataFormat.USER_FIELD_COUNT, path);
            MemberGamer gamer = new MemberGamer(
                    values[GConstant.DataFormat.USER_ID_INDEX],
                    values[GConstant.DataFormat.PASSWORD_INDEX],
                    values[GConstant.DataFormat.NAME_INDEX]
            );
            memberGamers.put(gamer.getUserId(), gamer);
        }
    }

    private void loadDevelopers(Path path) {
        for (String line : readDataLines(path)) {
            String[] values = split(line, GConstant.DataFormat.DEVELOPER_FIELD_COUNT, path);
            Developer developer = new Developer(
                    values[GConstant.DataFormat.USER_ID_INDEX],
                    values[GConstant.DataFormat.PASSWORD_INDEX],
                    values[GConstant.DataFormat.NAME_INDEX],
                    values[GConstant.DataFormat.COMPANY_NAME_INDEX]
            );
            developers.put(developer.getUserId(), developer);
        }
    }

    private void loadAdministrators(Path path) {
        for (String line : readDataLines(path)) {
            String[] values = split(line, GConstant.DataFormat.ADMINISTRATOR_FIELD_COUNT, path);
            Administrator administrator = new Administrator(
                    values[GConstant.DataFormat.USER_ID_INDEX],
                    values[GConstant.DataFormat.PASSWORD_INDEX],
                    values[GConstant.DataFormat.NAME_INDEX],
                    values[GConstant.DataFormat.DEPARTMENT_INDEX]
            );
            administrators.put(administrator.getUserId(), administrator);
        }
    }

    private void loadGames(Path path) {
        for (String line : readDataLines(path)) {
            String[] values = split(line, GConstant.DataFormat.GAME_FIELD_COUNT, path);
            GameFile gameFile = new GameFile(
                    values[GConstant.DataFormat.GAME_FILE_NAME_INDEX],
                    Integer.parseInt(values[GConstant.DataFormat.GAME_FILE_SIZE_INDEX])
            );
            DistributionStatus status = new DistributionStatus(
                    values[GConstant.DataFormat.GAME_DISTRIBUTION_STATUS_INDEX],
                    GConstant.DataFormat.LOADED_FROM_DATA_FILE
            );
            Game game = new Game(
                    values[GConstant.DataFormat.GAME_ID_INDEX],
                    values[GConstant.DataFormat.GAME_TITLE_INDEX],
                    values[GConstant.DataFormat.GAME_DEVELOPER_NAME_INDEX],
                    Integer.parseInt(values[GConstant.DataFormat.GAME_PRICE_INDEX]),
                    values[GConstant.DataFormat.GAME_GENRE_INDEX],
                    Boolean.parseBoolean(values[GConstant.DataFormat.GAME_DEMO_AVAILABLE_INDEX]),
                    values[GConstant.DataFormat.GAME_DETAIL_INDEX],
                    gameFile,
                    status
            );
            games.put(game.getGameId(), game);
        }
    }

    private void loadPaymentMethods(Path path) {
        for (String line : readDataLines(path)) {
            String[] values = split(line, GConstant.DataFormat.PAYMENT_METHOD_FIELD_COUNT, path);
            paymentMethods.add(values[GConstant.DataFormat.PAYMENT_METHOD_NAME_INDEX]);
        }
    }

    private List<String> readDataLines(Path path) {
        try {
            List<String> result = new ArrayList<>();
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith(GConstant.DataFormat.COMMENT_PREFIX)) {
                    result.add(trimmed);
                }
            }
            return result;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    GConstant.Message.CANNOT_READ_DATA_FILE_PREFIX + path.toAbsolutePath(),
                    exception
            );
        }
    }

    private String[] split(String line, int expectedLength, Path path) {
        String[] values = line.split(
                GConstant.DataFormat.FILE_DELIMITER_REGEX,
                GConstant.DataFormat.KEEP_TRAILING_EMPTY_VALUES
        );
        if (values.length != expectedLength) {
            throw new IllegalStateException(
                    GConstant.Message.INVALID_DATA_FORMAT_PREFIX
                            + path
                            + GConstant.Message.INVALID_DATA_FORMAT_SEPARATOR
                            + line
            );
        }
        return values;
    }
}
