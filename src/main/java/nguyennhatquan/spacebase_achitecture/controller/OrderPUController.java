package nguyennhatquan.spacebase_achitecture.controller;

import nguyennhatquan.spacebase_achitecture.model.Order;
import nguyennhatquan.spacebase_achitecture.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@CrossOrigin("*")
public class OrderPUController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            Order order = orderService.checkout(userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

