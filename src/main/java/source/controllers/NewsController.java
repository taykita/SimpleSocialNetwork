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
public class NewsController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/news")
    public String newsPage(@AuthenticationPrincipal Account activeUser, HttpServletRequest request, Model model) throws AccStorageException {
        String rawCount = request.getParameter("count");
        int count = checkCount(rawCount);

        List<Post> posts = accountRepository.getFriendsPosts(activeUser, count);

        updateModel(model, count, posts);

        return "news";
    }

    private void updateModel(Model model, int count, List<Post> posts) {
        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);
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
