package com.zhangqtest;

import java.io.IOException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
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
  void testCreateIndex1() throws IOException {
    CreateIndexRequest zhangqiang0 = new CreateIndexRequest("zhangqiang0");
    CreateIndexResponse createIndexResponse = client.indices()
        .create(zhangqiang0, RequestOptions.DEFAULT);
    System.out.println(createIndexResponse.isAcknowledged());
    System.out.println(createIndexResponse.isFragment());
    System.out.println(createIndexResponse);
  }

  @Test
  void testCreateIndex2() throws IOException {
    CreateIndexRequest zhangqiang1 = new CreateIndexRequest("zhangqiang2");
    CreateIndexResponse createIndexResponse = client.indices()
        .create(zhangqiang1, RequestOptions.DEFAULT);
    System.out.println(createIndexResponse);
  }


  // 测试获取索引,判断其是否存在
  @Test
  void testExistIndex() throws IOException {
    GetIndexRequest request = new GetIndexRequest("kuang_index2");
    boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
    System.out.println(exists);
  }

  @Test
  void testExistIndex1() throws IOException {
    GetIndexRequest zhangqiang1 = new GetIndexRequest("zhangqiang1");
    boolean exists = client.indices().exists(zhangqiang1, RequestOptions.DEFAULT);
    System.out.println(exists);
  }


  // 测试删除索引
  @Test
  void testDeleteIndex() throws IOException {
    DeleteIndexRequest request = new DeleteIndexRequest("kuang_index");
    // 删除
    AcknowledgedResponse delete = client.indices().delete(request,
        RequestOptions.DEFAULT);
    System.out.println(delete.isAcknowledged());
  }

  @Test
  void testDeleteIndex1() throws IOException {
    DeleteIndexRequest zhangqiang1 = new DeleteIndexRequest("zhangqiang1");
    AcknowledgedResponse delete = client.indices().delete(zhangqiang1, RequestOptions.DEFAULT);
    System.out.println(delete.isAcknowledged());

  }

  @Test
  void contextLoads() {
  }

}
