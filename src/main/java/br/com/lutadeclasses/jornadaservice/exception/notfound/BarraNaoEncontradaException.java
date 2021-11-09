package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class BarraNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public BarraNaoEncontradaException(String barra) {
        super(String.format("Barra ['%s'] nao foi encontrada", barra));
    }

}
