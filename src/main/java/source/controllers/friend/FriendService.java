package source.controllers.friend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.service.query.QueryClient;
import source.service.query.entity.AnalysisDTO;

import static source.configuration.Constants.MONITORING_QUEUE;

@Service
public class FriendService {
    @Autowired
    public FriendService(AccountRepository accountRepository,
                         QueryClient queryClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.queryClient = queryClient;
        this.accountRepository = accountRepository;
    }
    private final ObjectMapper objectMapper;
    private final QueryClient queryClient;
    private final AccountRepository accountRepository;

    public void addFriend(int id, User activeUser) throws AccStorageException, JsonProcessingException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.addFriend(user, accountRepository.getAccount(id));

        sendMessageToQuery(id, activeUser);
    }

    private void sendMessageToQuery(int id, User activeUser) throws JsonProcessingException {
        AnalysisDTO analysisDTO = new AnalysisDTO("Account", AnalysisDTO.Action.CREATED, activeUser.getId() + "-" + id);
        queryClient.sendMessage(objectMapper.writeValueAsString(analysisDTO), MONITORING_QUEUE);
    }

    public void deleteFriend(int id, User activeUser) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.deleteFriend(user, accountRepository.getAccount(id));
    }
}
