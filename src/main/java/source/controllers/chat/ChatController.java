package source.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Account;
import source.controllers.entity.Chat;
import source.controllers.entity.Message;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    public ChatController(AccountRepository accountRepository, SimpMessagingTemplate messagingTemplate,
                          ChatRepository chatRepository) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    @PostMapping("/create-chat")
    public String createChat(@AuthenticationPrincipal User activeUser,
                             @RequestParam Integer[] accIds,
                             @RequestParam(required = false, defaultValue = "default") String name) throws AccStorageException {

        List<Integer> ids = new ArrayList<>(Arrays.asList(accIds));
        ids.add(activeUser.getId());
        chatRepository.addChat(ids, name);

        return "redirect:chat-list";
    }

    @GetMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam int id,
                           Model model) throws AccStorageException {

        Chat chat = chatRepository.getChat(id);
        Account account = accountRepository.getAccount(activeUser.getId());

        updateModel(account, model, chat);

        return "chat";
    }

    private void updateModel(Account account, Model model, Chat chat) {

        model.addAttribute("name", chat.getName());
        model.addAttribute("userName", account.getName());
        model.addAttribute("userId", account.getId());
        model.addAttribute("chatId", chat.getId());
        model.addAttribute("active", SideMenuItems.NONE);
    }

    @MessageMapping("/chat")
    public void chatHandler(Message message) throws Exception {
        message = chatRepository.addMessage(message);

        for (String user : chatRepository.getUsersEmail(message.getChatId())) {
            messagingTemplate.convertAndSendToUser(user,
                    "/queue/chat/" + message.getChatId(), message);
        }
    }

    @GetMapping("/chat/get-messages")
    @ResponseBody
    public List<Message> getMessages(@RequestParam(required = false, defaultValue = "1") int firstMessageId,
                                     @RequestParam int chatId) throws AccStorageException {
        return chatRepository.getMessages(chatId, firstMessageId, 10);
    }

    @PostMapping("/private-chat")
    @ResponseBody
    public String privateChat(@AuthenticationPrincipal User activeUser,
                            @RequestParam int friendId) throws AccStorageException {
        int userId = activeUser.getId();
        if (chatRepository.existPrivateChat(userId, friendId)) {
            return "redirect:chat?id=";
        } else {
            String name = accountRepository.getAccount(userId).getName() + "-" + accountRepository.getAccount(friendId).getName();
            chatRepository.addPrivateChat(userId, friendId, name);
        }


    }

}
