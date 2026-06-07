USE gds_db;

-- =========================================================
-- 1. Table Creations
-- =========================================================

CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    user_type VARCHAR(30) NOT NULL CHECK (
        user_type IN ('MEMBER_GAMER', 'DEVELOPER', 'ADMINISTRATOR')
    )
);

CREATE TABLE member_gamers (
    user_id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(100),
    is_logged_in BOOLEAN NOT NULL DEFAULT FALSE,
    join_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    penalty_count INTEGER NOT NULL DEFAULT 0 CHECK (penalty_count >= 0),

    CONSTRAINT fk_member_gamers_users
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE developers (
    user_id VARCHAR(50) PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    business_address VARCHAR(255),
    bank_account VARCHAR(100),
    email VARCHAR(100),

    CONSTRAINT fk_developers_users
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE administrators (
    user_id VARCHAR(50) PRIMARY KEY,
    department VARCHAR(100) NOT NULL,

    CONSTRAINT fk_administrators_users
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE deployment_statuses (
    status_name VARCHAR(50) PRIMARY KEY,
    description TEXT
);

CREATE TABLE game_files (
    file_id INT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_size_mb INTEGER NOT NULL CHECK (file_size_mb >= 0),
    file_path VARCHAR(255),
    upload_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    version VARCHAR(50)
);

CREATE TABLE games (
    game_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    developer_id VARCHAR(50),
    developer_name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    genre VARCHAR(50) NOT NULL,
    demo_available BOOLEAN NOT NULL,
    detail TEXT,
    age_rating INTEGER CHECK (age_rating IS NULL OR age_rating >= 0),
    file_id INTEGER NOT NULL,
    deployment_status VARCHAR(50) NOT NULL,

    CONSTRAINT fk_games_developers
        FOREIGN KEY (developer_id)
        REFERENCES developers(user_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_games_game_files
        FOREIGN KEY (file_id)
        REFERENCES game_files(file_id),

    CONSTRAINT fk_games_deployment_statuses
        FOREIGN KEY (deployment_status)
        REFERENCES deployment_statuses(status_name)
);

CREATE TABLE shops (
    shop_id VARCHAR(50) PRIMARY KEY,
    shop_name VARCHAR(100) NOT NULL
);

CREATE TABLE shop_games (
    shop_id VARCHAR(50) NOT NULL,
    game_id VARCHAR(50) NOT NULL,

    PRIMARY KEY (shop_id, game_id),

    CONSTRAINT fk_shop_games_shops
        FOREIGN KEY (shop_id)
        REFERENCES shops(shop_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_shop_games_games
        FOREIGN KEY (game_id)
        REFERENCES games(game_id)
        ON DELETE CASCADE
);

CREATE TABLE carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL UNIQUE,

    CONSTRAINT fk_carts_member_gamers
        FOREIGN KEY (user_id)
        REFERENCES member_gamers(user_id)
        ON DELETE CASCADE
);

CREATE TABLE cart_items (
    cart_id INTEGER NOT NULL,
    game_id VARCHAR(50) NOT NULL,
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (cart_id, game_id),

    CONSTRAINT fk_cart_items_carts
        FOREIGN KEY (cart_id)
        REFERENCES carts(cart_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cart_items_games
        FOREIGN KEY (game_id)
        REFERENCES games(game_id)
        ON DELETE CASCADE
);

CREATE TABLE libraries (
    library_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL UNIQUE,

    CONSTRAINT fk_libraries_member_gamers
        FOREIGN KEY (user_id)
        REFERENCES member_gamers(user_id)
        ON DELETE CASCADE
);

CREATE TABLE library_games (
    library_id INTEGER NOT NULL,
    game_id VARCHAR(50) NOT NULL,
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (library_id, game_id),

    CONSTRAINT fk_library_games_libraries
        FOREIGN KEY (library_id)
        REFERENCES libraries(library_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_library_games_games
        FOREIGN KEY (game_id)
        REFERENCES games(game_id)
        ON DELETE CASCADE
);

CREATE TABLE payment_methods (
    payment_method_id VARCHAR(50) PRIMARY KEY,
    method_name VARCHAR(100) NOT NULL
);

CREATE TABLE purchases (
    purchase_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    purchase_status VARCHAR(30) NOT NULL DEFAULT 'READY' CHECK (
        purchase_status IN ('READY', 'PAID', 'FAILED', 'CANCELED')
    ),
    total_amount INTEGER NOT NULL CHECK (total_amount >= 0),

    CONSTRAINT fk_purchases_member_gamers
        FOREIGN KEY (user_id)
        REFERENCES member_gamers(user_id)
        ON DELETE CASCADE
);

CREATE TABLE purchase_games (
    purchase_id VARCHAR(50) NOT NULL,
    game_id VARCHAR(50) NOT NULL,
    price_at_purchase INTEGER NOT NULL CHECK (price_at_purchase >= 0),

    PRIMARY KEY (purchase_id, game_id),

    CONSTRAINT fk_purchase_games_purchases
        FOREIGN KEY (purchase_id)
        REFERENCES purchases(purchase_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_purchase_games_games
        FOREIGN KEY (game_id)
        REFERENCES games(game_id)
);

CREATE TABLE payments (
    payment_id VARCHAR(50) PRIMARY KEY,
    purchase_id VARCHAR(50) NOT NULL UNIQUE,
    payment_method_id VARCHAR(50) NOT NULL,
    order_no VARCHAR(100),
    card_company VARCHAR(100),
    card_number VARCHAR(100),
    amount INTEGER NOT NULL CHECK (amount >= 0),
    payment_status VARCHAR(30) NOT NULL DEFAULT 'READY' CHECK (
        payment_status IN ('READY', 'PAID', 'FAILED', 'CANCELED')
    ),
    paid_at TIMESTAMP,

    CONSTRAINT fk_payments_purchases
        FOREIGN KEY (purchase_id)
        REFERENCES purchases(purchase_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_payments_payment_methods
        FOREIGN KEY (payment_method_id)
        REFERENCES payment_methods(payment_method_id)
);

CREATE TABLE reviews (
    review_id VARCHAR(50) PRIMARY KEY,
    game_id VARCHAR(50) NOT NULL,
    author_id VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    write_date DATE NOT NULL DEFAULT (CURRENT_DATE),

    CONSTRAINT fk_reviews_games
        FOREIGN KEY (game_id)
        REFERENCES games(game_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_reviews_member_gamers
        FOREIGN KEY (author_id)
        REFERENCES member_gamers(user_id)
        ON DELETE CASCADE
);

CREATE TABLE reports (
    report_id VARCHAR(50) PRIMARY KEY,
    reporter_id VARCHAR(50) NOT NULL,
    target_id VARCHAR(50) NOT NULL,
    target_type VARCHAR(30) NOT NULL CHECK (
        target_type IN ('GAME', 'REVIEW', 'USER', 'DEVELOPER')
    ),
    reason TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (
        status IN ('PENDING', 'PROCESSING', 'APPROVED', 'REJECTED')
    ),
    report_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reports_users
        FOREIGN KEY (reporter_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE gds_staff (
    staff_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    access_level VARCHAR(50) NOT NULL
);

-- Indexes
CREATE INDEX idx_games_title ON games(title);
CREATE INDEX idx_games_genre ON games(genre);
CREATE INDEX idx_games_developer_name ON games(developer_name);
CREATE INDEX idx_shop_games_game_id ON shop_games(game_id);
CREATE INDEX idx_cart_items_game_id ON cart_items(game_id);
CREATE INDEX idx_library_games_game_id ON library_games(game_id);
CREATE INDEX idx_purchases_user_id ON purchases(user_id);
CREATE INDEX idx_purchase_games_game_id ON purchase_games(game_id);
CREATE INDEX idx_payments_status ON payments(payment_status);
CREATE INDEX idx_reviews_game_id ON reviews(game_id);
CREATE INDEX idx_reports_status ON reports(status);


-- =========================================================
-- 2. GDS Initial Data (data.sql)
-- =========================================================

-- 1. Users
INSERT INTO users (user_id, password, name, user_type)
VALUES
    ('user', 'user', 'Sample User', 'MEMBER_GAMER'),
    ('dev', 'dev', 'Sample Developer', 'DEVELOPER'),
    ('admin', 'admin', 'Sample Administrator', 'ADMINISTRATOR')
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    name = VALUES(name),
    user_type = VALUES(user_type);

-- 2. Member Gamer
INSERT INTO member_gamers (user_id, email, is_logged_in, join_date, penalty_count)
VALUES ('user', 'user@gds.com', FALSE, CURRENT_DATE, 0)
ON DUPLICATE KEY UPDATE
    email = VALUES(email),
    is_logged_in = VALUES(is_logged_in),
    penalty_count = VALUES(penalty_count);

-- 3. Developer
INSERT INTO developers (user_id, company_name, business_address, bank_account, email)
VALUES ('dev', 'Sample Dev Studio', 'Sample Business Address', 'Sample Bank Account', 'dev@gds.com')
ON DUPLICATE KEY UPDATE
    company_name = VALUES(company_name),
    business_address = VALUES(business_address),
    bank_account = VALUES(bank_account),
    email = VALUES(email);

-- 4. Administrator
INSERT INTO administrators (user_id, department)
VALUES ('admin', 'GDS Management Team')
ON DUPLICATE KEY UPDATE
    department = VALUES(department);

-- 5. Deployment Status
INSERT INTO deployment_statuses (status_name, description)
VALUES
    ('RELEASED', 'Officially released game.'),
    ('EARLY_ACCESS', 'Early access game.'),
    ('OFFICIAL_RELEASE', 'Official release status.'),
    ('DISCONTINUED', 'Discontinued game.'),
    ('PENDING_APPROVAL', 'Waiting for administrator approval.')
ON DUPLICATE KEY UPDATE
    description = VALUES(description);

-- 6. Game Files
INSERT INTO game_files (file_id, file_name, file_size_mb, file_path, upload_date, version)
VALUES
    (1, 'game1.exe', 500, '/games/game1/game1.exe', CURRENT_DATE, '1.0.0'),
    (2, 'game2.exe', 650, '/games/game2/game2.exe', CURRENT_DATE, '1.0.0'),
    (3, 'game3.exe', 1200, '/games/game3/game3.exe', CURRENT_DATE, '1.0.0'),
    (4, 'game4.exe', 900, '/games/game4/game4.exe', CURRENT_DATE, '1.0.0'),
    (5, 'game5.exe', 750, '/games/game5/game5.exe', CURRENT_DATE, '1.0.0')
ON DUPLICATE KEY UPDATE
    file_name = VALUES(file_name),
    file_size_mb = VALUES(file_size_mb),
    file_path = VALUES(file_path),
    upload_date = VALUES(upload_date),
    version = VALUES(version);

-- 7. Games
INSERT INTO games (game_id, title, developer_id, developer_name, price, genre, demo_available, detail, age_rating, file_id, deployment_status)
VALUES
    ('game1', 'Game One', 'dev', 'Sample Dev Studio', 10000, 'Action', TRUE, 'First sample action game.', 12, 1, 'RELEASED'),
    ('game2', 'Game Two', 'dev', 'Sample Dev Studio', 15000, 'Adventure', TRUE, 'Second sample adventure game.', 12, 2, 'RELEASED'),
    ('game3', 'Game Three', 'dev', 'Sample Dev Studio', 20000, 'RPG', FALSE, 'Third sample RPG game.', 15, 3, 'RELEASED'),
    ('game4', 'Game Four', 'dev', 'Sample Dev Studio', 25000, 'Simulation', FALSE, 'Fourth sample simulation game.', 12, 4, 'RELEASED'),
    ('game5', 'Game Five', 'dev', 'Sample Dev Studio', 30000, 'Strategy', TRUE, 'Fifth sample strategy game.', 12, 5, 'RELEASED')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    developer_id = VALUES(developer_id),
    developer_name = VALUES(developer_name),
    price = VALUES(price),
    genre = VALUES(genre),
    demo_available = VALUES(demo_available),
    detail = VALUES(detail),
    age_rating = VALUES(age_rating),
    file_id = VALUES(file_id),
    deployment_status = VALUES(deployment_status);

-- 8. Shop
INSERT INTO shops (shop_id, shop_name)
VALUES ('shop1', 'GDS Main Shop')
ON DUPLICATE KEY UPDATE
    shop_name = VALUES(shop_name);

-- 9. Shop Games (INSERT IGNORE 활용)
INSERT IGNORE INTO shop_games (shop_id, game_id)
VALUES
    ('shop1', 'game1'),
    ('shop1', 'game2'),
    ('shop1', 'game3'),
    ('shop1', 'game4'),
    ('shop1', 'game5');

-- 10. Cart
INSERT INTO carts (cart_id, user_id)
VALUES (1, 'user')
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id);

-- 11. Library
INSERT INTO libraries (library_id, user_id)
VALUES (1, 'user')
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id);

-- 12. Payment Methods
INSERT INTO payment_methods (payment_method_id, method_name)
VALUES
    ('payment1', 'Sample Card'),
    ('payment2', 'Sample Point')
ON DUPLICATE KEY UPDATE
    method_name = VALUES(method_name);

-- 13. GDS Staff
INSERT INTO gds_staff (staff_id, name, access_level)
VALUES ('staff1', 'Sample Staff', 'ADMIN')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    access_level = VALUES(access_level);


-- =========================================================
-- 3. Table Structure Modifications (ALTER & UPDATE)
-- =========================================================

-- 1. games에 새 컬럼 추가
ALTER TABLE games ADD COLUMN status_name VARCHAR(50);
ALTER TABLE games ADD COLUMN file_path VARCHAR(255);

-- 2. 기존 deployment_status 값을 status_name으로 복사
UPDATE games
SET status_name = deployment_status
WHERE status_name IS NULL;

-- 3. 기존 game_files.file_path 값을 games.file_path로 복사 (MySQL JOIN UPDATE 문법)
UPDATE games g
JOIN game_files gf ON g.file_id = gf.file_id
SET g.file_path = gf.file_path;

-- 4. status_name NOT NULL 설정
ALTER TABLE games MODIFY COLUMN status_name VARCHAR(50) NOT NULL;

-- 5. 외래키 제거 (MySQL 전용 문법)
ALTER TABLE games DROP FOREIGN KEY fk_games_game_files;
ALTER TABLE games DROP FOREIGN KEY fk_games_deployment_statuses;

-- 6. 기존 컬럼 제거
ALTER TABLE games DROP COLUMN file_id;
ALTER TABLE games DROP COLUMN deployment_status;

-- 7. 기존 테이블 제거
DROP TABLE IF EXISTS game_files;
DROP TABLE IF EXISTS deployment_statuses;


-- =========================================================
-- 4. Additional Inserts & Updates
-- =========================================================

-- (게임 추가 삽입 - 중복 발생 시 업데이트 되도록 ON DUPLICATE 구문 추가)
INSERT INTO games (
    game_id, title, developer_id, developer_name, price, genre, demo_available, detail, age_rating, status_name, file_path
)
VALUES
    ('game1', 'Game One', 'dev', 'Sample Dev Studio', 10000, 'Action', TRUE, 'First sample action game.', 12, 'RELEASED', 'game-files/game1.txt'),
    ('game2', 'Game Two', 'dev', 'Sample Dev Studio', 15000, 'Adventure', TRUE, 'Second sample adventure game.', 12, 'RELEASED', 'game-files/game2.txt')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    price = VALUES(price),
    status_name = VALUES(status_name),
    file_path = VALUES(file_path);

-- 개별 파일 경로 업데이트
UPDATE games SET file_path = 'game-files/game1.txt' WHERE game_id = 'game1';
UPDATE games SET file_path = 'game-files/game2.txt' WHERE game_id = 'game2';
UPDATE games SET file_path = 'game-files/game3.txt' WHERE game_id = 'game3';
UPDATE games SET file_path = 'game-files/game4.txt' WHERE game_id = 'game4';
UPDATE games SET file_path = 'game-files/game5.txt' WHERE game_id = 'game5';


-- =========================================================
-- 5. Manifest & Chunk Tables
-- =========================================================

CREATE TABLE game_manifests (
    manifest_id VARCHAR(80) PRIMARY KEY,
    game_id VARCHAR(50) NOT NULL,
    version VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE game_manifest_files (
    manifest_id VARCHAR(80) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    PRIMARY KEY (manifest_id, file_path)
);

CREATE TABLE game_chunks (
    chunk_id VARCHAR(150) PRIMARY KEY,
    manifest_id VARCHAR(80) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    chunk_index INT NOT NULL,
    size_bytes BIGINT NOT NULL,
    checksum VARCHAR(255) NOT NULL
);

CREATE TABLE download_tasks (
    task_id VARCHAR(80) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    game_id VARCHAR(50) NOT NULL,
    manifest_id VARCHAR(80) NOT NULL,
    status VARCHAR(30) NOT NULL,
    progress INT NOT NULL DEFAULT 0,
    completed_chunk_count INT NOT NULL DEFAULT 0,
    total_chunk_count INT NOT NULL DEFAULT 0,
    install_path VARCHAR(500) NOT NULL,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL
);

CREATE TABLE installed_games (
    user_id VARCHAR(50) NOT NULL,
    game_id VARCHAR(50) NOT NULL,
    manifest_id VARCHAR(80) NOT NULL,
    install_path VARCHAR(500) NOT NULL,
    installed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, game_id)
);