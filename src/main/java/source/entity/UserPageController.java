package source.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;

@Controller
public class UserPageController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/user-page")
    public String userPage(@RequestParam int id, Model model) throws AccStorageException {
        if (isCurrentUser(id)) {
            return "main";
        }

        model.addAttribute("name", getUserName(id));
        model.addAttribute("isFriend", isFriend(getUserId(), id));
        model.addAttribute("id", id);
        return "user-page";
    }

    private boolean isCurrentUser(int id) {
        return getUserId() == id;
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    private boolean isFriend(int userId, int friendId) throws AccStorageException {
        return accountRepository.isFriend(userId, friendId);
    }

    private String getUserName(Integer id) throws AccStorageException {
        return accountRepository.get(id).getUserName();
    }

    private int getUserId() {
        return (Integer) getSession().getAttribute("id");
    }

}
