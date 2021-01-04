package com.gmail.podkutin.dmitry.voting_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.podkutin.dmitry.voting_system.repository.RestaurantRepository;
import com.gmail.podkutin.dmitry.voting_system.repository.UserRepository;
import com.gmail.podkutin.dmitry.voting_system.repository.VoteRepository;
import com.gmail.podkutin.dmitry.voting_system.web.dish.DishController;
import com.gmail.podkutin.dmitry.voting_system.web.json.JacksonObjectMapper;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.util.Arrays;

import static com.gmail.podkutin.dmitry.voting_system.util.ValidationUtil.checkNotFoundWithId;

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
