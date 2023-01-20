package io.github.edsonisaac.psattornatus.repositories;

import io.github.edsonisaac.psattornatus.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The interface Endereco repository.
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> { }