package source.controllers.post;

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
import source.exception.AccStorageException;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    private final PostService postService;
    
    //TODO Разобраться с отображением ошибки
    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                             @AuthenticationPrincipal User activeUser) throws AccStorageException {
        if (bindingResult.hasErrors())
            return "redirect:main";

        postService.createPostAndSendToUsers(post, activeUser);

        return "redirect:main";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam int id) throws AccStorageException {
        postService.deletePost(id);
        return "redirect:main";
    }

    @PostMapping("/edit-post")
    public String editPost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                           @RequestParam int id) throws AccStorageException {
        if (bindingResult.hasErrors())
            return "redirect:edit-post";

        postService.updatePost(post, id);
        return "redirect:main";
    }

    @PostMapping("/edit-post-page")
    public String editPostPage(@RequestParam int id, Model model) throws AccStorageException {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("postId", id);

        return "edit-post";
    }

}
