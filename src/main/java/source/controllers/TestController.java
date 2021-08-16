package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;

@Controller
public class TestController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private HttpSession session;

    @GetMapping("/test")
    public String test(Model model) throws AccStorageException {
        return "test";
    }

}
