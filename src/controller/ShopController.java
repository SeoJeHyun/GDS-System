package controller;

import dao.CartDAO;
import dao.GameDAO;
import dto.ApiResponseDTO;
import dto.CartRequestDTO;
import dto.PurchaseRequestDTO;
import dto.GameResponseDTO;
import gds.Game;
import org.springframework.web.bind.annotation.*;
import service.ShopService;
import service.CartService;
import service.PurchaseService;

import java.util.List;
import java.util.Map;

@RestController
public class ShopController {
    private final ShopService shopService;
    private final CartService cartService;
    private final PurchaseService purchaseService;
    private final GameDAO gameDAO;
    private final CartDAO cartDAO;

    public ShopController(ShopService shopService, CartService cartService, PurchaseService purchaseService, GameDAO gameDAO, CartDAO cartDAO) {
        this.shopService = shopService;
        this.cartService = cartService;
        this.purchaseService = purchaseService;
        this.gameDAO = gameDAO;
        this.cartDAO = cartDAO;
    }

    @GetMapping("/api/games")
    public ApiResponseDTO<List<GameResponseDTO>> getGames() {
        try {
            List<GameResponseDTO> games = shopService.getAllGames();
            return ApiResponseDTO.success("게임 목록 조회 성공", games);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    @GetMapping("/api/games/search")
    public ApiResponseDTO<List<GameResponseDTO>> searchGames(@RequestParam String keyword) {
        List<GameResponseDTO> games = shopService.searchGames(keyword);
        if (games.isEmpty()) return ApiResponseDTO.fail("검색 결과가 없습니다.");
        return ApiResponseDTO.success("게임 검색 성공", games);
    }

    @GetMapping("/api/games/{gameId}")
    public ApiResponseDTO<GameResponseDTO> getGameDetail(@PathVariable String gameId) {
        Game g = shopService.getGameDetail(gameId);
        if (g == null) return ApiResponseDTO.fail("게임을 찾을 수 없습니다.");
        return ApiResponseDTO.success("게임 상세 조회 성공", g.toDTO()); // 💡 도메인 객체 대신 안전한 DTO로 포장
    }

    @GetMapping("/api/cart")
    public ApiResponseDTO<List<GameResponseDTO>> getCart(@RequestParam String userId) {
        try {
            List<GameResponseDTO> cartItems = cartService.getCartItems(userId);
            return ApiResponseDTO.success("장바구니 조회 성공", cartItems);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    @PostMapping("/api/cart")
    public ApiResponseDTO<GameResponseDTO> addToCart(@RequestBody CartRequestDTO request) {
        String userId = request.getUserId(); // 💡 임시: 프론트에서 보낸 userId 사용
        try {
            GameResponseDTO addedGame = cartService.addCartItem(userId, request.getGameId());
            return ApiResponseDTO.success("장바구니에 게임이 추가되었습니다.", addedGame);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    @DeleteMapping("/api/cart/{gameId}")
    public ApiResponseDTO<Object> removeFromCart(@PathVariable String gameId, @RequestParam String userId) {
        try {
            // 💡 문지기가 창고지기(DAO)를 직접 부르지 않고, 지휘자(Service)에게 위임합니다.
            cartService.removeCartItem(userId, gameId);
            return ApiResponseDTO.success("장바구니에서 게임이 삭제되었습니다.", Map.of("id", gameId));
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    @PostMapping("/api/purchase")
    public ApiResponseDTO<Object> purchaseCart(@RequestBody PurchaseRequestDTO request) {
        String userId = request.getUserId(); // 💡 임시: 프론트에서 보낸 userId 사용
        try {
            Map<String, Object> result = purchaseService.verifyAndPurchase(userId, request);
            return ApiResponseDTO.success("결제 검증 및 구매 처리가 완료되었습니다.", result);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }
}