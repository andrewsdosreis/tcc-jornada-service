package br.com.lutadeclasses.jornadaservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.Alternativa;

@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Integer> {
    Optional<Alternativa> findByIdAndCarta_Id(Integer id, Integer cartaId);
    Optional<Alternativa> findByDescricaoAndCarta_Id(String descricao, Integer cartaId);
}
