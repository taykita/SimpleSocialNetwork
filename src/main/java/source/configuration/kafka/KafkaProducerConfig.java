package source.configuration.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    private boolean kafkaEnable;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        if (kafkaEnable) {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    bootstrapAddress);
            configProps.put(
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class);
            configProps.put(
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }
        return null;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        if (kafkaEnable) {
            return new KafkaTemplate<>(producerFactory());
        }
        return null;
    }

    @Value("${kafka.enable}")
    public void setKafkaEnable(boolean kafkaEnable) {
        this.kafkaEnable = kafkaEnable;
    }
}