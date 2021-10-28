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
@Table(name = "jornada_carta_derrota", schema = "luta-de-classe-db")
public class JornadaCartaDerrota {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "jornada_carta_id", nullable = false)
    @JsonBackReference
    private JornadaCarta jornadaCarta;

    @ManyToOne
    @JoinColumn(name = "barra_id", nullable = false)
    @JsonBackReference
    private Barra barra;

    public JornadaCartaDerrota() {
    }

    public JornadaCartaDerrota(Consumer<JornadaCartaDerrota> jornadaCartaDerrota) {
        jornadaCartaDerrota.accept(this);
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

    public Barra getBarra() {
        return barra;
    }

    public void setBarra(Barra barra) {
        this.barra = barra;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
