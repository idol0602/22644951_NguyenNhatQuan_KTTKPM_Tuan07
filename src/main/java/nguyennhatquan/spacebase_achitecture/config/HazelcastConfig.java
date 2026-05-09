package nguyennhatquan.spacebase_achitecture.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelcastConfiguration() {
        Config config = new Config();
        config.setInstanceName("spacebase-hazelcast");

        MapConfig productMapConfig = new MapConfig();
        productMapConfig.setName("products")
                .setTimeToLiveSeconds(3600); // 1 hour TTL

        config.addMapConfig(productMapConfig);
        return config;
    }
}

