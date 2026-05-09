package nguyennhatquan.spacebase_achitecture.service;

import nguyennhatquan.spacebase_achitecture.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CART_PREFIX = "cart:";

    public void addToCart(String userId, String productId, int quantity) {
        String key = CART_PREFIX + userId;
        Cart cart = (Cart) redisTemplate.opsForValue().get(key);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }

        cart.addItem(productId, quantity);
        redisTemplate.opsForValue().set(key, cart);
    }

    public Cart getCart(String userId) {
        String key = CART_PREFIX + userId;
        Cart cart = (Cart) redisTemplate.opsForValue().get(key);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }
        return cart;
    }

    public void clearCart(String userId) {
        redisTemplate.delete(CART_PREFIX + userId);
    }
}

