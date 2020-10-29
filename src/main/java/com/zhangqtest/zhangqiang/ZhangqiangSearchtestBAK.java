/*
package com.zhangqtest.zhangqiang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lnsoft.common.elasticsearch.config.restHighLevelClient;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.client.security.GetUsersRequest;
import org.elasticsearch.client.security.GetUsersResponse;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

*/
/**
 * @PackageName com.lnsoft.intelligentsearch.controller
 * @ClassName ZhangqiangSearchtest
 * @Description 搜索相关的api
 * @Author zhangqiang
 * @Date 2020/9/27 17:48
 * @Version 1.0
 **//*

@RestController
@RequestMapping("/zhangqiangsearchbak")
public class ZhangqiangSearchtestBAK {

  @Autowired
  private RestHighLevelClient restHighLevelClient;


  */
/**
   * @ClassName ZhangqiangSearchtest
   * @MethodName createindexSettingandinsertdatasss
   * @Description 此方法可行  创建索引并设置参数并插入数据
   * @Author zhangqiang
   * @Date 2020/10/12 12:10
   * @Param [indexname, str]
   * @Return void
   **//*

  //  http://localhost:8080/zhangqiangsearchbak/createindexSettingandinsertdatasssaaa/ceshi3/name,address,title,content
  @RequestMapping("/createindexSettingandinsertdatasssaaa/{indexname}/{str}")
  public void createindexSettingandinsertdatasssaaa(@PathVariable String indexname,
      @PathVariable String str) throws IOException, InterruptedException {
    CreateIndexRequest ceshi = new CreateIndexRequest(indexname);
    HashMap<String, Object> analyzerandtokenizer = new HashMap<>();
    HashMap<String, Object> pinyin_analyzer = new HashMap<>();
    HashMap<String, Object> tokenizer = new HashMap<>();
    HashMap<String, Object> my_pinyin = new HashMap<>();
    HashMap<String, Object> my_pinyins = new HashMap<>();
    tokenizer.put("tokenizer", "my_pinyin");
    pinyin_analyzer.put("pinyin_analyzer", tokenizer);
    analyzerandtokenizer.put("analyzer", pinyin_analyzer);
    my_pinyin.put("type", "pinyin");
    my_pinyin.put("keep_separate_first_letter", "true");
    my_pinyin.put("limit_first_letter_length", "50");
    my_pinyin.put("keep_full_pinyin", "true");
    my_pinyin.put("keep_joined_full_pinyin", "true");
    my_pinyin.put("keep_none_chinese", "true");
    my_pinyin.put("keep_none_chinese_together", "true");
    my_pinyin.put("keep_none_chinese_in_first_letter", "true");
    my_pinyin.put("keep_none_chinese_in_joined_full_pinyin", "true");
    my_pinyin.put("none_chinese_pinyin_tokenize", "true");
    my_pinyin.put("keep_original", "true");
    my_pinyin.put("lowercase", "true");
    my_pinyin.put("trim_whitespace", "true");
    my_pinyin.put("remove_duplicated_term", "false");
    my_pinyins.put("my_pinyin", my_pinyin);
    analyzerandtokenizer.put("tokenizer", my_pinyins);

    HashMap<String, Object> settings = new HashMap<>();
    settings.put("number_of_shards", "5");
    settings.put("number_of_replicas", "1");
    //设置时间
    settings.put("default_pipeline", "my_timestamp_pipeline");

    settings.put("max_result_window", "2000000000");
    settings.put("analysis", analyzerandtokenizer);

    ceshi.settings(JSON.toJSONString(settings), XContentType.JSON);

    ceshi.setTimeout(TimeValue.timeValueHours(5));
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
        .create(ceshi, RequestOptions.DEFAULT);
    //设置mapping
    // 中英文结合的还没有设置setting

    HashMap<String, Object> stringObjectHashMap = new HashMap<>();
    stringObjectHashMap.put("description", "张强自定义时间");
    ArrayList<Object> objects = new ArrayList<>();
    HashMap<String, Object> s1 = new HashMap<>();
    HashMap<String, Object> s2 = new HashMap<>();
    s1.put("field", "zhangqiangdate");
    s1.put("value", "{{_ingest.timestamp}}");
    s2.put("set", s1);
    objects.add(s2);
    stringObjectHashMap.put("processors", objects);
    String sources = JSON.toJSONString(stringObjectHashMap);
    PutPipelineRequest putPipelineRequest = new PutPipelineRequest(
        "my_timestamp_pipeline",
        new BytesArray(sources.getBytes(StandardCharsets.UTF_8)),
        XContentType.JSON
    );
    AcknowledgedResponse acknowledgedResponse1 = restHighLevelClient.ingest()
        .putPipeline(putPipelineRequest, RequestOptions.DEFAULT);

    PutMappingRequest putMappingRequest = new PutMappingRequest(indexname);
    Map<String, Object> jsonMap = new HashMap<>();
    Map<String, Object> message = new HashMap<>();
    message.put("type", "text");
    message.put("fielddata", true);

    Map<String, Object> message1 = new HashMap<>();
//        message1.put("fielddata", true);
    message1.put("type", "keyword");
    Map<String, Object> fieldsmap = new HashMap<>();
    fieldsmap.put("keyword", message1);

    HashMap<String, Object> pinyin = new HashMap<>();
    HashMap<String, Object> pinyinmap = new HashMap<>();

    pinyin.put("type", "text");
    pinyin.put("store", "false");
    pinyin.put("term_vector", "with_offsets");
    pinyin.put("analyzer", "pinyin_analyzer");
    pinyin.put("boost", 10);
    fieldsmap.put("pinyin", pinyin);

    message.put("fields", fieldsmap);

    Map<String, Object> property = new HashMap<>();
//        property.put("name", message);
    //对每一个传入的字段进行设置mapping  张强测试
    String[] split = str.split(",");
    for (String s : split) {
      property.put(s, message);
    }
    jsonMap.put("properties", property);
    putMappingRequest.source(jsonMap);
    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putMapping(putMappingRequest, RequestOptions.DEFAULT);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date=sdf.format(new Date());
    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(new IndexRequest(indexname).id("1").source(JSON.toJSONString(
        new UserZhangqiang("1", "张强", 18,

            "供电服务指挥系统中配网23", "wangerxiao王二小names_pp_dd_hh_", new Date(), sdf.format(new Date()),
            "基于山东电网调度管理应用OMS系统中用户原因跳闸记录，结合营销应用业务系统中高压用户设备信息、供电服务指挥系统中配网设备参数信息，形成分析数据表，应用省公司自助式分析平台开展大数据挖掘分析。首先将相关数据进行数据清洗，处理异常值、缺失值，统一数据结构。其次通过柱状图、折线图、地理图等方式对客户原因造成的停电次数分布、停电时间点分布、累计停电时长、停电原因、停电设备的投运时间等开展数据可视化展示，初")),
        XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("2").source(JSON.toJSONString(
        new UserZhangqiang("2", "张明", 18,
            "张强测试aabbccddae", "明年山东省市场交易电量不低于1600亿千瓦时-省公司新闻", new Date(), sdf.format(new Date()),
            "山东反反复复凤飞飞反复的阳县的")), XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("3").source(JSON.toJSONString(
        new UserZhangqiang("3", "张明", 19,

            "我了个槽fddfald 133d qq aa ", "山东省济南市济阳县的", new Date(), sdf.format(new Date()),
            "上半年，公司受理新装增容申请148.74万户，容量3,213.20万千伏安，同比下降4.50%；完成新装增容131.51万户，容量2,309.56万千伏安，同比上升3.63%。其中第一、第三产业和居民报装容量同比上升39.81%、7.53%、32.68%，一般工商业和居民生活电量增长空间较大；第二产业报装容量同比下降14.25%，主要原因是随着我省新旧动能转换深入实施，部分高污染高耗能企业新增用电需求下降。业扩报装容量增速较快的行业主要为信息技术产业，同比增长47.23%，说明新技术产业新增需求旺盛；建筑业、房地产业同比增长12.29%、9.67%，显示建筑活动和房产开发回暖；制造业同比增长4.61%，说明制造行业产能平稳增长。报装容量下滑较大的行业主要有采矿业，同比下降23.78%；公共服务和管理行业同比下降11.23%。总体来看，传统行业用电需求稳中有升，新动能、新产业用电需求快速增长，在当前业扩接电提质提速的情况下，有助于将用电需求快速转化为售电量增长。 上半年，永久性减容、销户完成420.77万千伏安，同比下降36.37%（主要原因是2018年上半年，济钢拆迁销户，造成大量减容），净增1888.79万千伏安，同比上升20.50%。")),
        XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("4").source(JSON.toJSONString(
        new UserZhangqiang("4", "刘德华", 50,
            "Quick brown rabbits",
            "1.总体用电量 今年以来，全省全社会用电量保持平缓增长态势，上半年完成3008.49亿千瓦时，同比增长3.75%。扣除魏桥集团电量负增长8.21%后，全社会用电量同比增长6.35%，增幅同比回落1.22个百分点。全省用电保持平缓增长的主要原因：一是随着新旧动能转换工作的持续推进，全省工业去产能工作不断深化，重点工业行业市场供需改善，企业产能利用水平总体保持稳定；二是投资、消费、出口三大传统需求增长放缓，发展新动能动力不足，经济下行压力较大，对用电增长带来一定的不利影响。 2020年上半年全社会用电量 2.分产业用电量 从各产业来看，上半年，第一产业用电量稳步增长，完成38.86亿千瓦时，同比增长9.71%；第二产业用电量低速增长，完成2291.84亿千瓦时，同比增长1.93%；第三产业用电量较快增长，完成341.11亿千瓦时，同比增长10.50%；居民生活用电量平稳增长，完成336.67亿千瓦时，同比增长9.64%。 3.工业及重点行业用电量 上半年，全省工业用电量完成2262.49亿千瓦时，同比增长1.70%，增幅较全社会用电量低2.05个百分点。四大高耗能行业合计用电量1154.60亿千瓦时，同比下降1.06%，降幅同比收窄3.14个百分点。其中有色金属冶炼和压延加工行业用电同比下降9.29%，主要原因是受国际贸易形势复杂、成本上升、消费不振、环保力度加大，行业整体效益下滑等因素影响，行业用电下滑幅度较大。非金属矿物制品业、黑色金属冶炼和压延加工、化工行业用电同比均保持增长态势，增幅分别为15.54%、13.10%和2.36%。 4.发电情况 上半年，全省共净增发电装机319.2万千瓦，其中直调公用机组海阳核电1×125万千瓦、八角电厂1×60万千瓦，其余为地方公用及企业自备机组。净增风电45.2万千瓦，光伏76.1万千瓦，分别占全省净增装机的14.16%、23.84%。至6月底，全省发电装机总容量13425.7万千瓦，其中直调公用电厂5955.5万千瓦。 上半年，全省发电量完成2606.00亿千瓦时，同比增长1.75%。平均设备利用小时1945小时，同比下降93小时。其中火电机组平均利用小时2200小时，同比下降119小时。",
            new Date(), sdf.format(new Date()),
            "2020年全社会用电量统计（山东省）")), XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("5").source(JSON.toJSONString(
        new UserZhangqiang("5", "王二小", 100,

            "Brown rabbits are commonly seen.", "wangerxiao王二小names_pp_dd_hh_", new Date(),
            sdf.format(new Date()),
            "基于山东电网调度管理应用OMS系统中用户原因跳闸记录，结合营销应用业务系统中高压用户设备信息、供电服务指挥系统中配网设备参数信息，形成分析数据表，应用省公司自助式分析平台开展大数据挖掘分析。首先将相关数据进行数据清洗，处理异常值、缺失值，统一数据结构。其次通过柱状图、折线图、地理图等方式对客户原因造成的停电次数分布、停电时间点分布、累计停电时长、停电原因、停电设备的投运时间等开展数据可视化展示，初")),
        XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("6").source(JSON.toJSONString(
        new UserZhangqiang("6", "李白", 65,
            "Keeping pets healthy", "陈志奇 琪 1i 强 s 强奸 奸 人贱", new Date(), sdf.format(new Date()),
            "Keeping pets healthy")), XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("7").source(JSON.toJSONString(
        new UserZhangqiang("7", "兰陵王", 45,
            "明年山东省", "市场交易电量不低于1600亿千瓦时-省公司新闻", new Date(), sdf.format(new Date()),
            "明年山东省市场交易电量不低于1600亿千瓦时-省公司新闻")), XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("8").source(JSON.toJSONString(
        new UserZhangqiang("8", "凯", 65,
            "我要飞来了", "好的 没问题,names pp dd bb dfsasaf", new Date(), sdf.format(new Date()),
            "好的 没问题,names pp dd bb dfsasaf")), XContentType.JSON));
    Thread.sleep(1);
    bulkRequest.add(new IndexRequest(indexname).id("9").source(JSON.toJSONString(
        new UserZhangqiang("9", "陈庆河", 500,
            "张强测试张强测试", "张强测试张强测试", new Date(), sdf.format(new Date()),
            "今天测试一把吧haode meiyou wenti l aal")), XContentType.JSON));

    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
  }


  */
