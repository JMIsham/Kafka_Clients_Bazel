import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.omg.PortableServer.ThreadPolicyOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KafkaPartitionFinder {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaPartitionFinder.class);
    private final String[] bootstrapServers;
    private final int numRetries;
    private final int worldSize;
    private final int myIndex;
    private Consumer<?, ?> consumer;
    private final Properties kafkaConsumerConfig;
    private final KafkaTopicDescription topics;

    public KafkaPartitionFinder(
            Properties kafkaConsumerConfig,
            int worldSize,
            int myIndex,
            KafkaTopicDescription topics
    ) {
        this.bootstrapServers = kafkaConsumerConfig.getProperty( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG).split( ",");
        this.numRetries = 10;
        this.myIndex = myIndex;
        this.worldSize = worldSize;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.topics = topics;
        initializeConnection();
    }

    public List<TopicPartition> getAllPartitions() {
        if (this.topics.isFixedTopics()) {
            return getAllPartitionsForTopics(topics.getFixedTopics());
        } else {
            ArrayList<String> allTopics = allTopics();
            ArrayList<String> relevantTopics = new ArrayList<>();
            Pattern pattern = topics.getTopicPattern();
            for (String topic : allTopics) {
                Matcher matcher = pattern.matcher(topic);
                if (matcher.find()) {
                    relevantTopics.add(topic);
                }
            }
            return getAllPartitionsForTopics(relevantTopics);

        }
    }

    public List<TopicPartition> getRelevantPartitioins() {
        List<TopicPartition> relevantPartitions = new ArrayList<>();
        List<TopicPartition> allPartitions = getAllPartitions();
        for ( TopicPartition partition : allPartitions) {
            if (assignPartition(partition)) {
                relevantPartitions.add(partition);
            }
        }
        return relevantPartitions;
    }

    public KafkaPartitionFinder initializeConnection (){
        this.consumer = new KafkaConsumer<>(kafkaConsumerConfig);
        return this;
    }

    public ArrayList<String> allTopics() throws WakeupException{
        try {
            return new ArrayList<>(consumer.listTopics().keySet());
        } catch (WakeupException e) {
            throw new WakeupException();
        }
    }

    protected List<TopicPartition> getAllPartitionsForTopics(List<String> topics) throws WakeupException {
        List<TopicPartition> partitions = new LinkedList<>();

        try {
            for (String topic : topics) {
                for (PartitionInfo partitionInfo : consumer.partitionsFor(topic)) {
                    partitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
                }
            }
        } catch (org.apache.kafka.common.errors.WakeupException e) {
            throw new WakeupException();
        }

        return partitions;
    }

    protected void wakeupConnections() {
        if (this.consumer != null) {
            this.consumer.wakeup();
        }
    }

    protected void closeConnections() throws Exception {
        if (this.consumer != null) {
            this.consumer.close();

            // de-reference the consumer to avoid closing multiple times
            this.consumer = null;
        }
    }

    private boolean assignPartition(TopicPartition topicPartition) {
        int startIndxOfTopic = topicPartition.topic().hashCode();
        int partitionAssignIndex = (startIndxOfTopic + topicPartition.partition() )% (worldSize);
        return partitionAssignIndex == myIndex;
    }
}
