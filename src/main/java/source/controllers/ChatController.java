package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Account;
import source.controllers.entity.Message;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.enums.SideMenuEnum;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    public ChatController(AccountRepository accountRepository, SimpMessagingTemplate messagingTemplate, ChatRepository chatRepository) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    @PostMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam int id,
                           Model model) throws AccStorageException {
        if (chatRepository.exist(id)) {
            
        }

        Account friend = accountRepository.get(id);

        updateModel(activeUser, model, friend);

        return "chat";
    }

    private void updateModel(User activeUser, Model model, Account friend) {
        model.addAttribute("name", friend.getName());
        model.addAttribute("userName", activeUser.getUsername());
        model.addAttribute("chatId", activeUser.getId() + friend.getId());
        model.addAttribute("active", SideMenuEnum.NONE);
    }

    @MessageMapping("/chat")
    public void chatHandler(Message message) throws AccStorageException {
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        message.setAccount(accountRepository.get(activeUser.getId()));
//        accountRepository.addMessage(message);
//        messagingTemplate.convertAndSend("/queue/chat/" + message.getChatId(), message);
    }

    @GetMapping("/chat/get-messages")
    @ResponseBody
    public List<Message> getMessages(@RequestParam(required = false, defaultValue = "1") int firstMessageId,
                                     @RequestParam int chatId) throws AccStorageException {

        return accountRepository.getMessages(chatId, firstMessageId, 10);
    }

}
