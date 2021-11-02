package br.com.lutadeclasses.jornadaservice.exception.validation;

public class CartaComEstaDescricaoJaExisteException extends ValidacaoException {

    private static final long serialVersionUID = 1L;

    public CartaComEstaDescricaoJaExisteException(String descricao) {
        super(String.format("Carta com descrição '%s' já existe", descricao));
    }

}
