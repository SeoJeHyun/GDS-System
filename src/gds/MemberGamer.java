package gds;

import dao.CartDAO;
import dao.LibraryDAO;
import dao.UserDAO;
import dto.MemberGamerDTO;
import dto.UserDTO;

public class MemberGamer extends User {

    // 최초 클래스 다이어그램에 부합하도록 Cart(장바구니) 객체를 다시 품습니다.
    private Cart cart;

    // 라이브러리 객체도 품습니다.
    private Library library;

    public MemberGamer(String userId, String password, String name, UserDAO userDAO) {
        super(userId, password, name, userDAO);
    }

    // [풍부한 도메인 모델] 외부(Service)에서 조립해주길 기다리지 않고, 도구를 받아 스스로 세팅합니다.
    public void loadCart(CartDAO cartDAO) {
        this.cart = new Cart(this.userId, cartDAO.findCartGamesByUserId(this.userId), cartDAO);
    }

    public Cart getCart() {
        return this.cart;
    }

    // [풍부한 도메인 모델] 라이브러리 역시 스스로 로드합니다.
    public void loadLibrary(LibraryDAO libraryDAO) {
        this.library = new Library(this.userId, libraryDAO.findGamesByUserId(this.userId), libraryDAO);
    }

    public Library getLibrary() {
        return this.library;
    }

    // [풍부한 도메인 모델] 장바구니 담기 위임 로직
    public void addGameToCart(Game game) {
        if (this.cart == null) {
            throw new IllegalStateException("장바구니가 아직 로드되지 않았습니다.");
        }
        if (this.library == null) {
            throw new IllegalStateException("라이브러리가 아직 로드되지 않았습니다.");
        }
        
        // 1. 자신의 내장된 라이브러리에 있는지 검증 (외부 DAO 불필요)
        if (this.library.hasGame(game.getGameId())) {
            throw new IllegalStateException("이미 라이브러리에 있는 게임입니다.");
        }
        
        // 2. 장바구니에 담으라고 지시 (Cart 내부에서 중복 검증 수행)
        this.cart.addGame(game);
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.MEMBER_GAMER;
    }

    @Override
    public UserDTO toDTO() {
        return new MemberGamerDTO(this.userId, this.name, getUserType());
    }
}