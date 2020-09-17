package com.zhangqtest;

import java.io.IOException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZhangqiangEsApiApplicationTests {

  @Autowired
  @Qualifier("restHighLevelClient")
  private RestHighLevelClient client;



  // 测试索引的创建 Request PUT kuang_index

  @Test
  void testCreateIndex() throws IOException {
    // 1、创建索引请求
    CreateIndexRequest request = new CreateIndexRequest("kuang_index");
    // 2、客户端执行请求 IndicesClient,请求后获得响应
    CreateIndexResponse createIndexResponse =
        client.indices().create(request, RequestOptions.DEFAULT);
    System.out.println(createIndexResponse);
  }


  @Test
  void contextLoads() {
  }

}
