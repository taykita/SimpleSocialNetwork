package source.controllers.friend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.service.query.KafkaClient;
import source.service.query.entity.AnalysisDTO;

@Service
public class FriendService {
    @Autowired
    public FriendService(AccountRepository accountRepository,
                         KafkaClient kafkaClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.kafkaClient = kafkaClient;
        this.accountRepository = accountRepository;
    }
    private final ObjectMapper objectMapper;
    private final KafkaClient kafkaClient;
    private final AccountRepository accountRepository;

    public void addFriend(int id, User activeUser) throws AccStorageException, JsonProcessingException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.addFriend(user, accountRepository.getAccount(id));

        AnalysisDTO analysisDTO = new AnalysisDTO("Account", "FriendAdded", activeUser.getId() + "-" + id);
        kafkaClient.sendMessage(objectMapper.writeValueAsString(analysisDTO));
    }

    public void deleteFriend(int id, User activeUser) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.deleteFriend(user, accountRepository.getAccount(id));
    }
}
