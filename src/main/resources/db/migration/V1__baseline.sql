CREATE TABLE tb_enderecos
(
    id         UUID    NOT NULL,
    logradouro VARCHAR(100),
    numero     VARCHAR(10),
    cidade     VARCHAR(100),
    cep        VARCHAR(10),
    principal  BOOLEAN NOT NULL,
    pessoa_id  UUID,
    CONSTRAINT pk_tb_enderecos PRIMARY KEY (id)
);

CREATE TABLE tb_pessoas
(
    id              UUID NOT NULL,
    nome            VARCHAR(100),
    data_nascimento date NOT NULL,
    CONSTRAINT pk_tb_pessoas PRIMARY KEY (id)
);

ALTER TABLE tb_enderecos
    ADD CONSTRAINT FK_TB_ENDERECOS_ON_PESSOA FOREIGN KEY (pessoa_id) REFERENCES tb_pessoas (id);