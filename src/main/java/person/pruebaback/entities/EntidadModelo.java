package person.pruebaback.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class EntidadModelo {

    public abstract int getId();

    @Column(name = "creado_en")
    @CreatedDate
    private LocalDate insertadoE;

    @Column(name = "modificado_en")
    @LastModifiedDate
    private LocalDateTime actualizadoE;



    public boolean mismoId(EntidadModelo i) {
        return (i == this || (i.getId() == this.getId()));
    }
}
