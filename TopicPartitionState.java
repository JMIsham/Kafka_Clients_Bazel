import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class TopicPartitionState {
    private final TopicPartition topicPartition;
    private long positionOffset;
    private long commitOffset;

    public TopicPartitionState(TopicPartition topicPartition) {
        this.topicPartition = topicPartition;
    }

    public long getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(long positionOffset) {
        this.positionOffset = positionOffset;
    }

    public long getCommitOffset() {
        return commitOffset;
    }

    public void setCommitOffset(long commitOffset) {
        this.commitOffset = commitOffset;
    }

    public TopicPartition getTopicPartition() {
        return topicPartition;
    }

    public OffsetAndMetadata getEmptyMetadataOffset(){
        return new OffsetAndMetadata(positionOffset);
    }
}
