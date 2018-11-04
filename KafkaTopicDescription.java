
import java.util.List;
import java.util.regex.Pattern;

public class KafkaTopicDescription {
    private final List<String> fixedTopics;
    private final Pattern topicPattern;

    public KafkaTopicDescription(List<String> fixedTopics, Pattern topicPattern) {
        if ((fixedTopics != null && topicPattern != null)
                || (fixedTopics == null && topicPattern == null)) {
            throw new IllegalArgumentException("Exactly on of the topic must be provided");
        }

        if (fixedTopics != null && fixedTopics.isEmpty()) {
            throw new IllegalArgumentException("the fixed topic cannot be empty");
        }
        this.fixedTopics = fixedTopics;
        this.topicPattern = topicPattern;
    }

    public KafkaTopicDescription(Pattern topicPattern) {
        this(null, topicPattern);
    }

    public KafkaTopicDescription(List<String> fixedTopics) {
        this(fixedTopics, null);
    }
    public boolean isFixedTopics() {
        return fixedTopics != null;
    }

    public boolean isTopicPattern() {
        return topicPattern != null;
    }

    public List<String> getFixedTopics() {
        return fixedTopics;
    }

    public Pattern getTopicPattern() {
        return topicPattern;
    }
}