/**
   * @ClassName ZhangqiangSearchtest
   * @MethodName createindexSettingandinsertdatasss
   * @Description 此方法可行  创建索引并设置参数并插入数据
   * @Author zhangqiang
   * @Date 2020/10/12 12:10
   * @Param [indexname, str]
   * @Return void
   **//*

  //  http://localhost:8080/zhangqiangsearch/createindexSettingandinsertdatasss/ceshi3/name,address,title,content
  @RequestMapping("/createindexSettingandinsertdatasss/{indexname}/{str}")
  public void createindexSettingandinsertdatasss(@PathVariable String indexname,
      @PathVariable String str) throws IOException {
    CreateIndexRequest ceshi = new CreateIndexRequest(indexname);
    HashMap<String, Object> analyzerandtokenizer = new HashMap<>();
    HashMap<String, Object> pinyin_analyzer = new HashMap<>();
    HashMap<String, Object> tokenizer = new HashMap<>();
    HashMap<String, Object> my_pinyin = new HashMap<>();
    HashMap<String, Object> my_pinyins = new HashMap<>();
    tokenizer.put("tokenizer", "my_pinyin");
    pinyin_analyzer.put("pinyin_analyzer", tokenizer);
    analyzerandtokenizer.put("analyzer", pinyin_analyzer);
    my_pinyin.put("type", "pinyin");
    my_pinyin.put("keep_separate_first_letter", "true");
    my_pinyin.put("limit_first_letter_length", "50");
    my_pinyin.put("keep_full_pinyin", "true");
    my_pinyin.put("keep_joined_full_pinyin", "true");
    my_pinyin.put("keep_none_chinese", "true");
    my_pinyin.put("keep_none_chinese_together", "true");
    my_pinyin.put("keep_none_chinese_in_first_letter", "true");
    my_pinyin.put("keep_none_chinese_in_joined_full_pinyin", "true");
    my_pinyin.put("none_chinese_pinyin_tokenize", "true");
    my_pinyin.put("keep_original", "true");
    my_pinyin.put("lowercase", "true");
    my_pinyin.put("trim_whitespace", "true");
    my_pinyin.put("remove_duplicated_term", "false");
    my_pinyins.put("my_pinyin", my_pinyin);
    analyzerandtokenizer.put("tokenizer", my_pinyins);

    HashMap<String, Object> settings = new HashMap<>();
    settings.put("number_of_shards", "5");
    settings.put("number_of_replicas", "1");
    //设置时间
    settings.put("default_pipeline", "my_timestamp_pipeline");

    settings.put("max_result_window", "2000000000");
    settings.put("analysis", analyzerandtokenizer);

    ceshi.settings(JSON.toJSONString(settings), XContentType.JSON);

    ceshi.setTimeout(TimeValue.timeValueHours(5));
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
        .create(ceshi, RequestOptions.DEFAULT);
    //设置mapping
    // 中英文结合的还没有设置setting

    HashMap<String, Object> stringObjectHashMap = new HashMap<>();
    stringObjectHashMap.put("description", "张强自定义时间");
    ArrayList<Object> objects = new ArrayList<>();
    HashMap<String, Object> s1 = new HashMap<>();
    HashMap<String, Object> s2 = new HashMap<>();
    s1.put("field", "zhangqiangdate");
    s1.put("value", "{{_ingest.timestamp}}");
    s2.put("set", s1);
    objects.add(s2);
    stringObjectHashMap.put("processors", objects);
    String sources = JSON.toJSONString(stringObjectHashMap);
    PutPipelineRequest putPipelineRequest = new PutPipelineRequest(
        "my_timestamp_pipeline",
        new BytesArray(sources.getBytes(StandardCharsets.UTF_8)),
        XContentType.JSON
    );
    AcknowledgedResponse acknowledgedResponse1 = restHighLevelClient.ingest()
        .putPipeline(putPipelineRequest, RequestOptions.DEFAULT);

    PutMappingRequest putMappingRequest = new PutMappingRequest(indexname);
    Map<String, Object> jsonMap = new HashMap<>();
    Map<String, Object> message = new HashMap<>();
    message.put("type", "text");
    message.put("fielddata", true);

    Map<String, Object> message1 = new HashMap<>();
//        message1.put("fielddata", true);
    message1.put("type", "keyword");
    Map<String, Object> fieldsmap = new HashMap<>();
    fieldsmap.put("keyword", message1);

    HashMap<String, Object> pinyin = new HashMap<>();
    HashMap<String, Object> pinyinmap = new HashMap<>();

    pinyin.put("type", "text");
    pinyin.put("store", "false");
    pinyin.put("term_vector", "with_offsets");
    pinyin.put("analyzer", "pinyin_analyzer");
    pinyin.put("boost", 10);
    fieldsmap.put("pinyin", pinyin);

    message.put("fields", fieldsmap);

    Map<String, Object> property = new HashMap<>();
//        property.put("name", message);
    //对每一个传入的字段进行设置mapping  张强测试
    String[] split = str.split(",");
    for (String s : split) {
      property.put(s, message);
    }
    jsonMap.put("properties", property);
    putMappingRequest.source(jsonMap);
    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putMapping(putMappingRequest, RequestOptions.DEFAULT);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date=sdf.format(new Date());
    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(new IndexRequest(indexname).id("1").source(JSON.toJSONString(
        new UserZhangqiang("1", "张强", 18,

            "供电服务指挥系统中配网23", "wangerxiao王二小names_pp_dd_hh_", new Date(), sdf.format(new Date()),
            "基于山东电网调度管理应用OMS系统中用户原因跳闸记录，结合营销应用业务系统中高压用户设备信息、供电服务指挥系统中配网设备参数信息，形成分析数据表，应用省公司自助式分析平台开展大数据挖掘分析。首先将相关数据进行数据清洗，处理异常值、缺失值，统一数据结构。其次通过柱状图、折线图、地理图等方式对客户原因造成的停电次数分布、停电时间点分布、累计停电时长、停电原因、停电设备的投运时间等开展数据可视化展示，初")),
        XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("2").source(JSON.toJSONString(
        new UserZhangqiang("2", "张明", 18,
            "张强测试aabbccddae", "明年山东省市场交易电量不低于1600亿千瓦时-省公司新闻", new Date(), sdf.format(new Date()),
            "山东反反复复凤飞飞反复的阳县的")), XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("3").source(JSON.toJSONString(
        new UserZhangqiang("3", "张明", 19,

            "我了个槽fddfald 133d qq aa ", "山东省济南市济阳县的", new Date(), sdf.format(new Date()),
            "上半年，公司受理新装增容申请148.74万户，容量3,213.20万千伏安，同比下降4.50%；完成新装增容131.51万户，容量2,309.56万千伏安，同比上升3.63%。其中第一、第三产业和居民报装容量同比上升39.81%、7.53%、32.68%，一般工商业和居民生活电量增长空间较大；第二产业报装容量同比下降14.25%，主要原因是随着我省新旧动能转换深入实施，部分高污染高耗能企业新增用电需求下降。业扩报装容量增速较快的行业主要为信息技术产业，同比增长47.23%，说明新技术产业新增需求旺盛；建筑业、房地产业同比增长12.29%、9.67%，显示建筑活动和房产开发回暖；制造业同比增长4.61%，说明制造行业产能平稳增长。报装容量下滑较大的行业主要有采矿业，同比下降23.78%；公共服务和管理行业同比下降11.23%。总体来看，传统行业用电需求稳中有升，新动能、新产业用电需求快速增长，在当前业扩接电提质提速的情况下，有助于将用电需求快速转化为售电量增长。 上半年，永久性减容、销户完成420.77万千伏安，同比下降36.37%（主要原因是2018年上半年，济钢拆迁销户，造成大量减容），净增1888.79万千伏安，同比上升20.50%。")),
        XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("4").source(JSON.toJSONString(
        new UserZhangqiang("4", "刘德华", 50,
            "Quick brown rabbits",
            "1.总体用电量 今年以来，全省全社会用电量保持平缓增长态势，上半年完成3008.49亿千瓦时，同比增长3.75%。扣除魏桥集团电量负增长8.21%后，全社会用电量同比增长6.35%，增幅同比回落1.22个百分点。全省用电保持平缓增长的主要原因：一是随着新旧动能转换工作的持续推进，全省工业去产能工作不断深化，重点工业行业市场供需改善，企业产能利用水平总体保持稳定；二是投资、消费、出口三大传统需求增长放缓，发展新动能动力不足，经济下行压力较大，对用电增长带来一定的不利影响。 2020年上半年全社会用电量 2.分产业用电量 从各产业来看，上半年，第一产业用电量稳步增长，完成38.86亿千瓦时，同比增长9.71%；第二产业用电量低速增长，完成2291.84亿千瓦时，同比增长1.93%；第三产业用电量较快增长，完成341.11亿千瓦时，同比增长10.50%；居民生活用电量平稳增长，完成336.67亿千瓦时，同比增长9.64%。 3.工业及重点行业用电量 上半年，全省工业用电量完成2262.49亿千瓦时，同比增长1.70%，增幅较全社会用电量低2.05个百分点。四大高耗能行业合计用电量1154.60亿千瓦时，同比下降1.06%，降幅同比收窄3.14个百分点。其中有色金属冶炼和压延加工行业用电同比下降9.29%，主要原因是受国际贸易形势复杂、成本上升、消费不振、环保力度加大，行业整体效益下滑等因素影响，行业用电下滑幅度较大。非金属矿物制品业、黑色金属冶炼和压延加工、化工行业用电同比均保持增长态势，增幅分别为15.54%、13.10%和2.36%。 4.发电情况 上半年，全省共净增发电装机319.2万千瓦，其中直调公用机组海阳核电1×125万千瓦、八角电厂1×60万千瓦，其余为地方公用及企业自备机组。净增风电45.2万千瓦，光伏76.1万千瓦，分别占全省净增装机的14.16%、23.84%。至6月底，全省发电装机总容量13425.7万千瓦，其中直调公用电厂5955.5万千瓦。 上半年，全省发电量完成2606.00亿千瓦时，同比增长1.75%。平均设备利用小时1945小时，同比下降93小时。其中火电机组平均利用小时2200小时，同比下降119小时。",
            new Date(), sdf.format(new Date()),
            "2020年全社会用电量统计（山东省）")), XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("5").source(JSON.toJSONString(
        new UserZhangqiang("5", "王二小", 100,

            "Brown rabbits are commonly seen.", "wangerxiao王二小names_pp_dd_hh_", new Date(),
            sdf.format(new Date()),
            "基于山东电网调度管理应用OMS系统中用户原因跳闸记录，结合营销应用业务系统中高压用户设备信息、供电服务指挥系统中配网设备参数信息，形成分析数据表，应用省公司自助式分析平台开展大数据挖掘分析。首先将相关数据进行数据清洗，处理异常值、缺失值，统一数据结构。其次通过柱状图、折线图、地理图等方式对客户原因造成的停电次数分布、停电时间点分布、累计停电时长、停电原因、停电设备的投运时间等开展数据可视化展示，初")),
        XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("6").source(JSON.toJSONString(
        new UserZhangqiang("6", "李白", 65,
            "Keeping pets healthy", "陈志奇 琪 1i 强 s 强奸 奸 人贱", new Date(), sdf.format(new Date()),
            "Keeping pets healthy")), XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("7").source(JSON.toJSONString(
        new UserZhangqiang("7", "兰陵王", 45,
            "明年山东省", "市场交易电量不低于1600亿千瓦时-省公司新闻", new Date(), sdf.format(new Date()),
            "明年山东省市场交易电量不低于1600亿千瓦时-省公司新闻")), XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("8").source(JSON.toJSONString(
        new UserZhangqiang("8", "凯", 65,
            "我要飞来了", "好的 没问题,names pp dd bb dfsasaf", new Date(), sdf.format(new Date()),
            "好的 没问题,names pp dd bb dfsasaf")), XContentType.JSON));
    bulkRequest.add(new IndexRequest(indexname).id("9").source(JSON.toJSONString(
        new UserZhangqiang("9", "陈庆河", 500,
            "张强测试张强测试", "张强测试张强测试", new Date(), sdf.format(new Date()),
            "今天测试一把吧haode meiyou wenti l aal")), XContentType.JSON));

    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
  }


  //  http://localhost:8080/zhangqiangsearch/HighlightBuilderaaaaa/强
  @RequestMapping("/HighlightBuilderaaaaa/{names}")
  public ArrayList<Map<String, Object>> HighlightBuilderaaaaa(@PathVariable String names)
      throws IOException {


     */
/* BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest8").source(JSON.toJSONString(new User(123132123,"更尊令","基于山东电网调度管理应用OMS系统中用户原因跳闸记录，结合营销应用业务系统中高压用户设备信息、供电服务指挥系统中配网设备参数信息，形成分析数据表，应用省公司自助式分析平台开展大数据挖掘分析。首先将相关数据进行数据清洗，处理异常值、缺失值，统一数据结构。其次通过柱状图、折线图、地理图等方式对客户原因造成的停电次数分布、停电时间点分布、累计停电时长、停电原因、停电设备的投运时间等开展数据可视化展示，初")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);

*//*


    SearchRequest zqtest2 = new SearchRequest("zqtest8");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //这个可以模糊匹配
    String searchword = names;
    searchSourceBuilder.query(
                */
/*QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("name.keyword", "*" + searchword + "*"))
                        .should(QueryBuilders.matchQuery("name", searchword))
                        .should(QueryBuilders.matchPhraseQuery("name", searchword))*//*

        QueryBuilders.boolQuery()
            .should(QueryBuilders.wildcardQuery("address.keyword", "*" + searchword + "*"))
            .should(QueryBuilders.matchQuery("address", searchword))
            .should(QueryBuilders.matchPhraseQuery("address", searchword))
    );
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field("name");
    //防止高亮的字段数据展示补全
    highlightBuilder.field("address").fragmentSize(1000000).numOfFragments(0);
    highlightBuilder.highlighterType("unified");
    highlightBuilder.requireFieldMatch(false);//多个高亮展示！
    highlightBuilder.preTags("<span style='color:red'>");
    highlightBuilder.postTags("</span>");
    searchSourceBuilder.highlighter(highlightBuilder);

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.MILLISECONDS));
    zqtest2.source(searchSourceBuilder);
//        zqtest2.preference("_primary_first");
//        zqtest2.preference("_local");
//        zqtest2.preference("_shards:1");
//        zqtest2.preference("_shards:1,2,3,4,5");
//        zqtest2.preference("_primary");
//        zqtest2.preference("_only_nodes:node-1");
    SearchResponse search = restHighLevelClient.search(zqtest2, RequestOptions.DEFAULT);

    ArrayList<Map<String, Object>> list = new ArrayList<>();
      */
