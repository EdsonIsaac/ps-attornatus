package io.github.edsonisaac.psattornatus.dtos;

import io.github.edsonisaac.psattornatus.entities.Endereco;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * The type Endereco dto.
 */
public record EnderecoDTO(
        UUID id,
        @NotEmpty String logradouro,
        @NotEmpty String numero,
        @NotEmpty String cidade,
        @NotEmpty String cep,
        @NotNull Boolean principal
) implements Serializable {

    /**
     * To dto endereco.
     *
     * @param endereco the endereco
     * @return the endereco dto
     */
    public static EnderecoDTO toDTO(Endereco endereco) {

        return new EnderecoDTO(
                endereco.getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getCep(),
                endereco.getPrincipal()
        );
    }
}