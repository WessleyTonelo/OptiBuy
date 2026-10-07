package principal;

import modelo.*;
import repositorio.*;
import servico.UltimoPrecoPagoService;

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

        // Produto novo: Luva de Proteção
        Produto luva = new Produto(true, "EPI", "Luva de Proteção", 0, "Luva de Proteção", 12.00, 200);
        produtoRepo.salvar(luva);
        Produto luvaSalva = produtoRepo.listarTodos().get(produtoRepo.listarTodos().size() - 1);

        // Fornecedor (CNPJ aleatório)
        String cnpjAleatorio = "8" + (System.currentTimeMillis() % 100000000000L);
        Endereco endereco1 = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua C", 50, "Brasil");
        int idEndereco1 = enderecoRepo.salvar(endereco1);
        Endereco enderecoSalvo1 = enderecoRepo.buscarPorId(idEndereco1);

        Contato contatoForn = new Contato(0, "Vendedor", "49944445555", "fornluva@fornecedor.com", "EPI Seguranca LTDA Contato", "4933336666");
        int idContatoForn = contatoRepo.salvar(contatoForn);
        Contato contatoFornSalvo = contatoRepo.buscarPorId(idContatoForn);

        Fornecedor fornecedor = new Fornecedor(true, cnpjAleatorio, contatoFornSalvo, enderecoSalvo1, 0, "EPI Seguranca LTDA");
        fornecedorRepo.salvar(fornecedor);
        Fornecedor fornecedorSalvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // SolicitacaoCompra base
        List<ItemSolicitacao> itens = new ArrayList<>();
        itens.add(new ItemSolicitacao(luvaSalva, 50));
        SolicitacaoCompra solicitacao = new SolicitacaoCompra("2026-08-01", 0, "Media", itens, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        // PEDIDO ANTIGO (agosto) - preço R$ 10,00
        List<OrcamentoFornecedor> orcamentosAntigo = new ArrayList<>();
        orcamentosAntigo.add(new OrcamentoFornecedor(fornecedorSalvo, 0, 5, "Compra de agosto", 10.00, luvaSalva));
        Cotacao cotacaoAntiga = new Cotacao("2026-08-01", "2026-08-05", 0, "Cotacao agosto", solicitacaoSalva, orcamentosAntigo, "Fechada");
        int idCotacaoAntiga = cotacaoRepo.salvar(cotacaoAntiga);
        Cotacao cotacaoAntigaSalva = cotacaoRepo.buscarPorId(idCotacaoAntiga);

        PedidoCompra pedidoAntigo = new PedidoCompra(
                solicitanteSalvo, cotacaoAntigaSalva, "2026-08-10", "2026-08-05", "2026-08-10",
                0, fornecedorSalvo, 2001, "Pedido de agosto", "Entregue", 5.00, 500.00
        );
        pedidoRepo.salvar(pedidoAntigo);

        // PEDIDO RECENTE (hoje) - preço R$ 13,50
        List<OrcamentoFornecedor> orcamentosRecente = new ArrayList<>();
        orcamentosRecente.add(new OrcamentoFornecedor(fornecedorSalvo, 0, 5, "Compra de outubro", 13.50, luvaSalva));
        Cotacao cotacaoRecente = new Cotacao("2026-10-01", "2026-10-05", 0, "Cotacao outubro", solicitacaoSalva, orcamentosRecente, "Fechada");
        int idCotacaoRecente = cotacaoRepo.salvar(cotacaoRecente);
        Cotacao cotacaoRecenteSalva = cotacaoRepo.buscarPorId(idCotacaoRecente);

        PedidoCompra pedidoRecente = new PedidoCompra(
                solicitanteSalvo, cotacaoRecenteSalva, "2026-10-07", "2026-10-02", "2026-10-07",
                0, fornecedorSalvo, 2002, "Pedido de outubro", "Entregue", 5.00, 675.00
        );
        pedidoRepo.salvar(pedidoRecente);

        System.out.println("\n===== TESTANDO ULTIMO PRECO PAGO =====");
        UltimoPrecoPagoService ultimoPrecoService = new UltimoPrecoPagoService();
        String resultado = ultimoPrecoService.consultarUltimoPreco(luvaSalva);
        System.out.println(resultado);
    }
}