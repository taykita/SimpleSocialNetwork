package source.entity;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.AccountStorage;
import source.verification.Account;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class FriendListServlet extends HttpServlet {

    TemplateEngine templateEngine;
    AccountStorage accountStorage;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        accountStorage = (AccountStorage) getServletContext().getAttribute("collectionAccountStorage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        Context context = new Context(LOCALE);
        List<Account> allUsers = accountStorage.getAll();

        context.setVariable("users", allUsers.toArray());
        templateEngine.process("all-users", context, resp.getWriter());
    }
}
