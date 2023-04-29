package br.com.ucsal.projetofinal.resposta;

import br.com.ucsal.projetofinal.exceptions.ArquivoNaoEncontradoException;
import br.com.ucsal.projetofinal.exceptions.AtualizarException;
import br.com.ucsal.projetofinal.exceptions.IdNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/respostas")
public class RespostaController {

    private final RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Resposta>> listar() {
        List<Resposta> respostas = respostaService.listar();
        return ResponseEntity.ok().body(respostas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        try {
            Optional<Resposta> resposta = respostaService.listarPorId(id);
            return ResponseEntity.ok().body(resposta);
        } catch (IdNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        @GetMapping("/usuario/{id}")
    public ResponseEntity listarPorIdUsuario(@PathVariable Long id) {
        List<RespostaPorcentagemResponseDTO> respostas = null;
        try {
            respostas = respostaService.listarPorIdUsuario(id);
        } catch (IdNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(respostas);
    }

    @GetMapping("/tarefa/{id}")
    public ResponseEntity listarPorIdTarefa(@PathVariable Long id) {
        List<RespostaPorcentagemResponseDTO> respostas = null;
        try {
            respostas = respostaService.listarPorIdTarefa(id);
        } catch (IdNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(respostas);
    }

    @GetMapping("/prova/{idProva}")
    public ResponseEntity listarPorIdProva(@PathVariable Long idProva) {
        List<RespostaPorcentagemResponseDTO> respostas = null;
        try {
            respostas = respostaService.listarPorIdProva(idProva);
            return ResponseEntity.ok().body(respostas);
        } catch (IdNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity inserir(@RequestBody @Valid RespostaRequestDto respostaRequestDto) {
        Resposta resposta = respostaService.inserir(respostaRequestDto);
        respostaService.gerarResultado(resposta);
        return ResponseEntity.ok().body(new RespostaResponseDto(resposta));
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody Resposta resposta) {
        try {
            return ResponseEntity.ok().body(new RespostaResponseDto(respostaService.atualizar(id, resposta)));
        } catch (AtualizarException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/arquivo")
    public ResponseEntity inserirPorArquivo(@RequestBody @Valid RespostaRequestDto respostaRequestDto) {
        try {
            String codigo = respostaService.extrairCodigoArquivo(respostaRequestDto.getCodigo());
            respostaRequestDto.setCodigo(codigo);
            Resposta resposta = respostaService.inserir(respostaRequestDto);
            respostaService.gerarResultado(resposta);
            return ResponseEntity.ok().body(new RespostaResponseDto(resposta));
        } catch (ArquivoNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
