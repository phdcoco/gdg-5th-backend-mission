package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.*;
import gdg.hongik.mission.service.AdminProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;
    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    /**
     * 새로운 상품을 등록합니다. 중복된 상품명일 경우 예외가 발생합니다.
     *
     * @param request 등록할 상품의 정보를 담은 DTO
     * @return 생성된 상품 정보를 담은 ProductResponse
     */
    @PostMapping // 새로운 상품을 등록한다.
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) { // 기본적으로 한 번에 하나의 등록, 수정만 가능하다고 하자.
        // 객체를 생성하고 예외 처리하는 역할은 이제 서비스 계층이 맡도록 하자.
        // DTO를 받기 위해 받는 매개변순 타입을 ProductResponse로 변경하자.
        return ResponseEntity.ok(adminProductService.createProduct(request));
    }

    /**
     * 기존 상품의 재고를 변화시킨다. 입력받은대로 재고를 반영한 뒤 갱신된 상품 정보를 반환한다.
     *
     * @param request 추가 재고를 담은 DTO
     * @return 재고가 수정된 상품 정보를 담은 ProductResponse
     */
    @PatchMapping("/stock") // 상품의 재고를 변환한다.
    public ResponseEntity<ProductResponse> addStock(@RequestBody AddStockRequest request) { // dto에서 구현해야 할 내용.
        return ResponseEntity.ok(adminProductService.addStock(request));
    }

    /**
     * 하나 이상의 상품을 삭제한다. 삭제 후 남아있는 전체 상품 목록을 반환한다.
     *
     * @param request 삭제할 상품들의 ID 리스트를 담은 DTO
     * @return 삭제 후 남아있는 상품 정보를 담은 Response
     */
    @DeleteMapping// 입력은 한 개 이상의 물품일 수 있다고 했다.
    public ResponseEntity<ProductsStockResponse> deleteProducts(@RequestBody DeleteProductsRequest request) {
        return ResponseEntity.ok(adminProductService.deleteProducts(request));
    }
}
