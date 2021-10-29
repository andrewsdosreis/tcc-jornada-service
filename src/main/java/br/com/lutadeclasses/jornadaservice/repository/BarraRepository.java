package br.com.lutadeclasses.jornadaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.Barra;

@Repository
public interface BarraRepository extends JpaRepository<Barra, Integer> {
    
}
