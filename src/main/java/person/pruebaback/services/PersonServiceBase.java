package person.pruebaback.services;

import person.pruebaback.entities.PersonEntiti;

import java.util.List;

public interface PersonServiceBase {

    PersonEntiti create(PersonEntiti perso);

    void delete(int id);

    PersonEntiti update(PersonEntiti perso);

    PersonEntiti searchId(int id);

    List<PersonEntiti> list();

}
