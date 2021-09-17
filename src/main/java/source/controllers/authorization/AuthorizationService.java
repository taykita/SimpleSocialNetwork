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

    public String registration(Account account, BindingResult bindingResult, String chPass) throws AccStorageException {
        if (bindingResult.hasErrors())
            return "sign-in";

        if (account.getPass().equals(chPass)) {
            return checkExistAndCreateAcc(account);
        } else {
            return "redirect:" + "sign";
        }
    }

    public void checkErrorAndLogout(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Неправильное имя пользователя или пароль!");
        }

        if (logout != null) {
            model.addAttribute("logout", "Вы вышли!");
        }
    }

    private String checkExistAndCreateAcc(Account account) throws AccStorageException {
        if (existAccount(account.getEmail())) {
            return "redirect:" + "sign";
        } else {
            addAccount(account);
            return "redirect:" + "login";
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
