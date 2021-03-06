package source.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class NewsController {
    @Autowired
    public NewsController(PostService postService) {
        this.postService = postService;
    }

    private final PostService postService;


    @GetMapping("/news")
    public String newsPage(@AuthenticationPrincipal User activeUser, Model model) {
        model.addAttribute("id", activeUser.getId());
        model.addAttribute("active", SideMenuItems.NEWS);
        return "news";
    }

    @GetMapping("/news/get-posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return postService.getPosts(activeUser.getId(), firstPostId);
    }

}
