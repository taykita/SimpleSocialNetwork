package source.controllers.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.exception.AccStorageException;
import source.service.MessagingClient;

@Controller
public class PostController {
    @Autowired
    public PostController(PostService postService, MessagingClient messagingClient) {
        this.postService = postService;
        this.messagingClient = messagingClient;
    }

    private final MessagingClient messagingClient;
    private final PostService postService;

    @PostMapping("/posts")
    public String createPost(@RequestBody PostDTO postDTO,
                             @AuthenticationPrincipal User activeUser) throws AccStorageException, JsonProcessingException {

        Post post = new Post();
        post.setText(postDTO.getText());
        post = postService.addPost(post, activeUser.getId());
        messagingClient.sendPostToUsers(post, activeUser.getId());

        return "redirect:main";
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable int id) throws AccStorageException {
        postService.deletePost(id);
    }

    @GetMapping("/posts/{id}")
    @ResponseBody
    public Post getPost(@PathVariable int id) throws AccStorageException {
        Post post = postService.getPost(id);
        post.setAccount(null);
        return post;
    }

    @PutMapping(value = "/posts", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public PostDTO editPost(@RequestBody PostDTO postDTO) throws AccStorageException {
        postService.updatePost(postDTO.getText(), postDTO.getId());
        return postDTO;
    }

    @GetMapping("/edit-post-page")
    public String editPostPage(@RequestParam int id, Model model) throws AccStorageException {

        return "edit-post";
    }

}
