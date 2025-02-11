package com.br.higor.azevedo.teste.pratico.projedata;

import com.br.higor.azevedo.teste.pratico.projedata.model.Funcionario;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioDTO;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioMaiorIdadeDto;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioSalvoDTO;
import com.br.higor.azevedo.teste.pratico.projedata.repository.FuncionarioRepository;
import com.br.higor.azevedo.teste.pratico.projedata.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import static com.br.higor.azevedo.teste.pratico.projedata.model.mappers.FuncionarioMapper.toFuncionarioDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestConfiguration
public class FuncionarioServiceTest {
    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    private List<Funcionario> funcionarios;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        funcionarios = Arrays.asList(
                new Funcionario("Maria", LocalDate.of(2000, 10, 18), 1L, BigDecimal.valueOf(2009.44), "Operador"),
                new Funcionario("João", LocalDate.of(1990, 5, 12), 2L, BigDecimal.valueOf(2284.38), "Operador"),
                new Funcionario("Caio", LocalDate.of(1961, 5, 2), 3L, BigDecimal.valueOf(9836.14), "Coordenador"),
                new Funcionario("Miguel", LocalDate.of(1988, 10, 14), 4L, BigDecimal.valueOf(19119.88), "Diretor"),
                new Funcionario("Alice", LocalDate.of(1995, 1, 5), 5L,  BigDecimal.valueOf(2234.68), "Recepcionista"),
                new Funcionario("Heitor", LocalDate.of(1999, 11, 19), 6L,  BigDecimal.valueOf(1582.72), "Operador"),
                new Funcionario("Arthur", LocalDate.of(1993, 3, 31), 7L,  BigDecimal.valueOf(4071.84), "Contador"),
                new Funcionario("Laura", LocalDate.of(1994, 7, 8), 8L, BigDecimal.valueOf(3017.45), "Gerente"),
                new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), 9L, BigDecimal.valueOf(1606.85), "Eletricista"),
                new Funcionario("Helena", LocalDate.of(1996, 9, 2), 10L, BigDecimal.valueOf(2799.93), "Gerente")
        );
    }

    @Test
    @DisplayName("Deve salvar funcionário")
    void salvarListaTeste() {
        FuncionarioDTO funcionarioDTO = toFuncionarioDto(funcionarios.getFirst());

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionarios.getFirst());

        FuncionarioSalvoDTO funcionarioSalvo = funcionarioService.salvar(funcionarioDTO);

        assertNotNull(funcionarioSalvo);
        assertEquals(funcionarioDTO.nome(), funcionarioSalvo.nome());
        assertEquals(funcionarioDTO.dataNascimento(), funcionarioSalvo.dataNascimento());
        assertEquals(funcionarioDTO.salario(), funcionarioSalvo.salario());
        assertEquals(funcionarioDTO.funcao(), funcionarioSalvo.funcao());
    }

    @Test
    @DisplayName("Deve remover funcionário")
    void removerTeste() {
        Long id = funcionarios.get(1).getId();
        doNothing().when(funcionarioRepository).deleteById(id);

        funcionarioService.deletar(id);

        verify(funcionarioRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve retornar a lista de funcionários")
    void listarTeste() {
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        List<FuncionarioDTO> listaFuncionarios = funcionarioService.listar();
        listaFuncionarios.forEach(System.out::println);

        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve aplicar aumento de dez porcento no salário dos funcionários")
    public void aplicarAumentoSalarioTeste() {
        int porcentagem = 10;

        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        List<FuncionarioDTO> listaFuncionariosComAumento = funcionarioService.aplicarAumentoSalario(porcentagem);

        verify(funcionarioRepository, times(1)).findAll();
        verify(funcionarioRepository, times(1)).saveAll(funcionarios);
        listaFuncionariosComAumento.forEach(funcionario -> {
            BigDecimal salario = funcionario.salario();
            BigDecimal aumento = new BigDecimal(porcentagem).divide(new BigDecimal("100"));
            BigDecimal salarioComAumento = salario
                    .multiply(BigDecimal.ONE.add(aumento))
                    .setScale(2, RoundingMode.HALF_UP);

            assertEquals(salario, salarioComAumento);
        });
    }

    @Test
    @DisplayName("Deve agrupar os funcionários por função")
    public void agruparFuncionariosTeste() {
        when(funcionarioRepository.findByOrderByFuncaoAsc()).thenReturn(funcionarios);

        Map<String, List<FuncionarioSalvoDTO>> funcionariosAgrupadosMap = funcionarioService.agruparPorFuncao();
        funcionariosAgrupadosMap.forEach(System.out::printf);

        verify(funcionarioRepository, times(1)).findByOrderByFuncaoAsc();
        assertEquals(3, funcionariosAgrupadosMap.get("Operador").size());
        assertEquals(1, funcionariosAgrupadosMap.get("Coordenador").size());
        assertEquals("Maria", funcionariosAgrupadosMap.get("Operador").getFirst().nome());
        assertEquals("Caio", funcionariosAgrupadosMap.get("Coordenador").getFirst().nome());
    }

    @Test
    @DisplayName("Deve retornar funcionarios aniversariantes no(s) mese(s) informados")
    public void retornaListaAniversariante() {
        List<Integer> meses = List.of(10, 12);

        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        List<FuncionarioSalvoDTO> listaFuncionariosAniversariante = funcionarioService.buscarListaAniversariantes(meses);
        listaFuncionariosAniversariante.forEach(System.out::println);

        verify(funcionarioRepository, times(1)).findAll();
        assertEquals("Maria", listaFuncionariosAniversariante.getFirst().nome());
    }

    @Test
    @DisplayName("Deve retornar o funcionário com a maior idade")
    public void retornarFuncionarioMaiorIdade() {
        when(funcionarioRepository.findFirstByOrderByDataNascimentoAsc()).thenReturn(Optional.ofNullable(funcionarios.get(2)));

        FuncionarioMaiorIdadeDto funcionarioMaiorIdade = funcionarioService.buscarMaiorIdade();
        System.out.println(
                "Nome: " + funcionarioMaiorIdade.nome() +
                        " Idade: " + funcionarioMaiorIdade.idade());

        verify(funcionarioRepository, times(1)).findFirstByOrderByDataNascimentoAsc();
    }

    @Test
    @DisplayName("Deve retornar a soma dos salários dos funcionários")
    public void retornarSomaSalarios() throws ParseException {
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        when(funcionarioRepository.calcularTotalSalarios()).thenReturn(totalSalarios);

        String somaDosSalarios = funcionarioService.buscarSomaSalario();
        System.out.println(somaDosSalarios);

        NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
        Number number = format.parse(somaDosSalarios);
        BigDecimal resultado = new BigDecimal(number.toString());

        verify(funcionarioRepository, times(1)).calcularTotalSalarios();
        assertEquals(resultado, totalSalarios);
    }

    @Test
    @DisplayName("Deve retornar o nome do funcionario e a quantidade de salários minimo")
    public void retornarNomeEQuantidadeDeSalariosMinimo() {
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        Map<String, Integer> funcionariosEQuantidadeDeSalarios = funcionarioService.calcularQuantidadeDeSalariosMinimosIndividuais();
        for (Map.Entry<String, Integer> entry : funcionariosEQuantidadeDeSalarios.entrySet()) {
            System.out.println("Funcionario: " + entry.getKey() + " - Quantidade de Salários: " + entry.getValue());
        }

        verify(funcionarioRepository, times(1)).findAll();
        assertEquals(1, funcionariosEQuantidadeDeSalarios.get("João"));
    }
}
