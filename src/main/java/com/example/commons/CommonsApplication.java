package com.example.commons;

import com.example.commons.cache.config.CacheConfig;
import com.example.commons.cache.config.CacheNodes;
import com.example.commons.cache.core.StringCacheDao;
import com.example.commons.cache.core.TableCacheDao;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = {"com.example.**.dao"})
@EnableSwagger2Doc
public class CommonsApplication {

    @Value("#{'${cache.nodes}'.split(',')}")
    private Set<String> cachedNodes;
    // @Value("#{'${nodes.zk}'.split(',')}")
    // private Set<String> zkNodes;

    @Value("${cache.password}")
    private String redisPassword;

    public static void main(String[] args) {
        SpringApplication.run(CommonsApplication.class, args);
    }

    @Bean
    public StringCacheDao stringCacheDao() {
        return new StringCacheDao(buildCacheNodes(), buildCacheConfig());
    }

    @Bean
    public TableCacheDao tableCacheDao() {
        return new TableCacheDao(buildCacheNodes(), buildCacheConfig());
    }


    private CacheNodes buildCacheNodes() {
        CacheNodes cacheNodes = CacheNodes.newInstance();
        for (String hostAndPort : cachedNodes) {
            StringTokenizer tokenizer = new StringTokenizer(hostAndPort, ":");
            String host = tokenizer.nextElement().toString();
            int port = Integer.valueOf(tokenizer.nextElement().toString());
            cacheNodes.addNode(host, port);
        }
        return cacheNodes;
    }

    private CacheConfig buildCacheConfig() {
        CacheConfig cacheConfig = CacheConfig.newInstance();
        if (StringUtils.isNotBlank(redisPassword)) {
            cacheConfig.setPassword(redisPassword);
        }
        return cacheConfig;
    }

}
