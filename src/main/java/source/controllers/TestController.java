package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class TestController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/test")
    public String test(Model model) throws AccStorageException {
        Post post = accountRepository.getPost(1);
        return "test";
    }

    @GetMapping("/testJSON")
    @ResponseBody
    public List<Post> testJSON(@RequestParam(required = false) int count) throws AccStorageException {
        return accountRepository.getFriendsPosts(accountRepository.getAccount("123@123"), count, count + 9);
    }

//    @PostMapping("/edit-acc")
//    public String editAcc(@AuthenticationPrincipal User activeUser,
//                          @ModelAttribute("newAccount") Account newAccount,
//                          @RequestParam(required = false) String chPass,
//                          @RequestParam(required = false) String oldPass) throws AccStorageException {
//        Account currentAccount = accountRepository.getAccount(activeUser.getId());
//
//        boolean isEdit = false;
//
//        isEdit = copy(currentAccount.getName(), newAccount.getName(), isEdit);
//        isEdit = copy(currentAccount.getEmail(), newAccount.getEmail(), isEdit);
//
//        if (!newAccount.getPass().equals("") &&
//                currentAccount.getPass().equals(passwordEncoder.encode(oldPass)) &&
//                newAccount.getPass().equals(chPass)) {
//            newAccount.setPass(passwordEncoder.encode(newAccount.getPass()));
//            isEdit = copy(currentAccount.getPass(), newAccount.getPass(), isEdit);
//        }
//
//        if (isEdit) {
//            accountRepository.updateAccount(currentAccount);
//        }
//
//        return "redirect:main";
//    }
//
//    private boolean copy(String dest, String orig, boolean isEdit) {
//        if (!dest.equals(orig)) {
//            dest = orig;
//            return true;
//        }
//        return isEdit;
//    }

}
