import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import java.util.Locale;

public class ThymeleafEngine {
    public ThymeleafEngine(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
        this.context = new Context(new Locale("ru"));
    }

    private final Context context;
    private TemplateEngine templateEngine;

    public Context getContext() {
        return context;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
