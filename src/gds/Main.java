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

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 GDS 백엔드 서버 부팅 중...");

        // 1. DB 연결 통신망 확보
        // [내 컴퓨터 DB 사용 시]
        String url = "jdbc:mysql://localhost:3306/gds_db"; // 💡 포트 3306과 mysql로 변경!
        String dbUser = "root";          // 💡 MySQL의 기본 최고 관리자 아이디는 보통 root입니다.
        String dbPassword = "seo18220324";      // 💡 본인의 MySQL 비밀번호로 변경해주세요!

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            System.out.println("✅ DB 통신망(Connection) 연결 성공!\n");

            // ==========================================
            // 🏗️ 백엔드 의존성 주입(DI) 조립 공장 가동
            // ==========================================
            
            // 2. 창고지기(DAO) 고용 및 통신망(Connection) 쥐여주기
            GameDAO gameDAO = new GameDAOImpl(conn);
            UserDAO userDAO = new UserDAOImpl(conn);
            CartDAO cartDAO = new CartDAOImpl(conn, gameDAO);
            LibraryDAO libraryDAO = new LibraryDAOImpl(conn, gameDAO);
            PurchaseDAO purchaseDAO = new PurchaseDAOImpl(conn, gameDAO);

            // 3. 주방장(Service) 고용 및 창고지기 쥐여주기
            UserService userService = new UserService(userDAO);
            ShopService shopService = new ShopService(gameDAO);
            CartService cartService = new CartService(userDAO, gameDAO, cartDAO, libraryDAO);
            PurchaseService purchaseService = new PurchaseService(userDAO, cartDAO, libraryDAO, purchaseDAO);

            // 4. 문지기(Controller) 고용 및 주방장 쥐여주기
            UserController userController = new UserController(userService);
            ShopController shopController = new ShopController(shopService, cartService, purchaseService);

            System.out.println("✅ 백엔드 아키텍처 조립 완료! 프론트엔드 요청 대기 중...\n");

            // ==========================================
            // 🎬 [시뮬레이션] 프론트엔드 API 요청 테스트
            // ==========================================
            
            System.out.println("--- 🧑‍💻 [프론트엔드] API 4. 회원가입 요청 ---");
            RegisterRequestDTO regReq = new RegisterRequestDTO("tester1", "1234", "테스트유저");
            var regRes = userController.register(regReq);
            System.out.println("응답: " + regRes.getMessage() + " / 상태: " + regRes.getStatus());

            System.out.println("\n--- 🧑‍💻 [프론트엔드] API 3. 로그인 요청 ---");
            LoginRequestDTO loginReq = new LoginRequestDTO("tester1", "1234");
            var loginRes = userController.login(loginReq);
            System.out.println("응답: " + loginRes.getMessage() + " / 접속 유저: " + (loginRes.getData() != null ? loginRes.getData().getName() : "없음"));

            System.out.println("\n--- 🧑‍💻 [프론트엔드] API 1. 게임 목록 조회 요청 ---");
            var gamesRes = shopController.getGames();
            System.out.println("응답: " + gamesRes.getMessage() + " / 불러온 게임 수: " + (gamesRes.getData() != null ? gamesRes.getData().size() : 0));

            // (주의: DB에 게임이 존재해야 아래 장바구니/결제 테스트가 정상 작동합니다!)
            System.out.println("\n--- 🧑‍💻 [프론트엔드] API 5. 장바구니 담기 요청 ---");
            CartRequestDTO cartReq = new CartRequestDTO("game1"); // 💡 DB에 실제로 존재하는 게임 ID로 변경!
            var cartRes = shopController.addToCart("tester1", cartReq);
            System.out.println("응답: " + cartRes.getMessage());

        } catch (SQLException e) {
            System.err.println("\n❌ DB 연결 실패: MySQL 서버가 켜져 있는지, ID/PW가 맞는지 확인하세요!");
            System.err.println("에러 메시지: " + e.getMessage());
        }
    }
}