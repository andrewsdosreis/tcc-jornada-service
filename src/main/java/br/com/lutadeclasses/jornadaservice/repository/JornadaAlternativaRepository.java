package br.com.lutadeclasses.jornadaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.JornadaAlternativa;

@Repository
public interface JornadaAlternativaRepository extends JpaRepository<JornadaAlternativa, Integer> {
    
}
