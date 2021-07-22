package source.thymeleaf.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import java.util.Locale;

public class ThymeleafEngineInitializer {
    public TemplateEngine init(ServletContext servletContext) {
        TemplateEngine templateEngine = new TemplateEngine();

        ServletContextTemplateResolver resolver = getResolver(servletContext);

        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    private ServletContextTemplateResolver getResolver(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        configResolver(resolver);
        return resolver;
    }

    private void configResolver(ServletContextTemplateResolver resolver) {
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
    }

    public static final Locale LOCALE = new Locale("ru");
}
