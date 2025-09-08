package com.example.AgroElegivel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.AgroElegivel.model.Safra;

@Repository
public interface SafraRepository extends JpaRepository<Safra, Long> {
    
    // Buscar safras por talhão
    List<Safra> findByTalhaoId(Long talhaoId);
    
    // Buscar por cultura
    List<Safra> findByCultura(String cultura);
    
    // Buscar por ano
    List<Safra> findByAno(Integer ano);
    
    // Buscar por cultura e ano
    List<Safra> findByCulturaAndAno(String cultura, Integer ano);
    
    // Buscar por período de plantio
    List<Safra> findByDataPlantioBetween(LocalDate dataInicio, LocalDate dataFim);
    
    // Buscar por período de colheita prevista
    List<Safra> findByDataColheitaPrevistaBetween(LocalDate dataInicio, LocalDate dataFim);
    
    // Query personalizada - buscar safras com detalhes do talhão e propriedade
    @Query("SELECT s FROM Safra s " +
           "JOIN FETCH s.talhao t " +
           "JOIN FETCH t.propriedadeRural p " +
           "WHERE s.cultura = :cultura AND s.ano = :ano")
    List<Safra> findByCulturaAndAnoWithDetails(@Param("cultura") String cultura, @Param("ano") Integer ano);
    
    // Buscar safras por propriedade (através do talhão)
    @Query("SELECT s FROM Safra s WHERE s.talhao.propriedadeRural.id = :propriedadeId")
    List<Safra> findByPropriedadeRuralId(@Param("propriedadeId") Long propriedadeId);
    
    // Buscar safras com colheita próxima (próximos 30 dias)
    @Query("SELECT s FROM Safra s WHERE s.dataColheitaPrevista BETWEEN CURRENT_DATE AND :dataLimite")
    List<Safra> findSafrasComColheitaProxima(@Param("dataLimite") LocalDate dataLimite);
    
    // Contar safras por cultura e ano
    @Query("SELECT s.cultura, s.ano, COUNT(s) FROM Safra s GROUP BY s.cultura, s.ano ORDER BY s.ano DESC, s.cultura")
    List<Object[]> countSafrasByCulturaAndAno();
    
    // Buscar últimas safras por talhão
    @Query("SELECT s FROM Safra s WHERE s.talhao.id = :talhaoId ORDER BY s.ano DESC, s.dataPlantio DESC")
    List<Safra> findLatestSafrasByTalhao(@Param("talhaoId") Long talhaoId);
}
