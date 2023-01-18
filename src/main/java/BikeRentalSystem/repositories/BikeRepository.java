package BikeRentalSystem.repositories;

import BikeRentalSystem.models.RentalBike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<RentalBike, Long> {
    RentalBike findByUserId(Long id);
}
