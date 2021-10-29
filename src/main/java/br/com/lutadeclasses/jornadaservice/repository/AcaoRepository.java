package br.com.lutadeclasses.jornadaservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.jornadaservice.entity.Acao;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Integer> {
    Optional<Acao> findByIdAndAlternativa_Id(Integer id, Integer alternativaId);
}
