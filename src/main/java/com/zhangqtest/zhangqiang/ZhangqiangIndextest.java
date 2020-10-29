package com.zhangqtest.zhangqiang;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.shrink.ResizeRequest;
import org.elasticsearch.action.admin.indices.shrink.ResizeResponse;
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.validate.query.QueryExplanation;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryRequest;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryResponse;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.ShardsAcknowledgedResponse;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

/**
 * @PackageName com.zhangqtest.zhangqiang
 * @ClassName test
 * @Description TODO
 * @Author zhangqiang
 * @Date 2020/9/23 9:25
 * @Version 1.0
 **/
@RestController
@RequestMapping("/testzq")
public class ZhangqiangIndextest {

  @Autowired
  private RestHighLevelClient restHighLevelClient;


  @RequestMapping("/test")
  public String test() {
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName insert
   * @Description 创建索引同步
   * @Author zhangqiang
   * @Date 2020/9/25 16:09
   * @Param []
   * @Return java.lang.String
   **/
  //  http://localhost:8080/testzq/insert
  @RequestMapping("/insert")
  public String insert() throws IOException {
    CreateIndexRequest zq = new CreateIndexRequest("field");
//        CreateIndexRequest zq = new CreateIndexRequest("shrinkindex");

    zq.settings(Settings.builder().put("index.number_of_shards", "15"));
    zq.setTimeout(TimeValue.timeValueMinutes(2));
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
        .create(zq, RequestOptions.DEFAULT);
    System.out.println("创建索引成功" + createIndexResponse);
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName insert1
   * @Description 创建索引异步
   * @Author zhangqiang
   * @Date 2020/9/25 16:09
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/insert1
  @RequestMapping("/insert1")
  public String insert1() throws IOException {
    CreateIndexRequest zq = new CreateIndexRequest("zq456");
    zq.setTimeout(TimeValue.timeValueMinutes(2));
    ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
      @Override
      public void onResponse(CreateIndexResponse createIndexResponse) {
        System.out.println(createIndexResponse.isAcknowledged() + "====isAcknowledged");
        System.out.println(createIndexResponse.isShardsAcknowledged() + "====isShardsAcknowledged");
        System.out.println("成功了））））））））））））");
      }

      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
        System.out.println("失败了！！！！！！！！！！！！！！！！！！！！！！");
      }
    };
    restHighLevelClient.indices().
        createAsync(zq, RequestOptions.DEFAULT, listener);
    //System.out.println("创建索引成功");
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName delete
   * @Description 删除索引同步
   * @Author zhangqiang
   * @Date 2020/9/25 16:10
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/delete
  @RequestMapping("/delete")
  public String delete() throws IOException {
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("zq123");

    deleteIndexRequest.timeout(TimeValue.timeValueMinutes(2));
    AcknowledgedResponse delete = restHighLevelClient.indices()
        .delete(deleteIndexRequest, RequestOptions.DEFAULT);
    System.out.println("删除索引成功" + delete);
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName delete1
   * @Description 删除索引异步
   * @Author zhangqiang
   * @Date 2020/9/25 16:10
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/delete1
  @RequestMapping("/delete1")
  public String delete1() throws IOException {
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("zq456");
    deleteIndexRequest.timeout(TimeValue.timeValueMinutes(2));

    ActionListener<AcknowledgedResponse> actionListener = new ActionListener<AcknowledgedResponse>() {
      @Override
      public void onResponse(AcknowledgedResponse acknowledgedResponse) {
        System.out.println("删除成功了！！！！！！！");
        System.out.println("isAcknowledged:" + acknowledgedResponse.isAcknowledged());
        System.out.println("acknowledgedResponse:" + acknowledgedResponse.isFragment());
      }

      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
        System.out.println("删除失败！！！！！");
      }
    };
    restHighLevelClient.indices()
        .deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT, actionListener);

    System.out.println("删除索引成功");
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName exists
   * @Description 索引是否存在同步
   * @Author zhangqiang
   * @Date 2020/9/25 16:10
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/exists
  @RequestMapping("/exists")
  public String exists() throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest("zq123");
    boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    System.out.println("索引是否存在：" + exists);
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName exists1
   * @Description 索引是否存在异步
   * @Author zhangqiang
   * @Date 2020/9/25 16:11
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/exists1
  @RequestMapping("/exists1")
  public String exists1() throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest("zq456111");

    ActionListener<Boolean> actionListener = new ActionListener<Boolean>() {
      @Override
      public void onResponse(Boolean aBoolean) {
        System.out.println("索引是否存在：" + aBoolean);
      }

      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
        System.out.println("索引是否存在：" + "不存在");
      }
    };
    restHighLevelClient.indices()
        .existsAsync(getIndexRequest, RequestOptions.DEFAULT, actionListener);

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName openindex
   * @Description 打开索引同步
   * @Author zhangqiang
   * @Date 2020/9/25 16:11
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/openindex
  @RequestMapping("/openindex")
  public String openindex() throws IOException {
    OpenIndexRequest openIndexRequest = new OpenIndexRequest("shrinkindex_shrink");
//        OpenIndexRequest openIndexRequest = new OpenIndexRequest("zq123");
    openIndexRequest.timeout(TimeValue.timeValueMinutes(2));
    openIndexRequest.timeout("2m");
    OpenIndexResponse open = restHighLevelClient.indices()
        .open(openIndexRequest, RequestOptions.DEFAULT);

    System.out.println("打开索引是否成功isAcknowledged：" + open.isAcknowledged());
    System.out.println("打开索引是否成功isFragment：" + open.isFragment());
    System.out.println("打开索引是否成功isShardsAcknowledged：" + open.isShardsAcknowledged());
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName openindex1
   * @Description 打开索引异步
   * @Author zhangqiang
   * @Date 2020/9/25 16:11
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/openindex1
  @RequestMapping("/openindex1")
  public String openindex1() throws IOException {
    OpenIndexRequest openIndexRequest = new OpenIndexRequest("zq123");
    ActionListener<OpenIndexResponse> actionlistener = new ActionListener<OpenIndexResponse>() {
      @Override
      public void onResponse(OpenIndexResponse openIndexResponse) {
        System.out.println("打开索引是否成功isAcknowledged：" + openIndexResponse.isAcknowledged());
        System.out.println("打开索引是否成功isFragment：" + openIndexResponse.isFragment());
        System.out
            .println("打开索引是否成功isShardsAcknowledged：" + openIndexResponse.isShardsAcknowledged());
      }

      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
        System.out.println("失败");
      }
    };
    restHighLevelClient.indices()
        .openAsync(openIndexRequest, RequestOptions.DEFAULT, actionlistener);
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName closeindex
   * @Description 关闭索引同步
   * @Author zhangqiang
   * @Date 2020/9/25 16:11
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/closeindex
  /*@RequestMapping("/closeindex")此处报错是低版本
  public String closeindex() throws IOException {
    CloseIndexRequest closeIndexRequest = new CloseIndexRequest("zq123");
    closeIndexRequest.timeout(TimeValue.timeValueMinutes(2));
    closeIndexRequest.timeout("2m");
    AcknowledgedResponse close = restHighLevelClient.indices()
        .close(closeIndexRequest, RequestOptions.DEFAULT);
    System.out.println("关闭索引是否成功isAcknowledged：" + close.isAcknowledged());
    System.out.println("关闭索引是否成功isFragment：" + close.isFragment());
    return "11111111111111111111111";
  }*/

  /**
   * @ClassName zhangqiangtest
   * @MethodName closeindex1
   * @Description 关闭索引异步
   * @Author zhangqiang
   * @Date 2020/9/25 16:11
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/closeindex1
 /* @RequestMapping("/closeindex1")
  public String closeindex1() throws IOException {
    CloseIndexRequest closeIndexRequest = new CloseIndexRequest("zq123");
    closeIndexRequest.timeout(TimeValue.timeValueMinutes(2));
    closeIndexRequest.timeout("2m");
    ActionListener<AcknowledgedResponse> actionListener = new ActionListener<AcknowledgedResponse>() {
      @Override
      public void onResponse(AcknowledgedResponse acknowledgedResponse) {
        System.out.println("关闭索引是否成功isAcknowledged：" + acknowledgedResponse.isAcknowledged());
        System.out.println("关闭索引是否成功isFragment：" + acknowledgedResponse.isFragment());
      }

      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
        System.out.println("失败：");
      }
    };
    restHighLevelClient.indices()
        .closeAsync(closeIndexRequest, RequestOptions.DEFAULT, actionListener);
    return "11111111111111111111111";
  }*/

  /**
   * @ClassName zhangqiangtest
   * @MethodName shrinkindex
   * @Description 收缩索引
   * @Author zhangqiang
   * @Date 2020/9/25 16:12
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/shrinkindex
  @RequestMapping("/shrinkindex")
  public String shrinkindex() throws IOException {

    UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest("shrinkindex");
    updateSettingsRequest.settings(Settings.builder().put("index.blocks.write", true));
    restHighLevelClient.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);

    ResizeRequest resizeRequest = new ResizeRequest("shrinkindex_shrink", "shrinkindex");

//        resizeRequest.timeout(TimeValue.timeValueMinutes(1005));
//        resizeRequest.timeout("2m");

    resizeRequest.setCopySettings(true);

    //resizeRequest.getTargetIndexRequest().settings(Settings.builder().put("index.number_of_shards", "5"));
    resizeRequest.getTargetIndexRequest()
        .settings(Settings.builder().put("index.routing.allocation.require._name", "5")
            .put("index.blocks.write", true));
    resizeRequest.getTargetIndexRequest().alias(new Alias("zqfffffff"));

    resizeRequest.timeout(TimeValue.timeValueHours(100));
    ResizeResponse shrink = restHighLevelClient.indices()
        .shrink(resizeRequest, RequestOptions.DEFAULT);
    System.out.println("关闭索引是否成功isAcknowledged：" + shrink.isAcknowledged());
    System.out.println("关闭索引是否成功isFragment：" + shrink.isFragment());
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName putmapping
   * @Description putmapping
   * @Author zhangqiang
   * @Date 2020/9/25 16:12
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/putmapping
  @RequestMapping("/putmapping")
  public String putmapping() throws IOException {
    PutMappingRequest putMappingRequest = new PutMappingRequest("zq123");
    Map<String, Object> jsonMap = new HashMap<>();
    Map<String, Object> message = new HashMap<>();
    message.put("type", "text");
    Map<String, Object> property = new HashMap<>();
    property.put("message", message);
    jsonMap.put("properties", property);
    putMappingRequest.source(jsonMap);
    putMappingRequest.setTimeout(TimeValue.timeValueMinutes(5));

    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putMapping(putMappingRequest, RequestOptions.DEFAULT);
    System.out.println("putmapping:" + acknowledgedResponse.isAcknowledged());
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName getmapping
   * @Description getmapping
   * @Author zhangqiang
   * @Date 2020/9/25 16:23
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/getmapping
  @RequestMapping("/getmapping")
  public String getmapping() throws IOException {
    GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
    getMappingsRequest.indices("zq123");
    getMappingsRequest.setTimeout(TimeValue.timeValueMinutes(5));
    GetMappingsResponse getMappingsResponse = restHighLevelClient.indices()
        .getMapping(getMappingsRequest, RequestOptions.DEFAULT);

    Map<String, MappingMetaData> mappings = getMappingsResponse.mappings();
    MappingMetaData mappingMetaData = mappings.get("zq123");
    Map<String, Object> sourceAsMap = mappingMetaData.getSourceAsMap();
    System.out.println(JSON.toJSON(sourceAsMap));
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName getfieldmapping
   * @Description 获取字段的类型
   * @Author zhangqiang
   * @Date 2020/9/25 16:23
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/getfieldmapping
  @RequestMapping("/getfieldmapping")
  public String getfieldmapping() throws IOException {
    GetFieldMappingsRequest getFieldMappingsRequest = new GetFieldMappingsRequest();
    getFieldMappingsRequest.indices("field");
    getFieldMappingsRequest.fields("message", "timestamp");
    getFieldMappingsRequest.local(true);

    GetFieldMappingsResponse getFieldMappingsResponse = restHighLevelClient.indices()
        .getFieldMapping(getFieldMappingsRequest, RequestOptions.DEFAULT);
    Map<String, Map<String, GetFieldMappingsResponse.FieldMappingMetaData>> mappings1 = getFieldMappingsResponse
        .mappings();
    Map<String, GetFieldMappingsResponse.FieldMappingMetaData> field = mappings1.get("field");
    GetFieldMappingsResponse.FieldMappingMetaData fieldMappingMetaData = field.get("message");
    String s = fieldMappingMetaData.fullName();
    Map<String, Object> stringObjectMap = fieldMappingMetaData.sourceAsMap();
    System.out.println(JSON.toJSON(s) + "==========s");
    System.out.println(JSON.toJSON(stringObjectMap) + "==========stringObjectMap");

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName indexAliases
   * @Description 给索引创建别名，或者是给多个索引创建别名
   * @Author zhangqiang
   * @Date 2020/9/25 17:14
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/indexAliases
  @RequestMapping("/indexAliases")
  public String indexAliases() throws IOException {

    IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
    //索引field   添加别名testabcd
     /*   IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .index("field").alias("testabcd");*/

        /*IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .index("field").alias("testabcd").filter("{\"term\":{\"year\":2016}}");*/
    //索引field,zq123   添加别名testadb
        /*IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .indices("field", "zq123").alias("testadb");

        IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .indices("field", "zq123").alias("testadb").routing("1");*/

    //索引field    移除删除别名testadb
    /*    IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
                .index("field").alias("testadb");*/

    //直接删除索引
    IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(
        IndicesAliasesRequest.AliasActions.Type.REMOVE_INDEX)
        .index("field");

    indicesAliasesRequest.addAliasAction(aliasActions);

    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
    System.out.println(acknowledgedResponse.isAcknowledged() + ":isAcknowledged");

    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName ExistsAlias
   * @Description 判断别名是否存在
   * @Author zhangqiang
   * @Date 2020/9/25 17:34
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/ExistsAlias
  @RequestMapping("/ExistsAlias")
  public String ExistsAlias() throws IOException {
    GetAliasesRequest getAliasesRequest1 = new GetAliasesRequest();
    getAliasesRequest1.aliases("testadb");
    getAliasesRequest1.local(true);
    boolean existsAlias = restHighLevelClient.indices()
        .existsAlias(getAliasesRequest1, RequestOptions.DEFAULT);
    System.out.println(":new GetAliasesRequest()方式：testadb是否 存在：" + existsAlias);

    GetAliasesRequest getAliasesRequest2 = new GetAliasesRequest();
    getAliasesRequest2.aliases("testadb_test");
    getAliasesRequest2.local(true);
    boolean existsAlias2 = restHighLevelClient.indices()
        .existsAlias(getAliasesRequest2, RequestOptions.DEFAULT);
    System.out.println(":new GetAliasesRequest()方式：testadb_test 是否 存在：" + existsAlias2);

    GetAliasesRequest getAliasesRequest3 = new GetAliasesRequest("testadb");
    boolean existsAlias3 = restHighLevelClient.indices()
        .existsAlias(getAliasesRequest3, RequestOptions.DEFAULT);
    System.out.println(":new GetAliasesRequest(\"\")方式：testadb 是否 存在：" + existsAlias3);

    GetAliasesRequest getAliasesRequest4 = new GetAliasesRequest("testadb_test");
    boolean existsAlias4 = restHighLevelClient.indices()
        .existsAlias(getAliasesRequest4, RequestOptions.DEFAULT);
    System.out.println(":new GetAliasesRequest(\"\")方式：testadb_test 是否 存在：" + existsAlias4);

    GetAliasesRequest getAliasesRequest5 = new GetAliasesRequest(new String[]{"testadb"});
    boolean existsAlias5 = restHighLevelClient.indices()
        .existsAlias(getAliasesRequest5, RequestOptions.DEFAULT);
    System.out.println(
        ":new GetAliasesRequest(new String[]{\"testadb\"});方式：testadb 是否 存在：" + existsAlias5);

    GetAliasesRequest getAliasesRequest6 = new GetAliasesRequest(new String[]{"testadb_test"});
    boolean existsAlias6 = restHighLevelClient.indices()
        .existsAlias(getAliasesRequest6, RequestOptions.DEFAULT);
    System.out.println(
        ":new GetAliasesRequest(new String[]{\"testadb_test\"});方式：testadb_test 是否 存在："
            + existsAlias6);

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName getAliases
   * @Description 获取Aliases别名 与上方一样
   * @Author zhangqiang
   * @Date 2020/9/25 17:39
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/getAliases
  @RequestMapping("/getAliases")
  public String getAliases() throws IOException {
    GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
    getAliasesRequest.aliases("testadb");
    GetAliasesResponse getAliasesResponse = restHighLevelClient.indices()
        .getAlias(getAliasesRequest, RequestOptions.DEFAULT);
    Map<String, Set<AliasMetaData>> getAliasesResponseAliases = getAliasesResponse.getAliases();
    System.out.println(JSON.toJSON(getAliasesResponseAliases));

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName maxresultwindow
   * @Description 更新maxresultwindow
   * @Author zhangqiang
   * @Date 2020/9/26 11:22
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/maxresultwindow
  @RequestMapping("/maxresultwindow")
  public String maxresultwindow() throws IOException {

    UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest("zqtest7");
    updateSettingsRequest.settings(Settings.builder().put("index.max_result_window", "2000000000"));
    restHighLevelClient.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName updateindicesSetting
   * @Description 更新索引的数据配置setting
   * @Author zhangqiang
   * @Date 2020/9/26 15:52
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/updateindicesSetting
  @RequestMapping("/updateindicesSetting")
  public String updateindicesSetting() throws IOException {

    //更新所有的索引setting
    UpdateSettingsRequest updateSettingsRequest1 = new UpdateSettingsRequest();
    UpdateSettingsRequest updateSettingsRequest2 = new UpdateSettingsRequest("field", "field1");

    //更新一个或者是2个多个
    UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest("field");
    updateSettingsRequest.settings(Settings.builder().put("index.max_result_window", "2000000000"));
    restHighLevelClient.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName getSettingrequest
   * @Description 获取Setting 配置
   * @Author zhangqiang
   * @Date 2020/9/26 16:04
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/getSettingrequest
  @RequestMapping("/getSettingrequest")
  public String getSettingrequest() throws IOException {
    GetSettingsRequest getSettingsRequest = new GetSettingsRequest().indices("field");
    getSettingsRequest.names("index.number_of_shards");

    GetSettingsResponse settings = restHighLevelClient.indices()
        .getSettings(getSettingsRequest, RequestOptions.DEFAULT);
    System.out.println(JSON.toJSON(settings));
    String field = settings.getSetting("field", "index.number_of_shards");
    System.out.println(JSON.toJSON(field));
    Settings field1 = settings.getIndexToSettings().get("field");
    Integer asInt = field1.getAsInt("index.number_of_shards", null);
    System.out.println(asInt);
    String field2 = settings.getSetting("field", "index.refresh_interval");
    Settings field3 = settings.getIndexToDefaultSettings().get("field");
    System.out.println(field2);
    System.out.println(field3);
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName puttemplate
   * @Description 放置模板
   * @Author zhangqiang
   * @Date 2020/9/26 16:27
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/puttemplate
  @RequestMapping("/puttemplate")
  public String puttemplate() throws IOException {

    PutIndexTemplateRequest putIndexTemplateRequest = new PutIndexTemplateRequest("my-template");
    putIndexTemplateRequest.patterns(Arrays.asList("pattern-1", "log-*"));

    //分片数 副本数
    putIndexTemplateRequest.settings(Settings.builder().put("index.number_of_shards", 3)
        .put("index.number_of_replicas", 1));

       /* putIndexTemplateRequest.mapping(
                "{\n" +
                        "{ \"properties\": {\n" +
                        "  \"message\": {\n" +
                        " \"type\":\"text\" \n" +
                        "}\n" +
                        "}\n" +
                        "}",
                XContentType.JSON);*/

        /*HashMap<String, Object> jsonMap = new HashMap<>();
        {
            HashMap<String, Object> properties = new HashMap<>();
            {
                HashMap<String, Object> message = new HashMap<>();
                message.put("type", "text");
                properties.put("message", message);
            }
            jsonMap.put("properties", properties);
        }
        putIndexTemplateRequest.mapping(jsonMap);*/

    XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
    xContentBuilder.startObject();
    {
      xContentBuilder.startObject("properties");
      {
        xContentBuilder.startObject("message");
        {
          xContentBuilder.field("type", "text");
        }
        xContentBuilder.endObject();
      }
      xContentBuilder.endObject();
    }
    xContentBuilder.endObject();
    putIndexTemplateRequest.mapping(xContentBuilder);

    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT);
    System.out.println(acknowledgedResponse.isAcknowledged());

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName ValidateQueryRequest
   * @Description 验证查询
   * @Author zhangqiang
   * @Date 2020/9/26 16:29
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/ValidateQueryRequest
  @RequestMapping("/ValidateQueryRequest")
  public String ValidateQueryRequest() throws IOException {
    ValidateQueryRequest validateQueryRequest = new ValidateQueryRequest("field");
    QueryBuilder queryBuilder = QueryBuilders
        .boolQuery()
        .must(QueryBuilders.queryStringQuery("*:*"))
        .filter(QueryBuilders.termQuery("user", "kimchy"));
    validateQueryRequest.query(queryBuilder);
    validateQueryRequest.explain(true);
    validateQueryRequest.allShards(true);
    validateQueryRequest.rewrite(true);
    ValidateQueryResponse validateQueryResponse = restHighLevelClient.indices()
        .validateQuery(validateQueryRequest, RequestOptions.DEFAULT);
    System.out.println(JSON.toJSON(validateQueryResponse));
    boolean valid = validateQueryResponse.isValid();
    int totalShards = validateQueryResponse.getTotalShards();
    int successfulShards = validateQueryResponse.getSuccessfulShards();
    int failedShards = validateQueryResponse.getFailedShards();
    System.out.println("valid:" + valid);
    System.out.println("totalShards:" + totalShards);
    System.out.println("successfulShards:" + successfulShards);
    System.out.println("failedShards:" + failedShards);
    if (failedShards > 0) {
      for (DefaultShardOperationFailedException failure : validateQueryResponse
          .getShardFailures()) {
        String index = failure.index();
        int shardId = failure.shardId();
        String reason = failure.reason();
        System.out.println("***********************");
        System.out.println("failure:" + index);
        System.out.println("failure:" + shardId);
        System.out.println("failure:" + reason);
        System.out.println("***********************");
      }
    }
    for (QueryExplanation queryExplanation : validateQueryResponse.getQueryExplanation()) {
      String index = queryExplanation.getIndex();
      int shard = queryExplanation.getShard();
      System.out.println("======================");
      String explanation = queryExplanation.getExplanation();
      System.out.println("queryExplanation:" + index);
      System.out.println("queryExplanation:" + shard);
      System.out.println("queryExplanation:" + explanation);
      System.out.println("======================");
    }
    return "11111111111111111111111";
  }


  /**
   * @ClassName zhangqiangtest
   * @MethodName GetIndexTemplateRequest
   * @Description 获取索引模板
   * @Author zhangqiang
   * @Date 2020/9/26 16:53
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/GetIndexTemplateRequest
  @RequestMapping("/GetIndexTemplateRequest")
  public String GetIndexTemplateRequest() throws IOException {
    GetIndexTemplatesRequest getIndexTemplatesRequest = new GetIndexTemplatesRequest("my-template");
    getIndexTemplatesRequest.setMasterNodeTimeout(TimeValue.timeValueMinutes(2));
    GetIndexTemplatesResponse indexTemplate = restHighLevelClient.indices()
        .getIndexTemplate(getIndexTemplatesRequest, RequestOptions.DEFAULT);
    System.out.println(indexTemplate.getIndexTemplates());
    System.out.println(JSON.toJSON(indexTemplate.getIndexTemplates()));
    List<IndexTemplateMetaData> indexTemplates = indexTemplate.getIndexTemplates();
    for (IndexTemplateMetaData da : indexTemplates) {
      System.out.println(da.name());
      System.out.println(da.aliases());
      MappingMetaData mappings = da.mappings();
      Map<String, Object> sourceAsMap = mappings.getSourceAsMap();
      System.out.println(JSON.toJSON(sourceAsMap));
    }
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName IndexTemplateExistRequest
   * @Description 索引模板是否存在
   * @Author zhangqiang
   * @Date 2020/9/26 16:58
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/IndexTemplateExistRequest
  @RequestMapping("/IndexTemplateExistRequest")
  public String IndexTemplateExistRequest() throws IOException {
//        IndexTemplatesExistRequest indexTemplatesExistRequest = new IndexTemplatesExistRequest("my-template");
    IndexTemplatesExistRequest indexTemplatesExistRequest = new IndexTemplatesExistRequest(
        "my-template1");
    indexTemplatesExistRequest.setLocal(true);
    indexTemplatesExistRequest.setMasterNodeTimeout(TimeValue.timeValueMinutes(5));
    boolean b = restHighLevelClient.indices()
        .existsTemplate(indexTemplatesExistRequest, RequestOptions.DEFAULT);
    System.out.println(b);
    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName GetIndexRequest
   * @Description 获取索引信息
   * @Author zhangqiang
   * @Date 2020/9/26 17:16
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/GetIndexRequest
  @RequestMapping("/GetIndexRequest")
  public String GetIndexRequest() throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest("field");
    GetIndexResponse getIndexResponse = restHighLevelClient.indices()
        .get(getIndexRequest, RequestOptions.DEFAULT);
//        System.out.println("getIndexResponse:"+JSON.toJSON(getIndexResponse));
    MappingMetaData mappingMetaData = getIndexResponse.getMappings().get("field");
    Map<String, Object> sourceAsMap = mappingMetaData.getSourceAsMap();
    System.out.println("sourceAsMap:" + JSON.toJSON(sourceAsMap));
    List<AliasMetaData> field = getIndexResponse.getAliases().get("field");
    System.out.println("field:" + JSON.toJSON(field));
    String field1 = getIndexResponse.getSetting("field", "index.number_of_shards");
    System.out.println("field1:" + JSON.toJSON(field1));
    Settings field2 = getIndexResponse.getSettings().get("field");
    Integer asInt = field2.getAsInt("index.number_of_shards", null);
    System.out.println("asInt:" + asInt);
       /* TimeValue field3 = getIndexResponse.getDefaultSettings().get("field").getAsTime("index.refresh_interval", null);
        System.out.println("field3"+field3);*/

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName freezeindex
   * @Description 冻结索引
   * @Author zhangqiang
   * @Date 2020/9/26 17:17
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/freezeindex
  @RequestMapping("/freezeindex")
  public String freezeindex() throws IOException {

    FreezeIndexRequest field = new FreezeIndexRequest("field");
    ShardsAcknowledgedResponse freeze = restHighLevelClient.indices()
        .freeze(field, RequestOptions.DEFAULT);
    System.out.println(freeze);

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName unfreezeindex
   * @Description 取消冻结索引
   * @Author zhangqiang
   * @Date 2020/9/26 17:18
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/unfreezeindex
  @RequestMapping("/unfreezeindex")
  public String unfreezeindex() throws IOException {

    UnfreezeIndexRequest field = new UnfreezeIndexRequest("field");
    ShardsAcknowledgedResponse unfreeze = restHighLevelClient.indices()
        .unfreeze(field, RequestOptions.DEFAULT);
    System.out.println(unfreeze);

    return "11111111111111111111111";
  }

  /**
   * @ClassName zhangqiangtest
   * @MethodName deleteTemplaterequest
   * @Description 删除索引模板
   * @Author zhangqiang
   * @Date 2020/9/26 17:30
   * @Param []
   * @Return java.lang.String
   **/
  //    http://localhost:8080/testzq/deleteTemplaterequest
  @RequestMapping("/deleteTemplaterequest")
  public String deleteTemplaterequest() throws IOException {
    DeleteIndexTemplateRequest deleteIndexTemplateRequest = new DeleteIndexTemplateRequest(
        "my-template1");
    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .deleteTemplate(deleteIndexTemplateRequest, RequestOptions.DEFAULT);
    System.out.println(acknowledgedResponse);
    return "11111111111111111111111";
  }
}
