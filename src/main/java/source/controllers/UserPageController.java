package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserPageController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/user-page")
    public String userPage(@AuthenticationPrincipal Account activeUser,
                           HttpServletRequest request, Model model) throws AccStorageException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (id == activeUser.getId()) {
            return "redirect:main";
        }

        String rawCount = request.getParameter("count");
        int count = checkCount(rawCount);

        List<Post> posts = accountRepository.getPosts(id, count);

        updateModel(activeUser, model, id, count, posts);
        return "user-page";
    }

    private void updateModel(Account activeUser, Model model, int id, int count, List<Post> posts) throws AccStorageException {
        Account userAccount = accountRepository.get(id);

        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);

        model.addAttribute("name", userAccount.getName());
        model.addAttribute("isFriend", isFriend(activeUser, userAccount));
        model.addAttribute("id", id);
    }

    private int checkCount(String rawCount) {
        int count;
        if (rawCount == null) {
            count = 10;
        } else {
            count = Integer.parseInt(rawCount);
        }
        return count;
    }

    private boolean isFriend(Account user, Account friend) throws AccStorageException {
        return accountRepository.isFriend(user, friend);
    }

}
