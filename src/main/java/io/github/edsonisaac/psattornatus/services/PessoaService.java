package io.github.edsonisaac.psattornatus.services;

import io.github.edsonisaac.psattornatus.entities.Pessoa;
import io.github.edsonisaac.psattornatus.exceptions.ObjectNotFoundException;
import io.github.edsonisaac.psattornatus.exceptions.ValidationException;
import io.github.edsonisaac.psattornatus.repositories.PessoaRepository;
import io.github.edsonisaac.psattornatus.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The type Pessoa service.
 */
@Service
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class PessoaService {

    private final PessoaRepository repository;

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    /**
     * Find by id pessoa.
     *
     * @param id the id
     * @return the pessoa
     */
    public Pessoa findById(UUID id) {

        return repository.findById(id).orElseThrow(() -> {
           throw new ObjectNotFoundException(MessageUtils.PESSOA_NOT_FOUND);
        });
    }

    /**
     * Save pessoa.
     *
     * @param pessoa the pessoa
     * @return the pessoa
     */
    public Pessoa save(Pessoa pessoa) {

        if (pessoa == null) {
            throw new ValidationException(MessageUtils.PESSOA_NULL);
        }

        if (validatePessoa(pessoa)) {
            pessoa = repository.save(pessoa);
        }

        return pessoa;
    }

    /**
     * Validate pessoa.
     *
     * @param pessoa the pessoa
     * @return the boolean
     */
    private boolean validatePessoa(Pessoa pessoa) {

        var pessoa_findByNome = repository.findByNomeIgnoreCase(pessoa.getNome()).orElse(null);

        if (pessoa_findByNome != null && !pessoa_findByNome.equals(pessoa)) {
            throw new ValidationException("Pessoa já cadastrada!");
        }

        return true;
    }
}