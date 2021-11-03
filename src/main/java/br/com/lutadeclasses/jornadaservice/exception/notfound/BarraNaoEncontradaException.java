package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class BarraNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public BarraNaoEncontradaException(Integer id) {
        super(String.format("Barra [id '%s'] nao foi encontrada", id));
    }

}
