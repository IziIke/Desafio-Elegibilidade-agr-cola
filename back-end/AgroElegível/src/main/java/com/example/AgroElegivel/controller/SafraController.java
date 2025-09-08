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
import com.example.AgroElegivel.model.Safra;
import com.example.AgroElegivel.service.SafraService;

@RestController
@RequestMapping("/api/safras")
@CrossOrigin(origins = "*")
public class SafraController {

    private final SafraService safraService;

    @Autowired
    public SafraController(SafraService safraService) {
        this.safraService = safraService;
    }

    /**
     * POST /api/safras - Cadastra uma nova safra
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Safra safra) {
        try {
            Safra safraSalva = safraService.cadastrar(safra);
            return ResponseEntity.status(HttpStatus.CREATED).body(safraSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Talhão não encontrado: " + e.getMessage());
        } catch (DuplicateCodeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * GET /api/safras - Busca todas as safras
     */
    @GetMapping
    public ResponseEntity<List<Safra>> buscarTodas() {
        List<Safra> safras = safraService.buscarTodas();
        return ResponseEntity.ok(safras);
    }

    /**
     * GET /api/safras/{id} - Busca safra por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Safra safra = safraService.buscarPorId(id);
            return ResponseEntity.ok(safra);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/safras/codigo/{codigo} - Busca safra por código
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        try {
            Optional<Safra> safra = safraService.buscarPorCodigo(codigo);
            if (safra.isPresent()) {
                return ResponseEntity.ok(safra.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * PUT /api/safras/{id} - Atualiza safra existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Safra safra) {
        try {
            Safra safraAtualizada = safraService.atualizar(id, safra);
            return ResponseEntity.ok(safraAtualizada);
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
     * DELETE /api/safras/{id} - Deleta safra
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            safraService.deletar(id);
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
     * GET /api/safras/talhao/{talhaoId} - Busca safras por talhão
     */
    @GetMapping("/talhao/{talhaoId}")
    public ResponseEntity<?> buscarPorTalhao(@PathVariable Long talhaoId) {
        try {
            List<Safra> safras = safraService.buscarPorTalhao(talhaoId);
            return ResponseEntity.ok(safras);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Talhão não encontrado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/safras/cultura/{cultura} - Busca safras por cultura
     */
    @GetMapping("/cultura/{cultura}")
    public ResponseEntity<?> buscarPorCultura(@PathVariable String cultura) {
        try {
            List<Safra> safras = safraService.buscarPorCultura(cultura);
            return ResponseEntity.ok(safras);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/safras/ano/{ano} - Busca safras por ano
     */
    @GetMapping("/ano/{ano}")
    public ResponseEntity<?> buscarPorAno(@PathVariable Integer ano) {
        try {
            List<Safra> safras = safraService.buscarPorAno(ano);
            return ResponseEntity.ok(safras);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/safras/cultura/{cultura}/ano/{ano} - Busca safras por cultura e ano
     */
    @GetMapping("/cultura/{cultura}/ano/{ano}")
    public ResponseEntity<?> buscarPorCulturaEAno(@PathVariable String cultura, @PathVariable Integer ano) {
        try {
            List<Safra> safras = safraService.buscarPorCulturaEAno(cultura, ano);
            return ResponseEntity.ok(safras);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }

    /**
     * GET /api/safras/colheita-proxima - Busca safras com colheita próxima (próximos 30 dias)
     */
    @GetMapping("/colheita-proxima")
    public ResponseEntity<List<Safra>> buscarSafrasComColheitaProxima() {
        List<Safra> safras = safraService.buscarSafrasComColheitaProxima();
        return ResponseEntity.ok(safras);
    }
}