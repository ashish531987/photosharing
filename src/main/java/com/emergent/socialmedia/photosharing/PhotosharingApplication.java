package com.emergent.socialmedia.photosharing;

import com.emergent.socialmedia.photosharing.domain.FileStorageProperties;
import com.emergent.socialmedia.photosharing.domain.User;
import com.emergent.socialmedia.photosharing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class PhotosharingApplication {
	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(PhotosharingApplication.class, args);
	}

	@PostConstruct
	void populateDB(){
		// Create Users
		/*
				User1 - Photo1
					{likes}/   \{comments}
						User2	User3

				User2 - Photo2
					{likes}/	\{comments}
						User3	User2
		 */
		User user1 = new User();
		user1.setEmail("ashish@emergent.com");
		user1.setFullName("Ashish Mahamuni");
		user1.setPassword("vault@123");

		User user2 = new User();
		user2.setEmail("mangesh@emergent.com");
		user2.setFullName("Mangesh Kshirsagar");
		user2.setPassword("vault@123");

		User user3 = new User();
		user3.setEmail("mayur@emergent.com");
		user3.setFullName("Mayur Kore");
		user3.setPassword("vault@123");
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
	}
}
