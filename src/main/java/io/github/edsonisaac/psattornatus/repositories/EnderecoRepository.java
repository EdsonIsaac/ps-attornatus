package io.github.edsonisaac.psattornatus.repositories;

import io.github.edsonisaac.psattornatus.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * The interface Endereco repository.
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    /**
     * Find by pessoa list.
     *
     * @param pessoaId the pessoa id
     * @return the list
     */
    @Query("FROM tb_enderecos AS e WHERE e.pessoa.id = ?1")
    List<Endereco> findByPessoa(UUID pessoaId);
}