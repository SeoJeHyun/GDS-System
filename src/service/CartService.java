package service;

import dao.CartDAO;
import dao.GameDAO;
import dao.LibraryDAO;
import dao.UserDAO;
import dto.GameResponseDTO;
import gds.Game;
import gds.MemberGamer;
import gds.User;

import java.util.List;
import java.util.stream.Collectors;

public class CartService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final CartDAO cartDAO;
    private final LibraryDAO libraryDAO;

    public CartService(UserDAO userDAO, GameDAO gameDAO, CartDAO cartDAO, LibraryDAO libraryDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.cartDAO = cartDAO;
        this.libraryDAO = libraryDAO;
    }

    public GameResponseDTO addCartItem(String userId, String gameId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) {
            throw new IllegalStateException("게이머 회원만 장바구니를 이용할 수 있습니다.");
        }
        MemberGamer gamer = (MemberGamer) user;
        Game game = gameDAO.findById(gameId);

        // 💡 [도메인 지능 활용] 주방장이 직접 중복 검사하지 않고, 게이머에게 도구를 주며 "네가 직접 담아!" 라고 위임합니다.
        gamer.loadCart(cartDAO);
        gamer.loadLibrary(libraryDAO);
        gamer.addGameToCart(game); // 내부에 이미 있거나 중복되면 도메인 객체가 알아서 예외를 던짐!

        return game.toDTO();
    }

    public void removeCartItem(String userId, String gameId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) {
            throw new IllegalStateException("게이머 회원만 장바구니를 이용할 수 있습니다.");
        }
        MemberGamer gamer = (MemberGamer) user;
        
        // 💡 [도메인 지능 활용] 장바구니 로드 후, 객체 스스로 삭제하도록 위임
        gamer.loadCart(cartDAO);
        gamer.getCart().removeGame(gameId); 
    }

    // [기능 추가] 장바구니 목록 조회
    public List<GameResponseDTO> getCartItems(String userId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) {
            throw new IllegalStateException("게이머 회원만 장바구니를 조회할 수 있습니다.");
        }
        MemberGamer gamer = (MemberGamer) user;
        gamer.loadCart(cartDAO);
        
        return gamer.getCart().getGames().stream().map(Game::toDTO).collect(Collectors.toList());
    }
}