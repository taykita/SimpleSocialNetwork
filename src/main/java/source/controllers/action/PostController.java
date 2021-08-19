package source.controllers.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class PostController {
    @Autowired
    public PostController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    //TODO Разобраться с отображением ошибки
    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                             @AuthenticationPrincipal User activeUser) throws AccStorageException {
        if (bindingResult.hasErrors())
            return "redirect:main";

        accountRepository.addPost(post, accountRepository.get(activeUser.getId()));
        return "redirect:main";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam int id) throws AccStorageException {
        accountRepository.deletePost(accountRepository.getPost(id));
        return "redirect:main";
    }

    @PostMapping("/edit-post")
    public String editPost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                           @RequestParam int id) throws AccStorageException {
        if (bindingResult.hasErrors())
            return "redirect:edit-post";

        accountRepository.updatePost(accountRepository.getPost(id), post);
        return "redirect:main";
    }

    @PostMapping("/edit-post-page")
    public String editPostPage(@RequestParam int id, Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("postId", id);

        return "edit-post";
    }


}
