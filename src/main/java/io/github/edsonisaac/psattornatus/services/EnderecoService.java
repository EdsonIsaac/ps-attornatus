package io.github.edsonisaac.psattornatus.services;

import io.github.edsonisaac.psattornatus.entities.Endereco;
import io.github.edsonisaac.psattornatus.exceptions.ValidationException;
import io.github.edsonisaac.psattornatus.repositories.EnderecoRepository;
import io.github.edsonisaac.psattornatus.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The type Endereco service.
 */
@Service
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class EnderecoService {

    private final EnderecoRepository repository;

    /**
     * Find by pessoa list.
     *
     * @param pessoaId the pessoa id
     * @return the list
     */
    public List<Endereco> findByPessoa(UUID pessoaId) {
        return repository.findByPessoa(pessoaId);
    }

    /**
     * Save endereco.
     *
     * @param endereco the endereco
     * @return the endereco
     */
    public Endereco save(Endereco endereco) {

        if (endereco == null) {
            throw new ValidationException(MessageUtils.ENDERECO_NULL);
        }

        return repository.save(endereco);
    }
}