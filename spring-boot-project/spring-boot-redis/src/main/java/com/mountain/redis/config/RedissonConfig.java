package com.mountain.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Redisson配置
 *
 * @author shaozj
 * @since 2020/4/20 19:57
 */
@Configuration
@Slf4j
public class RedissonConfig {

    @Resource
    RedisProperties redisProperties;


    /**
     * 单机模式
     */
    @Bean
    public RedissonClient redissonSingle() {
        Config config = new Config();
        String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        config.useSingleServer().setAddress(address);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    /**
     * 集群配置
     */
/*    @Bean
    public RedissonClient redissonCluster() {
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.addNodeAddress(convert(redisProperties.getCluster().getNodes()));
        if (redisProperties.getPassword() != null && redisProperties.getPassword().trim().length() > 0) {
            clusterServersConfig.setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);
    }


    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<String>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                nodes.add("redis://" + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[nodes.size()]);
    }*/

}
