package BikeRentalSystem.repositories;
import BikeRentalSystem.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long> {

}
