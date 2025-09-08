package com.example.AgroElegivel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.AgroElegivel.model.Talhao;

@Repository
public interface TalhaoRepository extends JpaRepository<Talhao, Long> {
    
    // Buscar talhões por propriedade
    List<Talhao> findByPropriedadeRuralId(Long propriedadeId);
    
    // Buscar por nome contendo (ignora case)
    List<Talhao> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar por área maior que
    List<Talhao> findByAreaHaGreaterThan(Double area);
    
    // Buscar por área entre valores
    List<Talhao> findByAreaHaBetween(Double areaMin, Double areaMax);
    
    // Query personalizada - buscar talhões com suas propriedades
    @Query("SELECT t FROM Talhao t JOIN FETCH t.propriedadeRural WHERE t.propriedadeRural.id = :propriedadeId")
    List<Talhao> findByPropriedadeWithDetails(@Param("propriedadeId") Long propriedadeId);
    
    // Somar área total dos talhões por propriedade
    @Query("SELECT t.propriedadeRural.id, SUM(t.areaHa) FROM Talhao t GROUP BY t.propriedadeRural.id")
    List<Object[]> sumAreaByPropriedade();
    
    // Contar talhões por propriedade
    Long countByPropriedadeRuralId(Long propriedadeId);
}
