import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.Producer;

import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;

public class Kafka_Producer implements Runnable{

    @Override
    public void run() {
        String topicName = "sample_topic1";

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        ArrayList<String> logs = new ArrayList<>();
        logs.add("log:search: ");
        logs.add("log:Login:");
        logs.add("log:Logout:");
        logs.add("log:Register: ");
        logs.add("log:Buy: ");


        Producer<String, String> producer = new KafkaProducer<>(props);
        int i;
        long startingTime  = System.currentTimeMillis();

        String message1 = logs.get(1);
        message += System.currentTimeMillis();
        producer.send(new ProducerRecord<String, String>(topicName,
                message1, message1));

        for(i=0 ; i < 9998; i++) {
            Random r = new Random();
            int pos = r.nextInt(5);
            String message = logs.get(pos);
            message += System.currentTimeMillis();
            producer.send(new ProducerRecord<String, String>(topicName,
                    message, message));
//            System.out.println(message);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String message2 = logs.get(1);
        message += System.currentTimeMillis();
        producer.send(new ProducerRecord<String, String>(topicName,
                message2, message2));

        long endTime = System.currentTimeMillis();
        System.out.println(10000000.0/(endTime - startingTime));
//        System.out.println("Message sent successfully");
        producer.close();
    }
}
