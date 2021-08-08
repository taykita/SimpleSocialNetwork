package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class TestController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    HttpSession session;

    @GetMapping("/test")
    public String test(Model model) throws AccStorageException {

        Post post = new Post();

        post.setDate(new Date().toString());
        post.setText("text");
        post.setAccount(accountRepository.get("123@123"));

        model.addAttribute("post", post);

        return "test";
    }

}
