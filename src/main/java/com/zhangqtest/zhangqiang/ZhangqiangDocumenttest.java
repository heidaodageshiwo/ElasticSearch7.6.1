package com.zhangqtest.zhangqiang;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @PackageName com.zhangqtest.zhangqiang
 * @ClassName ZhangqiangDocumenttest
 * @Description TODO
 * @Author zhangqiang
 * @Date 2020/9/27 8:44
 * @Version 1.0
 **/
@RestController
@RequestMapping("/zhangqiangdocument")
public class ZhangqiangDocumenttest {

  @Autowired
  private RestHighLevelClient restHighLevelClient;


  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName IndexReques
   * @Description 创建文档
   * @Author zhangqiang
   * @Date 2020/9/27 9:59
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/IndexRequest
  @RequestMapping("/IndexRequest")
  public String IndexReques() throws IOException {
    //新添加一个索引
       /* CreateIndexRequest zqtest = new CreateIndexRequest("zqtest");
        zqtest.settings(Settings.builder().put("index.number_of_shards",5));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(zqtest, RequestOptions.DEFAULT);
        System.out.println("创建索引是否成功："+createIndexResponse.isAcknowledged());
*/

    IndexRequest indexRequest = new IndexRequest("zqtest");
    //map方式插入数据
  /*      HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("age",11);
        stringObjectHashMap.put("name","张强");
        //可以指定id，也可以不指定
        indexRequest.id("1");
        indexRequest.source(stringObjectHashMap);*/
    //直接插入方式
    //indexRequest.source("age",12,"name","张强12");
    //json数据插入方式
    User user = new User(14, "张强14");
    System.out.println(JSON.toJSONString(user));
    indexRequest.source(JSON.toJSONString(user), XContentType.JSON);

    indexRequest.timeout("5s");
    indexRequest.timeout(TimeValue.timeValueSeconds(5));
    IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    String index = indexResponse.getIndex();
    System.out.println("创建文档成功后获取索引名称：" + index);

    String id = indexResponse.getId();
    System.out.println("创建文档成功后获取文档id：" + id);

    DocWriteResponse.Result indexResponseResult = indexResponse.getResult();
    if (indexResponseResult == DocWriteResponse.Result.CREATED) {
      System.out.println("这个是创建文档的操作");
    } else if (indexResponseResult == DocWriteResponse.Result.DELETED) {
      System.out.println("这个是删除文档的操作");
    } else if (indexResponseResult == DocWriteResponse.Result.UPDATED) {
      System.out.println("这个是更新文档的操作");
    } else if (indexResponseResult == DocWriteResponse.Result.NOT_FOUND) {
      System.out.println("这个是没有发现文档的操作");
    } else if (indexResponseResult == DocWriteResponse.Result.NOOP) {
      System.out.println("我也不知道这是什么操作");
    }

    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
    if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
      //处理分片故障
    }
    //处理分片故障
    if (shardInfo.getFailed() > 0) {
      for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
        String reason = failure.reason();
        System.out.println(reason);
      }
    }

    return "11111111111111111111111";
  }

  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName GetRequest
   * @Description 获取文档
   * @Author zhangqiang
   * @Date 2020/9/27 9:59
   * @Param [ids]
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/GetRequest/1
  @RequestMapping("/GetRequest/{ids}")
  public String GetRequest(@PathVariable("ids") String ids) throws IOException {
    GetRequest zqtest = new GetRequest("zqtest", ids);
    GetResponse getResponse = restHighLevelClient.get(zqtest, RequestOptions.DEFAULT);
    String index = getResponse.getIndex();
    System.out.println(index);
    String id = getResponse.getId();
    System.out.println(id);
    if (getResponse.isExists()) {
      //文档存在
      System.out.println("文档存在");
      long version = getResponse.getVersion();
      System.out.println("version:" + version);
      String sourceAsString = getResponse.getSourceAsString();
      System.out.println("sourceAsString:" + sourceAsString);
      Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
      System.out.println("sourceAsMap:" + sourceAsMap);
      byte[] sourceAsBytes = getResponse.getSourceAsBytes();
      System.out.println("sourceAsBytes:" + sourceAsBytes);

    } else {
      //不存在
      System.out.println("不存在");
    }

    return "11111111111111111111111";
  }

  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName ExistsRequest
   * @Description 判断文档是否存在
   * @Author zhangqiang
   * @Date 2020/9/27 10:00
   * @Param [ids]
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/ExistsRequest/1
  @RequestMapping("/ExistsRequest/{ids}")
  public String ExistsRequest(@PathVariable("ids") String ids) throws IOException {
    GetRequest zqtest = new GetRequest("zqtest", ids);
    boolean exists = restHighLevelClient.exists(zqtest, RequestOptions.DEFAULT);
    System.out.println(exists);

    return "11111111111111111111111";
  }

  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName DeleteRequest
   * @Description 删除文档
   * @Author zhangqiang
   * @Date 2020/9/27 10:25
   * @Param [ids]
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/DeleteRequest/1
  @RequestMapping("/DeleteRequest/{ids}")
  public String DeleteRequest(@PathVariable("ids") String ids) throws IOException {
    DeleteRequest zqtest = new DeleteRequest("zqtest", ids);
    DeleteResponse deleteResponse = restHighLevelClient.delete(zqtest, RequestOptions.DEFAULT);
    System.out.println(deleteResponse.status());

    return "11111111111111111111111";
  }


  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName UpdateRequest
   * @Description 更新文档
   * @Author zhangqiang
   * @Date 2020/9/27 10:38
   * @Param [ids]
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/UpdateRequest/1
  @RequestMapping("/UpdateRequest/{ids}")
  public String UpdateRequest(@PathVariable("ids") String ids) throws IOException {
    UpdateRequest zqtest = new UpdateRequest("zqtest", ids);
    User user = new User(111, "333333333333333333333333333333333333");
    zqtest.doc(JSON.toJSONString(user), XContentType.JSON);
    UpdateResponse updateResponse = restHighLevelClient.update(zqtest, RequestOptions.DEFAULT);
    System.out.println(updateResponse.status());

    if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
      System.out.println("创建文档");
    } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
      System.out.println("更新文档");
    } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
      System.out.println("删除文档");
    } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
      System.out.println("没有操作文档");
    }

    return "11111111111111111111111";
  }

  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName BulkRequest
   * @Description 批量增加
   * @Author zhangqiang
   * @Date 2020/9/27 14:12
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/BulkRequest
  @RequestMapping("/BulkRequest")
  public String BulkRequest() throws IOException, InterruptedException {
    BulkRequest bulkRequest = new BulkRequest();

    ArrayList<User> userArrayList = new ArrayList<>();
    userArrayList.add(new User(31, "张强31"));
    userArrayList.add(new User(32, "张强32"));
    userArrayList.add(new User(33, "张强33"));
    userArrayList.add(new User(34, "张强34"));
    userArrayList.add(new User(35, "张强35"));
    userArrayList.add(new User(36, "张强36"));
    userArrayList.add(new User(37, "张强37"));

       /* for(int i=0;i<userArrayList.size();i++){
            bulkRequest.add(new IndexRequest("zqtest").id(i+"====").source(JSON.toJSONString(userArrayList.get(i)),XContentType.JSON));
        }*/
    for (int i = 0; i < 100000; i++) {
      bulkRequest.add(new IndexRequest("zqtest")
          .source(JSON.toJSONString(userArrayList.get(2)), XContentType.JSON));
    }


        /* for(int i=0;i<userArrayList.size();i++){
            bulkRequest.add(new IndexRequest("zqtest5").id(i+"====").source(JSON.toJSONString(userArrayList.get(i)),XContentType.JSON));
        }*/
//        bulkRequest.timeout(TimeValue.timeValueHours(1));
    bulkRequest.timeout(TimeValue.timeValueHours(1));
//        bulkRequest.timeout("1s");
    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println("执行了此请求");
    boolean b = bulkResponse.hasFailures();
    for (BulkItemResponse bulkItemResponse : bulkResponse) {
      if (bulkItemResponse.isFailed()) {
        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
        System.out.println(failure);
        System.out.println(JSON.toJSON(failure));
        System.out.println(JSON.toJSON(failure.getCause()));
        System.out.println(JSON.toJSON(failure.getMessage()));
      }
    }

    System.out.println("是否失败：" + b);

    return "11111111111111111111111";
  }


  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName BulkRequest
   * @Description 批量增加与修改与删除
   * @Author zhangqiang
   * @Date 2020/9/27 14:12
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/BulkadddeleteupdateRequest
  @RequestMapping("/BulkadddeleteupdateRequest")
  public String BulkadddeleteupdateRequest() throws IOException, InterruptedException {
    BulkRequest bulkRequest = new BulkRequest();
    ArrayList<User> userArrayList = new ArrayList<>();
    userArrayList.add(new User(31, "张三31"));
    userArrayList.add(new User(32, "李四32"));
    userArrayList.add(new User(33, "王五33"));
    userArrayList.add(new User(34, "小娃儿34"));
    userArrayList.add(new User(35, "小二35"));
    userArrayList.add(new User(36, "小三36"));
    userArrayList.add(new User(37, "小四37"));
    userArrayList.add(new User(38, "好的38"));
    userArrayList.add(new User(39, "五啊啊啊39"));
    userArrayList.add(new User(40, "张明40"));
    userArrayList.add(new User(41, "李丽丽41"));
    userArrayList.add(new User(42, "拜把42"));

    bulkRequest.timeout(TimeValue.timeValueMinutes(2));
    //1先增加数据
    for (int i = 0; i < userArrayList.size(); i++) {
      bulkRequest.add(new IndexRequest("zqtest1")
          .source(JSON.toJSONString(userArrayList.get(i)), XContentType.JSON));
    }
        /*//删除3条数据，更新一条数据
        bulkRequest.add(new DeleteRequest("zqtest1").id("pA82znQBwVtjHGNcf9VT"));
        bulkRequest.add(new DeleteRequest("zqtest1").id("pQ82znQBwVtjHGNcf9VT"));
        bulkRequest.add(new DeleteRequest("zqtest1").id("pg82znQBwVtjHGNcf9VT"));
        bulkRequest.add(new UpdateRequest("zqtest1","pw82znQBwVtjHGNcf9VT").doc(JSON.toJSONString(new User(6666,"66666")),XContentType.JSON));
*/
    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println("是否失败：" + bulkResponse.hasFailures());
    return "11111111111111111111111";
  }


  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName MultiGetRequest
   * @Description 多个请求的get
   * @Author zhangqiang
   * @Date 2020/9/27 14:34
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/MultiGetRequest
  @RequestMapping("/MultiGetRequest")
  public String MultiGetRequest() throws IOException, InterruptedException {
    MultiGetRequest multiGetRequest = new MultiGetRequest();
    multiGetRequest.add(new MultiGetRequest.Item("zqtest1", "pw82znQBwVtjHGNcf9VT"));
    multiGetRequest.add(new MultiGetRequest.Item("zqtest1", "qg82znQBwVtjHGNcf9VT"));
    MultiGetResponse mget = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);
    MultiGetItemResponse[] responsess = mget.getResponses();
    for (MultiGetItemResponse response : responsess) {
      MultiGetResponse.Failure failure = response.getFailure();
      System.out.println("是否获取失败：" + failure);
      String index = response.getIndex();
      String id = response.getId();
      System.out.println("index:" + index);
      System.out.println("id:" + id);
      GetResponse response1 = response.getResponse();
      if (response1.isExists()) {
        String sourceAsString = response1.getSourceAsString();
        Map<String, Object> sourceAsMap = response1.getSourceAsMap();
        byte[] sourceAsBytes = response1.getSourceAsBytes();
        System.out.println("sourceAsString:" + sourceAsString);
        System.out.println("sourceAsMap:" + sourceAsMap);
        System.out.println("sourceAsBytes:" + sourceAsBytes);
      } else {

      }

    }
    return "11111111111111111111111";
  }


  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName ReindexRequest
   * @Description 复制索引里面的文档
   * @Author zhangqiang
   * @Date 2020/9/27 14:55
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/ReindexRequest
  @RequestMapping("/ReindexRequest")
  public String ReindexRequest() throws IOException, InterruptedException {
    CreateIndexRequest zqtest2 = new CreateIndexRequest("zqtest2");
    restHighLevelClient.indices().create(zqtest2, RequestOptions.DEFAULT);
    ReindexRequest reindexRequest = new ReindexRequest();
    reindexRequest.setSourceIndices("zqtest1");
    reindexRequest.setDestIndex("zqtest2");
    BulkByScrollResponse reindex = restHighLevelClient
        .reindex(reindexRequest, RequestOptions.DEFAULT);
    System.out.println(reindex.getTotal());
    return "11111111111111111111111";
  }


  /***
   *@ClassName ZhangqiangDocumenttest
   *@MethodName UpdateByQueryRequest
   *@Description 通过查询出来的结果进行批量的更新
   *@Author zhangqiang
   *@Date 2020/9/27 15:16
   *@Param []
   *@Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/UpdateByQueryRequest
  @RequestMapping("/UpdateByQueryRequest")
  public String UpdateByQueryRequest() throws IOException, InterruptedException {
    UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest("zqtest2");
    updateByQueryRequest.setQuery(new TermQueryBuilder("age", "36"));
    //此处需要注意 我这里age==36  age是int类型 用==
    Script script = new Script(ScriptType.INLINE, "painless",
        " if(ctx._source.age==36){ctx._source.name='修改后姓名'} ", Collections.emptyMap());
    updateByQueryRequest.setScript(script);
    BulkByScrollResponse bulkByScrollResponse = restHighLevelClient
        .updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
    System.out.println(bulkByScrollResponse.getTotal());
    long updated = bulkByScrollResponse.getUpdated();
    System.out.println(updated);
    return "11111111111111111111111";
  }


  /**
   * @ClassName ZhangqiangDocumenttest
   * @MethodName DeleteByQueryRequest
   * @Description 通过查询出来的数据进行批量的删除
   * @Author zhangqiang
   * @Date 2020/9/27 17:21
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/zhangqiangdocument/DeleteByQueryRequest
  @RequestMapping("/DeleteByQueryRequest")
  public String DeleteByQueryRequest() throws IOException, InterruptedException {
    DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("zqtest2");
    deleteByQueryRequest.setQuery(new TermQueryBuilder("age", "38"));
    BulkByScrollResponse bulkByScrollResponse = restHighLevelClient
        .deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
    System.out.println(bulkByScrollResponse.getTotal());
    long updated = bulkByScrollResponse.getUpdated();
    System.out.println(updated);
    return "11111111111111111111111";
  }

    /*//  http://localhost:8080/zhangqiangdocument/MultiTermVectorsRequest
    @RequestMapping("/MultiTermVectorsRequest")
    public String MultiTermVectorsRequest() throws IOException {

        MultiTermVectorsRequest termVectorsRequests = new MultiTermVectorsRequest();
        TermVectorsRequest termVectorsRequest = new TermVectorsRequest("zqtest2","_doc","tQ9AznQBwVtjHGNcytU5");
        termVectorsRequest.set
//        MultiTermVectorsRequest add = termVectorsRequests.add("zqtest2", null, "tQ9AznQBwVtjHGNcytU5");


        return "11111111111111111111111";
    }*/


  public static class User {

    private int age;
    private String name;

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public User(int age, String name) {
      this.age = age;
      this.name = name;
    }
  }

}
