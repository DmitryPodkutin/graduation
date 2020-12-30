package ru.graduation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.graduation.repository.RestaurantRepository;
import ru.graduation.repository.UserRepository;
import ru.graduation.repository.VoteRepository;
import ru.graduation.web.json.JacksonObjectMapper;
import ru.graduation.web.dish.DishController;


import java.util.Arrays;

import static ru.graduation.util.ValidationUtil.checkNotFoundWithId;

public class SpringMain {

    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-db.xml","spring/spring-app.xml", "spring/spring-mvc.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            UserRepository userRepository = appCtx.getBean(UserRepository.class);
            RestaurantRepository restaurantRepository =appCtx.getBean(RestaurantRepository.class);
            VoteRepository voteRepository =appCtx.getBean(VoteRepository.class);
            DishController dishRestController =appCtx.getBean(DishController.class);
            JacksonObjectMapper objectMapper = appCtx.getBean(JacksonObjectMapper.class);
            ObjectMapper objectMapper2 = appCtx.getBean(ObjectMapper.class);
        }
    }
}
