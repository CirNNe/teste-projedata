package com.br.higor.azevedo.teste.pratico.projedata.repository;

import com.br.higor.azevedo.teste.pratico.projedata.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByNome(String nome);

    List<Funcionario> findByOrderByFuncaoAsc();

    Optional<Funcionario> findFirstByOrderByDataNascimentoAsc();

    @Query("SELECT SUM(f.salario) FROM Funcionario f")
    BigDecimal calcularTotalSalarios();
}
