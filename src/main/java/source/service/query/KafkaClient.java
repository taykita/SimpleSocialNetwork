package source.service.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class KafkaClient implements QueryClient {
    @Autowired
    public KafkaClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("booknetwork-events", message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            private Logger logger = LoggerFactory.getLogger("KAFKA");

            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.debug("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                logger.debug("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
