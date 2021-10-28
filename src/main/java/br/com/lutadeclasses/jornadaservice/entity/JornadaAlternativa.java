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
@Table(name = "jornada_alternativa", schema = "luta-de-classe-db")
public class JornadaAlternativa {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "jornada_carta_id", nullable = false)
    @JsonBackReference
    private JornadaCarta jornadaCarta;

    @ManyToOne
    @JoinColumn(name = "proxima_jornada_carta_id", nullable = true)
    @JsonBackReference
    private JornadaCarta proximaJornadaCarta;

    @ManyToOne
    @JoinColumn(name = "alternativa_id", nullable = false)
    @JsonBackReference
    private Alternativa alternativa;

    public JornadaAlternativa() {
    }

    public JornadaAlternativa(Consumer<JornadaAlternativa> jornadaAlternativa) {
        jornadaAlternativa.accept(this);
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JornadaCarta getJornadaCarta() {
        return jornadaCarta;
    }

    public void setJornadaCarta(JornadaCarta jornadaCarta) {
        this.jornadaCarta = jornadaCarta;
    }

    public JornadaCarta getProximaJornadaCarta() {
        return proximaJornadaCarta;
    }

    public void setProximaJornadaCarta(JornadaCarta proximaJornadaCarta) {
        this.proximaJornadaCarta = proximaJornadaCarta;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
