package io.github.edsonisaac.psattornatus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.edsonisaac.psattornatus.PsAttornatusApplication;
import io.github.edsonisaac.psattornatus.entities.Endereco;
import io.github.edsonisaac.psattornatus.entities.Pessoa;
import io.github.edsonisaac.psattornatus.exceptions.ObjectNotFoundException;
import io.github.edsonisaac.psattornatus.services.EnderecoService;
import io.github.edsonisaac.psattornatus.services.PessoaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Pessoa controller test.
 */
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PsAttornatusApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PessoaControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PessoaService pessoaService;

    @MockBean
    private EnderecoService enderecoService;

    @Autowired
    private MockMvc mvc;

    /**
     * Sets .
     */
    @BeforeAll
    public void setup() {
        this.mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Should return all pessoas.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Deve retornar todas as pessoas")
    public void shouldReturnAllPessoas() throws Exception {

        var pessoa = getPessoa();

        when(pessoaService.findAll()).thenReturn(List.of(pessoa));

        var result = this.mvc.perform(get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        var pessoas = (List<Pessoa>) mapper.readValue(result.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Pessoa.class));

        Assertions.assertEquals(1, pessoas.size());
    }

    /**
     * Should return a pessoa.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Deve retornar uma pessoa")
    public void shouldReturnAPessoa() throws Exception {

        var pessoa = getPessoa();

        when(pessoaService.findById(any())).thenReturn(pessoa);

        this.mvc.perform(
                        get("/pessoas/" + pessoa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not return a pessoa when id is null.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve retornar uma pessoa quando o ID é nulo")
    public void shouldNotReturnAPessoaWhenIDIsNull() throws Exception {

        this.mvc.perform(
                        get("/pessoas/null"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not return a pessoa when id not found.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve retornar uma pessoa quando o ID não é encontrado")
    public void shouldNotReturnAPessoaWhenIdNotFound() throws Exception {

        when(pessoaService.findById(any())).thenThrow(ObjectNotFoundException.class);

        this.mvc.perform(
                        get("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should return all enderecos.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Deve retornar todas os endereços")
    public void shouldReturnAllEnderecos() throws Exception {

        var pessoa = getPessoa();
        pessoa.setEnderecos(Set.of(getEndereco(), getEndereco()));

        when(pessoaService.findById(any())).thenReturn(pessoa);

        var result = this.mvc.perform(
                        get("/pessoas/" + pessoa.getId() + "/enderecos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        var enderecos = (List<Endereco>) this.mapper.readValue(result.getResponse().getContentAsString(),
                this.mapper.getTypeFactory().constructCollectionType(List.class, Endereco.class));

        Assertions.assertEquals(2, enderecos.size());
    }

    /**
     * Should not return enderecos when id is null.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve retornar endereços quando o ID é nulo")
    public void shouldNotReturnEnderecosWhenIDIsNull() throws Exception {

        this.mvc.perform(
                        get("/pessoas/null/enderecos"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not return enderecos when id not found.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve retornar endereços quando o ID não é encontrado")
    public void shouldNotReturnEnderecosWhenIDNotFound() throws Exception {

        when(pessoaService.findById(any())).thenThrow(ObjectNotFoundException.class);

        this.mvc.perform(
                        get("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should save the endereco.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Deve salvar o endereço")
    public void shouldSaveTheEndereco() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"Avenida Primero de Janeiro\",\"numero\":\"1\",\"cidade\":\"Irecê\",\"cep\":\"44900000\",\"principal\":true,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

//        Gostaria de fazer o teste da forma a seguir, mas por algum motivo o mapper não está incluido o objeto pessoa no endereço,
//        logo o código não funciona como deveria;

//        var pessoa = getPessoa();
//        var endereco = getEndereco();
//
//        endereco.setPessoa(pessoa);
//
//        when(pessoaService.findById(any())).thenReturn(pessoa);
//        when(enderecoService.save(any())).thenReturn(endereco);
//
//        this.mvc.perform(
//                        post("/pessoas/" + pessoa.getId() + "/enderecos")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(this.mapper.writeValueAsString(endereco)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
    }

    /**
     * Should not save the endereco when logradouro is empty.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve salvar o endereco quando o campo logradouro estiver vazio")
    public void shouldNotSaveTheEnderecoWhenLogradouroFieldIsEmpty() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"\",\"numero\":\"1\",\"cidade\":\"Irecê\",\"cep\":\"44900000\",\"principal\":true,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not save the endereco when numero is empty.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve salvar o endereco quando o campo numero estiver vazio")
    public void shouldNotSaveTheEnderecoWhenNumeroFieldIsEmpty() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"Avenida Primero de Janeiro\",\"numero\":\"\",\"cidade\":\"Irecê\",\"cep\":\"44900000\",\"principal\":true,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not save the endereco when cidade is empty.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve salvar o endereço quando a cidade estiver vazio")
    public void shouldNotSaveTheEnderecoWhenCidadeFieldIsEmpty() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"Avenida Primero de Janeiro\",\"numero\":\"1\",\"cidade\":\"\",\"cep\":\"44900000\",\"principal\":true,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not save the endereco when cep is empty.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve salvar o endereço quando o campo cep estiver vazio")
    public void shouldNotSaveTheEnderecoWhenCepFieldIsEmpty() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"Avenida Primero de Janeiro\",\"numero\":\"1\",\"cidade\":\"Irecê\",\"cep\":\"\",\"principal\":true,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not save the endereco when principal field is empty.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve salvar o endereço quando o campo principal estiver vazio")
    public void shouldNotSaveTheEnderecoWhenPrincipalFieldIsEmpty() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"Teste\",\"numero\":\"Teste\",\"cidade\":\"Teste\",\"cep\":\"44900000\",\"principal\":\"\",\"pessoa\":{\"id\":\"27d8dce7-a558-453e-bb19-7a95d4ff216b\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Shoud not save the endereco when pessoa field is null.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve salvar o endereço quando o campo pessoa estiver nulo")
    public void shoudNotSaveTheEnderecoWhenPessoaFieldIsNull() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"logradouro\":\"Teste\",\"numero\":\"Teste\",\"cidade\":\"Teste\",\"cep\":\"44900000\",\"principal\":\"true\"}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        post("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should update the endereco.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Deve atualizar o endereço")
    public void shouldUpdateTheEndereco() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"id\":\"e00041f8-f829-4d8d-b3ea-c94defe797ed\",\"logradouro\":\"Rua Felisberto de Castro Dourado\",\"numero\":\"328\",\"cidade\":\"Irecê\",\"cep\":\"44900000\",\"principal\":false,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        put("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos/e00041f8-f829-4d8d-b3ea-c94defe797ed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Should not update the endereco when endereco id is null.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Não deve atualizar o endereço quando o ID do endereço nulo")
    public void shouldNotUpdateTheEnderecoWhenEnderecoIdIsNull() throws Exception {

        var pessoa = getPessoa();
        var endereco = "{\"id\":\"\",\"logradouro\":\"Rua Felisberto de Castro Dourado\",\"numero\":\"328\",\"cidade\":\"Irecê\",\"cep\":\"44900000\",\"principal\":false,\"pessoa\":{\"id\":\"5fed0901-8f2c-4a1a-86b6-1756900f891e\"}}";

        pessoa.setId(UUID.fromString("5fed0901-8f2c-4a1a-86b6-1756900f891e"));

        when((pessoaService.findById(any()))).thenReturn(pessoa);
        when((enderecoService.save(any()))).thenReturn(this.mapper.readValue(endereco, Endereco.class));

        this.mvc.perform(
                        put("/pessoas/5fed0901-8f2c-4a1a-86b6-1756900f891e/enderecos/e00041f8-f829-4d8d-b3ea-c94defe797ed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(endereco))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private Pessoa getPessoa() {

        return Pessoa.builder()
                .id(UUID.randomUUID())
                .nome("Pessoa " + System.currentTimeMillis())
                .dataNascimento(LocalDate.now())
                .build();
    }

    private Endereco getEndereco() {

        return Endereco.builder()
                .id(UUID.randomUUID())
                .logradouro("Avenida Primero de Janeiro")
                .numero("1")
                .cidade("Irecê")
                .cep("44900000")
                .principal(true)
                .build();
    }
}