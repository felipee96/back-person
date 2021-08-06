package person.pruebaback.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import person.pruebaback.dtos.PersonDto;
import person.pruebaback.entities.PersonEntiti;
import person.pruebaback.services.PersonServiceBase;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/person", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200/")
public class PruebaController {

    private PersonServiceBase personServiceBase;
    private ModelMapper modelMapper;

    @Autowired
    public PruebaController(PersonServiceBase personServiceBase, ModelMapper modelMapper) {
        this.personServiceBase = personServiceBase;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> list() {
        List<PersonDto> cargos = this.personServiceBase.list()
                .stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(cargos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonDto> create(@Valid @RequestBody PersonDto personDto) {
        var person = this.modelMapper.map(personDto, PersonEntiti.class);
        person = this.personServiceBase.create(person);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@Valid @RequestBody PersonDto personDto,
                                            @PathVariable("id") int id) {
        if (personDto == null || personDto.getId() != id) {
            throw new RuntimeException("El id de la persona a actualizar no corresponde al path");
        }
        var person = this.modelMapper.map(personDto, PersonEntiti.class);
        person = this.personServiceBase.update(person);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable("id") int id,
                                   @PathVariable("id") int idUsuario) {
        personServiceBase.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
