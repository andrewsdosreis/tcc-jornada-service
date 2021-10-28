package br.com.lutadeclasses.jornadaservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected <T> ResponseEntity<T> ok(T body) {
        this.logger.info("Sucesso (200): {}", body);
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<T> created(T object) {
        this.logger.info("Registro Criado (201): {}", object);
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }

}