/*  for(SearchHit documentFields:search.getHits().getHits()){
            list.add(documentFields.getSourceAsMap());
        }*//*

    for (SearchHit hit : search.getHits().getHits()) {
      Map<String, HighlightField> highlightFields = hit.getHighlightFields();
      HighlightField name = highlightFields.get("name");
      Map<String, Object> sourceMap = hit.getSourceAsMap();//原来的结果
      if (name != null) {
        Text[] fragments = name.fragments();
        String n_name = "";
        for (Text text : fragments) {
          n_name += text;
        }
        sourceMap.put("name", "<span style='color:blue'><u>" + n_name + "</u></span>");
      }
      HighlightField address = highlightFields.get("address");
      if (address != null) {
        Text[] fragments = address.fragments();
        String n_address = "";
        for (Text text : fragments) {
          n_address += text;
        }
        sourceMap.put("address", "<span style='color:blue'>" + n_address + "</span>");
      }

      list.add(sourceMap);
    }
    return list;
  }











*/
/*BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(13,"张强张强1","山东省济南市济阳县的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(13,"陈庆河","山东谁知道是哪里的哪里的哪里的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(13,"陈庆河123","山东省方法反反复复反复发圣诞济阳县的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(14,"陈庆河123陈庆河123陈庆河","山东省啊啊啊啊啊啊啊啊啊啊啊啊阳县的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(14,"wangerxiao王二小names_pp_dd_hh_","山东反反复复凤飞飞反复的阳县的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(14,"陈志奇 琪 1i  强 s  强奸  奸   人贱","山东省顶顶顶顶顶大大大济阳县的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(15,"好的 没问题,names pp dd bb dfsasaf","山东省济嘎嘎嘎灌灌灌灌灌高达阳县的")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*



*/
/*

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(13,"2020年上半年业扩报装对售电量的影响","上半年，公司受理新装增容申请148.74万户，容量3,213.20万千伏安，同比下降4.50%；完成新装增容131.51万户，容量2,309.56万千伏安，同比上升3.63%。其中第一、第三产业和居民报装容量同比上升39.81%、7.53%、32.68%，一般工商业和居民生活电量增长空间较大；第二产业报装容量同比下降14.25%，主要原因是随着我省新旧动能转换深入实施，部分高污染高耗能企业新增用电需求下降。业扩报装容量增速较快的行业主要为信息技术产业，同比增长47.23%，说明新技术产业新增需求旺盛；建筑业、房地产业同比增长12.29%、9.67%，显示建筑活动和房产开发回暖；制造业同比增长4.61%，说明制造行业产能平稳增长。报装容量下滑较大的行业主要有采矿业，同比下降23.78%；公共服务和管理行业同比下降11.23%。总体来看，传统行业用电需求稳中有升，新动能、新产业用电需求快速增长，在当前业扩接电提质提速的情况下，有助于将用电需求快速转化为售电量增长。\n" +
                "\n" +
                "       上半年，永久性减容、销户完成420.77万千伏安，同比下降36.37%（主要原因是2018年上半年，济钢拆迁销户，造成大量减容），净增1888.79万千伏安，同比上升20.50%。")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(13,"2020年上半年售电量市场占有率","上半年，公司市场占有率完成70.56%，同比增长0.92%。临沂、莱芜分别受鑫海科技、泰山钢铁新建自备电厂影响，市场占有率分别下滑1.32%、2.67%；德州受莱钢永锋自备电厂自发自用电量增加影响，市场占有率下滑0.95%。（详见附表）")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(13,"2020年全社会用电量统计（山东省）","\n" +
                "1.总体用电量\n" +
                "\n" +
                "      今年以来，全省全社会用电量保持平缓增长态势，上半年完成3008.49亿千瓦时，同比增长3.75%。扣除魏桥集团电量负增长8.21%后，全社会用电量同比增长6.35%，增幅同比回落1.22个百分点。全省用电保持平缓增长的主要原因：一是随着新旧动能转换工作的持续推进，全省工业去产能工作不断深化，重点工业行业市场供需改善，企业产能利用水平总体保持稳定；二是投资、消费、出口三大传统需求增长放缓，发展新动能动力不足，经济下行压力较大，对用电增长带来一定的不利影响。\n" +
                "\n" +
                "2020年上半年全社会用电量\n" +
                "\n" +
                "\n" +
                "2.分产业用电量\n" +
                "\n" +
                "       从各产业来看，上半年，第一产业用电量稳步增长，完成38.86亿千瓦时，同比增长9.71%；第二产业用电量低速增长，完成2291.84亿千瓦时，同比增长1.93%；第三产业用电量较快增长，完成341.11亿千瓦时，同比增长10.50%；居民生活用电量平稳增长，完成336.67亿千瓦时，同比增长9.64%。\n" +
                "\n" +
                "\n" +
                "3.工业及重点行业用电量\n" +
                "\n" +
                "       上半年，全省工业用电量完成2262.49亿千瓦时，同比增长1.70%，增幅较全社会用电量低2.05个百分点。四大高耗能行业合计用电量1154.60亿千瓦时，同比下降1.06%，降幅同比收窄3.14个百分点。其中有色金属冶炼和压延加工行业用电同比下降9.29%，主要原因是受国际贸易形势复杂、成本上升、消费不振、环保力度加大，行业整体效益下滑等因素影响，行业用电下滑幅度较大。非金属矿物制品业、黑色金属冶炼和压延加工、化工行业用电同比均保持增长态势，增幅分别为15.54%、13.10%和2.36%。\n" +
                "\n" +
                "\n" +
                "4.发电情况\n" +
                "\n" +
                "       上半年，全省共净增发电装机319.2万千瓦，其中直调公用机组海阳核电1×125万千瓦、八角电厂1×60万千瓦，其余为地方公用及企业自备机组。净增风电45.2万千瓦，光伏76.1万千瓦，分别占全省净增装机的14.16%、23.84%。至6月底，全省发电装机总容量13425.7万千瓦，其中直调公用电厂5955.5万千瓦。\n" +
                "\n" +
                "上半年，全省发电量完成2606.00亿千瓦时，同比增长1.75%。平均设备利用小时1945小时，同比下降93小时。其中火电机组平均利用小时2200小时，同比下降119小时。")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(14,"明年山东省市场交易电量不低于1600亿千瓦时-省公司新闻","山东省啊啊啊啊啊啊啊啊啊啊啊啊阳县的")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(14,"1-10月全国全社会用电量同比增长8.7%-省公司新闻","山东反反复复凤飞飞反复的阳县的")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
*//*



  //  http://localhost:8080/zhangqiangsearch/createindexsssyanshi
  @RequestMapping("/createindexsssyanshi")
  public SearchResponse createindexsssyanshi() throws IOException {

    SearchRequest searchRequest = new SearchRequest("zqtest7");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

//         searchSourceBuilder.aggregation(AggregationBuilders.terms("results").field("name"));

        */
