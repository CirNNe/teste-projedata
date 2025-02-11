package com.br.higor.azevedo.teste.pratico.projedata.model.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioDTO(
        String nome,
        LocalDate dataNascimento,
        BigDecimal salario,
        String funcao
) {
}
