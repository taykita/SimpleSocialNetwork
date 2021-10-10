package source.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.exception.AccStorageException;
import source.service.MessagingClient;

import javax.validation.Valid;

@Controller
public class PostController {
    @Autowired
    public PostController(PostService postService, MessagingClient messagingClient) {
        this.postService = postService;
        this.messagingClient = messagingClient;
    }

    private final MessagingClient messagingClient;
    private final PostService postService;

//    @PostMapping("/create-post")
    @PostMapping("/posts")
    public String createPost(@RequestParam String postText,
                             @AuthenticationPrincipal User activeUser) throws AccStorageException {

        Post post = new Post();
        post.setText(postText);
        messagingClient.sendPostToUsers(post, activeUser.getId());

        return "redirect:main";
    }

//    @PostMapping("/delete-post")
    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable int id) throws AccStorageException {
        postService.deletePost(id);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable int id) throws AccStorageException {
        Post post = postService.getPost(id);
        post.setAccount(null);
        return post;
    }

//    @PostMapping("/edit-post")
    @PutMapping("/posts")
    public void editPost(@RequestParam String text, @RequestParam int id) throws AccStorageException {
        Post post = new Post();
        post.setText(text);
        postService.updatePost(post, id);
    }

    @GetMapping("/edit-post-page")
    public String editPostPage(@RequestParam int id, Model model) throws AccStorageException {

        return "edit-post";
    }

}
