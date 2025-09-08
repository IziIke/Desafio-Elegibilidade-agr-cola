package com.example.AgroElegivel.service;

import com.example.AgroElegivel.exceptions.EntityNotFoundException;
import com.example.AgroElegivel.model.PropriedadeRural;
import com.example.AgroElegivel.model.Talhao;
import com.example.AgroElegivel.repository.TalhaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TalhaoService {

    private final TalhaoRepository talhaoRepository;
    private final PropriedadeRuralService propriedadeRuralService;

    @Autowired
    public TalhaoService(TalhaoRepository talhaoRepository, PropriedadeRuralService propriedadeRuralService) {
        this.talhaoRepository = talhaoRepository;
        this.propriedadeRuralService = propriedadeRuralService;
    }

    /**
     * Cadastra um novo talhão
     */
    public Talhao cadastrar(Talhao talhao) {
        if (talhao == null) {
            throw new IllegalArgumentException("Talhão não pode ser nulo");
        }

        validarTalhao(talhao);

        // Verifica se a propriedade existe e associa
        PropriedadeRural propriedade = propriedadeRuralService.buscarPorId(
                talhao.getPropriedadeRural().getId()
        );
        talhao.setPropriedadeRural(propriedade);

        return talhaoRepository.save(talhao);
    }

    /**
     * Busca todos os talhões
     */
    @Transactional(readOnly = true)
    public List<Talhao> buscarTodos() {
        return talhaoRepository.findAll();
    }

    /**
     * Busca talhão por ID
     */
    @Transactional(readOnly = true)
    public Talhao buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        return talhaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Talhão não encontrado com ID: " + id));
    }

    /**
     * Deleta talhão por ID
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (!talhaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Talhão não encontrado com ID: " + id);
        }

        talhaoRepository.deleteById(id);
    }

    /**
     * Atualiza um talhão existente
     */
    public Talhao atualizar(Long id, Talhao talhaoAtualizado) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        Talhao talhaoExistente = buscarPorId(id);

        validarTalhao(talhaoAtualizado);

        // Verifica se a propriedade foi alterada
        if (!talhaoAtualizado.getPropriedadeRural().getId()
                .equals(talhaoExistente.getPropriedadeRural().getId())) {
            PropriedadeRural novaPropriedade = propriedadeRuralService.buscarPorId(
                    talhaoAtualizado.getPropriedadeRural().getId()
            );
            talhaoExistente.setPropriedadeRural(novaPropriedade);
        }

        talhaoExistente.setNome(talhaoAtualizado.getNome());
        talhaoExistente.setAreaHa(talhaoAtualizado.getAreaHa());

        return talhaoRepository.save(talhaoExistente);
    }

    /**
     * Busca talhões por propriedade
     */
    @Transactional(readOnly = true)
    public List<Talhao> buscarPorPropriedade(Long propriedadeId) {
        if (propriedadeId == null) {
            throw new IllegalArgumentException("ID da propriedade não pode ser nulo");
        }

        // Verifica se a propriedade existe
        propriedadeRuralService.buscarPorId(propriedadeId);

        return talhaoRepository.findByPropriedadeRuralId(propriedadeId);
    }

    /**
     * Busca talhões por nome (contendo)
     */
    @Transactional(readOnly = true)
    public List<Talhao> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        return talhaoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Conta talhões por propriedade
     */
    @Transactional(readOnly = true)
    public Long contarPorPropriedade(Long propriedadeId) {
        if (propriedadeId == null) {
            throw new IllegalArgumentException("ID da propriedade não pode ser nulo");
        }

        // Verifica se a propriedade existe
        propriedadeRuralService.buscarPorId(propriedadeId);

        return talhaoRepository.countByPropriedadeRuralId(propriedadeId);
    }

    private void validarTalhao(Talhao talhao) {
        if (talhao.getPropriedadeRural() == null || talhao.getPropriedadeRural().getId() == null) {
            throw new IllegalArgumentException("Propriedade é obrigatória");
        }

        if (talhao.getNome() == null || talhao.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do talhão é obrigatório");
        }

        if (talhao.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome do talhão deve ter no máximo 100 caracteres");
        }

        if (talhao.getAreaHa() == null || talhao.getAreaHa() <= 0) {
            throw new IllegalArgumentException("Área do talhão deve ser maior que zero");
        }
    }
}
