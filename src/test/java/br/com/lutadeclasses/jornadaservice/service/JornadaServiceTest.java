package br.com.lutadeclasses.jornadaservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.validation.JornadaCartaJaExisteException;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.repository.JornadaAlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaDerrotaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class JornadaServiceTest {

    private JornadaService jornadaService;

    @Mock
    private JornadaRepository jornadaRepository;

    @Mock
    private JornadaCartaRepository jornadaCartaRepository;

    @Mock
    private JornadaAlternativaRepository jornadaAlternativaRepository;

    @Mock
    private JornadaCartaDerrotaRepository jornadaCartaDerrotaRepository;

    @BeforeEach
    public void prepararTestes() {
        jornadaService = new JornadaService(jornadaRepository, jornadaCartaRepository, jornadaAlternativaRepository,
                jornadaCartaDerrotaRepository);
    }

    @Test
    void test_listarJornadas_sucesso() {
        List<Jornada> jornadas = new ArrayList<>();
        jornadas.add(montarJornada());
        when(jornadaRepository.findAll()).thenReturn(jornadas);

        List<JornadaDto> actual = jornadaService.listarJornadas();

        assertFalse(actual.isEmpty());
    }

    @Test
    void test_buscarJornadaPorId_sucesso() {
        Integer id = 1;
        var jornada = montarJornada();
        when(jornadaRepository.findById(id)).thenReturn(Optional.of(jornada));

        var expected = montarJornada();
        var actual = jornadaService.buscarJornadaPorId(id);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarJornadaPorId_jornadaNaoEncontrada() {
        Integer id = 1;
        when(jornadaRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(JornadaNaoEncontradaException.class, () -> {
            jornadaService.buscarJornadaPorId(id);
        });

        assertNotNull(exception);
    }

    @Test
    void test_criarJornada_sucesso() {
        var jornada = montarJornada();
        var novaJornadaDto = montarNovaJornadaDto();

        when(jornadaRepository.save(any())).thenReturn(jornada);

        var expected = jornada;
        var actual = jornadaService.criarJornada(novaJornadaDto);

        assertEquals(expected, actual);
    }

    @Test
    void test_adicionarCartaNaJornada_sucesso() {
        var carta = montarCarta();
        var jornada = montarJornada();
        when(jornadaRepository.findById(anyInt())).thenReturn(Optional.of(jornada));

        var expected = jornada;
        var actual = jornadaService.adicionarCartaNaJornada(1, carta);

        verify(jornadaCartaRepository, times(1)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void test_adicionarCartaNaJornada_cartaJaExisteNaJornada() {
        var carta = montarCarta();
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        when(jornadaCartaRepository.findByJornada_IdAndCarta_Id(any(), any())).thenReturn(Optional.of(jornadaCarta));

        Exception exception = assertThrows(JornadaCartaJaExisteException.class, () -> {
            jornadaService.adicionarCartaNaJornada(1, carta);
        });

        assertNotNull(exception);
    }

    @Test
    void test_definirProximaCarta_sucesso() {
        var carta = montarCarta();
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        var novaJornadaAlternativaDto = montarNovaJornadaAlternativaDto();
        when(jornadaCartaRepository.findByJornada_IdAndCarta_Id(any(), any())).thenReturn(Optional.of(jornadaCarta));

        var expected = jornadaCarta;
        var actual = jornadaService.definirProximaCarta(jornada.getId(), carta.getId(), novaJornadaAlternativaDto);

        verify(jornadaAlternativaRepository, times(1)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void test_definirProximaCarta_jornadaCartaNaoEncontrada() {
        var novaJornadaAlternativaDto = montarNovaJornadaAlternativaDto();
        when(jornadaCartaRepository.findByJornada_IdAndCarta_Id(1, 1)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(JornadaCartaNaoEncontradaException.class, () -> {
            jornadaService.definirProximaCarta(1, 1, novaJornadaAlternativaDto);
        });

        assertNotNull(exception);
    }

    @Test
    void test_definirProximaCarta_proximaJornadaCartaNaoEncontrada() {
        var novaJornadaAlternativaDto = montarNovaJornadaAlternativaDto();
        when(jornadaCartaRepository.findByJornada_IdAndCarta_Id(2, 2)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(JornadaCartaNaoEncontradaException.class, () -> {
            jornadaService.definirProximaCarta(2, 2, novaJornadaAlternativaDto);
        });

        assertNotNull(exception);
    }

    @Test
    void test_deletarCartaDaJornada_sucesso() {
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        when(jornadaCartaRepository.findByJornada_IdAndCarta_Id(any(), any())).thenReturn(Optional.of(jornadaCarta));

        jornadaService.deletarCartaDaJornada(1, 1);
        verify(jornadaCartaRepository, times(1)).delete(any());
    }

    private NovaJornadaDto montarNovaJornadaDto() {
        return NovaJornadaDto
                .builder()
                .titulo("Titulo")
                .build();
    }

    private NovaJornadaAlternativaDto montarNovaJornadaAlternativaDto() {
        return NovaJornadaAlternativaDto
                .builder()
                .alternativaId(1)
                .proximaCartaId(1)
                .build();
    }

    private Jornada montarJornada() {
        var jornada = new Jornada(obj -> {
            obj.setId(1);
            obj.setTitulo("Titulo");
            obj.setJornadasCartas(new ArrayList<>());
        });
        return jornada;
    }

    private JornadaCarta montarJornadaCarta(Jornada jornada) {
        var jornadaCarta = new JornadaCarta(obj -> {
            obj.setId(1);
            obj.setJornada(jornada);
            obj.setPosicao("INICIO");
        });
        jornadaCarta.setJornadasAlternativas(Arrays.asList(montarJornadaAlternativa(jornadaCarta)));
        return jornadaCarta;
    }

    private JornadaAlternativa montarJornadaAlternativa(JornadaCarta jornadaCarta) {
        var carta = montarCarta();
        var jornadaAlternativa = new JornadaAlternativa(obj -> {
            obj.setId(1);
            obj.setAlternativa(carta.getAlternativas().get(0));
            obj.setJornadaCarta(jornadaCarta);
            obj.setProximaJornadaCarta(jornadaCarta);
        });
        return jornadaAlternativa;
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

}
