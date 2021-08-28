package br.com.lutadeclasses.jornadaservice.entity;

import java.util.List;
import java.util.function.Consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "carta", schema = "luta-de-classe-db")
public class Carta {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "carta")
    @JsonManagedReference
    private List<Alternativa> alternativas;

    @OneToMany(mappedBy = "carta")
    @JsonManagedReference
    private List<JornadaCarta> jornadaCartas;

    public Carta() {
    }

    public Carta(Consumer<Carta> carta) {
        carta.accept(this);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public List<JornadaCarta> getJornadaCartas() {
        return jornadaCartas;
    }

    public void setJornadaCartas(List<JornadaCarta> jornadaCartas) {
        this.jornadaCartas = jornadaCartas;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
