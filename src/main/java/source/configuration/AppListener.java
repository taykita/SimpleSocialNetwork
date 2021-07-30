package source.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.thymeleaf.TemplateEngine;
import source.database.AccountStorage;
import source.database.DataBaseAccountStorage;
import source.database.QueryController;
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
        AccountStorage accountStorage = new DataBaseAccountStorage(createQueryController());
        servletContext.setAttribute("accountStorage", accountStorage);
    }

    private QueryController createQueryController() {
        return new QueryController(createPool());
    }

    private BasicDataSource createPool() {
        BasicDataSource connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("org.postgresql.Driver");
        connectionPool.setUrl("jdbc:postgresql://78.24.220.161/booknetwork_db");
        connectionPool.setUsername("admin");
        connectionPool.setPassword("admin");
        return connectionPool;
    }
}
