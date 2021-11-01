package br.com.lutadeclasses.jornadaservice.exception.validation;

public class JornadaCartaJaExisteException extends ValidacaoException {

    private static final long serialVersionUID = 1L;

    public JornadaCartaJaExisteException(Integer jornadaId, Integer cartaId) {
        super(String.format("Carta [id '%s'] já existe na Jornada [id '%s']", cartaId, jornadaId));
    }

}
