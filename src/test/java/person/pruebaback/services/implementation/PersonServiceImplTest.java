package person.pruebaback.services.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import person.pruebaback.entities.PersonEntiti;
import person.pruebaback.exceptions.ObjExistsExceptions;
import person.pruebaback.exceptions.ObjNoExistsExceptions;
import person.pruebaback.repositorys.PersonRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class PersonServiceImplTest {

    @Mock
    private static PersonRepository personRepository;

    @Mock
    private static LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

    @InjectMocks
    private static PersonServiceImpl personService;

    private final static String NOMBRE_NO_EXISTENTE = "a";
    private final static String PRINCIPAL = "Carlos";
    private final static String SUPLENTE = "Tovar";
    private final static LocalDate FECHA_NACIMIENTO = LocalDate.now();

    @Test
    void create_and_name_no_existe_se_debe_crear_persona() {
        when(personRepository.findByFullname(NOMBRE_NO_EXISTENTE)).thenReturn(Optional.ofNullable(null));
        var perso = this.BuildPerson(0, NOMBRE_NO_EXISTENTE, FECHA_NACIMIENTO);

        personService.create(perso);

        verify(personRepository, times(1))
                .findByFullname(perso.getFullname());
        verify(personRepository, times(1))
                .save(perso);
    }

    @Test
    void create_and_name_no_existe_se_envia_exception() {
        var perso = this.BuildPerson(0, PRINCIPAL, FECHA_NACIMIENTO);
        this.configPersonExist(PRINCIPAL);

        assertThrows(ObjExistsExceptions.class, () -> personService.create(perso));

        verify(personRepository, times(1))
                .findByFullname(perso.getFullname());
        verify(personRepository, times(0))
                .save(perso);
    }

    @Test
    void uptdate_y_person_no_existe_se_envia_exception() {
        var perso = this.BuildPerson(10, PRINCIPAL, FECHA_NACIMIENTO);
        when(personRepository.findById(perso.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(RuntimeException.class, () -> personService.update(perso));

        verify(personRepository, times(1))
                .findById(perso.getId());
        verify(personRepository, times(0))
                .save(perso);
    }

    @Test
    void update_y_name_existe_se_envia_exception() {
        var perso = this.BuildPerson(10, PRINCIPAL, FECHA_NACIMIENTO);
        this.configPersonExist(PRINCIPAL);
        when(personRepository.findById(perso.getId())).thenReturn(Optional.of(perso));

        assertThrows(ObjExistsExceptions.class, () -> personService.update(perso));

        verify(personRepository, times(1))
                .findById(perso.getId());
        verify(personRepository, times(1))
                .findByFullname(PRINCIPAL);
        verify(personRepository, times(0))
                .save(perso);
    }

    @Test
    void update_con_nombre_igual() {
        var perso = this.BuildPerson(10, PRINCIPAL, FECHA_NACIMIENTO);
        when(personRepository.findById(perso.getId())).thenReturn(Optional.of(perso));
        when(personRepository.findByFullname(PRINCIPAL)).thenReturn(Optional.of(perso));

        personService.update(perso);

        verify(personRepository, times(1))
                .findById(perso.getId());
        verify(personRepository, times(1))
                .findByFullname(PRINCIPAL);
        verify(personRepository, times(1))
                .save(perso);
    }

    @Test
    void existe_person_search_por_id() {
        var persona = this.BuildPerson(10, PRINCIPAL, FECHA_NACIMIENTO);

        when(personRepository.findById(persona.getId()))
                .thenReturn(Optional.of(persona));

        var persoExist = personService.searchId(persona.getId());
        assertThat(persoExist).isNotNull();
        assertThat(persoExist.getId()).isEqualTo(10);
    }

    @Test
    void no_existe_person_search_por_id() {
        Optional<PersonEntiti> personaNoExistente = Optional.ofNullable(null);
        when(personRepository.findById(5)).thenReturn(personaNoExistente);

        assertThrows(ObjNoExistsExceptions.class, () -> personService.searchId(5));

        verify(personRepository, times(1)).findById(5);
    }

    @Test
    void list() {
        Sort sort = Sort.by("id");
        when(personRepository.findAll(sort)).thenReturn(this.obtenerListPeople());

        List<PersonEntiti> personas = personService.list();

        assertThat(personas).isNotNull().isNotEmpty();
        verify(personRepository, times(1)).findAll(sort);
    }

    @Test
    void delete_person_existe() {
        var persona = this.BuildPerson(10, PRINCIPAL, FECHA_NACIMIENTO);
        Optional<PersonEntiti> personaExistente = Optional.of(persona);

        when(personRepository.findById(10)).thenReturn(personaExistente);

        personService.delete(10);
        verify(personRepository, times(1)).deleteById(10);
    }

    @Test
    void delete_person_no_existe() {
        var persona = this.BuildPerson(10, PRINCIPAL, FECHA_NACIMIENTO);
        Optional<PersonEntiti> personaExistente = Optional.ofNullable(null);

        when(personRepository.findById(10)).thenReturn(personaExistente);

        assertThrows(ObjNoExistsExceptions.class, () -> personService.delete(10));
        verify(personRepository, times(0)).deleteById(10);
        verify(personRepository, times(1)).findById(10);
    }

    private PersonEntiti BuildPerson(int id, String nombre, LocalDate fechaNacimiento) {
        var persona = new PersonEntiti();
        persona.setId(id);
        persona.setFullname(nombre);
        persona.setBirth(fechaNacimiento);
        return persona;
    }

    private List<PersonEntiti> obtenerListPeople() {
        var persona1 = this.BuildPerson(1, PRINCIPAL, FECHA_NACIMIENTO);
        var persona2 = this.BuildPerson(2, SUPLENTE, FECHA_NACIMIENTO);
        return Arrays.asList(persona1, persona2);
    }

    private void configPersonExist(String nombre) {
        var personaExistenteConMismoNombre = new PersonEntiti();
        personaExistenteConMismoNombre.setId(100);
        personaExistenteConMismoNombre.setFullname(nombre);
        when(personRepository.findByFullname(nombre))
                .thenReturn(Optional.of(personaExistenteConMismoNombre));
    }
}