import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

//SITEMA DE LACHONETE
//VENDAS

public class Main {
    public static class Produto {
        private String nome;
        private double preco;
        private String categoria;

        public Produto(String nome, double preco, String categoria) {
            this.nome = nome;
            this.preco = preco;
            this.categoria = categoria;
        }

        public String getNome() {
            return nome;
        }

        public double getPreco() {
            return preco;
        }

        public String getCategoria() {
            return categoria;
        }
    }


    public static class ItemPedido {
        private Produto produto;
        private int quantidade;

        public ItemPedido(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public double getPrecoTotal() {
            return produto.getPreco() * quantidade;
        }

        public Produto getProduto() {
            return produto;
        }

        public int getQuantidade() {
            return quantidade;
        }
    }

    public static class Carrinho {
        private ArrayList<ItemPedido> itens;

        public Carrinho() {
            this.itens = new ArrayList<>();
        }

        public void adicionarItem(ItemPedido item) {
            itens.add(item);
        }

        public ArrayList<ItemPedido> getItens() {
            return itens;
        }

        public double getTotal() {
            double total = 0;
            for (ItemPedido item : itens) {
                total = total + item.getPrecoTotal();
            }
            return total;
        }
    }


    public static class Pedido {
        private Carrinho carrinho;
        private double taxaEntrega;

        public Pedido(Carrinho carrinho, double taxaEntrega) {
            this.carrinho = carrinho;
            this.taxaEntrega = taxaEntrega;
        }

        public Carrinho getCarrinho() {
            return carrinho;
        }

        public double getTaxaEntrega() {
            return taxaEntrega;
        }

        public double getTotal() {
            return carrinho.getTotal() + taxaEntrega;
        }
    }

    public static class Lanchonete{
        private ArrayList<Produto> cardapio;

        public Lanchonete(){
            this.cardapio = new ArrayList<>();
        }
        public void cadastrarProduto(Produto produto){
            cardapio.add(produto);
        }
        public void listarProdutos(){
            for (int i = 0; i < cardapio.size(); i++){
                Produto p = cardapio.get(i);
                System.out.println((i+1) + ". " + p.getNome() + " - R$ " + p.getPreco());
            }
        }
    }

    public static void main(String[] args) {
        Produto p = new Produto("X-Burguer", 15.90, "Lanche");
        IO.print("Nome: " + p.getNome());
        IO.print("\nPreço: " + p.getPreco());
        IO.print("\nCategoria: " + p.getCategoria());

        Carrinho carrinho = new Carrinho();

        ItemPedido item = new ItemPedido(p, 2);

        carrinho.adicionarItem(item);

        Pedido pedido = new Pedido(carrinho, 5.00);

        IO.print("\nTotal: " + pedido.getTotal());

        Lanchonete lanchonete = new Lanchonete();

        lanchonete.cadastrarProduto(new Produto("X-Burguer", 15.90, "Lanche"));
        lanchonete.cadastrarProduto(new Produto("X-Bacon", 18.90, "Lanche"));
        lanchonete.cadastrarProduto(new Produto("Coca-Cola", 6.00, "Bebida"));
        lanchonete.cadastrarProduto(new Produto("Suco de Laranja", 7.00, "Bebida"));
        lanchonete.cadastrarProduto(new Produto("Sorvete", 8.00, "Sobremesa"));
        lanchonete.listarProdutos();
    }
}

