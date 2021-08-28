package br.com.lutadeclasses.jornadaservice.entity;

import java.util.function.Consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "jornada_carta", schema = "luta-de-classe-db")
public class JornadaCarta {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "ponto_inicial", nullable = false)
    private Boolean pontoInicial;

    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    @JsonBackReference
    private Jornada jornada;

    @ManyToOne
    @JoinColumn(name = "carta_id", nullable = false)
    @JsonBackReference
    private Carta carta;

    public JornadaCarta() {
    }

    public JornadaCarta(Consumer<JornadaCarta> jornadaCarta) {
        jornadaCarta.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPontoInicial() {
        return pontoInicial;
    }

    public void setPontoInicial(Boolean pontoInicial) {
        this.pontoInicial = pontoInicial;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