/*searchSourceBuilder.aggregation(

                AggregationBuilders.terms("zhangqiffdsaang").field("name.keyword")
        );*//*

//        searchSourceBuilder.sort("name.keyword",SortOrder.ASC);

     */
/*   searchSourceBuilder.query(
                QueryBuilders.matchPhraseQuery("name", "张强")
        );*//*

    searchSourceBuilder.aggregation(
        AggregationBuilders.terms("test").field("name.keyword")
    );

    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
*/
/*        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest("zqtest6").id("9g5Z3nQBOnCpEpbJxeBp"));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(2,"张强","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(3,"张明","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(4,"李四","山东4")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(5,"王二小","山东5")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"黑丝","山东6")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(2,"网名","山东7")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*


  }


  //  http://localhost:8080/zhangqiangsearch/createindexsssyanshiaa
  @RequestMapping("/createindexsssyanshiaa")
  public SearchResponse createindexsssyanshiaa() throws IOException {
    SearchRequest searchRequest = new SearchRequest("zqtest7");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      */
/*  searchSourceBuilder.aggregation(
                //group by name  分组  取前几条数据
                AggregationBuilders.terms("test").field("name.keyword")
                        //可以指定多少条
                        .size(1)
        );*//*

     */
/*   searchSourceBuilder.query(QueryBuilders.boolQuery()
//            .should(QueryBuilders.matchPhraseQuery("name","张强"))
                //filter 是 想要数据 age=2的数据   并且过滤 想要 name是  张强的数据
                .filter(QueryBuilders.termQuery("age","2"))
                .filter(QueryBuilders.termQuery("name.keyword","张强"))
        );*//*

//        searchSourceBuilder.from(9999).size(10);
       */
/* searchSourceBuilder.aggregation(
                //group by age  统计完并按照age 升序排序
//               AggregationBuilders.terms("zqtesdfds").field("age").order(BucketOrder.count(true))
                //group by age  统计完并按照age 降序排序
               AggregationBuilders.terms("zqtesdfds").field("age").order(BucketOrder.count(false))
        );*//*

    //结果进行按照age字段进行升序排序
//        searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.ASC));

    searchSourceBuilder.query(
        QueryBuilders.matchPhraseQuery("name", "zhang").analyzer("pinyin")
    );
    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;

  }

  //  http://localhost:8080/zhangqiangsearch/createindexsssyanshiaabbb
  @RequestMapping("/createindexsssyanshiaabbb")
  public SearchResponse createindexsssyanshiaabbb() throws IOException {

       */
/* BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(2,"张强","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(3,"张明","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(4,"李四","山东4")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(5,"王二小","山东5")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"黑丝","山东6")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(2,"网名","山东7")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*


    SearchRequest searchRequest = new SearchRequest("pinyin");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(
        QueryBuilders.boolQuery()
            .should(QueryBuilders.matchQuery("name.pinyin", "ldh").analyzer("pinyin_analyzer"))
    );

    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;

  }

  */
/**
   * @ClassName ZhangqiangSearchtest
   * @MethodName createindexSettingandinsertdata
   * @Description 创建索引，并设置参数(方法可用)
   * @Author zhangqiang
   * @Date 2020/10/11 11:46
   * @Param [indexname]
   * @Return void
   **//*

  //  http://localhost:8080/zhangqiangsearch/createindexSettingandinsertdata/ceshi3
  @RequestMapping("/createindexSettingandinsertdata/{indexname}")
  public void createindexSettingandinsertdata(@PathVariable String indexname) throws IOException {
    CreateIndexRequest ceshi = new CreateIndexRequest(indexname);
    HashMap<String, Object> analyzerandtokenizer = new HashMap<>();
    HashMap<String, Object> pinyin_analyzer = new HashMap<>();
    HashMap<String, Object> tokenizer = new HashMap<>();
    HashMap<String, Object> my_pinyin = new HashMap<>();
    HashMap<String, Object> my_pinyins = new HashMap<>();
    tokenizer.put("tokenizer", "my_pinyin");
    pinyin_analyzer.put("pinyin_analyzer", tokenizer);
    analyzerandtokenizer.put("analyzer", pinyin_analyzer);
    my_pinyin.put("type", "pinyin");
    my_pinyin.put("keep_separate_first_letter", "true");
    my_pinyin.put("limit_first_letter_length", "50");
    my_pinyin.put("keep_full_pinyin", "true");
    my_pinyin.put("keep_joined_full_pinyin", "true");
    my_pinyin.put("keep_none_chinese", "true");
    my_pinyin.put("keep_none_chinese_together", "true");
    my_pinyin.put("keep_none_chinese_in_first_letter", "true");
    my_pinyin.put("keep_none_chinese_in_joined_full_pinyin", "true");
    my_pinyin.put("none_chinese_pinyin_tokenize", "true");
    my_pinyin.put("keep_original", "true");
    my_pinyin.put("lowercase", "true");
    my_pinyin.put("trim_whitespace", "true");
    my_pinyin.put("remove_duplicated_term", "false");
    my_pinyins.put("my_pinyin", my_pinyin);
    analyzerandtokenizer.put("tokenizer", my_pinyins);

    HashMap<String, Object> settings = new HashMap<>();
    settings.put("number_of_shards", "5");
    settings.put("number_of_replicas", "1");
    settings.put("max_result_window", "2000000000");
    settings.put("analysis", analyzerandtokenizer);

    ceshi.settings(JSON.toJSONString(settings), XContentType.JSON);

    ceshi.setTimeout(TimeValue.timeValueHours(5));
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
        .create(ceshi, RequestOptions.DEFAULT);
    //设置mapping
    // 中英文结合的还没有设置setting

    PutMappingRequest putMappingRequest = new PutMappingRequest(indexname);
        */
/*HashMap<String, Object> properties = new HashMap<>();
        HashMap<String, Object> pinyin = new HashMap<>();
        HashMap<String, Object> pinyinmap = new HashMap<>();
        HashMap<String, Object> name = new HashMap<>();
        HashMap<String, Object> pro = new HashMap<>();
        pinyin.put("type","text");
        pinyin.put("store","false");
        pinyin.put("term_vector","with_offsets");
        pinyin.put("analyzer","pinyin_analyzer");
        pinyin.put("boost",10);
        pinyinmap.put("pinyin",pinyin);

        name.put("type","keyword");
        name.put("fields",pinyinmap);
        pro.put("name",name);
        properties.put("properties",pro);
        putMappingRequest.source(properties);*//*


    Map<String, Object> jsonMap = new HashMap<>();
    Map<String, Object> message = new HashMap<>();
    message.put("type", "text");
    message.put("fielddata", true);

    Map<String, Object> message1 = new HashMap<>();
//        message1.put("fielddata", true);
    message1.put("type", "keyword");
    Map<String, Object> fieldsmap = new HashMap<>();
    fieldsmap.put("keyword", message1);

    HashMap<String, Object> pinyin = new HashMap<>();
    HashMap<String, Object> pinyinmap = new HashMap<>();

    pinyin.put("type", "text");
    pinyin.put("store", "false");
    pinyin.put("term_vector", "with_offsets");
    pinyin.put("analyzer", "pinyin_analyzer");
    pinyin.put("boost", 10);
    fieldsmap.put("pinyin", pinyin);

    message.put("fields", fieldsmap);

    Map<String, Object> property = new HashMap<>();
    property.put("name", message);
    jsonMap.put("properties", property);
    putMappingRequest.source(jsonMap);

    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putMapping(putMappingRequest, RequestOptions.DEFAULT);


       */
/* BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(2,"张强","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(3,"张明","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(4,"李四","山东4")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(5,"王二小","山东5")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(1,"黑丝","山东6")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest6").source(JSON.toJSONString(new User(2,"网名","山东7")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*


      */
/*  SearchRequest searchRequest = new SearchRequest("pinyin");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
                QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("name.pinyin","ldh").analyzer("pinyin_analyzer"))
        );

        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return  search;*//*


  }


  public static void main(String[] args) {
    User user = new User(12, "姓名", "地址");
    System.out.println(JSON.toJSONString(user));
    System.out.println(JSON.toJSON(user));
    System.out.println(JSON.toJSONBytes(user));
    byte[] bytes = JSON.toJSONBytes(user);
    Object parse = JSON.parse(bytes);
    System.out.println(parse + "===");
    JSONObject bb = new JSONObject();
    bb.put("address", "address");
    bb.put("name", "name");
        */
/*bb.put("age",12);
        bb.put("age1",12);*//*

    bb.put("age1", 12);
    System.out.println(bb.toJavaObject(User.class).toString());

    System.out.println(JSON.parse("{\"address\":\"地址\",\"name\":\"姓名\",\"age\":12}"));
  }


  */
/**
   * @ClassName ZhangqiangSearchtest
   * @MethodName createindexsss
   * @Description 这个是可以的
   * @Author zhangqiang
   * @Date 2020/9/30 17:31
   * @Param []
   * @Return void
   **//*

  //  http://localhost:8080/zhangqiangsearch/createindexsss
  @RequestMapping("/createindexsss")
  public void createindexsss() throws IOException {
    CreateIndexRequest createIndexRequest = new CreateIndexRequest("zqtest7");
    createIndexRequest.settings(Settings.builder().put("number_of_shards", 5));
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
        .create(createIndexRequest, RequestOptions.DEFAULT);

    PutMappingRequest putMappingRequest = new PutMappingRequest("zqtest7");
    Map<String, Object> jsonMap = new HashMap<>();
    Map<String, Object> message = new HashMap<>();
    message.put("type", "text");
    message.put("fielddata", true);

    Map<String, Object> message1 = new HashMap<>();
//        message1.put("fielddata", true);
    message1.put("type", "keyword");
    Map<String, Object> fieldsmap = new HashMap<>();
    fieldsmap.put("keyword", message1);

    message.put("fields", fieldsmap);

    Map<String, Object> property = new HashMap<>();
    property.put("name", message);
    jsonMap.put("properties", property);
    putMappingRequest.source(jsonMap);
    putMappingRequest.setTimeout(TimeValue.timeValueMinutes(5));

    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putMapping(putMappingRequest, RequestOptions.DEFAULT);
    System.out.println("putmapping:" + acknowledgedResponse.isAcknowledged());

    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(1, "张强", "山东1")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(1, "张强", "山东1")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(2, "张强", "德州")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(3, "张明", "德州")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(4, "李四", "山东4")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(5, "王二小", "山东5")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(1, "黑丝", "山东6")), XContentType.JSON));
    bulkRequest.add(new IndexRequest("zqtest7")
        .source(JSON.toJSONString(new User(2, "网名", "山东7")), XContentType.JSON));
    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

  }


  //  http://localhost:8080/zhangqiangsearch/createindexandbulkandsearch
  @RequestMapping("/createindexandbulkandsearch")
  public void createindexandbulkandsearch() throws IOException {
     */
/* CreateIndexRequest createIndexRequest = new CreateIndexRequest("zqtest4");
        createIndexRequest.settings(Settings.builder().put("number_of_shards",5));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);



        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(1,"张强","山东1")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(2,"张强","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(3,"张明","德州")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(4,"李四","山东4")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(5,"王二小","山东5")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(1,"黑丝","山东6")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest3").source(JSON.toJSONString(new User(2,"网名","山东7")),XContentType.JSON));

        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*


     */
/*   GetMappingsRequest getMappingsRequest = new GetMappingsRequest().indices("zqtest3");

        GetMappingsResponse mapping = restHighLevelClient.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
//        mapping.mappings();
        System.out.println(JSON.toJSONString(mapping.mappings()));

        PutMappingRequest putMappingRequest = new PutMappingRequest("zqtest3");

*//*

    PutMappingRequest putMappingRequest = new PutMappingRequest("zqtest3");
       */
/* HashMap<String, Object> fielddataMap = new HashMap<>();
        fielddataMap.put("fielddata", true);

        HashMap<String, Object> jkeywordMap = new HashMap<>();
        jkeywordMap.put("keyword", fielddataMap);


        HashMap<String, Object> nameMap = new HashMap<>();

        nameMap.put("fields", jkeywordMap);*//*

    HashMap<String, Object> jkeywordMap = new HashMap<>();
    jkeywordMap.put("include_type_name", true);

//        putMappingRequest.indicesOptions(IndicesOptions.fromMap(jkeywordMap));
    HashMap<String, Object> fielddataMap = new HashMap<>();

    fielddataMap.put("fielddata", true);

    HashMap<String, Object> propertiesMap = new HashMap<>();
    propertiesMap.put("name", fielddataMap);

    HashMap<String, Object> propertiesMapss = new HashMap<>();
    propertiesMapss.put("properties", propertiesMap);
    HashMap<String, Object> docMapss = new HashMap<>();
    docMapss.put("_doc", propertiesMapss);

    putMappingRequest.source(docMapss);
    System.out.println(JSON.toJSONString(docMapss));
    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices()
        .putMapping(putMappingRequest, RequestOptions.DEFAULT);


  }


  //  http://localhost:8080/zhangqiangsearch/multiMatchQuery
  @RequestMapping("/multiMatchQuery")
  public SearchResponse multiMatchQuery() throws IOException {

      */
/*  BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest("zqtest2").id("KKWM2HQBpk7KNLDVtsOA"));
        bulkRequest.add(new DeleteRequest("zqtest2").id("KaWM2HQBpk7KNLDVtsOA"));
        bulkRequest.add(new DeleteRequest("zqtest2").id("KqWM2HQBpk7KNLDVtsOA"));
        bulkRequest.add(new DeleteRequest("zqtest2").id("K6WM2HQBpk7KNLDVtsOA"));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*


    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
   */
/*     searchSourceBuilder.query(
          QueryBuilders.multiMatchQuery("高达 王二小","name","address")
        );*//*

      */
/*  searchSourceBuilder.query(
                QueryBuilders.multiMatchQuery("高达 王二小","name","address").operator(Operator.OR)
        );*//*

    //
     */
/*   searchSourceBuilder.query(
                QueryBuilders.multiMatchQuery("pp_dd_hh","name","address").analyzer("ik_max_word").operator(Operator.OR)
        );*//*

    searchSourceBuilder.query(
        QueryBuilders.multiMatchQuery("pp_dd_hh", "name", "address")
    );
    searchSourceBuilder.query(QueryBuilders.commonTermsQuery("name", "强"));

    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/commonTermsQuery/强
  @RequestMapping("/commonTermsQuery/{names}")
  public SearchResponse commonTermsQuery(@PathVariable String names) throws IOException {
    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.commonTermsQuery("name", names));
    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/queryStringQuery
  @RequestMapping("/queryStringQuery")
  public SearchResponse queryStringQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //+表示必须含有 -不含有
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("+张强 -奸"));
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("+张强"));
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("+100"));
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("+儿"));
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("-100"));
    //name字段包含强的数据
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("name:强"));
    //英文暂时插叙不到
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("name:afdsaf"));
    ////name字段包含 拜把 or 张强 的数据
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("name:(拜把 or 张强)"));

    ////name字段包含  小二35 张强35 的数据
//        searchSourceBuilder.query(QueryBuilders.queryStringQuery("name: 小二35 张强35  "));
    ////有address字段的数据  的数据
    searchSourceBuilder.query(QueryBuilders.queryStringQuery("_exists_:address"));

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);

    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/dismaxquery
  @RequestMapping("/dismaxquery")
  public SearchResponse dismaxquery() throws IOException {
    //插入测试数据
        */
/*BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(99,"Quick brown rabbits","Brown rabbits are commonly seen.")),XContentType.JSON));
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(99,"Keeping pets healthy","My quick brown fox eats rabbits on a regular basis.")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*


    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //发现Quick brown rabbits得分比较高
        */
