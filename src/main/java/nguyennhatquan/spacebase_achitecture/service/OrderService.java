package nguyennhatquan.spacebase_achitecture.service;

import nguyennhatquan.spacebase_achitecture.model.Cart;
import nguyennhatquan.spacebase_achitecture.model.Order;
import nguyennhatquan.spacebase_achitecture.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ORDER_PREFIX = "order:";

    public Order checkout(String userId) {
        Cart cart = cartService.getCart(userId);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0;

        for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            boolean stockReduced = inventoryService.reduceStock(productId, quantity);
            if (!stockReduced) {
                throw new RuntimeException("Not enough stock for product: " + productId);
            }

            Product product = productService.getProduct(productId);
            totalAmount += product.getPrice() * quantity;
        }

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setItems(cart.getItems());
        order.setTotalAmount(totalAmount);
        order.setStatus("COMPLETED");

        redisTemplate.opsForValue().set(ORDER_PREFIX + order.getId(), order);

        cartService.clearCart(userId);

        return order;
    }
}

