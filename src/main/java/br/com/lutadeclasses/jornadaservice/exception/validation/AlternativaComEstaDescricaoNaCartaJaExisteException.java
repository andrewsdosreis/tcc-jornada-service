package br.com.lutadeclasses.jornadaservice.exception.validation;

public class AlternativaComEstaDescricaoNaCartaJaExisteException extends ValidacaoException {

    private static final long serialVersionUID = 1L;

    public AlternativaComEstaDescricaoNaCartaJaExisteException(String descricao, Integer cartaId) {
        super(String.format("Alternativa com descrição '%s' na Carta [id '%s'] já existe", descricao, cartaId));
    }

}
