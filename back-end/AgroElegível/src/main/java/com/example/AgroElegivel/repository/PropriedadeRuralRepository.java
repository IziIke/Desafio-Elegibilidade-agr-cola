package com.example.AgroElegivel.repository;

import com.example.AgroElegivel.model.PropriedadeRural;
import com.example.AgroElegivel.model.Uf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropriedadeRuralRepository extends JpaRepository<PropriedadeRural, Long> {
    
      // Buscar por município
    List<PropriedadeRural> findByMunicipio(String municipio);
    
    // Buscar por UF
    List<PropriedadeRural> findByUf(Uf uf);
    
    // Buscar por nome contendo (ignora case)
    List<PropriedadeRural> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar por área total maior que
    List<PropriedadeRural> findByAreaTotalHaGreaterThan(Double area);
    
    // Buscar por área total entre valores
    List<PropriedadeRural> findByAreaTotalHaBetween(Double areaMin, Double areaMax);
    
    // Query personalizada - buscar propriedades por município e UF
    @Query("SELECT p FROM PropriedadeRural p WHERE p.municipio = :municipio AND p.uf = :uf")
    List<PropriedadeRural> findByMunicipioAndUf(@Param("municipio") String municipio, @Param("uf") String uf);
    
    // Query para somar área total por UF
    @Query("SELECT p.uf, SUM(p.areaTotalHa) FROM PropriedadeRural p GROUP BY p.uf")
    List<Object[]> sumAreaTotalByUf();
}
