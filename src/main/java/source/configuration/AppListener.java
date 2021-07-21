package source.configuration;

import org.thymeleaf.TemplateEngine;
import source.database.DataBase;
import source.thymeleaf.config.ThymeleafEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        DataBase dataBase = new DataBase();
        TemplateEngine templateEngine = new ThymeleafEngine(servletContext).getTemplateEngine();
        servletContext.setAttribute("dataBase", dataBase);
        servletContext.setAttribute("templateEngine", templateEngine);
        int usersCount = 0;
        servletContext.setAttribute("usersCount", usersCount);
    }
}
