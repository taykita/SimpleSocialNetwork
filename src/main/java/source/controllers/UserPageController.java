package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserPageController {
    @Autowired
    public UserPageController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @GetMapping("/user-page")
    public String userPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam Integer id,
                           @RequestParam(required = false, defaultValue = "10") int count,
                           Model model) throws AccStorageException {

        if (isActiveUser(activeUser, id)) {
            return "redirect:main";
        }

        Account user = accountRepository.get(activeUser.getId());

        List<Post> posts = accountRepository.getPosts(id, count);

        updateModel(user, model, id, count, posts);

        return "user-page";
    }

    private boolean isActiveUser(User activeUser, int id) {
        return id == activeUser.getId();
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    private void updateModel(Account activeUser, Model model, int id, int count, List<Post> posts) throws AccStorageException {
        Account userAccount = accountRepository.get(id);

        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);
        model.addAttribute("name", userAccount.getName());
        model.addAttribute("isFriend", isFriend(activeUser, userAccount));
        model.addAttribute("id", id);
    }

    private boolean isFriend(Account user, Account friend) throws AccStorageException {
        return accountRepository.isFriend(user, friend);
    }

    @GetMapping("/user-page/get-posts")
    @ResponseBody
    public List<Post> getPosts(@RequestParam int id,
                               @RequestParam(required = false, defaultValue = "10") int count) throws AccStorageException {

        int postsLength = accountRepository.getPostsLength(id);
        if (count + 9 <= postsLength) {
            return accountRepository.getPosts(id, count, count + 9);
        } else {
            return accountRepository.getPosts(id, count, postsLength);
        }
    }

}
