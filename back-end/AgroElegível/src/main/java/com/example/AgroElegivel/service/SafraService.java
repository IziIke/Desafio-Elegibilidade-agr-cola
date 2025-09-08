package com.example.AgroElegivel.service;

import com.example.AgroElegivel.exceptions.DuplicateCodeException;
import com.example.AgroElegivel.exceptions.EntityNotFoundException;
import com.example.AgroElegivel.model.Safra;
import com.example.AgroElegivel.repository.SafraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SafraService {
    
    private final SafraRepository safraRepository;
    private final TalhaoService talhaoService;
    
    @Autowired
    public SafraService(SafraRepository safraRepository, TalhaoService talhaoService) {
        this.safraRepository = safraRepository;
        this.talhaoService = talhaoService;
    }
    
    /**
     * Cadastra uma nova safra
     */
    public Safra cadastrar(Safra safra) {
        if (safra == null) {
            throw new IllegalArgumentException("Safra não pode ser nula");
        }
        
        validarSafra(safra);
        
        // Verifica se o talhão existe
        talhaoService.buscarPorId(safra.getTalhao().getId());
        
        try {
            return safraRepository.save(safra);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCodeException("Código já existe: " + safra.getCodigo());
        }
    }
    
    /**
     * Busca todas as safras
     */
    @Transactional(readOnly = true)
    public List<Safra> buscarTodas() {
        return safraRepository.findAll();
    }
    
    /**
     * Busca safra por ID
     */
    @Transactional(readOnly = true)
    public Safra buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        return safraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Safra não encontrada com ID: " + id));
    }
    
    /**
     * Busca safra por código
     */
    @Transactional(readOnly = true)
    public Optional<Safra> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código não pode ser nulo ou vazio");
        }
        
        return safraRepository.findAll().stream()
                .filter(s -> codigo.equals(s.getCodigo()))
                .findFirst();
    }
    
    /**
     * Deleta safra por ID
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        if (!safraRepository.existsById(id)) {
            throw new EntityNotFoundException("Safra não encontrada com ID: " + id);
        }
        
        safraRepository.deleteById(id);
    }
    
    /**
     * Atualiza uma safra existente
     */
    public Safra atualizar(Long id, Safra safraAtualizada) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Safra safraExistente = buscarPorId(id);
        
        validarSafra(safraAtualizada);
        
        // Verifica se o código não está sendo usado por outra safra
        if (!safraAtualizada.getCodigo().equals(safraExistente.getCodigo())) {
            Optional<Safra> safraComMesmoCodigo = buscarPorCodigo(safraAtualizada.getCodigo());
            if (safraComMesmoCodigo.isPresent() && !safraComMesmoCodigo.get().getId().equals(id)) {
                throw new DuplicateCodeException("Código já existe: " + safraAtualizada.getCodigo());
            }
        }
        
        // Verifica se o novo talhão existe (se foi alterado)
        if (!safraAtualizada.getTalhao().getId().equals(safraExistente.getTalhao().getId())) {
            talhaoService.buscarPorId(safraAtualizada.getTalhao().getId());
        }
        
        safraExistente.setCodigo(safraAtualizada.getCodigo());
        safraExistente.setTalhao(safraAtualizada.getTalhao());
        safraExistente.setCultura(safraAtualizada.getCultura());
        safraExistente.setAno(safraAtualizada.getAno());
        safraExistente.setDataPlantio(safraAtualizada.getDataPlantio());
        safraExistente.setDataColheitaPrevista(safraAtualizada.getDataColheitaPrevista());
        
        try {
            return safraRepository.save(safraExistente);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCodeException("Código já existe: " + safraAtualizada.getCodigo());
        }
    }
    
    /**
     * Busca safras por talhão
     */
    @Transactional(readOnly = true)
    public List<Safra> buscarPorTalhao(Long talhaoId) {
        if (talhaoId == null) {
            throw new IllegalArgumentException("ID do talhão não pode ser nulo");
        }
        
        // Verifica se o talhão existe
        talhaoService.buscarPorId(talhaoId);
        
        return safraRepository.findByTalhaoId(talhaoId);
    }
    
    /**
     * Busca safras por cultura
     */
    @Transactional(readOnly = true)
    public List<Safra> buscarPorCultura(String cultura) {
        if (cultura == null || cultura.trim().isEmpty()) {
            throw new IllegalArgumentException("Cultura não pode ser nula ou vazia");
        }
        
        return safraRepository.findByCultura(cultura);
    }
    
    /**
     * Busca safras por ano
     */
    @Transactional(readOnly = true)
    public List<Safra> buscarPorAno(Integer ano) {
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        
        return safraRepository.findByAno(ano);
    }
    
    /**
     * Busca safras por cultura e ano
     */
    @Transactional(readOnly = true)
    public List<Safra> buscarPorCulturaEAno(String cultura, Integer ano) {
        if (cultura == null || cultura.trim().isEmpty()) {
            throw new IllegalArgumentException("Cultura não pode ser nula ou vazia");
        }
        
        if (ano == null) {
            throw new IllegalArgumentException("Ano não pode ser nulo");
        }
        
        return safraRepository.findByCulturaAndAno(cultura, ano);
    }
    
    /**
     * Busca safras com colheita próxima (próximos 30 dias)
     */
    @Transactional(readOnly = true)
    public List<Safra> buscarSafrasComColheitaProxima() {
        LocalDate dataLimite = LocalDate.now().plusDays(30);
        return safraRepository.findSafrasComColheitaProxima(dataLimite);
    }
    
    private void validarSafra(Safra safra) {
        if (safra.getCodigo() == null || safra.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("Código da safra é obrigatório");
        }
        
        if (safra.getCodigo().length() > 100) {
            throw new IllegalArgumentException("Código da safra deve ter no máximo 100 caracteres");
        }
        
        if (safra.getTalhao() == null || safra.getTalhao().getId() == null) {
            throw new IllegalArgumentException("Talhão é obrigatório");
        }
        
        if (safra.getCultura() == null || safra.getCultura().trim().isEmpty()) {
            throw new IllegalArgumentException("Cultura é obrigatória");
        }
        
        if (safra.getAno() == null || safra.getAno() <= 0) {
            throw new IllegalArgumentException("Ano deve ser válido");
        }
        
        if (safra.getDataPlantio() == null) {
            throw new IllegalArgumentException("Data de plantio é obrigatória");
        }
        
        if (safra.getDataColheitaPrevista() == null) {
            throw new IllegalArgumentException("Data de colheita prevista é obrigatória");
        }
        
        if (safra.getDataColheitaPrevista().isBefore(safra.getDataPlantio())) {
            throw new IllegalArgumentException("Data de colheita deve ser posterior à data de plantio");
        }
    }
}
