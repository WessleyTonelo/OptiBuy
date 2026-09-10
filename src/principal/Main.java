package principal;

import modelo.Produto;
import repositorio.ProdutoRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ProdutoRepository produtoRepo = new ProdutoRepository();

        // 1. Criar e salvar um Produto
        Produto produto = new Produto(true, "Informática", "Mouse sem fio", 0, "Mouse Logitech M170", 49.90, 10);
        produtoRepo.salvar(produto);

        // 2. Listar todos os produtos
        List<Produto> produtos = produtoRepo.listarTodos();
        for (Produto p : produtos) {
            System.out.println(p.getNome() + " - R$ " + p.getPreco() + " - Qtd: " + p.getQuantidade());
        }
    }
}