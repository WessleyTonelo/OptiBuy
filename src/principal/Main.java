package principal;

import modelo.*;
import repositorio.*;
import servico.ComparadorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        ContatoRepository contatoRepo = new ContatoRepository();
        SolicitanteRepository solicitanteRepo = new SolicitanteRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();
        EnderecoRepository enderecoRepo = new EnderecoRepository();
        FornecedorRepository fornecedorRepo = new FornecedorRepository();
        SolicitacaoCompraRepository solicitacaoRepo = new SolicitacaoCompraRepository();
        CotacaoRepository cotacaoRepo = new CotacaoRepository();

        System.out.println("===== PREPARANDO DADOS =====");

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
        List<ItemSolicitacao> itens = new ArrayList<>();
        itens.add(new ItemSolicitacao(parafusoSalvo, 100));
        SolicitacaoCompra solicitacao = new SolicitacaoCompra("2026-10-07", 0, "Alta", itens, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        // Fornecedor 1
        Endereco endereco1 = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua A", 100, "Brasil");
        int idEndereco1 = enderecoRepo.salvar(endereco1);
        Endereco enderecoSalvo1 = enderecoRepo.buscarPorId(idEndereco1);

        Contato contatoForn1 = new Contato(0, "Vendedor", "49922223333", "forn1@fornecedor.com", "Fornecedor Um", "4933332222");
        int idContatoForn1 = contatoRepo.salvar(contatoForn1);
        Contato contatoForn1Salvo = contatoRepo.buscarPorId(idContatoForn1);

        Fornecedor fornecedor1 = new Fornecedor(true, "11111111000111", contatoForn1Salvo, enderecoSalvo1, 0, "Fornecedor Parafusos LTDA");
        fornecedorRepo.salvar(fornecedor1);
        Fornecedor fornecedor1Salvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // Fornecedor 2
        Endereco endereco2 = new Endereco(0, "Bairro Industrial", "89805-000", "Chapeco", "", "SC", "Rua B", 200, "Brasil");
        int idEndereco2 = enderecoRepo.salvar(endereco2);
        Endereco enderecoSalvo2 = enderecoRepo.buscarPorId(idEndereco2);

        Contato contatoForn2 = new Contato(0, "Vendedor", "49933334444", "forn2@fornecedor.com", "Fornecedor Dois", "4933335555");
        int idContatoForn2 = contatoRepo.salvar(contatoForn2);
        Contato contatoForn2Salvo = contatoRepo.buscarPorId(idContatoForn2);

        Fornecedor fornecedor2 = new Fornecedor(true, "22222222000122", contatoForn2Salvo, enderecoSalvo2, 0, "Ferragens Baratinho LTDA");
        fornecedorRepo.salvar(fornecedor2);
        Fornecedor fornecedor2Salvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // Cotação com DOIS orçamentos pro mesmo produto, preços diferentes
        List<OrcamentoFornecedor> orcamentos = new ArrayList<>();
        orcamentos.add(new OrcamentoFornecedor(fornecedor1Salvo, 0, 5, "Preco normal", 0.50, parafusoSalvo));
        orcamentos.add(new OrcamentoFornecedor(fornecedor2Salvo, 0, 7, "Promocao essa semana", 0.38, parafusoSalvo));

        Cotacao cotacao = new Cotacao("2026-10-07", "2026-10-14", 0, "Cotacao teste comparador", solicitacaoSalva, orcamentos, "Fechada");
        int idCotacao = cotacaoRepo.salvar(cotacao);
        Cotacao cotacaoSalva = cotacaoRepo.buscarPorId(idCotacao);

        System.out.println("\n===== COMPARANDO PREÇOS =====");
        ComparadorService comparador = new ComparadorService();
        Map<Produto, OrcamentoFornecedor> melhores = comparador.encontrarMelhoresPrecos(cotacaoSalva);

        for (Map.Entry<Produto, OrcamentoFornecedor> entrada : melhores.entrySet()) {
            Produto produto = entrada.getKey();
            OrcamentoFornecedor melhor = entrada.getValue();
            System.out.println("Produto: " + produto.getNome());
            System.out.println("Melhor fornecedor: " + melhor.getFornecedor().getNome());
            System.out.println("Preço: R$ " + melhor.getPrecoOfertado());
            System.out.println("Prazo de entrega: " + melhor.getPrazoEntregaDias() + " dias");
        }
    }
}