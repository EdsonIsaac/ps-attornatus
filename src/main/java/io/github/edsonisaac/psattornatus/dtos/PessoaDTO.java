package io.github.edsonisaac.psattornatus.dtos;

import io.github.edsonisaac.psattornatus.entities.Pessoa;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Pessoa dto.
 */
public record PessoaDTO(
        UUID id,
        @NotEmpty String nome,
        @NotNull LocalDate dataNascimento,
        Set<EnderecoDTO> enderecos
) implements Serializable {

    /**
     * To dto pessoa.
     *
     * @param pessoa the pessoa
     * @return the pessoa dto
     */
    public static PessoaDTO toDTO(Pessoa pessoa) {

        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getDataNascimento(),
                pessoa.getEnderecos() != null ? pessoa.getEnderecos().stream().map(e -> EnderecoDTO.toDTO(e)).collect(Collectors.toSet()) : null
        );
    }
}