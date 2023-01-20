package io.github.edsonisaac.psattornatus.repositories;

import io.github.edsonisaac.psattornatus.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface Pessoa repository.
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    /**
     * Find by nome ignore case optional.
     *
     * @param nome the nome
     * @return the optional
     */
    Optional<Pessoa> findByNomeIgnoreCase(String nome);
}