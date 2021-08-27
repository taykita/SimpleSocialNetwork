package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.Message;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Controller
public class ChatController {
    @Autowired
    public ChatController(AccountRepository accountRepository, SimpMessagingTemplate messagingTemplate) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    @PostMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam int id,
                           Model model) throws AccStorageException {
        Account friend = accountRepository.get(id);

        updateModel(activeUser, model, friend);

        return "chat";
    }

    private void updateModel(User activeUser, Model model, Account friend) {
        model.addAttribute("name", friend.getName());
        model.addAttribute("userName", activeUser.getUsername());
        model.addAttribute("chatId", activeUser.getId() + friend.getId());

        model.addAttribute("isMain", false);
        model.addAttribute("isChat", false);
        model.addAttribute("isNews", false);
        model.addAttribute("isFriends", false);
        model.addAttribute("isUsers", false);
    }

    @MessageMapping("/chat")
    public void chatHandler(Message message) throws AccStorageException {
        accountRepository.addMessage(message);
        messagingTemplate.convertAndSend("/queue/chat/" + message.getChatId(), message);
    }

}
