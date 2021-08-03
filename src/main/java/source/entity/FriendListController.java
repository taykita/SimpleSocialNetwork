package source.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.verification.entity.Account;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FriendListController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/friend-list")
    public String friendListPage(Model model) throws AccStorageException {
        List<Account> allUsers = getFriends();

        model.addAttribute("users", allUsers.toArray());
        return "friend-list";
    }

    private List<Account> getFriends() throws AccStorageException {
        List<Account> allUsers;
        allUsers = accountRepository.getFriends(getUserId());
        return allUsers;
    }

    private int getUserId() {
        return (Integer) getSession().getAttribute("id");
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
