package source.configuration;

import org.hibernate.Session;
import org.thymeleaf.TemplateEngine;
import source.database.AccountStorage;
import source.database.HibernateAccountStorage;
import source.database.hibernate.HibernateUtil;
import source.thymeleaf.config.ThymeleafEngineInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        connectPSQLDriver();
        ServletContext servletContext = sce.getServletContext();

        initDataBase(servletContext);
        initTemplateEngine(servletContext);
    }

    private void initTemplateEngine(ServletContext servletContext) {
        TemplateEngine templateEngine = new ThymeleafEngineInitializer().init(servletContext);
        servletContext.setAttribute("templateEngine", templateEngine);
    }

    private void initDataBase(ServletContext servletContext) {
        AccountStorage accountStorage = new HibernateAccountStorage(new HibernateUtil().getSessionFactory());
        servletContext.setAttribute("accountStorage", accountStorage);
    }

    private void connectPSQLDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
