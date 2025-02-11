package com.br.higor.azevedo.teste.pratico.projedata.model.mappers;

import com.br.higor.azevedo.teste.pratico.projedata.model.Funcionario;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioDTO;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioMaiorIdadeDto;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioSalvoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FuncionarioMapper {

    public static Funcionario toFuncionario(FuncionarioDTO funcionarioDTO) {
        return new Funcionario(
                funcionarioDTO.nome(),
                funcionarioDTO.dataNascimento(),
                funcionarioDTO.salario(),
                funcionarioDTO.funcao()
        );
    }

    public static FuncionarioDTO toFuncionarioDto(Funcionario funcionario) {
        return new FuncionarioDTO(
                funcionario.getNome(),
                funcionario.getDataNascimento(),
                funcionario.getSalario(),
                funcionario.getFuncao()
        );
    }

    public static FuncionarioSalvoDTO toFuncionarioSalvoDto(Funcionario funcionario) {
        return new FuncionarioSalvoDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getDataNascimento(),
                funcionario.getSalario(),
                funcionario.getFuncao()
        );
    }

    public static List<FuncionarioSalvoDTO> toFuncionarioSalvoDtoLista(List<Funcionario> funcionarioLista) {
        return funcionarioLista
                .stream()
                .map(funcionario -> new FuncionarioSalvoDTO(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getDataNascimento(),
                        funcionario.getSalario(),
                        funcionario.getFuncao()
                )).collect(Collectors.toList());
    }

    public static List<FuncionarioDTO> toFuncionarioDtoLista(List<Funcionario> funcionarioLista) {
        return funcionarioLista
                .stream()
                .map(funcionario -> new FuncionarioDTO(
                        funcionario.getNome(),
                        funcionario.getDataNascimento(),
                        funcionario.getSalario(),
                        funcionario.getFuncao()
                )).collect(Collectors.toList());
    }

    public static FuncionarioMaiorIdadeDto toFuncionarioMaiorIdadeDto(String nome, long idade) {
        return new FuncionarioMaiorIdadeDto(
                nome,
                idade
        );
    }
}
