package com.talkingdata.flink.function;

import com.talkingdata.flink.entity.TraceLog;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2019-12-31
 */
public class ParserFlatMapFunction implements FlatMapFunction<String, Tuple2<String, TraceLog>> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ParserFlatMapFunction.class);

    /**
     * json转换
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ParserFlatMapFunction(){

    }

    /**
     * The core method of the FlatMapFunction. Takes an element from the input data set and transforms
     * it into zero, one, or more elements.
     *
     * @param value The input value.
     * @param out   The collector for returning result values.
     * @throws Exception This method may throw exceptions. Throwing an exception will cause the operation
     *                   to fail and may trigger recovery.
     */
    @Override
    public void flatMap(String value, Collector<Tuple2<String, TraceLog>> out) throws Exception {

        TraceLog[] traceLogs =  objectMapper.readValue(value, TraceLog[].class);

        if (traceLogs == null || traceLogs.length <= 0){
            return ;
        }

        for (TraceLog traceLog : traceLogs){
            out.collect(new Tuple2<>(traceLog.getId(), traceLog));
        }
    }
}
