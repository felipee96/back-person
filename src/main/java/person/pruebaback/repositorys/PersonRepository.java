package person.pruebaback.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import person.pruebaback.entities.PersonEntiti;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntiti, Integer> {
    Optional<PersonEntiti> findByFullname(String fullname);
}
