import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class KafkaConsumerThread <T> extends Thread{

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerThread.class);
    private Consumer<String,String> consumer;
    private Properties kafkaConsumerConfig;
    private volatile boolean isOffsetsToCommit = false;
    private volatile boolean isCommitCommenced = false;
    private Map<TopicPartition, OffsetAndMetadata> offsetsToCommit;
    private Map<TopicPartition, Long> offsetsToSubscribe;
    private List<TopicPartition> topicPartitions;
    private List<TopicPartitionState> topicPartitionStates;
    private volatile boolean active = true;

    public KafkaConsumerThread
            (Properties kafkaConsumerConfig,
             Map<TopicPartition, OffsetAndMetadata> offsetsToCommit,
             List<TopicPartition> topicPartitions,
             List<TopicPartitionState> topicPartitionStates) {
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.offsetsToCommit = offsetsToCommit;
        this.topicPartitions = topicPartitions;
        this.topicPartitionStates = topicPartitionStates;

    }

    @Override
    public void run() {
        LOG.info("starting");
        initiateConnection();
//        commitOffsets();
        if (!active) return;
        if (topicPartitions == null) throw new Error("Topic Partition is not defined");
        consumer.assign(topicPartitions);
        ConsumerRecords<String,String> records = null;
        while (active) {
//            System.out.println("polling");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (records == null){
                records = consumer.poll(100);
                for (ConsumerRecord<String,String> record : records) {
                    System.out.println(record.value().getClass());
//                    System.out.println(String.valueOf(value));
                    System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
                }
                for (TopicPartitionState topicPartitionState : topicPartitionStates) {

                    List<ConsumerRecord<String,String>> partitionRecords =
                            records.records(topicPartitionState.getTopicPartition());


                    for (ConsumerRecord<String,String> record2 : partitionRecords) {
//                            String value = record2.value();
//                        emitRecord(value, topicPartitionState, record2.offset());
                    }
                }

            }


        }

    }
    public void initiateConnection (){
        if (this.consumer == null) {
            this.consumer = new KafkaConsumer<>(kafkaConsumerConfig);
        }
    }

    public void commitOffsets (){
        synchronized (this){
            if (!isOffsetsToCommit) return;
        }
        if (isCommitCommenced) return;
        isCommitCommenced = true;
        isOffsetsToCommit = false;
        consumer.commitAsync(offsetsToCommit, new OffsetCommitCallback() {
            @Override
            public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                isCommitCommenced = false;
            }
        });
    }

    public synchronized void setOffsetsToCommit(Map<TopicPartition, OffsetAndMetadata> offsetsToCommit) {
        this.offsetsToCommit = offsetsToCommit;
        this.isOffsetsToCommit = true;
    }

    public void setSeek () {
        initiateConnection();
        for (TopicPartition topicPartition : offsetsToSubscribe.keySet()) {
            consumer.seek(topicPartition, offsetsToSubscribe.get(topicPartition));
        }
    }

    public void setSeekToBeginning () {
        initiateConnection();
        consumer.seekToBeginning(topicPartitions);
    }

    public void assignPartitions(){
        initiateConnection();
        consumer.assign(topicPartitions);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public void emitRecord(String value, TopicPartitionState tps, Long offset) {
        LOG.info("emitting record {} from the partition {}",value,offset);
        System.out.println(value);
        tps.setPositionOffset(offset);
    }


}
