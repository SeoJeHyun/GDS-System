package gds;

public final class GConstant {
    private GConstant() {
    }

    public static final class UserType {
        public static final String NON_MEMBER_GAMER = "NonMemberGamer";
        public static final String MEMBER_GAMER = "MemberGamer";
        public static final String DEVELOPER = "Developer";
        public static final String ADMINISTRATOR = "Administrator";

        private UserType() {
        }
    }

    public static final class Guest {
        public static final String USER_ID = "guest";
        public static final String PASSWORD = "";
        public static final String NAME = "Guest Gamer";

        private Guest() {
        }
    }

    public static final class DataPath {
        public static final String DATA_DIRECTORY = "data";
        public static final String USER_ACCOUNTS_FILE = "user_accounts.txt";
        public static final String DEVELOPER_ACCOUNTS_FILE = "developer_accounts.txt";
        public static final String ADMIN_ACCOUNTS_FILE = "admin_accounts.txt";
        public static final String GAMES_FILE = "games.txt";
        public static final String PAYMENT_METHODS_FILE = "payment_methods.txt";

        private DataPath() {
        }
    }

    public static final class DataFormat {
        public static final String COMMENT_PREFIX = "#";
        public static final String FILE_DELIMITER_REGEX = "\\|";
        public static final int KEEP_TRAILING_EMPTY_VALUES = -1;
        public static final String DEFAULT_PAYMENT_METHOD = "Default Payment";
        public static final String LOADED_FROM_DATA_FILE = "Loaded from data file";

        public static final int USER_FIELD_COUNT = 3;
        public static final int DEVELOPER_FIELD_COUNT = 4;
        public static final int ADMINISTRATOR_FIELD_COUNT = 4;
        public static final int GAME_FIELD_COUNT = 10;
        public static final int PAYMENT_METHOD_FIELD_COUNT = 2;

        public static final int FIRST_ITEM_INDEX = 0;
        public static final int USER_ID_INDEX = 0;
        public static final int PASSWORD_INDEX = 1;
        public static final int NAME_INDEX = 2;
        public static final int COMPANY_NAME_INDEX = 3;
        public static final int DEPARTMENT_INDEX = 3;

        public static final int GAME_ID_INDEX = 0;
        public static final int GAME_TITLE_INDEX = 1;
        public static final int GAME_DEVELOPER_NAME_INDEX = 2;
        public static final int GAME_PRICE_INDEX = 3;
        public static final int GAME_GENRE_INDEX = 4;
        public static final int GAME_DEMO_AVAILABLE_INDEX = 5;
        public static final int GAME_DETAIL_INDEX = 6;
        public static final int GAME_FILE_NAME_INDEX = 7;
        public static final int GAME_FILE_SIZE_INDEX = 8;
        public static final int GAME_DISTRIBUTION_STATUS_INDEX = 9;

        public static final int PAYMENT_METHOD_NAME_INDEX = 1;

        private DataFormat() {
        }
    }

    public static final class Console {
        public static final String DISPLAY_SEPARATOR = " | ";
        public static final String USER_SEPARATOR = " / ";
        public static final String FILE_SIZE_UNIT_MB = "MB";

        public static final String MAIN_MENU_TITLE = "===== GDS Console Menu =====";
        public static final String CURRENT_USER_LABEL = "Current user: ";
        public static final String MENU_LOGIN = "1. Login";
        public static final String MENU_SHOP = "2. Shop";
        public static final String MENU_CART_CHECKOUT = "3. Cart / Purchase";
        public static final String MENU_CHECK_LIBRARY = "4. Check Library";
        public static final String MENU_SHOW_GAME_DETAIL = "5. Show Game Detail";
        public static final String MENU_EXIT = "0. Exit";
        public static final String SELECT_MENU_PROMPT = "Select menu: ";

        public static final String LOGIN_TYPE_TITLE = "===== Login Type =====";
        public static final String LOGIN_TYPE_MEMBER_GAMER = "1. Member Gamer";
        public static final String LOGIN_TYPE_DEVELOPER = "2. Developer";
        public static final String LOGIN_TYPE_ADMINISTRATOR = "3. Administrator";
        public static final String SELECT_LOGIN_TYPE_PROMPT = "Select login type: ";
        public static final String ID_PROMPT = "ID: ";
        public static final String PASSWORD_PROMPT = "Password: ";

        public static final String SHOP_MENU_TITLE = "===== Shop =====";
        public static final String SHOP_MENU_SHOW_GAME_LIST = "1. Show Game List";
        public static final String SHOP_MENU_SEARCH_GAME = "2. Search Game";
        public static final String SHOP_MENU_SHOW_GAME_DETAIL = "3. Show Game Detail";
        public static final String SHOP_MENU_ADD_TO_CART = "4. Add Game to Cart";
        public static final String SHOP_MENU_BACK = "0. Back";
        public static final String SEARCH_KEYWORD_PROMPT = "Search keyword: ";
        public static final String ADD_TO_CART_CONFIRM_PROMPT = "Add this game to cart? (1. Yes / 0. No): ";

