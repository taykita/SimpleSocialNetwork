package source.configuration;

import org.thymeleaf.TemplateEngine;
import source.database.DataBase;
import source.thymeleaf.config.ThymeleafEngineInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        initDataBase(servletContext);
        initTemplateEngine(servletContext);
    }

    private void initTemplateEngine(ServletContext servletContext) {
        TemplateEngine templateEngine = new ThymeleafEngineInitializer().init(servletContext);
        servletContext.setAttribute("templateEngine", templateEngine);
    }

    private void initDataBase(ServletContext servletContext) {
        DataBase dataBase = new DataBase();
        servletContext.setAttribute("dataBase", dataBase);
    }
}
