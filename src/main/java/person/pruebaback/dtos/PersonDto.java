package person.pruebaback.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PersonDto {
    private int id;

    @NotNull(message = "El fullname no puede ser null")
    @Size(min = 1, max = 255, message = "fullname debe tener mínimo un carácter y máximo 255")
    private String fullname;

    @NotNull(message = "birth no puede ser vacío")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birth;

}
