package source.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;

@Controller
public class AddFriendController {
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("add-friend")
    public String addFriend(@RequestParam int id) throws AccStorageException {
        addFriend(getUserId(), id);

        return "redirect:" + "user-page?id=" + id;
    }

    private void addFriend(int userId, int friendId) throws AccStorageException {
        accountRepository.addFriend(userId, friendId);
    }

    private int getUserId() {
        return (Integer) getSession().getAttribute("id");
    }


    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
