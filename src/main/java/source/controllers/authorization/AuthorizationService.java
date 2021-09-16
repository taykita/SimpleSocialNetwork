package source.controllers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationService {
    @Autowired
    public AuthorizationService(AccountRepository accountRepository, ChatRepository chatRepository) {
        this.accountRepository = accountRepository;
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;

    public void addAccount(Account account) throws AccStorageException {
        account = accountRepository.addAccount(account);
        List<Integer> ids = new ArrayList<>();
        ids.add(account.getId());
        chatRepository.addChat(ids, "Сохраненные сообщения", 3);
    }

    public boolean existAccount(String email) throws AccStorageException {
        return accountRepository.existAccount(email);
    }
}
