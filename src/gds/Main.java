package gds;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataStore dataStore = new DataStore();
        dataStore.load();

        Shop shop = new Shop(dataStore);
        Scanner scanner = new Scanner(System.in);
        User currentUser = new NonMemberGamer();
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println(GConstant.Console.MAIN_MENU_TITLE);
            System.out.println(
                    GConstant.Console.CURRENT_USER_LABEL
                            + currentUser.getName()
                            + GConstant.Console.USER_SEPARATOR
                            + currentUser.getUserType()
            );
            System.out.println(GConstant.Console.MENU_LOGIN);
            System.out.println(GConstant.Console.MENU_SHOP);
            System.out.println(GConstant.Console.MENU_CART_CHECKOUT);
            System.out.println(GConstant.Console.MENU_CHECK_LIBRARY);
            System.out.println(GConstant.Console.MENU_SHOW_GAME_DETAIL);
            System.out.println(GConstant.Console.MENU_EXIT);
            System.out.print(GConstant.Console.SELECT_MENU_PROMPT);

            String input = scanner.nextLine();
            System.out.println();

            switch (input) {
                case GConstant.MenuCommand.LOGIN:
                    currentUser = login(scanner, dataStore, currentUser);
                    break;

                case GConstant.MenuCommand.SHOP:
                    openShop(scanner, currentUser, shop);
                    break;

                case GConstant.MenuCommand.CART_CHECKOUT:
                    checkoutCart(scanner, currentUser, dataStore);
                    break;

                case GConstant.MenuCommand.CHECK_LIBRARY:
                    checkLibrary(currentUser);
                    break;

                case GConstant.MenuCommand.SHOW_GAME_DETAIL:
                    showGameDetail(scanner, shop);
                    break;

                case GConstant.MenuCommand.EXIT:
                    running = false;
                    System.out.println(GConstant.Message.PROGRAM_FINISHED);
                    break;

                default:
                    System.out.println(GConstant.Message.INVALID_MENU_NUMBER);
            }
        }

        scanner.close();
    }

    private static User login(Scanner scanner, DataStore dataStore, User currentUser) {
        System.out.println(GConstant.Console.LOGIN_TYPE_TITLE);
        System.out.println(GConstant.Console.LOGIN_TYPE_MEMBER_GAMER);
        System.out.println(GConstant.Console.LOGIN_TYPE_DEVELOPER);
        System.out.println(GConstant.Console.LOGIN_TYPE_ADMINISTRATOR);
        System.out.print(GConstant.Console.SELECT_LOGIN_TYPE_PROMPT);
        String type = scanner.nextLine();

        System.out.print(GConstant.Console.ID_PROMPT);
        String id = scanner.nextLine();
        System.out.print(GConstant.Console.PASSWORD_PROMPT);
        String password = scanner.nextLine();

        User loginTarget = null;
        if (GConstant.LoginTypeCommand.MEMBER_GAMER.equals(type)) {
            loginTarget = dataStore.findMemberGamer(id);
        } else if (GConstant.LoginTypeCommand.DEVELOPER.equals(type)) {
            loginTarget = dataStore.findDeveloper(id);
        } else if (GConstant.LoginTypeCommand.ADMINISTRATOR.equals(type)) {
            loginTarget = dataStore.findAdministrator(id);
        } else {
            System.out.println(GConstant.Message.INVALID_LOGIN_TYPE);
            return currentUser;
        }

        if (loginTarget == null || !loginTarget.checkPassword(password)) {
            System.out.println(GConstant.Message.LOGIN_FAILED);
            return currentUser;
        }

        System.out.println(
                GConstant.Message.LOGIN_SUCCESS_PREFIX
                        + loginTarget.getName()
                        + GConstant.Console.USER_SEPARATOR
                        + loginTarget.getUserType()
        );
        return loginTarget;
    }

    private static void openShop(Scanner scanner, User currentUser, Shop shop) {
        boolean shopping = true;

        while (shopping) {
            System.out.println();
            System.out.println(GConstant.Console.SHOP_MENU_TITLE);
            System.out.println(GConstant.Console.SHOP_MENU_SHOW_GAME_LIST);
            System.out.println(GConstant.Console.SHOP_MENU_SEARCH_GAME);
            System.out.println(GConstant.Console.SHOP_MENU_SHOW_GAME_DETAIL);
            System.out.println(GConstant.Console.SHOP_MENU_ADD_TO_CART);
            System.out.println(GConstant.Console.SHOP_MENU_BACK);
            System.out.print(GConstant.Console.SELECT_MENU_PROMPT);

            String input = scanner.nextLine();
            System.out.println();

            switch (input) {
                case GConstant.ShopCommand.SHOW_GAME_LIST:
                    shop.showGameList();
                    break;

                case GConstant.ShopCommand.SEARCH_GAME:
                    searchGame(scanner, shop);
                    break;

                case GConstant.ShopCommand.SHOW_GAME_DETAIL:
                    showGameDetailInShop(scanner, currentUser, shop);
                    break;

                case GConstant.ShopCommand.ADD_TO_CART:
                    addGameToCart(scanner, currentUser, shop);
                    break;

                case GConstant.ShopCommand.BACK:
                    shopping = false;
                    break;

                default:
                    System.out.println(GConstant.Message.INVALID_MENU_NUMBER);
            }
        }
    }

    private static void searchGame(Scanner scanner, Shop shop) {
        System.out.print(GConstant.Console.SEARCH_KEYWORD_PROMPT);
        String keyword = scanner.nextLine();
        shop.showSearchResult(keyword);
    }

    private static void showGameDetailInShop(Scanner scanner, User currentUser, Shop shop) {
        Game game = selectGame(scanner, shop);
        if (game == null) {
            return;
        }

        shop.showGameDetail(game);

        if (!(currentUser instanceof MemberGamer)) {
            return;
        }

        System.out.print(GConstant.Console.ADD_TO_CART_CONFIRM_PROMPT);
        String input = scanner.nextLine();
        if (GConstant.ConfirmationCommand.YES.equals(input)) {
            shop.addGameToCart((MemberGamer) currentUser, game);
        }
    }

    private static void addGameToCart(Scanner scanner, User currentUser, Shop shop) {
        if (!(currentUser instanceof MemberGamer)) {
            System.out.println(GConstant.Message.SHOP_CART_MEMBER_ONLY);
            return;
        }

        Game game = selectGame(scanner, shop);
        if (game == null) {
            return;
        }

        shop.addGameToCart((MemberGamer) currentUser, game);
    }

    private static void checkoutCart(Scanner scanner, User currentUser, DataStore dataStore) {
        if (!(currentUser instanceof MemberGamer)) {
            System.out.println(GConstant.Message.CART_MEMBER_ONLY);
            return;
        }

        MemberGamer gamer = (MemberGamer) currentUser;
        List<Game> games = gamer.getCart().getGames();

        System.out.println(GConstant.Console.CART_TITLE);
        if (games.isEmpty()) {
            System.out.println(GConstant.Message.CART_EMPTY);
            return;
        }

        for (Game game : games) {
            System.out.println(
                    game.getGameId()
                            + GConstant.Console.DISPLAY_SEPARATOR
                            + game.getTitle()
                            + GConstant.Console.DISPLAY_SEPARATOR
                            + game.getPrice()
            );
        }

        System.out.print(GConstant.Console.CART_CHECKOUT_PROMPT);
        String gameId = scanner.nextLine();
        Game game = findGameInCart(gamer, gameId);

        if (game == null) {
            System.out.println(GConstant.Message.GAME_NOT_FOUND_IN_CART);
            return;
        }

        Payment payment = new Payment(
                GConstant.Payment.PAYMENT_ID_PREFIX + System.currentTimeMillis(),
                dataStore.getDefaultPaymentMethod(),
                game.getPrice()
        );

        Purchase purchase = gamer.purchase(game, payment);
        if (purchase.isCompleted()) {
            System.out.println(GConstant.Message.PURCHASE_COMPLETED);
            System.out.println(GConstant.Message.PAYMENT_METHOD_PREFIX + payment.getMethodName());
            System.out.println(GConstant.Message.AMOUNT_PREFIX + payment.getAmount());
            System.out.println(GConstant.Message.ADDED_TO_LIBRARY_PREFIX + game.getTitle());
        } else {
            System.out.println(GConstant.Message.PURCHASE_FAILED);
        }
    }

    private static Game findGameInCart(MemberGamer gamer, String gameId) {
        for (Game game : gamer.getCart().getGames()) {
            if (game.getGameId().equals(gameId)) {
                return game;
            }
        }

        return null;
    }

    private static void checkLibrary(User currentUser) {
        if (!(currentUser instanceof MemberGamer)) {
            System.out.println(GConstant.Message.LIBRARY_MEMBER_ONLY);
            return;
        }

        MemberGamer gamer = (MemberGamer) currentUser;
        List<Game> games = gamer.getLibrary().getGames();

        System.out.println(GConstant.Console.LIBRARY_TITLE);
        if (games.isEmpty()) {
            System.out.println(GConstant.Message.LIBRARY_EMPTY);
            return;
        }

        for (Game game : games) {
            System.out.println(
                    game.getGameId()
                            + GConstant.Console.DISPLAY_SEPARATOR
                            + game.getTitle()
                            + GConstant.Console.DISPLAY_SEPARATOR
                            + game.getGenre()
            );
        }
    }

    private static void showGameDetail(Scanner scanner, Shop shop) {
        Game game = selectGame(scanner, shop);
        if (game == null) {
            return;
        }

        shop.showGameDetail(game);
    }

    private static Game selectGame(Scanner scanner, Shop shop) {
        shop.showGameList();

        System.out.print(GConstant.Console.SELECT_GAME_ID_PROMPT);
        String gameId = scanner.nextLine();
        Game game = shop.findGame(gameId);

        if (game == null) {
            System.out.println(GConstant.Message.GAME_NOT_FOUND);
        }
        return game;
    }
}
