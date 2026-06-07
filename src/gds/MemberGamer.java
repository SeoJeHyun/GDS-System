package gds;

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

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setLibrary(Library library) {
        this.library = library;
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