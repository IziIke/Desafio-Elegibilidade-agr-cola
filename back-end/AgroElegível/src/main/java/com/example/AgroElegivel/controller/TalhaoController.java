package com.example.AgroElegivel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.AgroElegivel.exceptions.EntityNotFoundException;
import com.example.AgroElegivel.model.Talhao;
import com.example.AgroElegivel.service.TalhaoService;

import java.util.List;

@RestController
@RequestMapping("/api/talhoes")
@CrossOrigin(origins = "*")
public class TalhaoController {

    private final TalhaoService talhaoService;

    @Autowired
    public TalhaoController(TalhaoService talhaoService) {
        this.talhaoService = talhaoService;
    }

    /**
     * POST /api/talhoes - Cadastra um novo talhão
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Talhao talhao) {
        try {
            Talhao talhaoSalvo = talhaoService.cadastrar(talhao);
            return ResponseEntity.status(HttpStatus.CREATED).body(talhaoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Propriedade não encontrada: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * GET /api/talhoes - Busca todos os talhões
     */
    @GetMapping
    public ResponseEntity<List<Talhao>> buscarTodos() {
        List<Talhao> talhoes = talhaoService.buscarTodos();
        return ResponseEntity.ok(talhoes);
    }

    /**
     * GET /api/talhoes/{id} - Busca talhão por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Talhao talhao = talhaoService.buscarPorId(id);
            return ResponseEntity.ok(talhao);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * PUT /api/talhoes/{id} - Atualiza talhão existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Talhao talhao) {
        try {
            Talhao talhaoAtualizado = talhaoService.atualizar(id, talhao);
            return ResponseEntity.ok(talhaoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/talhoes/{id} - Deleta talhão
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            talhaoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * GET /api/talhoes/propriedade/{propriedadeId} - Busca talhões por propriedade
     */
    @GetMapping("/propriedade/{propriedadeId}")
    public ResponseEntity<?> buscarPorPropriedade(@PathVariable Long propriedadeId) {
        try {
            List<Talhao> talhoes = talhaoService.buscarPorPropriedade(propriedadeId);
            return ResponseEntity.ok(talhoes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Propriedade não encontrada: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/talhoes/nome/{nome} - Busca talhões por nome (contendo)
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> buscarPorNome(@PathVariable String nome) {
        try {
            List<Talhao> talhoes = talhaoService.buscarPorNome(nome);
            return ResponseEntity.ok(talhoes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/talhoes/propriedade/{propriedadeId}/count - Conta talhões por propriedade
     */
    @GetMapping("/propriedade/{propriedadeId}/count")
    public ResponseEntity<?> contarPorPropriedade(@PathVariable Long propriedadeId) {
        try {
            Long count = talhaoService.contarPorPropriedade(propriedadeId);
            return ResponseEntity.ok(count);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Propriedade não encontrada: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }
}
