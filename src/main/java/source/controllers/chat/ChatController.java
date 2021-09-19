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
import source.controllers.entity.User;
import source.controllers.entity.chat.Chat;
import source.controllers.entity.chat.Message;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    public ChatController(ChatService chatService,
                          SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/create-chat")
    public String createChatPage(@AuthenticationPrincipal User activeUser,
                                 Model model) throws AccStorageException {

        List<Account> allFriends = chatService.getFriends(activeUser);
        model.addAttribute("users", allFriends);
        return "create-chat";
    }


    @PostMapping("/create-chat")
    public String createChat(@AuthenticationPrincipal User activeUser,
                             @RequestParam(required = false) Integer[] accIds,
                             @RequestParam(required = false, defaultValue = "default") String name) throws AccStorageException {

        chatService.addChat(activeUser, accIds, name);

        return "redirect:chat-list";
    }

    @GetMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam int chatId,
                           Model model) throws AccStorageException {


        Account account = chatService.getAccount(activeUser);
        Chat chat = chatService.getChat(chatId, account);

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
        message = chatService.addMessage(message);

        for (String user : chatService.getUsersEmail(message.getChatId())) {
            messagingTemplate.convertAndSendToUser(user,
                    "/queue/chat/" + message.getChatId(), message);
        }
    }

    @GetMapping("/chat/get-messages")
    @ResponseBody
    public List<Message> getMessages(@RequestParam(required = false, defaultValue = "1") int firstMessageId,
                                     @RequestParam int chatId) throws AccStorageException {
        return chatService.getMessages(chatId, firstMessageId);
    }

    @PostMapping("/private-chat")
    public String privateChat(@AuthenticationPrincipal User activeUser,
                              @RequestParam int friendId) throws AccStorageException {
        int userId = activeUser.getId();
        return "redirect:chat?chatId=" + chatService.redirectToChat(friendId, userId);
    }

    @GetMapping("/edit-chat")
    public String editChatPage(@RequestParam int chatId,
                               Model model) throws AccStorageException {
        List<Account> allUsers = chatService.getAllUsersFromChat(chatId);
        updateModel(model, allUsers, chatId);
        return "edit-chat";
    }

    @GetMapping("/add-chat-user")
    public String addChatUserPage(@RequestParam int chatId,
                                  @AuthenticationPrincipal User activeUser,
                                  Model model) throws AccStorageException {
        List<Account> otherFriends = chatService.getOtherFriends(chatId, activeUser.getId());
        updateModel(model, otherFriends, chatId);
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
        chatService.deleteChatUser(id, chatId);
        return "redirect:chat?id=" + chatId;
    }

    @PostMapping("/add-in-chat")
    public String addInChat(@RequestParam int id,
                            @RequestParam int chatId) throws AccStorageException {
        chatService.addChatUser(id, chatId);
        return "redirect:chat?id=" + chatId;
    }

}
