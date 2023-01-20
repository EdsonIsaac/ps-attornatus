package io.github.edsonisaac.psattornatus.controllers;

import io.github.edsonisaac.psattornatus.dtos.EnderecoDTO;
import io.github.edsonisaac.psattornatus.dtos.PessoaDTO;
import io.github.edsonisaac.psattornatus.entities.Endereco;
import io.github.edsonisaac.psattornatus.entities.Pessoa;
import io.github.edsonisaac.psattornatus.exceptions.ObjectNotFoundException;
import io.github.edsonisaac.psattornatus.services.EnderecoService;
import io.github.edsonisaac.psattornatus.services.PessoaService;
import io.github.edsonisaac.psattornatus.utils.MessageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Pessoa controller.
 */
@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class PessoaController {

    private final EnderecoService enderecoService;
    private final PessoaService pessoaService;

    /**
     * Find all response entity.
     *
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity findAll() {

        var pessoas = pessoaService.findAll().stream().map(p -> PessoaDTO.toDTO(p)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(pessoas);
    }

    /**
     * Find by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {

        var pessoa = PessoaDTO.toDTO(pessoaService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(pessoa);
    }

    /**
     * Save response entity.
     *
     * @param pessoa the pessoa
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity save(@RequestBody @Valid Pessoa pessoa) {

        var pessoaSaved = PessoaDTO.toDTO(pessoaService.save(pessoa));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSaved);
    }

    /**
     * Update response entity.
     *
     * @param id     the id
     * @param pessoa the pessoa
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody @Valid Pessoa pessoa) {

        if (pessoa.getId().equals(id)) {
            var pessoaUpdated = PessoaDTO.toDTO(pessoaService.save(pessoa));
            return ResponseEntity.status(HttpStatus.OK).body(pessoaUpdated);
        }

        throw new ObjectNotFoundException(MessageUtils.PESSOA_NOT_FOUND);
    }

    ////////////////////////////////// ENDEREÇO //////////////////////////////////

    /**
     * Find all response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}/enderecos")
    public ResponseEntity findAll(@PathVariable UUID id) {
        var enderecos = PessoaDTO.toDTO(pessoaService.findById(id)).enderecos();
        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    /**
     * Save response entity.
     *
     * @param id       the id
     * @param endereco the endereco
     * @return the response entity
     */
    @PostMapping("/{id}/enderecos")
    public ResponseEntity save(@PathVariable UUID id, @RequestBody @Valid Endereco endereco) {

        if (!endereco.getPessoa().getId().equals(id)) {
            throw new ObjectNotFoundException(MessageUtils.PESSOA_NOT_FOUND);
        }

        var pessoa = pessoaService.findById(id);

        if (endereco.getPrincipal()) {

            if (pessoa.getEnderecos() != null && pessoa.getEnderecos().size() > 0) {

                pessoa.getEnderecos().forEach(e -> {
                    e.setPrincipal(false);
                    enderecoService.save(e);
                });
            }
        }

        var enderecoSaved = EnderecoDTO.toDTO(enderecoService.save(endereco));

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSaved);
    }

    /**
     * Update response entity.
     *
     * @param id         the id
     * @param enderecoId the endereco id
     * @param endereco   the endereco
     * @return the response entity
     */
    @PutMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity update(@PathVariable UUID id, @PathVariable UUID enderecoId, @RequestBody @Valid Endereco endereco) {

        if (id == null || endereco.getPessoa().getId() == null || !endereco.getPessoa().getId().equals(id)) {
            throw new ObjectNotFoundException(MessageUtils.PESSOA_NOT_FOUND);
        }

        if (enderecoId == null || endereco.getId() ==  null || !endereco.getId().equals(enderecoId)) {
            throw new ObjectNotFoundException(MessageUtils.ENDERECO_NOT_FOUND);
        }

        var pessoa = pessoaService.findById(id);

        if (endereco.getPrincipal()) {

            pessoa.getEnderecos().forEach(e -> {
                e.setPrincipal(false);
                enderecoService.save(e);
            });
        }

        var enderecoUpdated = EnderecoDTO.toDTO(enderecoService.save(endereco));

        return ResponseEntity.status(HttpStatus.OK).body(enderecoUpdated);
    }
}