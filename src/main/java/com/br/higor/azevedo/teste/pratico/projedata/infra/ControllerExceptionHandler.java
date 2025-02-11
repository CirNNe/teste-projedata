package com.br.higor.azevedo.teste.pratico.projedata.infra;

import com.br.higor.azevedo.teste.pratico.projedata.model.dtos.ExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity trataArgumentoIlegal(IllegalArgumentException exception) {
        ExceptionDto exceptionDTO = new ExceptionDto(exception.getMessage(), "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity trataTentativaSalvarObjetoDuplicado(DataIntegrityViolationException exception) {
        ExceptionDto exceptionDTO = new ExceptionDto(exception.getMessage(), "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity trataExcecaoEntidadeNaoEncontrada() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity trataExcecaoGeral(Exception exception) {
        ExceptionDto exceptionDTO = new ExceptionDto(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
