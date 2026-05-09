package nguyennhatquan.spacebase_achitecture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {
    private String userId;
    // ProductId to Quantity
    private Map<String, Integer> items = new HashMap<>();

    public void addItem(String productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }
}

