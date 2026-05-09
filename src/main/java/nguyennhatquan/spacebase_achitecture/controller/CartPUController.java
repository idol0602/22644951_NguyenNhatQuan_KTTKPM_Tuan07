package nguyennhatquan.spacebase_achitecture.controller;

import nguyennhatquan.spacebase_achitecture.model.Cart;
import nguyennhatquan.spacebase_achitecture.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartPUController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");

        cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok("Added to cart successfully");
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}

