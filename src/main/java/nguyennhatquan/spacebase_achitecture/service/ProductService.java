package nguyennhatquan.spacebase_achitecture.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import nguyennhatquan.spacebase_achitecture.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private static final String PRODUCT_KEY = "Product";
    private static final String HAZELCAST_MAP = "products";

    public List<Product> getAllProducts() {
        IMap<String, Product> productCache = hazelcastInstance.getMap(HAZELCAST_MAP);
        if (!productCache.isEmpty()) {
            return new ArrayList<>(productCache.values());
        }

        Map<Object, Object> redisProducts = redisTemplate.opsForHash().entries(PRODUCT_KEY);
        List<Product> products = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : redisProducts.entrySet()) {
            Product product = (Product) entry.getValue();
            products.add(product);
            productCache.put(product.getId(), product);
        }

        return products;
    }

    public Product getProduct(String id) {
        IMap<String, Product> productCache = hazelcastInstance.getMap(HAZELCAST_MAP);
        Product cachedProduct = productCache.get(id);
        if (cachedProduct != null) {
            return cachedProduct;
        }

        Product product = (Product) redisTemplate.opsForHash().get(PRODUCT_KEY, id);
        if (product != null) {
            productCache.put(id, product);
        }
        return product;
    }
}

