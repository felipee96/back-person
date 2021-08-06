package person.pruebaback.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import person.pruebaback.entities.PersonEntiti;
import person.pruebaback.exceptions.ObjExistsExceptions;
import person.pruebaback.exceptions.ObjNoExistsExceptions;
import person.pruebaback.repositorys.PersonRepository;
import person.pruebaback.services.PersonServiceBase;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PersonServiceImpl implements PersonServiceBase {

    private Validator validator;
    private PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, Validator validator) {
        this.personRepository = personRepository;
        this.validator = validator;
    }

    @Override
    public PersonEntiti create(PersonEntiti perso) {
        this.validate(perso);
        this.personRepository.save(perso);
        return perso;
    }

    @Override
    public void delete(int id) {
        this.searchId(id);
        this.personRepository.deleteById(id);
    }

    @Override
    public PersonEntiti update(PersonEntiti perso) {
        this.searchId(perso.getId());
        this.validate(perso);
        this.personRepository.save(perso);
        return perso;
    }

    @Override
    public PersonEntiti searchId(int id) {
        return this.personRepository
                .findById(id)
                .orElseThrow(() -> new ObjNoExistsExceptions("No existe la persona con id " + id));
    }

    @Override
    public List<PersonEntiti> list() {
        return this.personRepository.findAll(Sort.by("id"));
    }

    private void validate(PersonEntiti person) {
        Set<ConstraintViolation<PersonEntiti>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            var sb = new StringBuilder();
            for (ConstraintViolation<PersonEntiti> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb, violations);
        }
        this.validatorExistPorFullname(person);
    }

    private void validatorExistPorFullname(PersonEntiti person) {
        Optional<PersonEntiti> personOk = this.personRepository.findByFullname(person.getFullname());
        if (personOk.isPresent() && !personOk.get().mismoId(person)) {
            throw new ObjExistsExceptions("Ya existe la persona " + person.getFullname());
        }
    }
}
