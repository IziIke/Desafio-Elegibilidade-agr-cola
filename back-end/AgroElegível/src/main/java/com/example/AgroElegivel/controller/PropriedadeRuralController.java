package com.example.AgroElegivel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AgroElegivel.exceptions.DuplicateCodeException;
import com.example.AgroElegivel.exceptions.EntityNotFoundException;
import com.example.AgroElegivel.model.PropriedadeRural;
import com.example.AgroElegivel.model.Uf;
import com.example.AgroElegivel.service.PropriedadeRuralService;


@RestController
@RequestMapping("/api/propriedades")
@CrossOrigin(origins = "*")
public class PropriedadeRuralController {

    private final PropriedadeRuralService propriedadeRuralService;

    @Autowired
    public PropriedadeRuralController(PropriedadeRuralService propriedadeRuralService) {
        this.propriedadeRuralService = propriedadeRuralService;
    }

    /**
     * POST /api/propriedades - Cadastra uma nova propriedade
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody PropriedadeRural propriedade) {
        try {
            PropriedadeRural propriedadeSalva = propriedadeRuralService.cadastrar(propriedade);
            return ResponseEntity.status(HttpStatus.CREATED).body(propriedadeSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (DuplicateCodeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * GET /api/propriedades - Busca todas as propriedades
     */
    @GetMapping
    public ResponseEntity<List<PropriedadeRural>> buscarTodas() {
        List<PropriedadeRural> propriedades = propriedadeRuralService.buscarTodas();
        return ResponseEntity.ok(propriedades);
    }

    /**
     * GET /api/propriedades/{id} - Busca propriedade por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            PropriedadeRural propriedade = propriedadeRuralService.buscarPorId(id);
            return ResponseEntity.ok(propriedade);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/propriedades/codigo/{codigo} - Busca propriedade por código
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        try {
            Optional<PropriedadeRural> propriedade = propriedadeRuralService.buscarPorCodigo(codigo);
            if (propriedade.isPresent()) {
                return ResponseEntity.ok(propriedade.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * PUT /api/propriedades/{id} - Atualiza propriedade existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PropriedadeRural propriedade) {
        try {
            PropriedadeRural propriedadeAtualizada = propriedadeRuralService.atualizar(id, propriedade);
            return ResponseEntity.ok(propriedadeAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (DuplicateCodeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/propriedades/{id} - Deleta propriedade
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            propriedadeRuralService.deletar(id);
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
     * GET /api/propriedades/municipio/{municipio} - Busca propriedades por município
     */
    @GetMapping("/municipio/{municipio}")
    public ResponseEntity<?> buscarPorMunicipio(@PathVariable String municipio) {
        try {
            List<PropriedadeRural> propriedades = propriedadeRuralService.buscarPorMunicipio(municipio);
            return ResponseEntity.ok(propriedades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/propriedades/uf/{uf} - Busca propriedades por UF
     */
    @GetMapping("/uf/{uf}")
    public ResponseEntity<?> buscarPorUf(@PathVariable String uf) {
        try {
            Uf ufEnum = Uf.fromSigla(uf);
            List<PropriedadeRural> propriedades = propriedadeRuralService.buscarPorUf(ufEnum.getSigla());
            return ResponseEntity.ok(propriedades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("UF inválida: " + uf);
        }
    }

    /**
     * GET /api/propriedades/ufs - Lista todas as UFs disponíveis
     */
    @GetMapping("/ufs")
    public ResponseEntity<Uf[]> listarUfs() {
        return ResponseEntity.ok(Uf.values());
    }
}