/*searchSourceBuilder.query(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name", "Brown fox"))
                .should(QueryBuilders.matchQuery("address", "Brown fox"))

        );*//*

    //发现Keeping pets healthy得分高 address有Brown fox整个单词
    searchSourceBuilder.query(
        QueryBuilders.disMaxQuery()
            .add(QueryBuilders.matchQuery("name", "Brown fox"))
            .add(QueryBuilders.matchQuery("address", "Brown fox"))
    );

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);

    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/constantScoreQuery
  @RequestMapping("/constantScoreQuery")
  public SearchResponse constantScoreQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    searchSourceBuilder.query(
              */
/*QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("name.keyword","张强35")
                        .boost(3.232121f))  *//*

        //term 查询精确查询  查询出来的score  3.232121
//                QueryBuilders.termQuery("name.keyword","张强35")
        QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery("name", "张强").boost(2.0f))
//                QueryBuilders.matchQuery("name","张强")
    );

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);
    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/aggregation
  @RequestMapping("/aggregation")
  public SearchResponse aggregation() throws IOException {
        */
/*BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("zqtest2").source(JSON.toJSONString(new User(35,"张强35","山东省济南市济阳县的")),XContentType.JSON));
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);*//*

    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    searchSourceBuilder.query(
        QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("name", "张强35"))
    );

//        searchSourceBuilder.aggregation(AggregationBuilders.terms("zhangqiang").field("age"));
       */
/* searchSourceBuilder.aggregation(
                AggregationBuilders.terms("zhangqiang").field("age")
                        .subAggregation(AggregationBuilders.avg())
        );*//*

    searchSourceBuilder.aggregation(

        AggregationBuilders.terms("zhangqiang").field("age")
            .subAggregation(
                AggregationBuilders.avg("avgsss").field("age")*/
/*.order(BucketOrder.count(true)*//*
)
    );
    */
/*.order(BucketOrder.count(true)升序*//*

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);
    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    Aggregations aggregations = search.getAggregations();
    Map<String, Aggregation> asMap = aggregations.getAsMap();
    System.out.println(JSON.toJSONString(asMap));
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/simpleQueryStringQuery
  @RequestMapping("/simpleQueryStringQuery")
  public SearchResponse simpleQueryStringQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    ////有张强  的数据
//        searchSourceBuilder.query(QueryBuilders.simpleQueryStringQuery("张强"));
    searchSourceBuilder.query(QueryBuilders.simpleQueryStringQuery("圣诞"));

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);

    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/leixingbijiao
  @RequestMapping("/leixingbijiao")
  public SearchResponse leixingbijiao() throws IOException {

    SearchRequest searchRequest = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    //matchPhraseQuery  张强* 包含张强的数据
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("name","张强*")));

    //queryStringQuery  张强* 无数据 name:张强*无数据 ame:张强有数据
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery("张强*").field("name")));
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery("name:张强*")));
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery("name:张强")));
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery("张强*").defaultField("name")));

    //simpleQueryStringQuery  张强* 无数据
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.simpleQueryStringQuery("张强*").field("name")));
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.dismatchPhraseQuery("张强*").field("name")));

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);

    searchRequest.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    return search;
  }


  //  http://localhost:8080/zhangqiangsearch/CountRequest
  @RequestMapping("/CountRequest")
  public CountResponse CountRequest() throws IOException {

    CountRequest countRequest = new CountRequest("zqtest2");

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    countRequest.source(searchSourceBuilder);
    CountResponse count = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);

    return count;
  }

  */
/**
   * @ClassName ZhangqiangSearchtest
   * @MethodName GetUserRequest
   * @Description 获取用户
   * @Author zhangqiang
   * @Date 2020/9/29 17:21
   * @Param []
   * @Return org.elasticsearch.client.security.GetUsersResponse
   **//*

  //  http://localhost:8080/zhangqiangsearch/GetUserRequest
  @RequestMapping("/GetUserRequest")
  public GetUsersResponse GetUserRequest() throws IOException {

    GetUsersRequest getUsersRequest = new GetUsersRequest("elastic");
    GetUsersResponse users = restHighLevelClient.security()
        .getUsers(getUsersRequest, RequestOptions.DEFAULT);

    return users;
  }


  //  http://localhost:8080/zhangqiangsearch/HighlightBuilder/强
  @RequestMapping("/HighlightBuilder/{names}")
  public ArrayList<Map<String, Object>> HighlightBuilder(@PathVariable String names)
      throws IOException {

    SearchRequest zqtest2 = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //这个可以模糊匹配
    String searchword = names;
    searchSourceBuilder.query(
        QueryBuilders.boolQuery()
            .should(QueryBuilders.wildcardQuery("name.keyword", "*" + searchword + "*"))
            .should(QueryBuilders.matchQuery("name", searchword))
            .should(QueryBuilders.matchPhraseQuery("name", searchword))
    );
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field("name");
    highlightBuilder.field("address");
    highlightBuilder.requireFieldMatch(false);//多个高亮展示！
    highlightBuilder.preTags("<span style='color:red'>");
    highlightBuilder.postTags("</span>");
    searchSourceBuilder.highlighter(highlightBuilder);

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);

    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.MILLISECONDS));
    zqtest2.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(zqtest2, RequestOptions.DEFAULT);

    ArrayList<Map<String, Object>> list = new ArrayList<>();
      */
/*  for(SearchHit documentFields:search.getHits().getHits()){
            list.add(documentFields.getSourceAsMap());
        }*//*

    for (SearchHit hit : search.getHits().getHits()) {
      Map<String, HighlightField> highlightFields = hit.getHighlightFields();
      HighlightField name = highlightFields.get("name");
      Map<String, Object> sourceMap = hit.getSourceAsMap();//原来的结果
      if (name != null) {
        Text[] fragments = name.fragments();
        String n_name = "";
        for (Text text : fragments) {
          n_name += text;
        }
        sourceMap.put("name", "<span style='color:blue'><u>" + n_name + "</u></span>");
      }
      HighlightField address = highlightFields.get("address");
      if (address != null) {
        Text[] fragments = address.fragments();
        String n_address = "";
        for (Text text : fragments) {
          n_address += text;
        }
        sourceMap.put("address", "<span style='color:blue'>" + n_address + "</span>");
      }

      list.add(sourceMap);
    }
    return list;
  }


  //  http://localhost:8080/zhangqiangsearch/searchrequest1
  @RequestMapping("/searchrequest1")
  public String searchrequest1() throws IOException {
    SearchRequest zqtest2 = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.termQuery("age",36));
//        searchSourceBuilder.query(QueryBuilders.termQuery("age",6666));
//        searchSourceBuilder.query(QueryBuilders.termQuery("age",6666123));

    //注意termquery查询中文没有数据  查询 age（int类型） 时没有问题
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","李丽丽41"));

    //matchquery 查询时会分词，匹配所有的数据 例如 张三  带35的数据
//        searchSourceBuilder.query(QueryBuilders.matchQuery("name","张强35"));

    //需要添加keyword才能查询出来 精确的查询
//        searchSourceBuilder.query(QueryBuilders.termQuery("name.keyword","张强35"));
//        searchSourceBuilder.query(QueryBuilders.termQuery("name.keyword","李丽丽"));
//        searchSourceBuilder.query(QueryBuilders.termQuery("name.keyword","李丽丽41"));

    //类似于 in  查询字段name  值有 李丽丽41 与张强的数据进行查找
//        searchSourceBuilder.query(QueryBuilders.termsQuery( "name.keyword","李丽丽41","张强"));
    //age int 类型，不能使用age.keyword
//        searchSourceBuilder.query(QueryBuilders.termsQuery( "age","6666","36"));

    //查询所有的数据并按照age排序
*/
/*        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.ASC));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);*//*


    //过滤_source   包含title字段 ，排除 user字段
   */
/*     searchSourceBuilder.fetchSource(true);
        String[] includeFields = new String[]{"age","innerobject.*"};
        String[] excludeFields = new String[]{"name"};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);*//*

    //rangeQuery  gt 大于40; gte 大于等于40;lt 小于35;lte 小于等于35
//        searchSourceBuilder.query(QueryBuilders.rangeQuery("age").gt(40));
//        searchSourceBuilder.query(QueryBuilders.rangeQuery("age").gte(40));
//        searchSourceBuilder.query(QueryBuilders.rangeQuery("age").lt(35));
    //age 大于等于31 ，小于等于33的数据     includeLower(false)就是不包含31 includeUpper(false)就是不包含33
//        searchSourceBuilder.query(QueryBuilders.rangeQuery("age").from(31).to(33));
//        searchSourceBuilder.query(QueryBuilders.rangeQuery("age").from(31).to(33).includeLower(false).includeUpper(false));

    //查询name 为空的数据
//        searchSourceBuilder.query(QueryBuilders.termQuery("name.keyword",""));

      */
