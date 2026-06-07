package gds;

import controller.ShopController;
import controller.UserController;
import dao.*;
import dto.CartRequestDTO;
import dto.LoginRequestDTO;
import dto.PurchaseRequestDTO;
import dto.RegisterRequestDTO;
import service.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("🚀 스프링 부트 서버가 8080 포트에서 성공적으로 부팅되었습니다!");
    }

    // ==========================================
    // 🏗️ 스프링 부트용 의존성 주입(Bean) 공장
    // ==========================================
    @Bean
    public Connection connection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gds_db";
        String dbUser = "root";
        String dbPassword = "seo18220324"; 
        Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
        System.out.println("✅ 스프링 빈: DB 통신망(Connection) 연결 성공!");
        return conn;
    }

    @Bean public GameDAO gameDAO(Connection conn) { return new GameDAOImpl(conn); }
    @Bean public UserDAO userDAO(Connection conn) { return new UserDAOImpl(conn); }
    @Bean public CartDAO cartDAO(Connection conn, GameDAO gameDAO) { return new CartDAOImpl(conn, gameDAO); }
    @Bean public LibraryDAO libraryDAO(Connection conn, GameDAO gameDAO) { return new LibraryDAOImpl(conn, gameDAO); }
    @Bean public PurchaseDAO purchaseDAO(Connection conn, GameDAO gameDAO) { return new PurchaseDAOImpl(conn, gameDAO); }

    @Bean public UserService userService(UserDAO userDAO) { return new UserService(userDAO); }
    @Bean public ShopService shopService(GameDAO gameDAO) { return new ShopService(gameDAO); }
    @Bean public CartService cartService(UserDAO userDAO, GameDAO gameDAO, CartDAO cartDAO, LibraryDAO libraryDAO) { return new CartService(userDAO, gameDAO, cartDAO, libraryDAO); }
    @Bean public PurchaseService purchaseService(UserDAO userDAO, CartDAO cartDAO, LibraryDAO libraryDAO, PurchaseDAO purchaseDAO) { return new PurchaseService(userDAO, cartDAO, libraryDAO, purchaseDAO); }

    @Bean public UserController userController(UserService userService) { return new UserController(userService); }
    @Bean public ShopController shopController(ShopService shopService, CartService cartService, PurchaseService purchaseService) { return new ShopController(shopService, cartService, purchaseService); }

    // ==========================================
    // 🌐 스프링 부트 REST API 엔드포인트
    // ==========================================
    @RestController
    public static class SpringWebController {
        private final UserController userController;
        private final ShopController shopController;
        private final GameDAO gameDAO;
        private final CartDAO cartDAO;
        private final LibraryDAO libraryDAO;
        private final PurchaseDAO purchaseDAO;

        // 💡 스프링이 위에서 만든 DAO 빈(Bean)들도 알아서 주입해 줍니다!
        public SpringWebController(UserController userController, ShopController shopController,
                                   GameDAO gameDAO, CartDAO cartDAO, LibraryDAO libraryDAO, PurchaseDAO purchaseDAO) {
            this.userController = userController;
            this.shopController = shopController;
            this.gameDAO = gameDAO;
            this.cartDAO = cartDAO;
            this.libraryDAO = libraryDAO;
            this.purchaseDAO = purchaseDAO;
        }

        // ==========================================
        // 👤 유저 관련 API (apiInstruction.md)
        // ==========================================

        // [유저 4] 회원가입
        @PostMapping("/api/register")
        public Object register(@RequestBody RegisterRequestDTO req) {
            return userController.register(req);
        }

        // [유저 3] 로그인
        @PostMapping("/api/login")
        public Object login(@RequestBody LoginRequestDTO req) {
            return userController.login(req);
        }

        // [유저 2] 라이브러리(구매한 게임) 목록 조회
        @GetMapping("/api/users/{userId}/library")
        public Object getLibrary(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token,
                @PathVariable String userId,
                @RequestParam(defaultValue = "1") int page,
                @RequestParam(defaultValue = "100") int size) {
            
            // 💡 DAO를 통해 실제 DB에서 유저의 라이브러리(구매 내역)를 조회!
            List<Game> libGames = libraryDAO.findGamesByUserId(userId);
            List<Map<String, Object>> data = libGames.stream().map(g -> Map.of(
                "id", g.getGameId(),
                "title", g.getTitle(),
                "developer", g.getDeveloperName(),
                "genre", List.of(g.getGenre()),
                "price", g.getPrice(),
                "demo", g.isDemoAvailable() ? "available" : "none",
                "distributionStatus", g.getDeploymentStatus(),
                "isDownloadable", true,
                "downloadUrl", "/api/users/" + userId + "/library/" + g.getGameId() + "/download"
            )).toList();

            return Map.of(
                "status", "success",
                "message", libGames.isEmpty() ? "구매한 게임이 없습니다." : "라이브러리 조회 성공",
                "totalCount", libGames.size(),
                "page", page,
                "size", size,
                "data", data
            );
        }

        // [유저 6 / 게임 7] 라이브러리 게임 다운로드 요청
        @GetMapping("/api/users/{userId}/library/{gameId}/download")
        public ResponseEntity<String> downloadGame(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token,
                @PathVariable String userId,
                @PathVariable String gameId) {
            
            // 문서 명세에 맞춘 임시 txt 파일 생성 반환
            String fileContent = "Game ID: " + gameId + "\nTitle: 임시 다운로드 파일\nDeveloper: Sample Dev Studio\nGenre: RPG, horror\nDescription: 게임 설명입니다.";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + gameId + ".txt\"")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fileContent);
        }

        // ==========================================
        // 🎮 게임 & 상점 관련 API (apiInstruction.md)
        // ==========================================

        // [게임 1] 게임 목록 전체 보기
        @GetMapping("/api/games")
        public Object getGames(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token) {
            return shopController.getGames();
        }

        // [게임 2] 게임 검색
        @GetMapping("/api/games/search")
        public Object searchGames(
                @RequestParam String keyword) {
            // 💡 DB에서 실제로 게임 제목 키워드로 검색!
            List<Game> games = gameDAO.findByTitleContaining(keyword);
            List<Map<String, Object>> data = games.stream().map(g -> Map.of(
                "id", g.getGameId(), "title", g.getTitle(), "developer", g.getDeveloperName(),
                "genre", List.of(g.getGenre()), "price", g.getPrice(),
                "demo", g.isDemoAvailable() ? "available" : "none",
                "distributionStatus", g.getDeploymentStatus()
            )).toList();
            
            return Map.of("status", "success", "message", games.isEmpty() ? "검색 결과가 없습니다." : "게임 검색 성공", "totalCount", games.size(), "data", data);
        }

        // [게임 3] 게임 상세 정보
        @GetMapping("/api/games/{gameId}")
        public Object getGameDetail(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token,
                @PathVariable String gameId) {
            // 💡 특정 게임 1개의 모든 정보를 상세 조회!
            Game g = gameDAO.findById(gameId);
            if (g == null) return Map.of("status", "fail", "message", "게임을 찾을 수 없습니다.", "data", null);

            return Map.of(
                "id", g.getGameId(), "developer", g.getDeveloperName(),
                "genre", List.of(g.getGenre()), "price", g.getPrice(),
                "isDemoAvailable", g.isDemoAvailable(),
                "description", g.getDetail() != null ? g.getDetail() : "",
                "fileSizeGb", g.getFileSizeMb() / 1024.0,
                "distributionStatus", g.getDeploymentStatus()
            );
        }

        // [게임 5] 장바구니 담기 요청
        @PostMapping("/api/cart")
        public Object addToCart(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token,
                @RequestBody CartRequestDTO req) {
            
            // 💡 세션/토큰 인증 기능이 완성되기 전까지 'user' 계정으로 하드코딩
            String userId = "user"; 
            return shopController.addToCart(userId, req);
        }

        // [게임 6] 장바구니 삭제 요청
        @DeleteMapping("/api/cart/{gameId}")
        public Object removeFromCart(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token,
                @PathVariable String gameId) {
            
            String userId = "user"; // 💡 세션 인증 전까지 하드코딩
            cartDAO.delete(userId, gameId);
            return Map.of("status", "success", "message", "장바구니에서 게임이 삭제되었습니다.", "data", Map.of("id", gameId));
        }

        // [게임 4] 장바구니 구매 (결제 검증) 요청
        @PostMapping("/api/purchase")
        public Object purchaseCart(
                @RequestHeader(value = "Session-Id", required = false) String sessionId,
                @RequestHeader(value = "Authorization", required = false) String token,
                @RequestBody PurchaseRequestDTO req) {
            
            String userId = "user"; // 💡 세션 인증 전까지 하드코딩
            List<Game> cartGames = cartDAO.findCartGamesByUserId(userId);
            
            if (cartGames.isEmpty()) {
                return Map.of("status", "fail", "message", "장바구니가 비어 있습니다.", "data", null);
            }
            
            int totalAmount = cartGames.stream().mapToInt(Game::getPrice).sum();
            
            // 1. 장바구니의 모든 게임을 진짜 '라이브러리'에 영구 등록!
            for (Game g : cartGames) {
                libraryDAO.addGame(userId, g.getGameId());
            }
            // 2. 장바구니 비우기
            cartDAO.deleteAll(userId);
            
            // 3. 결제 내역(Purchase)을 영수증으로 DB에 박제
            String orderId = "order_" + System.currentTimeMillis();
            Purchase purchase = new Purchase(orderId, userId, totalAmount, "PAID", cartGames, purchaseDAO);
            purchaseDAO.save(purchase);
            
            List<Map<String, Object>> purchasedData = cartGames.stream().map(g -> Map.<String, Object>of(
                "id", g.getGameId(), "title", g.getTitle(), "price", g.getPrice()
            )).toList();
            
            return Map.of(
                "status", "success", "message", "결제 검증 및 구매 처리가 완료되었습니다.",
                "data", Map.of(
                    "transactionId", "tx_" + System.currentTimeMillis(),
                    "orderId", orderId,
                    "totalPaidAmount", totalAmount,
                    "purchaseDate", java.time.Instant.now().toString(),
                    "purchasedGames", purchasedData
                )
            );
        }
    }
}