package source.verification;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class SignIn extends HttpServlet {
    TemplateEngine templateEngine;
    DataBase dataBase;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        dataBase = (DataBase) getServletContext().getAttribute("dataBase");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        Context context = new Context(LOCALE);
        templateEngine.process("sign-in", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");
        String checkPassword = req.getParameter("chPass");
        if (password.equals(checkPassword)) {
            Account newAccount = new Account(email, password);
            newAccount.setUserName(req.getParameter("userName"));
            if (dataBase.exist(email)) {
                resp.sendRedirect("sign");
            } else {
                newAccount.setId(dataBase.getCountUsers() + 1);
                dataBase.add(newAccount);
                req.getSession().setAttribute("id", newAccount.getId());
                resp.sendRedirect("main");
            }
        } else {
            resp.sendRedirect("sign");
        }
    }
}
