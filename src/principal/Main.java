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
        EnderecoRepository enderecoRepo = new EnderecoRepository();
        FornecedorRepository fornecedorRepo = new FornecedorRepository();
        SolicitacaoCompraRepository solicitacaoRepo = new SolicitacaoCompraRepository();
        CotacaoRepository cotacaoRepo = new CotacaoRepository();
        PedidoCompraRepository pedidoRepo = new PedidoCompraRepository();
        RecebimentoRepository recebimentoRepo = new RecebimentoRepository();

        System.out.println("===== PREPARANDO DADOS (fluxo completo) =====");

        // Contato + Solicitante
        Contato contato = new Contato(0, "Comprador", "49911112222", "ana@optibuy.com", "Ana Requisitante", "4933331111");
        int idContato = contatoRepo.salvar(contato);
        Contato contatoSalvo = contatoRepo.buscarPorId(idContato);

        Solicitante solicitante = new Solicitante("Analista", contatoSalvo, 0, "Ana Requisitante", "Producao");
        solicitanteRepo.salvar(solicitante);
        Solicitante solicitanteSalvo = solicitanteRepo.listarTodos().get(solicitanteRepo.listarTodos().size() - 1);

        // Produto
        Produto parafuso = new Produto(true, "Ferragens", "Parafuso M6", 0, "Parafuso M6", 0.50, 1000);
        produtoRepo.salvar(parafuso);
        Produto parafusoSalvo = produtoRepo.listarTodos().get(produtoRepo.listarTodos().size() - 1);

        // SolicitacaoCompra
        List<ItemSolicitacao> itensSolicitacao = new ArrayList<>();
        itensSolicitacao.add(new ItemSolicitacao(parafusoSalvo, 100));
        SolicitacaoCompra solicitacao = new SolicitacaoCompra("2026-10-04", 0, "Alta", itensSolicitacao, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        // Endereco + Contato + Fornecedor
        Endereco endereco1 = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua A", 100, "Brasil");
        int idEndereco1 = enderecoRepo.salvar(endereco1);
        Endereco enderecoSalvo1 = enderecoRepo.buscarPorId(idEndereco1);

        Contato contatoForn1 = new Contato(0, "Vendedor", "49922223333", "forn1@fornecedor.com", "Fornecedor Um", "4933332222");
        int idContatoForn1 = contatoRepo.salvar(contatoForn1);
        Contato contatoForn1Salvo = contatoRepo.buscarPorId(idContatoForn1);

        Fornecedor fornecedor1 = new Fornecedor(true, "11111111000111", contatoForn1Salvo, enderecoSalvo1, 0, "Fornecedor Parafusos LTDA");
        fornecedorRepo.salvar(fornecedor1);
        Fornecedor fornecedor1Salvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // Cotacao
        List<OrcamentoFornecedor> orcamentos = new ArrayList<>();
        orcamentos.add(new OrcamentoFornecedor(fornecedor1Salvo, 0, 5, "Preco a vista", 0.45, parafusoSalvo));
        Cotacao cotacao = new Cotacao("2026-10-04", "2026-10-10", 0, "Cotacao urgente", solicitacaoSalva, orcamentos, "Fechada");
        int idCotacao = cotacaoRepo.salvar(cotacao);
        Cotacao cotacaoSalva = cotacaoRepo.buscarPorId(idCotacao);

        // PedidoCompra
        PedidoCompra pedido = new PedidoCompra(
                solicitanteSalvo, cotacaoSalva, "", "2026-10-04", "2026-10-15",
                0, fornecedor1Salvo, 1001, "Pedido urgente", "Enviado", 15.00, 45.00
        );
        int idPedido = pedidoRepo.salvar(pedido);
        PedidoCompra pedidoSalvo = pedidoRepo.buscarPorId(idPedido);

        System.out.println("\n===== 1. SALVAR RECEBIMENTO =====");
        List<ItemRecebimento> itensRecebimento = new ArrayList<>();
        // Entregou 95 de 100 pedidos -> divergência!
        itensRecebimento.add(new ItemRecebimento(true, 0, "Faltaram 5 unidades na caixa", parafusoSalvo, 95, 100));

        Recebimento recebimento = new Recebimento(
                "2026-10-13", true, 0, pedidoSalvo, itensRecebimento, "Ana Requisitante", "Parcial"
        );
        int idRecebimento = recebimentoRepo.salvar(recebimento);

        System.out.println("\n===== 2. BUSCAR POR ID =====");
        Recebimento encontrado = recebimentoRepo.buscarPorId(idRecebimento);
        System.out.println("Pedido relacionado: " + encontrado.getPedidoCompra().getNumeroPedido());
        System.out.println("Divergência geral: " + encontrado.isDivergenciaEncontrada());
        System.out.println("Itens recebidos:");
        for (ItemRecebimento item : encontrado.getItensrecebidos()) {
            System.out.println(" - " + item.getProduto().getNome() + " | Esperado: " + item.getQuantidadeEsperada() + " | Entregue: " + item.getQuantidadeEntregue() + " | Divergência: " + item.isDivergencia() + " | Obs: " + item.getObs());
        }

        System.out.println("\n===== 3. LISTAR TODOS =====");
        System.out.println("Total de recebimentos no banco: " + recebimentoRepo.listarTodos().size());

        System.out.println("\n===== 4. DELETAR =====");
        recebimentoRepo.deletar(idRecebimento);
        Recebimento deveSerNulo = recebimentoRepo.buscarPorId(idRecebimento);
        System.out.println(deveSerNulo == null ? "Confirmado: recebimento removido." : "ERRO: ainda existe!");

        System.out.println("\n===== FLUXO COMPLETO TESTADO COM SUCESSO =====");
    }
}