package com.talkingdata.flink.kafka;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

/**
 * @author tao.yang
 * @date 2019-12-31
 */
public class CubeSerializationSchema implements KafkaSerializationSchema {

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Object o, @Nullable Long aLong) {
        return null;
    }
}
