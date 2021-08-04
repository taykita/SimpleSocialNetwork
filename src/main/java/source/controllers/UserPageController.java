package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Controller
public class UserPageController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/user-page")
    public String userPage(@RequestParam int id, @SessionAttribute("id") int userId, Model model) throws AccStorageException {
        if (id == userId) {
            return "main";
        }

        model.addAttribute("name", getUserName(id));
        model.addAttribute("isFriend", isFriend(userId, id));
        model.addAttribute("id", id);
        return "user-page";
    }

    private boolean isFriend(int userId, int friendId) throws AccStorageException {
        return accountRepository.isFriend(userId, friendId);
    }

    private String getUserName(Integer id) throws AccStorageException {
        return accountRepository.get(id).getUserName();
    }

}
