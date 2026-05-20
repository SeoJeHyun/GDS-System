package dao;

import gds.Game;
import java.util.List;

public interface GameDAO {
    // 1. 새로운 게임 등록 (개발자가 게임을 출시할 때 사용)
    void save(Game game);

    // 2. 게임 ID로 특정 게임 상세 정보 조회
    Game findById(String gameId);

    // 3. 상점에 전시할 모든 게임 목록 불러오기
    List<Game> findAll();

    // 4. [기능 이관] 기존 Shop.searchGames()의 역할을 DB가 수행하도록 쿼리 규격 정의
    // 제목(Title)에 키워드가 포함된 게임 목록 검색
    List<Game> findByTitleContaining(String keyword);

    // 5. 특정 장르의 게임 목록 검색 (향후 N:M 매핑 테이블 조회에 활용)
    List<Game> findByGenre(String genre);

    // 6. 게임 정보 수정 (가격 변동, 패치 업데이트, 배포 상태 변경 등)
    void update(Game game);

    // 7. 게임 삭제 (상점 서비스 종료)
    void delete(String gameId);
}
