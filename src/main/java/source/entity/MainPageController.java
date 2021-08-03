package source.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;

@Controller
public class MainPageController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/main")
    public String mainPage(Model model) throws AccStorageException {
        HttpSession session = getSession();

        Integer id = (Integer) session.getAttribute("id");

        if (id == null) {
            return "login";
        }

        model.addAttribute("name", getUserName(id));
        return "main";
    }

    private String getUserName(Integer id) throws AccStorageException {
        return accountRepository.get(id).getUserName();
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
