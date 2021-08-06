package source.controllers.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.validation.Valid;

@Controller
public class CreatePostController {
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") Post post,
                             @SessionAttribute int id) throws AccStorageException {

        accountRepository.addPost(post, id);
        return "redirect:main";
    }
}
