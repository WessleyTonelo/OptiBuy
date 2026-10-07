package principal;

import modelo.*;
import repositorio.*;
import servico.HistoricoPrecoFornecedorService;

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

        // Fornecedor (CNPJ aleatório pra evitar duplicidade)
        String cnpjAleatorio = "9" + System.currentTimeMillis() % 100000000000L;
        Endereco endereco1 = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua A", 100, "Brasil");
        int idEndereco1 = enderecoRepo.salvar(endereco1);
        Endereco enderecoSalvo1 = enderecoRepo.buscarPorId(idEndereco1);

        Contato contatoForn1 = new Contato(0, "Vendedor", "49922223333", "forn1@fornecedor.com", "Fornecedor Historico", "4933332222");
        int idContatoForn1 = contatoRepo.salvar(contatoForn1);
        Contato contatoForn1Salvo = contatoRepo.buscarPorId(idContatoForn1);

        Fornecedor fornecedor1 = new Fornecedor(true, cnpjAleatorio, contatoForn1Salvo, enderecoSalvo1, 0, "Fornecedor Historico LTDA");
        fornecedorRepo.salvar(fornecedor1);
        Fornecedor fornecedor1Salvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        // SolicitacaoCompra (base pra criar as cotações)
        List<ItemSolicitacao> itens = new ArrayList<>();
        itens.add(new ItemSolicitacao(parafusoSalvo, 100));
        SolicitacaoCompra solicitacao = new SolicitacaoCompra("2026-09-01", 0, "Alta", itens, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        // COTAÇÃO 1 (mês passado) - preço R$ 0.45
        List<OrcamentoFornecedor> orcamentos1 = new ArrayList<>();
        orcamentos1.add(new OrcamentoFornecedor(fornecedor1Salvo, 0, 5, "Cotacao antiga", 0.45, parafusoSalvo));
        Cotacao cotacao1 = new Cotacao("2026-09-01", "2026-09-05", 0, "Cotacao de setembro", solicitacaoSalva, orcamentos1, "Fechada");
        cotacaoRepo.salvar(cotacao1);

        // COTAÇÃO 2 (hoje) - preço R$ 0.60 (subiu!)
        List<OrcamentoFornecedor> orcamentos2 = new ArrayList<>();
        orcamentos2.add(new OrcamentoFornecedor(fornecedor1Salvo, 0, 5, "Cotacao nova", 0.60, parafusoSalvo));
        Cotacao cotacao2 = new Cotacao("2026-10-07", "2026-10-10", 0, "Cotacao de outubro", solicitacaoSalva, orcamentos2, "Aberta");
        cotacaoRepo.salvar(cotacao2);

        System.out.println("\n===== TESTANDO O HISTORICO DE PRECOS =====");
        HistoricoPrecoFornecedorService historicoService = new HistoricoPrecoFornecedorService();

        // Simula uma cotação nova chegando com preço R$ 0.60
        String resultado = historicoService.compararComHistorico(parafusoSalvo, fornecedor1Salvo, 0.60);
        System.out.println(resultado);

        // Simula uma cotação nova chegando com preço mais barato: R$ 0.40
        String resultado2 = historicoService.compararComHistorico(parafusoSalvo, fornecedor1Salvo, 0.40);
        System.out.println(resultado2);
    }
}