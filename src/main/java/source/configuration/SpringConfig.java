package source.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import source.database.ChatRepository;
import source.database.HibernateChatRepository;
import source.database.hibernate.HibernateUtil;
import source.interceptors.ChatCheckAuthInterceptor;

import javax.servlet.ServletContext;

@Configuration
@ComponentScan("source.controllers, source.database, source.file")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    @Autowired
    public SpringConfig(ApplicationContext applicationContext, ServletContext servletContext,
                        HibernateUtil hibernateUtil) {
        this.applicationContext = applicationContext;
        this.servletContext = servletContext;
        this.hibernateUtil = hibernateUtil;
    }

    private final ApplicationContext applicationContext;
    private final ServletContext servletContext;
    private final HibernateUtil hibernateUtil;

    @Bean
    public SessionFactory sessionFactory() {
        return hibernateUtil.getSessionFactory();
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public ResourceHandlerRegistry resourceHandlerRegistry() {
        return new ResourceHandlerRegistry(applicationContext, servletContext);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public ChatRepository chatRepository() {
        return new HibernateChatRepository(sessionFactory());
    }

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("obs.ru-moscow-1.hc.sbercloud.ru", "ru-moscow"))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                "XERXALIJGGXADWXT5GT4",
                                "49LJyMHC1sXRvjBSqS6QH4RjG2pR4BL1FSEEjxUb"
                        )))
                .build();
        return amazonS3;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ChatCheckAuthInterceptor(chatRepository()))
                .addPathPatterns("/chat", "/edit-chat", "/add-chat-user");
    }


}
