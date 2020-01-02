package com.talkingdata.flink;

import com.talkingdata.flink.function.ParserFlatMapFunction;
import com.talkingdata.flink.kafka.CubeSerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Properties;

/**
 * @author tao.yang
 * @date 2019-12-26
 */
public class WiFiAnalyticsStreamingJob {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(WiFiAnalyticsStreamingJob.class);

    public static void main(String[] args) throws Exception {

        // 创建本地的环境
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        // 配置环境
        environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        environment.getConfig().setAutoWatermarkInterval(1000L);

        ParameterTool argsParameter = ParameterTool.fromArgs(args);
        String configPath = argsParameter.get("path");

        ParameterTool parameter = ParameterTool.fromPropertiesFile(configPath + "/application.properties");

        String wiFiCollectorKafkaTopic = parameter.get("wifi-analytics.collector.kafka.topic", "wifi.collector");
        String wifiCubeKafkaTopic = parameter.get("wifi-analytics.cube.kafka.topic", "wifi.cube");
        String kafkaBrokerServers = parameter.get("bootstrap.servers");
        String kafkaGroupId = parameter.get("group.id", "WiFiAnalyticsStreamingJob");

        logger.info("kafkaWiFiTopic [{}] kafkaBrokerServers [{}] kafkaGroupId [{}] wifiCubeKafkaTopic [{}]", wiFiCollectorKafkaTopic, kafkaBrokerServers, kafkaGroupId, wifiCubeKafkaTopic);

        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers", kafkaBrokerServers);
        kafkaProperties.setProperty("group.id", kafkaGroupId);

        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<>(wiFiCollectorKafkaTopic, new SimpleStringSchema(), kafkaProperties);
        consumer.setStartFromEarliest();
        environment.addSource(consumer).flatMap(new ParserFlatMapFunction()).keyBy(0).print();
                //.addSink(new FlinkKafkaProducer(wifiCubeKafkaTopic, new CubeSerializationSchema(), kafkaProperties, FlinkKafkaProducer.Semantic.EXACTLY_ONCE));

        environment.execute("WiFi-Analytics-Steaming");
    }

}
