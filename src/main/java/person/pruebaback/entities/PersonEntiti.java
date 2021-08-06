package person.pruebaback.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "person_tab")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonEntiti extends EntidadModelo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private int id;

    @Column(name = "person_fullname")
    @NotNull(message = "El fullname no puede ser null")
    @Size(min = 1, max = 255, message = "fullname debe tener mínimo un carácter y máximo 255")
    private String fullname;

    @Column(name = "person_birth")
    @NotNull(message = "birth no puede ser vacío")
    private LocalDate birth;
}
