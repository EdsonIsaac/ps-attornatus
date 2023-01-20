package io.github.edsonisaac.psattornatus.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * The type Pessoa.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_pessoas")
public class Pessoa extends AbstractEntity implements Serializable {

    @NotEmpty
    @Column(length = 100)
    private String nome;

    @NotNull
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Valid
    @OneToMany(mappedBy = "pessoa")
    @JsonManagedReference
    private Set<Endereco> enderecos;
}