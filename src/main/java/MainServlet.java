
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;

import mainLogic.WelcomeLogic;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(getServletContext());
        resolver.setPrefix("/webapp/");
        resolver.setSuffix(".jsp");
        //resolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        final Context context = new Context(Locale.US);
        context.setVariable("name", "User_name");
        templateEngine.process("main", context, resp.getWriter());

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        String welcomeString = new WelcomeLogic().getWelcomeString(name, surname);

        out.println("<h1>" + welcomeString + "</h1>");

        out.println("Текущее время - " + new Date());
        out.println("<a href=\"/count\"> Кол-во </a>");

    }

}