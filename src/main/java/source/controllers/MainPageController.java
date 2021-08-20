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

import java.util.List;

@Controller
public class MainPageController {
    @Autowired
    public MainPageController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;


    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam(required = false, defaultValue = "10") int count,
                           Model model) throws AccStorageException {

        Account user = accountRepository.get(activeUser.getId());

        List<Post> posts = accountRepository.getPosts(activeUser.getId(), count);

        updateModel(user, model, count, posts);

        return "main";
    }

    private void updateModel(Account activeUser, Model model, int count, List<Post> posts) {
        model.addAttribute("post", new Post());
        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);
        model.addAttribute("name", activeUser.getName());
    }

    @GetMapping("/main/get-posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return accountRepository.getPosts(activeUser.getId(), firstPostId, 10);
    }

}