/*   IndexRequest zqtest21 = new IndexRequest("zqtest2");
        zqtest21.source(JSON.toJSONString(new User(100,"100","100")), XContentType.JSON);
        restHighLevelClient.index(zqtest21,RequestOptions.DEFAULT);*//*


    //存在address 有值或者是没值得进行过滤
//        searchSourceBuilder.query(QueryBuilders.existsQuery("address"));
//        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.existsQuery("address")));

    //从前往后进行匹配  前缀匹配
//        searchSourceBuilder.query(QueryBuilders.prefixQuery("name.keyword","娃"));
    //通配符查询 *  ？  可以模糊查
//        searchSourceBuilder.query(QueryBuilders.wildcardQuery("name.keyword","小*"));
//        searchSourceBuilder.query( QueryBuilders.wildcardQuery("name.keyword","*3*"));
    //根据id进行查询
//        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds("tQ9AznQBwVtjHGNcytU5","3s2b03QBG2P0-miG51So"));
    //模糊搜索
//        searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name","改后").fuzziness(Fuzziness.ONE));

//mustNot 字段必须不包含啥  must 字段必须包含  should 字段可以包含啥，相当于或者     matchQuery必须是整个单词才能匹配
//            searchSourceBuilder.query(QueryBuilders.boolQuery().mustNot(QueryBuilders.matchQuery("name.keyword","39")));
//            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name.keyword","张强35")));
    //字段name=张强35 or name=小娃儿34
//            searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name.keyword","张强35")).should(QueryBuilders.matchQuery("name.keyword","小娃儿34")));

    //添加上.keyword 就是不分词  ，不添加则是模糊匹配
//    searchSourceBuilder.query(QueryBuilders.matchQuery("name","强"));
//    searchSourceBuilder.query(QueryBuilders.matchQuery("name.keyword","强"));
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name","五")));

    //name 有强的展示   name.keyword匹配==强的数据   "name.keyword","张强37"精确匹配
//        searchSourceBuilder.query(QueryBuilders.matchQuery("name","强").analyzer("ik_max_word"));
    //这个查询的结果包含张强的，也可能有张三 张四的
//        searchSourceBuilder.query(QueryBuilders.matchQuery("name","张强"));
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","小"));
    //使用 matchPhraseQuery替换掉 termQuery 可以精确的插叙
        */
/*searchSourceBuilder.query(
            QueryBuilders.boolQuery().should( QueryBuilders.matchPhraseQuery("name","四3"))
                                    .should(QueryBuilders.wildcardQuery("name","*四3*"))


        );*//*

 */
/*   searchSourceBuilder.query(
            QueryBuilders.wildcardQuery("name.keyword","*四3*")

            );*//*

//        searchSourceBuilder.query( QueryBuilders.wildcardQuery("name.keyword","*3*"));
    //searchSourceBuilder.query(QueryBuilders.termQuery("name","张强"));

      */
/*  searchSourceBuilder.query(
                QueryBuilders.boolQuery().should( QueryBuilders.matchPhraseQuery("name","四3").slop(10))
//                        .should(QueryBuilders.wildcardQuery("name","*四3*"))


        );*//*

//        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("name","强") );
//        searchSourceBuilder.query(QueryBuilders.matchQuery("fields.name","3").analyzer("ik_max_word") );

    //这个可以模糊匹配
    String searchword = "2";
    searchSourceBuilder.query(
        QueryBuilders.boolQuery()
            .should(QueryBuilders.wildcardQuery("name.keyword", "*" + searchword + "*"))
            .should(QueryBuilders.matchQuery("name", searchword))
            .should(QueryBuilders.matchPhraseQuery("name", searchword))
    );
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field("name");
    highlightBuilder.requireFieldMatch(false);//多个高亮展示！
    highlightBuilder.preTags("<span style='color:red'>");
    highlightBuilder.postTags("</span>");
    searchSourceBuilder.highlighter(highlightBuilder);

    searchSourceBuilder.from(0);
    searchSourceBuilder.size(100);

    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.MILLISECONDS));
    zqtest2.source(searchSourceBuilder);
    SearchResponse search = restHighLevelClient.search(zqtest2, RequestOptions.DEFAULT);
    System.out.println(JSON.toJSONString(search.getHits()));
    System.out.println(
        "==========getTotalHits().relation========:" + search.getHits().getTotalHits().relation);
    System.out
        .println("==========getTotalHits().value========:" + search.getHits().getTotalHits().value);
    for (SearchHit searchHit : search.getHits().getHits()) {
      System.out.println(searchHit.getId());
      System.out.println(searchHit.getScore());
      System.out.println(searchHit.getSourceAsMap());
    }

    return "11111111111111111111";
  }


  //  http://localhost:8080/zhangqiangsearch/searchrequest
  @RequestMapping("/searchrequest")
  public String searchrequest() throws IOException {
        */
/*SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);*//*

    //只是针对于zqtest2的这个索引进行搜索
       */
/* SearchRequest searchRequest = new SearchRequest("zqtest2");
        searchRequest.routing("routing");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("age",41));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.MILLISECONDS));


        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("age",41);
        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        matchQueryBuilder.prefixLength(3);
        matchQueryBuilder.maxExpansions(10);

        searchSourceBuilder.query(matchQueryBuilder);

        QueryBuilder queryBuilders= QueryBuilders.matchQuery("age", 41).fuzziness(Fuzziness.AUTO)
                .prefixLength(3).maxExpansions(10);
        searchSourceBuilder.query(queryBuilders);

       searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
       searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.DESC));


       //过滤_source   包含title字段 ，排除 user字段
       searchSourceBuilder.fetchSource(false);
        String[] includeFields = new String[]{"title","innerobject.*"};
        String[] excludeFields = new String[]{"user"};
       searchSourceBuilder.fetchSource(includeFields,excludeFields);




        searchRequest.source(searchSourceBuilder);*//*


    SearchRequest zqtest2 = new SearchRequest("zqtest2");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.termQuery("age", 41));
    searchSourceBuilder.from(0);
    searchSourceBuilder.size(10);
    searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
    searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.ASC));
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.MINUTES));
    //过滤_source   包含title字段 ，排除 user字段
    searchSourceBuilder.fetchSource(false);
    String[] includeFields = new String[]{"title", "innerobject.*"};
    String[] excludeFields = new String[]{"user"};
    searchSourceBuilder.fetchSource(includeFields, excludeFields);

    HighlightBuilder highlightBuilder = new HighlightBuilder();
    HighlightBuilder.Field title = new HighlightBuilder.Field("title");
    title.highlighterType("unified");
    highlightBuilder.field(title);

    HighlightBuilder.Field user = new HighlightBuilder.Field("user");
    highlightBuilder.field(user);

    searchSourceBuilder.highlighter(highlightBuilder);

    zqtest2.routing("");
    zqtest2.source();
    SearchResponse searchResponse = restHighLevelClient.search(zqtest2, RequestOptions.DEFAULT);

    return "11111111111111111111111";
  }

  public static class UserZhangqiang {

    private String id;
    private String name;
    private Integer age;

    private String title;
    private String content;
    private Date birth;
    private String date;
    private String address;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public Date getBirth() {
      return birth;
    }

    public void setBirth(Date birth) {
      this.birth = birth;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

       */
/* public UserZhangqiang(String id, String name, Integer age, String address, String title, String content, Date birth) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.address = address;
            this.title = title;
            this.content = content;
            this.birth = birth;
        }*//*


    public UserZhangqiang(String id, String name, Integer age, String title, String content,
        Date birth, String date, String address) {
      this.id = id;
      this.name = name;
      this.age = age;

      this.title = title;
      this.content = content;
      this.birth = birth;
      this.date = date;
      this.address = address;
    }
  }


  public static class User {

    private int age;
    private String name;

    private String address;

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

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

    public User(int age, String name, String address) {
      this.age = age;
      this.name = name;
      this.address = address;
    }

    @Override
    public String toString() {
      return "User{" +
          "age=" + age +
          ", name='" + name + '\'' +
          ", address='" + address + '\'' +
          '}';
    }
  }
}
*/
