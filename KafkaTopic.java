import java.util.ArrayList;

public class KafkaTopic {
    private final String name;
    private  ArrayList<Integer> partitions;
    private  int leader;


    public KafkaTopic(String name, ArrayList<Integer> partitions, int leader) {
        this.name = name;
        this.partitions = partitions;
        this.leader = leader;
    }


    public String getName() {
        return name;
    }

    public ArrayList<Integer> getPartitions() {
        return partitions;
    }

    public void setPartitions(ArrayList<Integer> partitions) {
        this.partitions = partitions;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }
}
