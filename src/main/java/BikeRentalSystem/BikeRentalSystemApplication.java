package BikeRentalSystem;

import BikeRentalSystem.models.Authority;
import BikeRentalSystem.models.AuthorityEnum;
import BikeRentalSystem.models.UserMeta;
import BikeRentalSystem.models.UserPrincipal;
import BikeRentalSystem.repositories.AuthorityRepo;
import BikeRentalSystem.repositories.UserPrincipalRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class BikeRentalSystemApplication implements CommandLineRunner {
	@Autowired
	private AuthorityRepo authorityRepo;

	@Autowired
	private UserPrincipalRepo userPrincipalRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BikeRentalSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Authority userAuth = Authority.builder().authority(AuthorityEnum.ROLE_USER).build();
		Authority adminAuth = Authority.builder().authority(AuthorityEnum.ROLE_ADMIN).build();
		Authority customerAuth = Authority.builder().authority(AuthorityEnum.ROLE_CUSTOMER).build();

		if (authorityRepo.findAll().isEmpty()) {
			authorityRepo.saveAll(Arrays.asList(userAuth,adminAuth, customerAuth));
		}

		if (userPrincipalRepo.findAll().isEmpty()) {
			UserMeta admin = UserMeta.builder()
					.surname("Admin")
					.givenName("User")
					.email("admin@email.com")
					.age(35)
					.build();
			UserMeta customer1 = UserMeta.builder()
					.surname("Customer")
					.givenName("One")
					.email("customer1@email.com")
					.age(25)
					.build();
			UserMeta customer2 = UserMeta.builder()
					.surname("Customer")
					.givenName("Two")
					.email("customer2@email.com")
					.age(55)
					.build();
			UserMeta customer3 = UserMeta.builder()
					.surname("Customer")
					.givenName("Three")
					.email("customer3@email.com")
					.age(29)
					.build();
			userPrincipalRepo.saveAll(
					Arrays.asList(
							new UserPrincipal("CUSTOMER1", passwordEncoder.encode("testpassword"),
									Arrays.asList(userAuth, customerAuth), customer1),
							new UserPrincipal("CUSTOMER2", passwordEncoder.encode("testpassword"),
									Arrays.asList(userAuth, customerAuth), customer2),
							new UserPrincipal("CUSTOMER3", passwordEncoder.encode("testpassword"),
									Arrays.asList(userAuth, customerAuth), customer3),
							new UserPrincipal("ADMIN", passwordEncoder.encode("admin"),
									Arrays.asList(adminAuth, userAuth), admin)
					)
			);
		}


	}
}
