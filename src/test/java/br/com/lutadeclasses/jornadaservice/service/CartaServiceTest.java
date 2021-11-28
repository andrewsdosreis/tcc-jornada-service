package br.com.lutadeclasses.jornadaservice.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.lutadeclasses.jornadaservice.entity.Acao;
import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Barra;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.exception.notfound.AcaoNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.AlternativaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.validation.AlternativaComEstaDescricaoNaCartaJaExisteException;
import br.com.lutadeclasses.jornadaservice.exception.validation.CartaComEstaDescricaoJaExisteException;
import br.com.lutadeclasses.jornadaservice.model.request.NovaAcaoDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;
import br.com.lutadeclasses.jornadaservice.repository.AcaoRepository;
import br.com.lutadeclasses.jornadaservice.repository.AlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.BarraRepository;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class CartaServiceTest {

    private CartaService cartaService;

    @Mock
    private CartaRepository cartaRepository;

    @Mock
    private AlternativaRepository alternativaRepository;

    @Mock
    private AcaoRepository acaoRepository;

    @Mock
    private BarraRepository barraRepository;

    @BeforeEach
    public void prepararTestes() {
        cartaService = new CartaService(cartaRepository, alternativaRepository, acaoRepository, barraRepository);
    }

    @Test
    void test_listarCartas_sucesso() {
        List<Carta> cartas = new ArrayList<>();
        cartas.add(montarCarta());
        when(cartaRepository.findAll()).thenReturn(cartas);

        List<CartaDto> actual = cartaService.listarCartas();

        assertFalse(actual.isEmpty());
    }

    @Test
    void test_buscarCarta_sucesso() {
        Integer id = 1;
        when(cartaRepository.findById(id)).thenReturn(Optional.of(montarCarta()));

        Carta expected = montarCarta();
        Carta actual = cartaService.buscarCarta(id);

        assertTrue(verificaSeValoresSaoIdenticos(expected, actual));
    }

    @Test
    void test_buscarCarta_NaoEncontrada() {
        Integer id = 1;
        when(cartaRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(CartaNaoEncontradaException.class, () -> {
            cartaService.buscarCarta(id);
        });

        assertNotNull(exception);
    }

    @Test
    void test_criarCarta_sucesso() {
        when(cartaRepository.save(any())).thenReturn(montarCarta());

        Carta expected = montarCarta();
        Carta actual = cartaService.criarCarta(montarNovaCartaDto());

        assertTrue(verificaSeValoresSaoIdenticos(expected, actual));
    }

    @Test
    void test_criarCarta_descricaoJaExiste() {
        when(cartaRepository.findByDescricao(any())).thenReturn(Optional.of(montarCarta()));

        Exception exception = assertThrows(CartaComEstaDescricaoJaExisteException.class, () -> {
            cartaService.criarCarta(montarNovaCartaDto());
        });

        assertNotNull(exception);
    }

    @Test
    void test_deletarCarta_sucesso() {
        Integer id = 1;
        when(cartaRepository.findById(id)).thenReturn(Optional.of(montarCarta()));
        doNothing().when(cartaRepository).delete(any());
        
        cartaService.deletarCarta(id);
        verify(cartaRepository, times(1)).delete(any());
    }

    @Test
    void test_deletarCarta_cartaNaoEncontrada() {
        Integer id = 1;
        when(cartaRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(CartaNaoEncontradaException.class, () -> {
            cartaService.deletarCarta(id);
        });

        assertNotNull(exception);
    }

    @Test
    void test_adicionarAlternativaNaCarta_sucesso() {
        var carta = montarCarta();
        var novaAlternativaDto = montarNovaAlternativaDto();
        when(cartaRepository.findById(any())).thenReturn(Optional.of(montarCarta()));
        
        cartaService.adicionarAlternativaNaCarta(carta.getId(), novaAlternativaDto);

        verify(alternativaRepository, times(1)).save(any());
    }

    @Test
    void test_adicionarAlternativaNaCarta_cartaNaoEncontrada() {
        Integer id = 1;
        var novaAlternativaDto = montarNovaAlternativaDto();
        when(cartaRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(CartaNaoEncontradaException.class, () -> {
            cartaService.adicionarAlternativaNaCarta(id, novaAlternativaDto);
        });

        assertNotNull(exception);
    }

    @Test
    void test_adicionarAlternativaNaCarta_alternativaInvalida() {
        var carta = montarCarta();
        var alternativa = montarAlternativa(carta);
        var novaAlternativaDto = montarNovaAlternativaDto();
        when(cartaRepository.findById(any())).thenReturn(Optional.of(montarCarta()));
        when(alternativaRepository.findByDescricaoAndCarta_Id(any(), any())).thenReturn(Optional.of(alternativa));

        Exception exception = assertThrows(AlternativaComEstaDescricaoNaCartaJaExisteException.class, () -> {
            cartaService.adicionarAlternativaNaCarta(carta.getId(), novaAlternativaDto);
        });

        assertNotNull(exception);
    }

    @Test
    void test_deletarAlternativaNaCarta_sucesso() {
        Carta carta = montarCarta();
        Alternativa alternativa = montarAlternativa(carta);
        when(alternativaRepository.findByIdAndCarta_Id(any(), any())).thenReturn(Optional.of(alternativa));
        
        cartaService.deletarAlternativaNaCarta(carta.getId(), alternativa.getId());

        verify(alternativaRepository, times(1)).delete(alternativa);
    }

    @Test
    void test_deletarAlternativaNaCarta_alternativaNaoEncontrada() {
        Carta carta = montarCarta();
        Alternativa alternativa = montarAlternativa(carta);
        when(alternativaRepository.findByIdAndCarta_Id(any(), any())).thenReturn(Optional.ofNullable(null));
        
        Exception exception = assertThrows(AlternativaNaoEncontradaException.class, () -> {
            cartaService.deletarAlternativaNaCarta(carta.getId(), alternativa.getId());
        });

        assertNotNull(exception);
    }

    @Test
    void test_adicionarAcaoNaAlternativa_sucesso() {
        var carta = montarCarta();
        var alternativa = montarAlternativa(carta);
        var novaAcaoDto = montarNovaAcaoDto();
        List<Barra> barras = Arrays.asList(montarBarra());
        cartaService.barras = barras;

        when(alternativaRepository.findByIdAndCarta_Id(any(), any())).thenReturn(Optional.of(alternativa));

        cartaService.adicionarAcaoNaAlternativa(carta.getId(), alternativa.getId(), novaAcaoDto);

        verify(alternativaRepository, times(1)).save(any());
    }
    
    @Test
    void test_deletarAcaoNaAlternativa_sucesso() {
        Carta carta = montarCarta();
        Alternativa alternativa = montarAlternativa(carta);
        Acao acao = montarAcao(alternativa);
        when(acaoRepository.findByIdAndAlternativa_Id(any(), any())).thenReturn(Optional.of(acao));
        
        cartaService.deletarAcaoNaAlternativa(carta.getId(), alternativa.getId(), acao.getId());

        verify(acaoRepository, times(1)).delete(acao);
    }

    @Test
    void test_deletarAcaoNaAlternativa_acaoNaoEncontrada() {
        Carta carta = montarCarta();
        Alternativa alternativa = montarAlternativa(carta);
        Acao acao = montarAcao(alternativa);
        when(acaoRepository.findByIdAndAlternativa_Id(any(), any())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(AcaoNaoEncontradaException.class, () -> {
            cartaService.deletarAcaoNaAlternativa(carta.getId(), alternativa.getId(), acao.getId());
        });

        assertNotNull(exception);
    }

    private boolean verificaSeValoresSaoIdenticos(Carta expected, Carta actual) {
        return expected.getId().equals(actual.getId()) &&
                expected.getDescricao().equals(actual.getDescricao()) &&
                expected.getAtor().equals(actual.getAtor()) &&
                expected.getAlternativas().size() > 0;
    }

    private Carta montarCarta() {
        Carta carta = new Carta(obj -> {
            obj.setId(1);
            obj.setDescricao("Carta");
            obj.setAtor("Ator");
            obj.setAlternativas(new ArrayList<>());
        });
        List<Alternativa> alternativas = new ArrayList<>();
        alternativas.add(montarAlternativa(carta));
        carta.setAlternativas(alternativas);
        return carta;
    }

    private Alternativa montarAlternativa(Carta carta) {
        Alternativa alternativa = new Alternativa(obj -> {
            obj.setId(1);
            obj.setDescricao("Alternativa");
            obj.setCarta(carta);
        });
        List<Acao> acoes = new ArrayList<>();
        acoes.add(montarAcao(alternativa));
        alternativa.setAcoes(acoes);
        return alternativa;
    }

    private Acao montarAcao(Alternativa alternativa) {
        return new Acao(obj -> {
            obj.setId(1);
            obj.setTipo("SOMA");
            obj.setValor(10);
            obj.setAlternativa(alternativa);
            obj.setBarra(montarBarra());
        });
    }

    private Barra montarBarra() {
        return new Barra(obj -> {
            obj.setId(1);
            obj.setDescricao("VIDA");
        });
    }

    private NovaCartaDto montarNovaCartaDto() {
        return NovaCartaDto.builder()
                .descricao("Carta")
                .ator("Ator")
                .alternativas(new ArrayList<>())
                .build();
    }

    private NovaAlternativaDto montarNovaAlternativaDto() {
        return NovaAlternativaDto
                .builder()
                .descricao("Alternativa")
                .acoes(new ArrayList<>())
                .build();
    }

    private NovaAcaoDto montarNovaAcaoDto() {
        return NovaAcaoDto
                .builder()
                .barra("VIDA")
                .valor(10)
                .tipo("SOMA")
                .build();
    }
}