        public static final String CART_TITLE = "===== Cart =====";
        public static final String CART_CHECKOUT_PROMPT = "Select game ID to purchase: ";

        public static final String LIBRARY_TITLE = "===== Library =====";
        public static final String GAME_DETAIL_TITLE = "===== Game Detail =====";
        public static final String GAME_LIST_TITLE = "===== Game List =====";
        public static final String SEARCH_RESULT_TITLE = "===== Search Result =====";
        public static final String SELECT_GAME_ID_PROMPT = "Select game ID: ";

        public static final String GAME_ID_LABEL = "ID: ";
        public static final String GAME_TITLE_LABEL = "Title: ";
        public static final String GAME_DEVELOPER_LABEL = "Developer: ";
        public static final String GAME_PRICE_LABEL = "Price: ";
        public static final String GAME_GENRE_LABEL = "Genre: ";
        public static final String GAME_DEMO_AVAILABLE_LABEL = "Demo Available: ";
        public static final String GAME_DETAIL_LABEL = "Detail: ";
        public static final String GAME_FILE_LABEL = "File: ";
        public static final String GAME_FILE_SIZE_LABEL = "File Size: ";
        public static final String GAME_DISTRIBUTION_STATUS_LABEL = "Distribution Status: ";

        private Console() {
        }
    }

    public static final class MenuCommand {
        public static final String LOGIN = "1";
        public static final String SHOP = "2";
        public static final String CART_CHECKOUT = "3";
        public static final String CHECK_LIBRARY = "4";
        public static final String SHOW_GAME_DETAIL = "5";
        public static final String EXIT = "0";

        private MenuCommand() {
        }
    }

    public static final class ShopCommand {
        public static final String SHOW_GAME_LIST = "1";
        public static final String SEARCH_GAME = "2";
        public static final String SHOW_GAME_DETAIL = "3";
        public static final String ADD_TO_CART = "4";
        public static final String BACK = "0";

        private ShopCommand() {
        }
    }

    public static final class LoginTypeCommand {
        public static final String MEMBER_GAMER = "1";
        public static final String DEVELOPER = "2";
        public static final String ADMINISTRATOR = "3";

        private LoginTypeCommand() {
        }
    }

    public static final class ConfirmationCommand {
        public static final String YES = "1";
        public static final String NO = "0";

        private ConfirmationCommand() {
        }
    }

    public static final class Message {
        public static final String PROGRAM_FINISHED = "Program finished.";
        public static final String INVALID_MENU_NUMBER = "Invalid menu number.";
        public static final String INVALID_LOGIN_TYPE = "Invalid login type.";
        public static final String LOGIN_FAILED = "Login failed.";
        public static final String LOGIN_SUCCESS_PREFIX = "Login success: ";
        public static final String PURCHASE_MEMBER_ONLY = "Only a logged-in member gamer can purchase a game.";
        public static final String CART_MEMBER_ONLY = "Only a logged-in member gamer can use a cart.";
        public static final String SHOP_CART_MEMBER_ONLY = "Only a logged-in member gamer can add a game to the cart.";
        public static final String GAME_ALREADY_IN_LIBRARY = "This game is already in your library.";
        public static final String GAME_ALREADY_IN_CART = "This game is already in your cart.";
        public static final String ADDED_TO_CART_PREFIX = "Added to cart: ";
        public static final String PURCHASE_COMPLETED = "Purchase completed.";
        public static final String PAYMENT_METHOD_PREFIX = "Payment method: ";
        public static final String AMOUNT_PREFIX = "Amount: ";
        public static final String ADDED_TO_LIBRARY_PREFIX = "Added to library: ";
        public static final String PURCHASE_FAILED = "Purchase failed.";
        public static final String LIBRARY_MEMBER_ONLY = "Only a logged-in member gamer can check a library.";
        public static final String LIBRARY_EMPTY = "Library is empty.";
        public static final String CART_EMPTY = "Cart is empty.";
        public static final String SEARCH_RESULT_EMPTY = "No matching games.";
        public static final String GAME_NOT_FOUND = "Game not found.";
        public static final String GAME_NOT_FOUND_IN_CART = "Game not found in cart.";
        public static final String CANNOT_READ_DATA_FILE_PREFIX = "Cannot read data file: ";
        public static final String INVALID_DATA_FORMAT_PREFIX = "Invalid data format in ";
        public static final String INVALID_DATA_FORMAT_SEPARATOR = ": ";

        private Message() {
        }
    }

    public static final class Payment {
        public static final String PAYMENT_ID_PREFIX = "payment-";
        public static final boolean INITIAL_PAID = false;
        public static final boolean PAID = true;

        private Payment() {
        }
    }

    public static final class Purchase {
        public static final boolean INITIAL_COMPLETED = false;

        private Purchase() {
        }
    }
}
