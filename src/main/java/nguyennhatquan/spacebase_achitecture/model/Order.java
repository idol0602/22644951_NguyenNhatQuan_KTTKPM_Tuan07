package nguyennhatquan.spacebase_achitecture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Order")
public class Order implements Serializable {
    @Id
    private String id;
    private String userId;
    private Map<String, Integer> items;
    private double totalAmount;
    private String status;
}

