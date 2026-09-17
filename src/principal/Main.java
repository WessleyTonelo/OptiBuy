package principal;

import modelo.Contato;
import modelo.Solicitante;
import repositorio.ContatoRepository;
import repositorio.SolicitanteRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ContatoRepository contatoRepo = new ContatoRepository();
        SolicitanteRepository solicitanteRepo = new SolicitanteRepository();

        System.out.println("===== 1. SALVAR =====");
        Contato contato = new Contato(0, "Comprador", "49977776666", "teste.crud@optibuy.com", "Carlos Teste", "4933339999");
        int idContato = contatoRepo.salvar(contato);
        Contato contatoSalvo = contatoRepo.buscarPorId(idContato);

        Solicitante solicitante = new Solicitante("Assistente", contatoSalvo, 0, "Carlos Teste", "Logistica");
        solicitanteRepo.salvar(solicitante);

        System.out.println("\n===== 2. LISTAR TODOS =====");
        List<Solicitante> solicitantes = solicitanteRepo.listarTodos();
        for (Solicitante s : solicitantes) {
            System.out.println("ID: " + s.getId() + " - " + s.getNome() + " - " + s.getCargo());
        }

        // Pega o ID do solicitante que acabamos de criar (o último da lista, já que sabemos que foi salvo agora)
        int idSolicitanteTeste = solicitantes.get(solicitantes.size() - 1).getId();

        System.out.println("\n===== 3. BUSCAR POR ID (id=" + idSolicitanteTeste + ") =====");
        Solicitante encontrado = solicitanteRepo.buscarPorId(idSolicitanteTeste);
        if (encontrado != null) {
            System.out.println("Encontrado: " + encontrado.getNome() + " - " + encontrado.getSetor());
        } else {
            System.out.println("Não encontrado!");
        }

        System.out.println("\n===== 4. ATUALIZAR =====");
        encontrado.setCargo("Coordenador");
        encontrado.setSetor("Compras Estrategicas");
        solicitanteRepo.atualizar(encontrado, idSolicitanteTeste);

        Solicitante atualizado = solicitanteRepo.buscarPorId(idSolicitanteTeste);
        System.out.println("Depois de atualizar: " + atualizado.getCargo() + " - " + atualizado.getSetor());

        System.out.println("\n===== 5. DELETAR =====");
        solicitanteRepo.deletar(idSolicitanteTeste);

        Solicitante deveSerNulo = solicitanteRepo.buscarPorId(idSolicitanteTeste);
        if (deveSerNulo == null) {
            System.out.println("Confirmado: solicitante foi removido (retornou null).");
        } else {
            System.out.println("ERRO: solicitante ainda existe!");
        }
    }
}