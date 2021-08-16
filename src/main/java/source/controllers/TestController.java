package source.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.exception.AccStorageException;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test(Model model) throws AccStorageException {
        return "test";
    }

}
