package com.br.higor.azevedo.teste.pratico.projedata.service;

import com.br.higor.azevedo.teste.pratico.projedata.model.Funcionario;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioDTO;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioMaiorIdadeDto;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioSalvoDTO;
import com.br.higor.azevedo.teste.pratico.projedata.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.br.higor.azevedo.teste.pratico.projedata.model.mappers.FuncionarioMapper.*;

@Service
public class FuncionarioService {

    final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public FuncionarioSalvoDTO salvar(FuncionarioDTO funcionarioDTO) {
        boolean funcionarioNoBanco = funcionarioRepository.existsByNome(funcionarioDTO.nome());

        if (funcionarioNoBanco) {
            throw new DataIntegrityViolationException("Funcionario já cadastrado!");
        }
        Funcionario funcionarioSalvo = funcionarioRepository.save(toFuncionario(funcionarioDTO));
        return toFuncionarioSalvoDto(funcionarioSalvo);
    }

    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public List<FuncionarioDTO> listar() {
        List<Funcionario> funcionariosLista = funcionarioRepository.findAll();
        return toFuncionarioDtoLista(funcionariosLista);
    }

    public List<FuncionarioDTO> aplicarAumentoSalario(Integer porcentagem) {
        List<Funcionario> funcionariosComAumentoLista = aplicaPorcentagemAumentoSalario(
                funcionarioRepository.findAll(),
                porcentagem
        );
        List<Funcionario> listaFuncionariosSalvos = funcionarioRepository.saveAll(funcionariosComAumentoLista);
        return toFuncionarioDtoLista(listaFuncionariosSalvos);
    }

    private List<Funcionario> aplicaPorcentagemAumentoSalario(List<Funcionario> funcionarioLista, Integer porcentagem) {
        funcionarioLista.forEach(funcionario -> {
            BigDecimal salario = funcionario.getSalario();
            BigDecimal aumento = new BigDecimal(porcentagem).divide(new BigDecimal("100"));
            BigDecimal salarioComAumento = salario.multiply(BigDecimal.ONE.add(aumento));
            funcionario.setSalario(salarioComAumento.setScale(2, RoundingMode.HALF_UP));
        });
        return funcionarioLista;
    }

    public Map<String, List<FuncionarioSalvoDTO>> agruparPorFuncao() {
        List<Funcionario> funcionarioLista = funcionarioRepository.findByOrderByFuncaoAsc();
        return toFuncionarioSalvoDtoLista(funcionarioLista).stream().collect(Collectors.groupingBy(FuncionarioSalvoDTO::funcao));
    }

    public List<FuncionarioSalvoDTO> buscarListaAniversariantes(List<Integer> meses) {
        List<Funcionario> funcionarioLista = funcionarioRepository.findAll();
        return toFuncionarioSalvoDtoLista(funcionarioLista).stream()
                .filter(funcionarioSalvoDTO -> {
                    int mes = funcionarioSalvoDTO.dataNascimento().getMonthValue();
                    return meses.contains(mes);
                })
                .toList();
    }

    public FuncionarioMaiorIdadeDto buscarMaiorIdade() {
        Optional<Funcionario> funcionarioBanco = funcionarioRepository.findFirstByOrderByDataNascimentoAsc();

        if (funcionarioBanco.isPresent()) {
            Funcionario funcionario = funcionarioBanco.get();
            long idade = ChronoUnit.YEARS.between(funcionario.getDataNascimento(), LocalDate.now());
            return toFuncionarioMaiorIdadeDto(funcionario.getNome(), idade);
        }
        throw new EntityNotFoundException("Funcionário não encontrado.");
    }

    public String buscarSomaSalario() {
        try {
            BigDecimal resultado = funcionarioRepository.calcularTotalSalarios();
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            return formatter.format(resultado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Não foi possível retornar a soma dos salários.");
        }

    }

    public Map<String, Integer> calcularQuantidadeDeSalariosMinimosIndividuais() {
        List<Funcionario> funcionarioLista = funcionarioRepository.findAll();
        Map<String, Integer> funcionarioQuantidadeSalarioMap = new HashMap<>();

        funcionarioLista.forEach(funcionario -> {
            BigDecimal quantidadeSalarios = funcionario.getSalario().divide(SALARIO_MINIMO, RoundingMode.DOWN);
            funcionarioQuantidadeSalarioMap.put(funcionario.getNome(), quantidadeSalarios.intValue());
        });

        return funcionarioQuantidadeSalarioMap;
    }
}
