package principal;

import modelo.*;
import repositorio.*;
import servico.PrecoMedioPonderadoService;

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

        // Produto novo (nome único, pra não misturar com testes antigos)
        String nomeProduto = "Cabo de Rede " + System.currentTimeMillis();
        Produto cabo = new Produto(true, "Informatica", "Cabo de rede cat6", 0, nomeProduto, 4.50, 0);
        produtoRepo.salvar(cabo);
        Produto caboSalvo = produtoRepo.listarTodos().get(produtoRepo.listarTodos().size() - 1);

        // Fornecedor (CNPJ aleatório)
        String cnpjAleatorio = "7" + (System.currentTimeMillis() % 100000000000L);
        Endereco endereco = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua D", 70, "Brasil");
        int idEndereco = enderecoRepo.salvar(endereco);
        Endereco enderecoSalvo = enderecoRepo.buscarPorId(idEndereco);

        Contato contatoForn = new Contato(0, "Vendedor", "49955556666", "redes@fornecedor.com", "Redes Chapeco Contato", "4933337777");
        int idContatoForn = contatoRepo.salvar(contatoForn);
        Contato contatoFornSalvo = contatoRepo.buscarPorId(idContatoForn);

        Fornecedor fornecedor = new Fornecedor(true, cnpjAleatorio, contatoFornSalvo, enderecoSalvo, 0, "Redes Chapeco LTDA");
        fornecedorRepo.salvar(fornecedor);
        Fornecedor fornecedorSalvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // ===== COMPRA 1: 10 unidades a R$ 5,00 =====
        List<ItemSolicitacao> itens1 = new ArrayList<>();
        itens1.add(new ItemSolicitacao(caboSalvo, 10));
        SolicitacaoCompra solicitacao1 = new SolicitacaoCompra("2026-08-01", 0, "Media", itens1, "TI", "Cotado", solicitanteSalvo);
        int idSolicitacao1 = solicitacaoRepo.salvar(solicitacao1);
        SolicitacaoCompra solicitacao1Salva = solicitacaoRepo.buscarPorId(idSolicitacao1);

        List<OrcamentoFornecedor> orcamentos1 = new ArrayList<>();
        orcamentos1.add(new OrcamentoFornecedor(fornecedorSalvo, 0, 5, "Compra pequena", 5.00, caboSalvo));
        Cotacao cotacao1 = new Cotacao("2026-08-01", "2026-08-05", 0, "Cotacao 1", solicitacao1Salva, orcamentos1, "Fechada");
        int idCotacao1 = cotacaoRepo.salvar(cotacao1);
        Cotacao cotacao1Salva = cotacaoRepo.buscarPorId(idCotacao1);

        PedidoCompra pedido1 = new PedidoCompra(
                solicitanteSalvo, cotacao1Salva, "2026-08-10", "2026-08-05", "2026-08-10",
                0, fornecedorSalvo, 3001, "Pedido 1", "Entregue", 0.00, 50.00
        );
        pedidoRepo.salvar(pedido1);

        // ===== COMPRA 2: 100 unidades a R$ 4,00 =====
        List<ItemSolicitacao> itens2 = new ArrayList<>();
        itens2.add(new ItemSolicitacao(caboSalvo, 100));
        SolicitacaoCompra solicitacao2 = new SolicitacaoCompra("2026-10-01", 0, "Alta", itens2, "TI", "Cotado", solicitanteSalvo);
        int idSolicitacao2 = solicitacaoRepo.salvar(solicitacao2);
        SolicitacaoCompra solicitacao2Salva = solicitacaoRepo.buscarPorId(idSolicitacao2);

        List<OrcamentoFornecedor> orcamentos2 = new ArrayList<>();
        orcamentos2.add(new OrcamentoFornecedor(fornecedorSalvo, 0, 5, "Compra grande", 4.00, caboSalvo));
        Cotacao cotacao2 = new Cotacao("2026-10-01", "2026-10-05", 0, "Cotacao 2", solicitacao2Salva, orcamentos2, "Fechada");
        int idCotacao2 = cotacaoRepo.salvar(cotacao2);
        Cotacao cotacao2Salva = cotacaoRepo.buscarPorId(idCotacao2);

        PedidoCompra pedido2 = new PedidoCompra(
                solicitanteSalvo, cotacao2Salva, "2026-10-07", "2026-10-02", "2026-10-07",
                0, fornecedorSalvo, 3002, "Pedido 2", "Entregue", 0.00, 400.00
        );
        pedidoRepo.salvar(pedido2);

        System.out.println("\n===== TESTANDO PRECO MEDIO PONDERADO =====");
        PrecoMedioPonderadoService precoMedioService = new PrecoMedioPonderadoService();
        System.out.println(precoMedioService.consultarPrecoMedio(caboSalvo));
        System.out.println("(Media simples seria R$ 4,50, mas a ponderada leva em conta que voce comprou muito mais a R$ 4,00)");
    }
}