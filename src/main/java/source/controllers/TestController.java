package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class TestController {
    @Autowired
    AccountRepository accountRepository;


    @GetMapping("/test")
    public String test(Model model) throws AccStorageException {
        return "test";
    }

    @GetMapping("/testJSON")
    @ResponseBody
    public List<Post> testJSON(@RequestParam(required = false) int count) throws AccStorageException {
        return accountRepository.getFriendsPosts(accountRepository.get("123@123"), count, count + 9);
    }

}
