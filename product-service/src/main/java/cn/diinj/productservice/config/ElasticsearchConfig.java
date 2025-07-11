package cn.diinj.productservice.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
public class ElasticsearchConfig {
@Value("${elasticsearch.host}")
private String host;
@Value("${elasticsearch.port}")
private int port;

@Bean
public RestHighLevelClient restHighLevelClient() {
    return new RestHighLevelClient(
            RestClient.builder(
                    new org.apache.http.HttpHost(host, port, "http")
            )
    );
}
} 