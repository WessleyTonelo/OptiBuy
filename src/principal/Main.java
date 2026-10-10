package principal;

import modelo.*;
import repositorio.*;
import servico.AlertaRecompraService;
import servico.EstoqueService;

import java.time.LocalDate;
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

        System.out.println("===== PREPARANDO DADOS =====");

        // Contato + Solicitante
        Contato contato = new Contato(0, "Comprador", "49911112222", "ana@optibuy.com", "Ana Requisitante", "4933331111");
        int idContato = contatoRepo.salvar(contato);
        Contato contatoSalvo = contatoRepo.buscarPorId(idContato);

        Solicitante solicitante = new Solicitante("Analista", contatoSalvo, 0, "Ana Requisitante", "Producao");
        solicitanteRepo.salvar(solicitante);
        Solicitante solicitanteSalvo = solicitanteRepo.listarTodos().get(solicitanteRepo.listarTodos().size() - 1);

        // Produto novo (nome único) com estoque ZERO
        String nomeProduto = "Fita Isolante Teste " + System.currentTimeMillis();
        Produto fita = new Produto(true, "Materiais", "Fita para teste de recompra", 0, nomeProduto, 8.00, 0);
        produtoRepo.salvar(fita);
        Produto fitaSalva = produtoRepo.listarTodos().get(produtoRepo.listarTodos().size() - 1);

        // Fornecedor (CNPJ aleatório)
        String cnpjAleatorio = "5" + (System.currentTimeMillis() % 100000000000L);
        Endereco endereco = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua F", 90, "Brasil");
        int idEndereco = enderecoRepo.salvar(endereco);
        Endereco enderecoSalvo = enderecoRepo.buscarPorId(idEndereco);

        Contato contatoForn = new Contato(0, "Vendedor", "49977778888", "estoque@fornecedor.com", "Materiais Estoque Contato", "4933339999");
        int idContatoForn = contatoRepo.salvar(contatoForn);
        Contato contatoFornSalvo = contatoRepo.buscarPorId(idContatoForn);

        Fornecedor fornecedor = new Fornecedor(true, cnpjAleatorio, contatoFornSalvo, enderecoSalvo, 0, "Materiais Estoque LTDA");
        fornecedorRepo.salvar(fornecedor);
        Fornecedor fornecedorSalvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // Datas calculadas a partir de HOJE (formato 2026-10-07)
        String tresDiasAtras = LocalDate.now().minusDays(3).toString();
        String hoje = LocalDate.now().toString();

        // PEDIDO DE 3 DIAS ATRÁS: 100 unidades
        List<ItemSolicitacao> itens = new ArrayList<>();
        itens.add(new ItemSolicitacao(fitaSalva, 100));
        SolicitacaoCompra solicitacao = new SolicitacaoCompra(tresDiasAtras, 0, "Media", itens, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        List<OrcamentoFornecedor> orcamentos = new ArrayList<>();
        orcamentos.add(new OrcamentoFornecedor(fornecedorSalvo, 0, 5, "Cotacao teste", 8.00, fitaSalva));
        Cotacao cotacao = new Cotacao(tresDiasAtras, hoje, 0, "Cotacao recompra", solicitacaoSalva, orcamentos, "Fechada");
        int idCotacao = cotacaoRepo.salvar(cotacao);
        Cotacao cotacaoSalva = cotacaoRepo.buscarPorId(idCotacao);

        PedidoCompra pedido = new PedidoCompra(
                solicitanteSalvo, cotacaoSalva, "", tresDiasAtras, hoje,
                0, fornecedorSalvo, 5001, "Pedido para teste de recompra", "Enviado", 0.00, 800.00
        );
        int idPedido = pedidoRepo.salvar(pedido);
        PedidoCompra pedidoSalvo = pedidoRepo.buscarPorId(idPedido);

        AlertaRecompraService alerta = new AlertaRecompraService();
        EstoqueService estoque = new EstoqueService();

        System.out.println("\n===== 1. ALERTA DE RECOMPRA (pedido feito há 3 dias) =====");
        System.out.println("--- Janela de 7 dias (semana) ---");
        System.out.println(alerta.verificarRecompra(fitaSalva, 7));
        System.out.println("--- Janela de 1 dia ---");
        System.out.println(alerta.verificarRecompra(fitaSalva, 1));

        System.out.println("\n===== 2. RECEBIMENTO ATUALIZANDO O ESTOQUE =====");
        System.out.println("Estoque ANTES do recebimento: " + estoque.consultarEstoque(fitaSalva));

        // Chegaram 95 de 100 -> divergência
        List<ItemRecebimento> itensRecebidos = new ArrayList<>();
        itensRecebidos.add(new ItemRecebimento(true, 0, "Faltaram 5 unidades", fitaSalva, 95, 100));
        Recebimento recebimento = new Recebimento(hoje, true, 0, pedidoSalvo, itensRecebidos, "Ana Requisitante", "Parcial");
        estoque.registrarRecebimento(recebimento);

        System.out.println("Estoque DEPOIS do recebimento: " + estoque.consultarEstoque(fitaSalva));

        System.out.println("\n===== 3. ALERTA DE NOVO (agora com estoque) =====");
        System.out.println(alerta.verificarRecompra(fitaSalva, 7));
    }
}