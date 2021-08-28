package br.com.lutadeclasses.jornadaservice.exception;

public class CartaNaoEncontradaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public CartaNaoEncontradaException(Integer id) {
        super(String.format("Carta com o Id '%s' nao foi encontrado", id));
    }
    
}
