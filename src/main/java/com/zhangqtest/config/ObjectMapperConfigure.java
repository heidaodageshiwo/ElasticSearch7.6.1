package com.zhangqtest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zhangqtest.controller.ParseStringTermsBucketsSerializer;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对es结果进行特殊处理（如果是分组的字段是text类型时）
 *
 * @ProjectName: zhangqiang-es-api
 * @Package: com.zhangqtest.config
 * @ClassName: dasf
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2020-10-29 15:37
 * @UpdateUser: zhangq
 * @UpdateDate: 2020-10-29 15:37
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Configuration
public class ObjectMapperConfigure {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(simpleModules());
    return objectMapper;
  }


  public SimpleModule simpleModules() {
    ParseStringTermsBucketsSerializer parseStringTermsBucketsSerializer = new ParseStringTermsBucketsSerializer(
        ParsedStringTerms.ParsedBucket.class);
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(parseStringTermsBucketsSerializer);
    return simpleModule;
  }

}
