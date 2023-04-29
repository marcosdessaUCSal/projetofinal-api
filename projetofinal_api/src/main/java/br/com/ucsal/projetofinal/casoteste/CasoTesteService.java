package br.com.ucsal.projetofinal.casoteste;

import br.com.ucsal.projetofinal.exceptions.AtualizarException;
import br.com.ucsal.projetofinal.exceptions.IdNaoEncontradoException;
import br.com.ucsal.projetofinal.exceptions.InserirException;
import br.com.ucsal.projetofinal.tarefa.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class CasoTesteService {

    private final CasoTesteRepository casoTesteRepository;

    private final TarefaRepository tarefaRepository;

    public CasoTesteService(CasoTesteRepository casoTesteRepository, TarefaRepository tarefaRepository) {
        this.casoTesteRepository = casoTesteRepository;
        this.tarefaRepository = tarefaRepository;
    }

    public List<CasoTeste> listar() {
        return casoTesteRepository.findAll();
    }

    public Optional<CasoTeste> listarPorId(Long id) throws IdNaoEncontradoException {
        Optional<CasoTeste> casoTeste = casoTesteRepository.findById(id);
        if (casoTeste.isEmpty()) {
            throw new IdNaoEncontradoException("Não foi possível encontrar caso de teste com id " + id);
        } else {
            return casoTeste;
        }
    }

    public List<CasoTeste> listarPorTarefa(Long id) throws IdNaoEncontradoException {
        List<CasoTeste> lista = casoTesteRepository.findByTarefaId(id);
        if (lista.isEmpty()) {
            throw new IdNaoEncontradoException("Não foi possível encontrar tarefa com id " + id);
        } else {
            return casoTesteRepository.findByTarefaId(id);
        }
    }

    public CasoTeste inserir(CasoTesteRequestDto casoTesteRequestDto) throws InserirException {
        try {
            CasoTeste casoTeste = casoTesteRequestDto.toModel(tarefaRepository);
            return casoTesteRepository.save(casoTeste);
        } catch (NoSuchElementException e) {
            throw new InserirException("Não foi possível inserir caso de teste");
        }
    }

    public CasoTeste atualizar(Long id, CasoTeste casoTeste) throws AtualizarException {
        CasoTeste ctAtualizado = casoTesteRepository.findById(id).map(
                caso -> {
                    caso.setNomeTeste(casoTeste.getNomeTeste());
                    caso.setEntrada(casoTeste.getEntrada());
                    caso.setSaida(casoTeste.getSaida());
                    caso.setComparacao(casoTeste.getComparacao());
                    CasoTeste casoTesteAtualizado = casoTesteRepository.save(caso);
                    return casoTesteAtualizado;
                }).orElse(null);
        if (ctAtualizado != null) {
            return ctAtualizado;
        } else {
            throw new AtualizarException("Impossível atualizar caso de teste");
        }
    }
}
