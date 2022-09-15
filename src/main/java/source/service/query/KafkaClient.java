package source.service.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

//@Service
public class KafkaClient {
    public KafkaClient (@Value("${kafka.enable}") boolean kafkaEnable) {
        this.kafkaEnable = kafkaEnable;
    }

    private KafkaTemplate<String, String> kafkaTemplate;

    private final boolean kafkaEnable;

    public void sendMessage(String message) {
        if (kafkaEnable) {
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

//    @Autowired(required = false)
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
