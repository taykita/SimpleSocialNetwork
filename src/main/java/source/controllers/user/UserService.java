package source.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.chat.ChatType;
import source.controllers.main.MainService;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;
import source.file.store.FileStoreClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserService(AccountRepository accountRepository, ChatRepository chatRepository,
                       MainService mainService, FileStoreClient fileStoreClient) {
        this.accountRepository = accountRepository;
        this.chatRepository = chatRepository;
        this.mainService = mainService;
        this.fileStoreClient = fileStoreClient;
    }

    private final AccountRepository accountRepository;
    private final ChatRepository chatRepository;
    private final MainService mainService;
    private final FileStoreClient fileStoreClient;

    public List<Account> getAllAccounts() throws AccStorageException {
        return accountRepository.getAllAccounts();
    }

    public Account getAccount(int id) throws AccStorageException {
        return accountRepository.getAccount(id);
    }

    public boolean isFriend(Account user, Account friend) throws AccStorageException {
        return accountRepository.isFriend(user, friend);
    }

    public List<Post> getPosts(int id, int firstPostId) throws AccStorageException {
        return mainService.getPosts(id, firstPostId);
    }

    public byte[] loadImage(String fullName) throws IOException {
        return fileStoreClient.loadImage(fullName);
    }

    public void addAccount(Account account) throws AccStorageException {
        account = accountRepository.addAccount(account);
        List<Integer> ids = new ArrayList<>();
        ids.add(account.getId());
        chatRepository.addChat(ids, "Сохраненные сообщения", ChatType.SAVED);
    }
}
