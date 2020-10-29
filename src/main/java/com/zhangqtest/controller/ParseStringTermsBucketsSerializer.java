package com.zhangqtest.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @PackageName com.zhangqtest.controller
 * @ClassName ParseStringTermsBucketsSerializer
 * @Description TODO
 * @Author zhangqiang
 * @Date 2020/9/30 21:41
 * @Version 1.0
 **/
public class ParseStringTermsBucketsSerializer extends StdSerializer<ParsedStringTerms.ParsedBucket> {
    public ParseStringTermsBucketsSerializer(Class<ParsedStringTerms.ParsedBucket> ts) {
        super(ts);
    }

    @Override
    public void serialize(ParsedStringTerms.ParsedBucket parsedBucket, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("aggregations",parsedBucket.getAggregations());
        jsonGenerator.writeObjectField("key",parsedBucket.getKey());
        jsonGenerator.writeStringField("keyAsString",parsedBucket.getKeyAsString());
        jsonGenerator.writeNumberField("docCount",parsedBucket.getDocCount());
        jsonGenerator.writeEndObject();

    }
}
