package com.davita;

import com.davita.model.ContentRepository;
import com.davita.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DoclinkApplication {

    private static final Logger log = LoggerFactory.getLogger(DoclinkApplication.class);

    @Autowired
	private ContentRepository contentRepository;

	public static void main(String[] args) {
		SpringApplication.run(DoclinkApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository) {
		return (args) -> {
/*

			// create a couple of users
			userRepository.save(new User("jbauer", "Jack Bauer"));
			userRepository.save(new User("jdavis", "Jane Davis"));

			// create some content
            contentRepository.save(new Content("100", "Title 1", "Description 1"));
            contentRepository.save(new Content("200", "Title 2", "Description 2"));

            // fetch all users
            log.info("Users found with findAll():");
            log.info("-------------------------------");
            for (User user : userRepository.findAll()) {
                log.info(user.toString());
            }
            log.info("");

            // fetch all content
            log.info("Content found with findAll():");
            log.info("-------------------------------");
            for (Content content : contentRepository.findAll()) {
                log.info(content.toString());
            }
            log.info("");

            // attach content to user
            log.info("Looking for Jack:");
            log.info("-------------------------------");
            User jack = userRepository.findOne("jbauer");

            if (jack != null) {
                // Find a content
                Content newContent = contentRepository.findOne("100");

                if (newContent != null) {
                    jack.addReadContent(newContent.getId());
                    userRepository.save(jack);

                    // attach content to user
                    log.info("Looking for Jack:");
                    log.info("-------------------------------");
                    User searchJack = userRepository.findOne("jbauer");

                    log.info(searchJack.toString());
                }

            }
*/
		};
	}
}
