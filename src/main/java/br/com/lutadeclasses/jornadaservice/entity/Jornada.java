package br.com.lutadeclasses.jornadaservice.entity;

import java.util.List;
import java.util.function.Consumer;

import javax.persistence.CascadeType;
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
@Table(name = "jornada", schema = "luta-de-classe-db")
public class Jornada {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @OneToMany(mappedBy = "jornada", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JornadaCarta> jornadasCartas;

    public Jornada() {
    }

    public Jornada(Consumer<Jornada> jornada) {
        jornada.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<JornadaCarta> getJornadasCartas() {
        return jornadasCartas;
    }

    public void setJornadasCartas(List<JornadaCarta> jornadasCartas) {
        this.jornadasCartas = jornadasCartas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Jornada other = (Jornada) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jornadasCartas == null) {
            if (other.jornadasCartas != null)
                return false;
        } else if (!jornadasCartas.equals(other.jornadasCartas))
            return false;
        if (titulo == null) {
            if (other.titulo != null)
                return false;
        } else if (!titulo.equals(other.titulo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
