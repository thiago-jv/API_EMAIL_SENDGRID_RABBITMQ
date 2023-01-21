package com.email.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LancamentoDTO {

    private Long idLancamento;
    private String dataEntrada;
    private String nome;
    private String predio;
    private String numeroQuarto;
    private String email;

}
