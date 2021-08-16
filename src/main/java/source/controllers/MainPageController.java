package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainPageController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal Account activeUser, HttpServletRequest request, Model model) throws AccStorageException {
        model.addAttribute("post", new Post());

        String rawCount = request.getParameter("count");
        int count = checkCount(rawCount);

        List<Post> posts = accountRepository.getPosts(activeUser.getId(), count);

        updateModel(activeUser, model, count, posts);

        return "main";
    }

    private void updateModel(Account activeUser, Model model, int count, List<Post> posts) {
        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);
        model.addAttribute("name", activeUser.getName());
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

}
