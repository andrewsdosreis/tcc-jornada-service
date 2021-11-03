package br.com.lutadeclasses.jornadaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.JornadaCartaDerrota;

@Repository
public interface JornadaCartaDerrotaRepository extends JpaRepository<JornadaCartaDerrota, Integer> {
    
}
