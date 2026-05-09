package nguyennhatquan.spacebase_achitecture.service;

import com.hazelcast.core.HazelcastInstance;
import nguyennhatquan.spacebase_achitecture.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private static final String PRODUCT_KEY = "Product";
    private static final String HAZELCAST_MAP = "products";

    public boolean reduceStock(String productId, int quantity) {
        Product product = (Product) redisTemplate.opsForHash().get(PRODUCT_KEY, productId);

        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            redisTemplate.opsForHash().put(PRODUCT_KEY, productId, product);

            // Update cache
            hazelcastInstance.getMap(HAZELCAST_MAP).put(productId, product);

            return true;
        }
        return false;
    }

    public int getStock(String productId) {
        Product product = productService.getProduct(productId);
        return product != null ? product.getStock() : 0;
    }
}

