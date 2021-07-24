package source.entity;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.AccountStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class UserPageServlet extends HttpServlet {

    TemplateEngine templateEngine;
    AccountStorage accountStorage;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        accountStorage = (AccountStorage) getServletContext().getAttribute("collectionAccountStorage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        Context context = new Context(LOCALE);
        int id = Integer.parseInt(req.getParameter("id"));

        context.setVariable("name", accountStorage.get(id).getUserName());
        templateEngine.process("user-page", context, resp.getWriter());
    }

}