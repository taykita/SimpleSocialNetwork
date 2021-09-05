package source.controllers.main;

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
import source.controllers.entity.html.SideMenuItems;
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
                           Model model) throws AccStorageException {

        Account user = accountRepository.getAccount(activeUser.getId());

        updateModel(user, model);

        return "main";
    }

    private void updateModel(Account activeUser, Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("name", activeUser.getName());
        model.addAttribute("id", activeUser.getId());
        model.addAttribute("active", SideMenuItems.MAIN);
    }

    @GetMapping("/main/get-posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return accountRepository.getPosts(activeUser.getId(), firstPostId, 10);
    }

}
