package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aluracursos.screenmatch.model.Serie;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {
    //Optional<Serie> buscar(String nombreSerie);
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionLessThanEqual(int totalTemporadas, Double evaluacion);

}
