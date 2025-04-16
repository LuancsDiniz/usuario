package com.luanProject.usuario.business.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnderecoDTO {

    private Long id;
    private String rua;
    private Integer numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
}
