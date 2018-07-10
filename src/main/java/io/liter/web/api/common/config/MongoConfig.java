package io.liter.web.api.common.config;

import com.mongodb.MongoCredential;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.lang.NonNull;

import javax.annotation.PreDestroy;

@Slf4j
@EnableReactiveMongoRepositories
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final Environment environment;

    @Value("${spring.data.mongodb.username}")
    private String userName;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Autowired
    public MongoConfig(Environment environment) {

        this.environment = environment;
    }

    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @NonNull
    @Override
    public MongoClient reactiveMongoClient() {
        log.info("]-----] reactiveMongoClient [-----[");

        MongoCredential mongoCredential = MongoCredential.createCredential(userName, database, password.toCharArray());
        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder().maxSize(200).maxWaitQueueSize(1000).build();

        return MongoClients.create(MongoClientSettings.builder()
                .credential(mongoCredential)
                .connectionPoolSettings(connectionPoolSettings)
                .build());
    }

    @Bean
    public GridFSBucket gridFsTemplate(MongoClient reactiveMongoClient) throws Exception {
        log.info("]-----] gridFsTemplate [-----[");

        return GridFSBuckets.create(reactiveMongoClient.getDatabase(getDatabaseName()));
    }

    @PreDestroy
    public void shutdown() {
        eventLoopGroup.shutdownGracefully();
    }
}
