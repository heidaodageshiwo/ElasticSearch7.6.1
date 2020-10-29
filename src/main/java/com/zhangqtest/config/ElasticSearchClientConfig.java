package com.zhangqtest.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * java类简单作用描述
 *
 * @ProjectName: zhangqiang-es-api
 * @Package: com.zhangqtest.config
 * @ClassName: ElasticSearchClientConfig
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2020-09-17 22:48
 * @UpdateUser: zhangq
 * @UpdateDate: 2020-09-17 22:48
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Configuration
public class ElasticSearchClientConfig {

  @Bean
  public RestHighLevelClient restHighLevelClient() {
    RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(
            new HttpHost("localhost", 9200, "http")));
    return client;
  }
}
