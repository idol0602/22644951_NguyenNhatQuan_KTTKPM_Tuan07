package nguyennhatquan.spacebase_achitecture.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nguyennhatquan.spacebase_achitecture.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_KEY = "Product";

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading initial data into Redis...");

        List<Product> products = Arrays.asList(
            new Product("p1", "iPhone 15 Pro Max", 1200.0, "Apple Smartphone", 100),
            new Product("p2", "Samsung Galaxy S24 Ultra", 1300.0, "Samsung Smartphone", 150),
            new Product("p3", "MacBook Pro M3", 2500.0, "Apple Laptop", 50),
            new Product("p4", "Sony WH-1000XM5", 400.0, "Noise Cancelling Headphones", 200),
            new Product("p5", "iPad Air 5", 600.0, "Apple Tablet", 120)
        );

        for (Product product : products) {
            redisTemplate.opsForHash().put(PRODUCT_KEY, product.getId(), product);
        }

        System.out.println("Data loaded successfully!");
    }
}

