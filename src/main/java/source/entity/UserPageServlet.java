package source.entity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import source.database.DataBase;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.thymeleaf.config.ThymeleafEngine;

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
        PrintWriter out = resp.getWriter();

        Context context = new Context(new Locale("ru"));
        context.setVariable("name", dataBase.get(Integer.parseInt(req.getParameter("id"))).getUserName());
        templateEngine.process("user-page", context, resp.getWriter());
    }

}