package source.controllers.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import source.controllers.entity.Account;
import source.controllers.entity.chat.ChatType;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;
import source.service.query.KafkaClient;
import source.service.query.entity.AnalysisDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationService {
    @Autowired
    public AuthorizationService(AccountRepository accountRepository, ChatRepository chatRepository,
                                KafkaClient kafkaClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.kafkaClient = kafkaClient;
        this.accountRepository = accountRepository;
        this.chatRepository = chatRepository;
    }
    private final ObjectMapper objectMapper;
    private final KafkaClient kafkaClient;
    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;

    public boolean registration(Account account, BindingResult bindingResult, String chPass)
            throws AccStorageException, JsonProcessingException {

        if (account.getPass().equals(chPass)) {
            return checkExistAndCreateAcc(account);
        } else {
            return false;
        }
    }

    private boolean checkExistAndCreateAcc(Account account) throws AccStorageException, JsonProcessingException {
        if (existAccount(account.getEmail())) {
            return false;
        } else {
            addAccount(account);
            return true;
        }
    }

    private void addAccount(Account account) throws AccStorageException, JsonProcessingException {
        account = accountRepository.addAccount(account);
        List<Integer> ids = new ArrayList<>();
        ids.add(account.getId());
        chatRepository.addChat(ids, "Сохраненные сообщения", ChatType.SAVED);

        AnalysisDTO analysisDTO = new AnalysisDTO("Account", "Created", account);
        kafkaClient.sendMessage(objectMapper.writeValueAsString(analysisDTO));
    }

    private boolean existAccount(String email) throws AccStorageException {
        return accountRepository.existAccount(email);
    }

}
