package source.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.LoggerFactory;
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
import source.interceptors.ChatCheckAuthInterceptor;

import javax.servlet.ServletContext;
import java.util.Arrays;

@Configuration
@ComponentScan("source.controllers, source.database, source.file, source.service")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    @Autowired
    public SpringConfig(ApplicationContext applicationContext, ServletContext servletContext) {
        this.applicationContext = applicationContext;
        this.servletContext = servletContext;
    }

    private final ApplicationContext applicationContext;
    private final ServletContext servletContext;

    @Value("${s3.accessKey}")
    private String accessKey;
    @Value("${s3.secretKey}")
    private String secretKey;
    @Value("${s3.serviceEndpoint}")
    private String serviceEndpoint;
    @Value("${s3.signingRegion}")
    private String signingRegion;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public SessionFactory sessionFactory() {
        return getSessionFactory();
    }

    private SessionFactory getSessionFactory() {
        final StandardServiceRegistry registry = buildRegistry();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException(e);
        }
    }

    private StandardServiceRegistry buildRegistry() {
        return new StandardServiceRegistryBuilder()
                .configure()
                .build();
    }

    @Bean
    public ChatRepository chatRepository() {
        return new HibernateChatRepository(sessionFactory());
    }

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion))
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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
