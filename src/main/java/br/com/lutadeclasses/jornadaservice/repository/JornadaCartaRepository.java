package br.com.lutadeclasses.jornadaservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;

@Repository
public interface JornadaCartaRepository extends JpaRepository<JornadaCarta, Integer> {
    
    Optional<JornadaCarta> findByJornada_IdAndCarta_Id(Integer jornadaId, Integer cartaId);

}
