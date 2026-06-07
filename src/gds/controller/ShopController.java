package controller;

import dto.ApiResponseDTO;
import dto.CartRequestDTO;
import dto.PurchaseRequestDTO;
import dto.GameResponseDTO;
import service.ShopService;
import service.CartService;
import service.PurchaseService;

import java.util.List;
import java.util.Map;

public class ShopController {
    private final ShopService shopService;
    private final CartService cartService;
    private final PurchaseService purchaseService;

    // 3명의 주방장을 모두 주입받습니다.
    public ShopController(ShopService shopService, CartService cartService, PurchaseService purchaseService) {
        this.shopService = shopService;
        this.cartService = cartService;
        this.purchaseService = purchaseService;
    }

    // [API 1. 게임 목록 전체 보기]
    // (이 API는 GET 요청이라 RequestDTO가 필요 없습니다!)
    public ApiResponseDTO<List<GameResponseDTO>> getGames() {
        try {
            List<GameResponseDTO> games = shopService.getAllGames();
            return ApiResponseDTO.success("게임 목록 조회 성공", games);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    // [API 5. 장바구니 담기 요청]
    public ApiResponseDTO<GameResponseDTO> addToCart(String userId, CartRequestDTO request) {
        try {
            GameResponseDTO addedGame = cartService.addCartItem(userId, request.getGameId());
            return ApiResponseDTO.success("장바구니에 게임이 추가되었습니다.", addedGame);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    // [API 4. 장바구니 구매 요청] (위변조 검증)
    public ApiResponseDTO<Object> purchaseCart(String userId, PurchaseRequestDTO request) {
        try {
            Map<String, Object> result = purchaseService.verifyAndPurchase(userId, request);
            return ApiResponseDTO.success("결제 검증 및 구매 처리가 완료되었습니다.", result);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }
}