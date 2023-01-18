package BikeRentalSystem.services;
import BikeRentalSystem.models.RentalBike;
import BikeRentalSystem.repositories.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {
    @Autowired
    BikeRepository bikeRepository;

    public List<RentalBike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public List<RentalBike> getAvailableBikes() {
        return getAllBikes().stream().filter(
                rentalBike -> rentalBike.getUser() == null
        ).collect(Collectors.toList());
    }

    public RentalBike getBike(Long id) {
        return bikeRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public RentalBike saveBike(RentalBike bike) throws IllegalArgumentException {
        return bikeRepository.save(bike);
    }

    public void removeAssignment(Long customerId) {
        RentalBike bike = bikeRepository.findByUserId(customerId);
        if (bike != null) {
            bike.setUser(null);
            saveBike(bike);
        }
    }
}
