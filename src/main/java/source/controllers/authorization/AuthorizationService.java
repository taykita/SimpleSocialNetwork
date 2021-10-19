package source.controllers.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.chat.ChatType;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;
import source.service.query.KafkaClient;
import source.service.query.QueryClient;
import source.service.query.entity.AnalysisDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationService {
    @Autowired
    public AuthorizationService(AccountRepository accountRepository, ChatRepository chatRepository,
                                QueryClient queryClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.queryClient = queryClient;
        this.accountRepository = accountRepository;
        this.chatRepository = chatRepository;
    }

    private final ObjectMapper objectMapper;
    private final QueryClient queryClient;
    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;

    public boolean registration(Account account, String chPass)
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
        createSaveMessagesChat(account);

        sendMessageToQuery(account);
    }

    private void createSaveMessagesChat(Account account) throws AccStorageException {
        List<Integer> ids = new ArrayList<>();
        ids.add(account.getId());
        chatRepository.addChat(ids, "Сохраненные сообщения", ChatType.SAVED);
    }

    private void sendMessageToQuery(Account account) throws JsonProcessingException {
        AnalysisDTO analysisDTO = new AnalysisDTO("Account", "Created", account.getId() + "=" + account.getName());
        queryClient.sendMessage(objectMapper.writeValueAsString(analysisDTO));
    }

    private boolean existAccount(String email) throws AccStorageException {
        return accountRepository.existAccount(email);
    }

}
