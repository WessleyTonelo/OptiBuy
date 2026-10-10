package principal;

import modelo.*;
import repositorio.*;
import servico.DescontoVolumeService;

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

        // Produto novo (nome único)
        String nomeProduto = "Parafuso Teste " + System.currentTimeMillis();
        Produto parafuso = new Produto(true, "Ferragens", "Parafuso para teste de desconto", 0, nomeProduto, 10.00, 0);
        produtoRepo.salvar(parafuso);
        Produto parafusoSalvo = produtoRepo.listarTodos().get(produtoRepo.listarTodos().size() - 1);

        // Fornecedor (CNPJ aleatório)
        String cnpjAleatorio = "6" + (System.currentTimeMillis() % 100000000000L);
        Endereco endereco = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua E", 80, "Brasil");
        int idEndereco = enderecoRepo.salvar(endereco);
        Endereco enderecoSalvo = enderecoRepo.buscarPorId(idEndereco);

        Contato contatoForn = new Contato(0, "Vendedor", "49966667777", "desconto@fornecedor.com", "Ferragens Desconto Contato", "4933338888");
        int idContatoForn = contatoRepo.salvar(contatoForn);
        Contato contatoFornSalvo = contatoRepo.buscarPorId(idContatoForn);

        Fornecedor fornecedor = new Fornecedor(true, cnpjAleatorio, contatoFornSalvo, enderecoSalvo, 0, "Ferragens Desconto LTDA");
        fornecedorRepo.salvar(fornecedor);
        Fornecedor fornecedorSalvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // COMPRA ANTERIOR: 5 unidades a R$ 10,00
        List<ItemSolicitacao> itens = new ArrayList<>();
        itens.add(new ItemSolicitacao(parafusoSalvo, 5));
        SolicitacaoCompra solicitacao = new SolicitacaoCompra("2026-09-01", 0, "Media", itens, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        List<OrcamentoFornecedor> orcamentos = new ArrayList<>();
        orcamentos.add(new OrcamentoFornecedor(fornecedorSalvo, 0, 5, "Compra anterior", 10.00, parafusoSalvo));
        Cotacao cotacao = new Cotacao("2026-09-01", "2026-09-05", 0, "Cotacao anterior", solicitacaoSalva, orcamentos, "Fechada");
        int idCotacao = cotacaoRepo.salvar(cotacao);
        Cotacao cotacaoSalva = cotacaoRepo.buscarPorId(idCotacao);

        PedidoCompra pedido = new PedidoCompra(
                solicitanteSalvo, cotacaoSalva, "2026-09-10", "2026-09-05", "2026-09-10",
                0, fornecedorSalvo, 4001, "Compra anterior", "Entregue", 0.00, 50.00
        );
        pedidoRepo.salvar(pedido);

        System.out.println("\n===== ANALISANDO UMA COMPRA DE 100 UNIDADES (desconto esperado: 15%) =====");
        DescontoVolumeService descontoService = new DescontoVolumeService();

        System.out.println("\n--- Cenário 1: preço cotado R$ 8,00 ---");
        System.out.println(descontoService.analisarCompra(parafusoSalvo, 100, 8.00, 15));

        System.out.println("\n--- Cenário 2: preço cotado R$ 9,50 ---");
        System.out.println(descontoService.analisarCompra(parafusoSalvo, 100, 9.50, 15));

        System.out.println("\n--- Cenário 3: preço cotado R$ 10,50 ---");
        System.out.println(descontoService.analisarCompra(parafusoSalvo, 100, 10.50, 15));
    }
}