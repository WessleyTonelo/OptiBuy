package principal;

import modelo.*;
import repositorio.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ContatoRepository contatoRepo = new ContatoRepository();
        SolicitanteRepository solicitanteRepo = new SolicitanteRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();
        SolicitacaoCompraRepository solicitacaoRepo = new SolicitacaoCompraRepository();

        System.out.println("===== PREPARANDO DADOS =====");

        // 1. Criar um Contato e um Solicitante
        Contato contato = new Contato(0, "Comprador", "49911112222", "teste@optibuy.com", "Ana Requisitante", "4933331111");
        int idContato = contatoRepo.salvar(contato);
        Contato contatoSalvo = contatoRepo.buscarPorId(idContato);

        Solicitante solicitante = new Solicitante("Analista", contatoSalvo, 0, "Ana Requisitante", "Producao");
        solicitanteRepo.salvar(solicitante);
        List<Solicitante> solicitantes = solicitanteRepo.listarTodos();
        Solicitante solicitanteSalvo = solicitantes.get(solicitantes.size() - 1);

        // 2. Criar dois Produtos
        Produto parafuso = new Produto(true, "Ferragens", "Parafuso M6", 0, "Parafuso M6", 0.50, 1000);
        produtoRepo.salvar(parafuso);

        Produto fita = new Produto(true, "Materiais", "Fita isolante", 0, "Fita Isolante", 8.90, 50);
        produtoRepo.salvar(fita);

        List<Produto> produtos = produtoRepo.listarTodos();
        Produto parafusoSalvo = produtos.get(produtos.size() - 2);
        Produto fitaSalva = produtos.get(produtos.size() - 1);

        System.out.println("\n===== 1. SALVAR SOLICITACAO =====");
        List<ItemSolicitacao> itens = new ArrayList<>();
        itens.add(new ItemSolicitacao(parafusoSalvo, 5));
        itens.add(new ItemSolicitacao(fitaSalva, 2));

        SolicitacaoCompra solicitacao = new SolicitacaoCompra(
                "2026-10-04", 0, "Alta", itens, "Producao", "Rascunho", solicitanteSalvo
        );
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);

        System.out.println("\n===== 2. BUSCAR POR ID =====");
        SolicitacaoCompra encontrada = solicitacaoRepo.buscarPorId(idSolicitacao);
        System.out.println("Solicitante: " + encontrada.getSolicitante().getNome());
        System.out.println("Itens:");
        for (ItemSolicitacao item : encontrada.getItens()) {
            System.out.println(" - " + item.getProduto().getNome() + " | Qtd: " + item.getQuantidade());
        }

        System.out.println("\n===== 3. LISTAR TODOS =====");
        List<SolicitacaoCompra> todas = solicitacaoRepo.listarTodos();
        System.out.println("Total de solicitações no banco: " + todas.size());

        System.out.println("\n===== 4. DELETAR =====");
        solicitacaoRepo.deletar(idSolicitacao);
        SolicitacaoCompra deveSerNulo = solicitacaoRepo.buscarPorId(idSolicitacao);
        System.out.println(deveSerNulo == null ? "Confirmado: solicitação removida." : "ERRO: ainda existe!");
    }
}