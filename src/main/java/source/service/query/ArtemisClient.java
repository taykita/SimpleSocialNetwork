package source.service.query;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;
import java.util.Arrays;

@Service
public class ArtemisClient implements QueryClient {
    public ArtemisClient(
            @Value("${artemis.url}") String url,
            @Value("${artemis.pool.enabled}") boolean poolEnabled,
            @Value("${artemis.pool.max.connections}") int maxConnections) {

        this.connectionFactory = getConnectionFactory(url, poolEnabled, maxConnections);
    }

    private final Logger logger = LoggerFactory.getLogger(ArtemisClient.class);

    private ConnectionFactory getConnectionFactory(
            String url, boolean poolEnabled, int maxConnections) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        if (poolEnabled) {
            PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
            pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
            pooledConnectionFactory.setMaxConnections(maxConnections);
            return pooledConnectionFactory;
        } else {
            return activeMQConnectionFactory;
        }

    }

    private final ConnectionFactory connectionFactory;

    @Override
    public void sendMessage(String message, String dist) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            logger.debug("Создана сессия");

            Queue queue = session.createQueue(dist);
            MessageProducer producer = session.createProducer(queue);
            logger.debug("Создан отправитель");
            TextMessage textMessage = session.createTextMessage(message);

            logger.debug("Начинаем посылать сообщение");
            producer.send(textMessage);
            logger.debug("Сообщение успешно послано в очередь %s", dist);
            logger.debug("Сообщение - %s", textMessage);

            connection.close();
        } catch (JMSException e) {
            logger.error("Ошибка отправки сообщения : " + e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
