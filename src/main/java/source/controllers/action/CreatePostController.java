package source.controllers.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import source.controllers.entity.Post;
import source.database.AccountRepository;

import javax.validation.Valid;

@Controller
public class CreatePostController {
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") @Valid Post post,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "main";
        }
        accountRepository.

    }
}
