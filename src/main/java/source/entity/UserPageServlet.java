package source.entity;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class UserPageServlet extends HttpServlet {

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
        int id = Integer.parseInt(req.getParameter("id"));

        context.setVariable("name", dataBase.get(id).getUserName());
        templateEngine.process("user-page", context, resp.getWriter());
    }

}