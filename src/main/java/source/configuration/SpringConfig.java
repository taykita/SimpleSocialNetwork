package source.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import source.database.ChatRepository;
import source.database.HibernateChatRepository;
import source.database.hibernate.HibernateUtil;
import source.interceptors.ChatCheckAuthInterceptor;

import javax.servlet.ServletContext;

@Configuration
@ComponentScan("source.controllers, source.database, source.file, source.service")
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

    @Value("${s3.accessKey}")
    private String accessKey;
    @Value("${s3.secretKey}")
    private String secretKey;

    @Bean
    public SessionFactory sessionFactory() {
        return hibernateUtil.getSessionFactory();
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
                                accessKey,
                                secretKey
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
