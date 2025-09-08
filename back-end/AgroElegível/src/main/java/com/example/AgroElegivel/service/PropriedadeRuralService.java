package com.example.AgroElegivel.service;

import com.example.AgroElegivel.exceptions.DuplicateCodeException;
import com.example.AgroElegivel.exceptions.EntityNotFoundException;
import com.example.AgroElegivel.model.PropriedadeRural;
import com.example.AgroElegivel.model.Uf;
import com.example.AgroElegivel.repository.PropriedadeRuralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropriedadeRuralService {
    
    private final PropriedadeRuralRepository propriedadeRuralRepository;
    
    @Autowired
    public PropriedadeRuralService(PropriedadeRuralRepository propriedadeRuralRepository) {
        this.propriedadeRuralRepository = propriedadeRuralRepository;
    }
    
    /**
     * Cadastra uma nova propriedade rural
     */
    public PropriedadeRural cadastrar(PropriedadeRural propriedade) {
        if (propriedade == null) {
            throw new IllegalArgumentException("Propriedade rural não pode ser nula");
        }
        
        validarPropriedadeRural(propriedade);
        
        try {
            return propriedadeRuralRepository.save(propriedade);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCodeException("Código já existe: " + propriedade.getCodigo());
        }
    }
    
    /**
     * Busca todas as propriedades rurais
     */
    @Transactional(readOnly = true)
    public List<PropriedadeRural> buscarTodas() {
        return propriedadeRuralRepository.findAll();
    }
    
    /**
     * Busca propriedade rural por ID
     */
    @Transactional(readOnly = true)
    public PropriedadeRural buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        return propriedadeRuralRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Propriedade rural não encontrada com ID: " + id));
    }
    
    /**
     * Busca propriedade rural por código
     */
    @Transactional(readOnly = true)
    public Optional<PropriedadeRural> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código não pode ser nulo ou vazio");
        }
        
        return propriedadeRuralRepository.findAll().stream()
                .filter(p -> codigo.equals(p.getCodigo()))
                .findFirst();
    }
    
    /**
     * Deleta propriedade rural por ID
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        if (!propriedadeRuralRepository.existsById(id)) {
            throw new EntityNotFoundException("Propriedade rural não encontrada com ID: " + id);
        }
        
        propriedadeRuralRepository.deleteById(id);
    }
    
    /**
     * Atualiza uma propriedade rural existente
     */
    public PropriedadeRural atualizar(Long id, PropriedadeRural propriedadeAtualizada) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        PropriedadeRural propriedadeExistente = buscarPorId(id);
        
        validarPropriedadeRural(propriedadeAtualizada);
        
        // Verifica se o código não está sendo usado por outra propriedade
        if (!propriedadeAtualizada.getCodigo().equals(propriedadeExistente.getCodigo())) {
            Optional<PropriedadeRural> propriedadeComMesmoCodigo = buscarPorCodigo(propriedadeAtualizada.getCodigo());
            if (propriedadeComMesmoCodigo.isPresent() && !propriedadeComMesmoCodigo.get().getId().equals(id)) {
                throw new DuplicateCodeException("Código já existe: " + propriedadeAtualizada.getCodigo());
            }
        }
        
        propriedadeExistente.setCodigo(propriedadeAtualizada.getCodigo());
        propriedadeExistente.setNome(propriedadeAtualizada.getNome());
        propriedadeExistente.setMunicipio(propriedadeAtualizada.getMunicipio());
        propriedadeExistente.setUf(propriedadeAtualizada.getUf());
        propriedadeExistente.setAreaTotalHa(propriedadeAtualizada.getAreaTotalHa());
        
        try {
            return propriedadeRuralRepository.save(propriedadeExistente);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCodeException("Código já existe: " + propriedadeAtualizada.getCodigo());
        }
    }
    
    /**
     * Busca propriedades por município
     */
    @Transactional(readOnly = true)
    public List<PropriedadeRural> buscarPorMunicipio(String municipio) {
        if (municipio == null || municipio.trim().isEmpty()) {
            throw new IllegalArgumentException("Município não pode ser nulo ou vazio");
        }
        
        return propriedadeRuralRepository.findByMunicipio(municipio);
    }
    
    /**
     * Busca propriedades por UF
     */
    @Transactional(readOnly = true)
    public List<PropriedadeRural> buscarPorUf(String uf) {
        if (uf == null) {
            throw new IllegalArgumentException("UF não pode ser nula");
        }
        Uf ufEnum = Uf.valueOf(uf.toUpperCase());
        return propriedadeRuralRepository.findByUf(ufEnum);
    }
    
    private void validarPropriedadeRural(PropriedadeRural propriedade) {
        if (propriedade.getCodigo() == null || propriedade.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("Código da propriedade é obrigatório");
        }
        
        if (propriedade.getCodigo().length() > 100) {
            throw new IllegalArgumentException("Código da propriedade deve ter no máximo 100 caracteres");
        }
        
        if (propriedade.getNome() == null || propriedade.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da propriedade é obrigatório");
        }
        
        if (propriedade.getMunicipio() == null || propriedade.getMunicipio().trim().isEmpty()) {
            throw new IllegalArgumentException("Município é obrigatório");
        }
        
        if (propriedade.getUf() == null) {
            throw new IllegalArgumentException("UF é obrigatório");
        }
        
        if (propriedade.getAreaTotalHa() <= 0) {
            throw new IllegalArgumentException("Área total deve ser maior que zero");
        }
    }
}