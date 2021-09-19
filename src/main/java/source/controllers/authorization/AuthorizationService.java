package source.controllers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import source.controllers.entity.Account;
import source.controllers.entity.chat.ChatType;
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

    public boolean registration(Account account, BindingResult bindingResult, String chPass) throws AccStorageException {

        if (account.getPass().equals(chPass)) {
            return checkExistAndCreateAcc(account);
        } else {
            return false;
        }
    }

    private boolean checkExistAndCreateAcc(Account account) throws AccStorageException {
        if (existAccount(account.getEmail())) {
            return false;
        } else {
            addAccount(account);
            return true;
        }
    }

    private void addAccount(Account account) throws AccStorageException {
        account = accountRepository.addAccount(account);
        List<Integer> ids = new ArrayList<>();
        ids.add(account.getId());
        chatRepository.addChat(ids, "Сохраненные сообщения", ChatType.SAVED);
    }

    private boolean existAccount(String email) throws AccStorageException {
        return accountRepository.existAccount(email);
    }

}
