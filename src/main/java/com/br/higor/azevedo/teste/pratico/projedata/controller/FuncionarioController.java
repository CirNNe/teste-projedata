package com.br.higor.azevedo.teste.pratico.projedata.controller;

import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioDTO;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioMaiorIdadeDto;
import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.FuncionarioSalvoDTO;
import com.br.higor.azevedo.teste.pratico.projedata.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<FuncionarioSalvoDTO> salvar(@RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioSalvoDTO funcionario = funcionarioService.salvar(funcionarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable("id") Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body("Dados do funcionario removidos.");
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listar() {
        List<FuncionarioDTO> funcionarios = funcionarioService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(funcionarios);
    }

    @PutMapping("/{porcentagem}")
    public ResponseEntity<List<FuncionarioDTO>> adicionarAumentoSalario(@PathVariable("porcentagem") Integer porcentagem) {
        List<FuncionarioDTO> funcionarios = funcionarioService.aplicarAumentoSalario(porcentagem);
        return ResponseEntity.status(HttpStatus.OK).body(funcionarios);
    }

    @GetMapping("/agrupar")
    public ResponseEntity<Map<String, List<FuncionarioSalvoDTO>>> listarAgrupadoPorFuncao() {
        Map<String, List<FuncionarioSalvoDTO>> funcionariosAgrupados = funcionarioService.agruparPorFuncao();
        return ResponseEntity.status(HttpStatus.OK).body(funcionariosAgrupados);
    }

    @GetMapping("/aniversariantes")
    public ResponseEntity<List<FuncionarioSalvoDTO>> buscarAniversariante(@RequestBody List<Integer> meses) {
        List<FuncionarioSalvoDTO> funcionarios = funcionarioService.buscarListaAniversariantes(meses);
        return ResponseEntity.status(HttpStatus.OK).body(funcionarios);
    }

    @GetMapping("/maiorIdade")
    public ResponseEntity<FuncionarioMaiorIdadeDto> buscarMaiorIdade() {
        FuncionarioMaiorIdadeDto funcionario = funcionarioService.buscarMaiorIdade();
        return ResponseEntity.status(HttpStatus.OK).body(funcionario);
    }

    @GetMapping("/somaSalario")
    public ResponseEntity<String> buscarSomaSalario() {
        String somaDosSalarios = funcionarioService.buscarSomaSalario();
        return ResponseEntity.status(HttpStatus.OK).body(somaDosSalarios);
    }

    @GetMapping("/quantidadeSalariosMinimo")
    public ResponseEntity<Map<String, Integer>> buscarQuantidadeSalariosMinimo() {
        Map<String, Integer> quantidadeSalariosMinimoPorFuncionario = funcionarioService.calcularQuantidadeDeSalariosMinimosIndividuais();
        return ResponseEntity.status(HttpStatus.OK).body(quantidadeSalariosMinimoPorFuncionario);
    }
}
