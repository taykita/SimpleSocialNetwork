package source.entity;

import source.database.DataBase;
import source.thymeleaf.config.ThymeleafEngine;
import source.verification.Account;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public class FriendListServlet extends HttpServlet {

    TemplateEngine templateEngine;
    DataBase dataBase;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        dataBase = (DataBase) getServletContext().getAttribute("dataBase");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Context context = new Context(new Locale("ru"));
        List<Account> allUsers = dataBase.getAll();
        context.setVariable("users", allUsers.toArray());
        templateEngine.process("all-users", context, resp.getWriter());
    }
}
