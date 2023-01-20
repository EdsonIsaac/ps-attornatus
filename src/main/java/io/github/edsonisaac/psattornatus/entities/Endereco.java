package io.github.edsonisaac.psattornatus.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * The type Endereco.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_enderecos")
public class Endereco extends AbstractEntity {

    @NotEmpty
    @Column(length = 100)
    private String logradouro;

    @NotEmpty
    @Column(length = 10)
    private String numero;

    @NotEmpty
    @Column(length = 100)
    private String cidade;

    @NotEmpty
    @Column(length = 10)
    private String cep;

    @NotNull
    private Boolean principal;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    @JsonBackReference
    private Pessoa pessoa;
}