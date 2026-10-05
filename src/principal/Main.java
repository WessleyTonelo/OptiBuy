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
        SolicitacaoCompra solicitacao = new SolicitacaoCompra("2026-10-04", 0, "Alta", itens, "Producao", "Cotado", solicitanteSalvo);
        int idSolicitacao = solicitacaoRepo.salvar(solicitacao);
        SolicitacaoCompra solicitacaoSalva = solicitacaoRepo.buscarPorId(idSolicitacao);

        // Endereco + Contato + Fornecedor (dois fornecedores pra comparar preços)
        Endereco endereco1 = new Endereco(0, "Centro", "89800-000", "Chapeco", "", "SC", "Rua A", 100, "Brasil");
        int idEndereco1 = enderecoRepo.salvar(endereco1);
        Endereco enderecoSalvo1 = enderecoRepo.buscarPorId(idEndereco1);

        Contato contatoForn1 = new Contato(0, "Vendedor", "49922223333", "forn1@fornecedor.com", "Fornecedor Um", "4933332222");
        int idContatoForn1 = contatoRepo.salvar(contatoForn1);
        Contato contatoForn1Salvo = contatoRepo.buscarPorId(idContatoForn1);

        Fornecedor fornecedor1 = new Fornecedor(true, "11111111000111", contatoForn1Salvo, enderecoSalvo1, 0, "Fornecedor Parafusos LTDA");
        fornecedorRepo.salvar(fornecedor1);
        Fornecedor fornecedor1Salvo = fornecedorRepo.listarTodos().get(fornecedorRepo.listarTodos().size() - 1);

        System.out.println("\n===== 1. SALVAR COTACAO =====");
        List<OrcamentoFornecedor> orcamentos = new ArrayList<>();
        orcamentos.add(new OrcamentoFornecedor(fornecedor1Salvo, 0, 5, "Preco a vista", 0.45, parafusoSalvo));

        Cotacao cotacao = new Cotacao("2026-10-04", "2026-10-10", 0, "Cotacao urgente", solicitacaoSalva, orcamentos, "Aberta");
        int idCotacao = cotacaoRepo.salvar(cotacao);

        System.out.println("\n===== 2. BUSCAR POR ID =====");
        Cotacao encontrada = cotacaoRepo.buscarPorId(idCotacao);
        System.out.println("Solicitacao relacionada: " + encontrada.getSolicitacaoDeCompra().getId());
        System.out.println("Orcamentos recebidos:");
        for (OrcamentoFornecedor orc : encontrada.getOrcamentoDoFornecedor()) {
            System.out.println(" - " + orc.getFornecedor().getNome() + " ofertou R$ " + orc.getPrecoOfertado() + " para " + orc.getProduto().getNome());
        }

        System.out.println("\n===== 3. LISTAR TODOS =====");
        System.out.println("Total de cotações no banco: " + cotacaoRepo.listarTodos().size());

        System.out.println("\n===== 4. DELETAR =====");
        cotacaoRepo.deletar(idCotacao);
        Cotacao deveSerNulo = cotacaoRepo.buscarPorId(idCotacao);
        System.out.println(deveSerNulo == null ? "Confirmado: cotação removida." : "ERRO: ainda existe!");
    }
}