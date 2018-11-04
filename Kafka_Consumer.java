
import java.util.*;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Kafka_Consumer <T> {

    private static final Logger LOG = LoggerFactory.getLogger(Kafka_Consumer.class);

    private Properties kafkaConfigs;
    private List<TopicPartition> topicPartitions;
    private int myIndex;
    private int worldSize;
    private TaskContext taskContext;
    private Map<TopicPartition, OffsetAndMetadata> offsetsToCommit;
    private List<TopicPartitionState> topicPartitionStates;

    private boolean restoreState = false;
    private volatile boolean consumerThreadStarted = false;

    private  KafkaPartitionFinder partitionFinder;
    private  KafkaTopicDescription topicDescription;
    private  KafkaConsumerThread kafkaConsumerThread;


    public static void main(String[] args) throws Exception {

//        String topicName = "outTopic";
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
//        props.put("group.id", "test2");
//        props.put("enable.auto.commit", "true");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("session.timeout.ms", "30000");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
//        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<byte[], byte[]>(props);
//
//        consumer.subscribe(Arrays.asList(topicName));
//
//        System.out.println("Subscribed to topic " + topicName);
//
//        TopicPartition tp = new TopicPartition("test", 0);
//        Thread producer = new Thread(new Kafka_Producer());
//        producer.start();
//        while (true) {
//            System.out.println("listenning");
//            ConsumerRecords<byte[], byte[]> records = consumer.poll(100);
//            for (ConsumerRecord<byte[], byte[]> record : records)
//
//                System.out.printf("offset = %d, key = %s, value = %s\n",
//                        record.offset(), record.key(), record.value());
//        }

        Thread producer = new Thread(new Kafka_Producer());
        producer.start();
        List<String> topics = Arrays.asList("outTopic");
        List<String> servers = Arrays.asList("localhost:9092");
        Kafka_Consumer<String> consumer = new Kafka_Consumer<String>(topics,servers,"test");
//        consumer.init();
//        consumer.run();
    }



    public void process() {

    }

    public Kafka_Consumer(
            List<String> topics,
            List<String> servers,
            String consumerGroup
    ) {
        this.topicDescription = new KafkaTopicDescription(topics);
        createKafkaConfig(servers, consumerGroup);
    }

    public Kafka_Consumer(
            Pattern topicPattern,
            List<String> servers,
            String consumerGroup
    ) {
        this.topicDescription = new KafkaTopicDescription(topicPattern);
        createKafkaConfig(servers, consumerGroup);
    }

    public Properties getKafkaConfigs() {
        return kafkaConfigs;
    }

    public void setKafkaConfigs(Properties kafkaConfigs) {
        this.kafkaConfigs = kafkaConfigs;
    }

//    public void prepare(Config cfg, TaskContext context) {
//        this.
//    }
    public void init (

    ) {
        System.out.println("init");
        this.worldSize = 1;
        this.myIndex = 0;
        this.partitionFinder = new KafkaPartitionFinder(this.kafkaConfigs,worldSize, myIndex, topicDescription);
        this.topicPartitions = partitionFinder.getRelevantPartitioins();

        this.topicPartitionStates = new ArrayList<>();
        for (TopicPartition tp : topicPartitions) {
            topicPartitionStates.add(new TopicPartitionState(tp));
        }

        this.kafkaConsumerThread = new KafkaConsumerThread(kafkaConfigs, offsetsToCommit, topicPartitions, topicPartitionStates);
        kafkaConsumerThread.assignPartitions();
        kafkaConsumerThread.setSeekToBeginning();

    }

    public void run(){
        System.out.println("start");
        if (!consumerThreadStarted) {
            try {
                kafkaConsumerThread.start();

            } catch (IllegalThreadStateException e){
                LOG.info("consumer is already started");
            }
        }


    }

    private Properties createKafkaConfig(List<String> servers, String consumerGroup) {

        StringBuilder strServers = new StringBuilder();
        for (int i = 0; i < servers.size(); i++) {
            strServers.append(servers.get(i));
            if ( i+1 < servers.size()) {
                strServers.append(",");
            }
        }
        this.kafkaConfigs = new Properties();
        kafkaConfigs.put("bootstrap.servers", strServers.toString());
        kafkaConfigs.put("group.id", consumerGroup);
        kafkaConfigs.put("enable.auto.commit", "false");
        kafkaConfigs.put("session.timeout.ms", "30000");
        kafkaConfigs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfigs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return kafkaConfigs;
    }

}

