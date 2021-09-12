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
import source.controllers.entity.html.SideMenuItems;
import source.database.AccountRepository;
import source.database.ChatRepository;
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

    @GetMapping("/create-chat")
    public String createChatPage(@AuthenticationPrincipal User activeUser,
                                 Model model) throws AccStorageException {

        List<Account> allFriends = accountRepository.getFriends(activeUser.getId());
        model.addAttribute("users", allFriends);
        return "create-chat";
    }

    @PostMapping("/create-chat")
    public String createChat(@AuthenticationPrincipal User activeUser,
                             @RequestParam Integer[] accIds,
                             @RequestParam(required = false, defaultValue = "default") String name) throws AccStorageException {

        List<Integer> ids = new ArrayList<>(Arrays.asList(accIds));
        ids.add(activeUser.getId());
        chatRepository.addChat(ids, name, 1);

        return "redirect:chat-list";
    }

    @GetMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam int id,
                           Model model) throws AccStorageException {

        Chat chat = chatRepository.getChat(id);

        List<String> usersEmail = chatRepository.getUsersEmail(chat.getId());
        if (!usersEmail.contains(activeUser.getUsername())) {
            return "redirect:chat-list";
        }

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
        model.addAttribute("chatType", chat.getType());
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
    public String privateChat(@AuthenticationPrincipal User activeUser,
                              @RequestParam int friendId) throws AccStorageException {
        int userId = activeUser.getId();
        if (chatRepository.existPrivateChat(userId, friendId)) {
            Chat privateChat = chatRepository.getPrivateChat(userId, friendId);
            return "redirect:chat?id=" + privateChat.getId();
        } else {
            String name = accountRepository.getAccount(userId).getName() + "-" + accountRepository.getAccount(friendId).getName();
            Chat chat = chatRepository.addPrivateChat(userId, friendId, name);
            return "redirect:chat?id=" + chat.getId();
        }
    }

    @GetMapping("/edit-chat")
    public String editChatPage(@RequestParam int chatId,
                               Model model) throws AccStorageException {
        List<Account> allUsers = chatRepository.getUsersFromChat(chatId);
        updateModel(model, allUsers, chatId);
        return "edit-chat";
    }

    @GetMapping("/add-chat-user")
    public String addChatUserPage(@RequestParam int chatId,
                                  Model model) throws AccStorageException {
        List<Account> otherUsers = accountRepository.getAllAccounts();
        updateModel(model, otherUsers, chatId);
        return "add-chat-user";
    }

    private void updateModel(Model model, List<Account> allUsers, int chatId) {
        model.addAttribute("users", allUsers);
        model.addAttribute("active", SideMenuItems.NONE);
        model.addAttribute("chatId", chatId);
    }

    @PostMapping("/delete-from-chat")
    public String deleteFromChat(@RequestParam int id,
                                 @RequestParam int chatId) throws AccStorageException {
        chatRepository.deleteChatUser(id, chatId);
        return "redirect:chat?id=" + chatId;
    }

    @PostMapping("/add-in-chat")
    public String addInChat(@RequestParam int id,
                            @RequestParam int chatId) throws AccStorageException {
        chatRepository.addChatUser(id, chatId);
        return "redirect:chat?id=" + chatId;
    }
}
