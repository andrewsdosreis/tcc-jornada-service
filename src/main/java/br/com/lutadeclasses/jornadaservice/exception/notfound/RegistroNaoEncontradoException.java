package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class RegistroNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegistroNaoEncontradoException(String message) {
        super(message);
    }

}
