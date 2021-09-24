package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import source.database.AccountRepository;

@Controller
public class TestController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


}
