package gds;

import controller.ShopController;
import controller.UserController;
import dao.*;

import service.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


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
    @Bean public PaymentDAO paymentDAO(Connection conn) { return new PaymentDAOImpl(conn); }
    @Bean public TossPaymentGateway tossPaymentGateway() { return new TossPaymentGatewayImpl(); }

    @Bean public UserService userService(UserDAO userDAO) { return new UserService(userDAO); }
    @Bean public ShopService shopService(GameDAO gameDAO) { return new ShopService(gameDAO); }
    @Bean public CartService cartService(UserDAO userDAO, GameDAO gameDAO, CartDAO cartDAO, LibraryDAO libraryDAO) { return new CartService(userDAO, gameDAO, cartDAO, libraryDAO); }
    @Bean public PurchaseService purchaseService(UserDAO userDAO, CartDAO cartDAO, LibraryDAO libraryDAO, PurchaseDAO purchaseDAO, PaymentDAO paymentDAO, TossPaymentGateway tossPaymentGateway) { return new PurchaseService(userDAO, cartDAO, libraryDAO, purchaseDAO, paymentDAO, tossPaymentGateway); }
    @Bean public LibraryService libraryService(UserDAO userDAO, LibraryDAO libraryDAO) { return new LibraryService(userDAO, libraryDAO); }

    @Bean public UserController userController(UserService userService, LibraryService libraryService) { return new UserController(userService, libraryService); }
    @Bean public ShopController shopController(ShopService shopService, CartService cartService, PurchaseService purchaseService, GameDAO gameDAO, CartDAO cartDAO) { return new ShopController(shopService, cartService, purchaseService, gameDAO, cartDAO); }
}