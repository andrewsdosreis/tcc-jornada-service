package br.com.lutadeclasses.jornadaservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.Carta;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Integer> {
    Optional<Carta> findByDescricao(String descricao);
}
