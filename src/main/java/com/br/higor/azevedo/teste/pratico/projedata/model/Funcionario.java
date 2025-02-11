package com.br.higor.azevedo.teste.pratico.projedata.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Funcionario extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 18, scale = 2)
    private BigDecimal salario;

    private String funcao;

    public Funcionario(String nome, LocalDate dataNascimento, Long id, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.id = id;
        this.salario = salario;
        this.funcao = funcao;
    }

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    public String dataNascimentoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataNascimento.format(formatter);
    }

    public String salarioFormatado() {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(salario);
    }

    @Override
    public String toString() {
        return "Nome: " + nome +
                ", Data de Nascimento: " + dataNascimentoFormatada() +
                ", Salário: " + salarioFormatado() +
                ", Função: " + funcao;
    }
}
