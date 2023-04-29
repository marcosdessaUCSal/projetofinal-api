package br.com.ucsal.projetofinal.casoteste;

import br.com.ucsal.projetofinal.exceptions.AtualizarException;
import br.com.ucsal.projetofinal.exceptions.IdNaoEncontradoException;
import br.com.ucsal.projetofinal.exceptions.InserirException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/casoteste")
public class CasoTesteController {

    private final CasoTesteService casoTesteService;

    public CasoTesteController(CasoTesteService casoTesteService) {
        this.casoTesteService = casoTesteService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CasoTeste>> listar() {
        List<CasoTeste> casoTestes = casoTesteService.listar();
        return ResponseEntity.ok().body(casoTestes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        try {
            Optional<CasoTeste> casoTeste = casoTesteService.listarPorId(id);
            return ResponseEntity.ok().body(casoTeste);
        } catch (IdNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tarefa/{id}")
    public ResponseEntity<?> listarPorTarefa(@PathVariable Long id) {
        List<CasoTeste> casoTestes = null;
        try {
            casoTestes = casoTesteService.listarPorTarefa(id);
            return ResponseEntity.ok().body(casoTestes);
        } catch (IdNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity inserir(@RequestBody @Valid CasoTesteRequestDto casoTesteRequestDto) {
        try {
            return ResponseEntity.ok().body(new CasoTesteResponseDto(casoTesteService.inserir(casoTesteRequestDto)));
        } catch (InserirException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody CasoTeste casoTeste) {
        try {
            return ResponseEntity.ok().body(new CasoTesteResponseDto((casoTesteService.atualizar(id, casoTeste))));
        } catch (AtualizarException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
