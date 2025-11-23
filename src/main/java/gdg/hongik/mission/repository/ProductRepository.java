package gdg.hongik.mission.repository;

import gdg.hongik.mission.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 상품 엔티티에 대한 데이터베이스 접근 인터페이스
 * Spring Data JPA를 사용하여 기본 CRUD 및 쿼리 메서드를 제공한다.
 */
public interface ProductRepository extends JpaRepository<Product,Long> { // 인터페이스 상속 시 extends 사용
    // JpaRepository로 기본 CRUD 자동 구현 가능
    Optional<Product> findByName(String name); // 이름으로 상품을 찾는다.
    // Optional을 쓰면 NULL 처리를 안전하게 할 수 있다.
}
