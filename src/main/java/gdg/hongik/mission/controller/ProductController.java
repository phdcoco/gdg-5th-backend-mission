package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.dto.PurchaseRequest;
import gdg.hongik.mission.dto.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 상품명을 기준으로 개별 상품을 조회한다.
     *
     * @param name 조회할 상품명
     * @return 조회된 상품 정보를 담은 DTO
     */
    @GetMapping // 이름으로 상품을 검색한다.
    public ResponseEntity<ProductResponse> getProduct(@RequestParam String name) {
        ProductResponse response = productService.getProductByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * 장바구니에 담긴 상품들을 구매 처리한다.
     *
     * @param request 구매할 상품 ID 및 수량 목록을 담은 DTO
     * @return 총 구매 금액과 구매 항목 리스트를 담은 DTO
     */
    @PostMapping("/purchase") // 장바구니에 담긴 상품들을 구매한다.
    public ResponseEntity<PurchaseResponse> purchase(@RequestBody PurchaseRequest request) {
        PurchaseResponse response = productService.purchase(request);
        return ResponseEntity.ok(response);
    }
}