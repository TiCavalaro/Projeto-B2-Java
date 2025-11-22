package model;

import util.ConnectionFactory;
import java.sql.*;
import java.util.*;

public class PedidoDAO {

    // Listar todos os pedidos
    public List<Pedido> listar() throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT p.id, p.data_pedido, p.total, c.nome AS cliente_nome " +
                     "FROM pedido p JOIN cliente c ON p.cliente_id = c.id " +
                     "ORDER BY p.id DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Pedido p = new Pedido();
            p.setId(rs.getInt("id"));
            p.setDataPedido(rs.getTimestamp("data_pedido"));
            p.setTotal(rs.getDouble("total"));
            p.setClienteNome(rs.getString("cliente_nome"));
            pedidos.add(p);
        }

        rs.close();
        ps.close();
        conn.close();
        return pedidos;
    }

    // Salvar pedido e produtos com cálculo do total e verificação de estoque
    public void salvar(Pedido p) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        try {
            conn.setAutoCommit(false);

            // Calcular total e verificar estoque
            double total = 0;
            String sqlPreco = "SELECT preco, estoque FROM produto WHERE id = ?";
            PreparedStatement psPreco = conn.prepareStatement(sqlPreco);

            for (Map.Entry<Integer, Integer> entry : p.getProdutos().entrySet()) {
                int produtoId = entry.getKey();
                int quantidade = entry.getValue();

                psPreco.setInt(1, produtoId);
                ResultSet rsPreco = psPreco.executeQuery();
                if (rsPreco.next()) {
                    int estoque = rsPreco.getInt("estoque");
                    double preco = rsPreco.getDouble("preco");

                    if (quantidade > estoque) {
                        throw new Exception("Produto ID " + produtoId + " não possui estoque suficiente!");
                    }

                    total += preco * quantidade;
                } else {
                    throw new Exception("Produto ID " + produtoId + " não encontrado!");
                }
                rsPreco.close();
            }
            psPreco.close();

            p.setTotal(total);

            // Inserir pedido
            String sqlPedido = "INSERT INTO pedido (cliente_id, total) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getClienteId());
            ps.setDouble(2, p.getTotal());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
            }
            rs.close();
            ps.close();

            // Inserir produtos do pedido e atualizar estoque
            String sqlProduto = "INSERT INTO pedido_produto (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";
            PreparedStatement psProd = conn.prepareStatement(sqlProduto);

            String sqlAtualizaEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id = ?";
            PreparedStatement psEstoque = conn.prepareStatement(sqlAtualizaEstoque);

            for (Map.Entry<Integer, Integer> entry : p.getProdutos().entrySet()) {
                int produtoId = entry.getKey();
                int quantidade = entry.getValue();

                psProd.setInt(1, p.getId());
                psProd.setInt(2, produtoId);
                psProd.setInt(3, quantidade);
                psProd.addBatch();

                psEstoque.setInt(1, quantidade);
                psEstoque.setInt(2, produtoId);
                psEstoque.addBatch();
            }

            psProd.executeBatch();
            psProd.close();

            psEstoque.executeBatch();
            psEstoque.close();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // Atualizar pedido
    public void atualizar(Pedido p) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        try {
            conn.setAutoCommit(false);

            // Calcular total e verificar estoque
            double total = 0;
            String sqlPreco = "SELECT preco, estoque FROM produto WHERE id = ?";
            PreparedStatement psPreco = conn.prepareStatement(sqlPreco);

            // Recuperar produtos antigos para ajustar estoque
            String sqlAntigos = "SELECT produto_id, quantidade FROM pedido_produto WHERE pedido_id = ?";
            PreparedStatement psAntigos = conn.prepareStatement(sqlAntigos);
            psAntigos.setInt(1, p.getId());
            ResultSet rsAntigos = psAntigos.executeQuery();
            Map<Integer, Integer> produtosAntigos = new HashMap<>();
            while (rsAntigos.next()) {
                produtosAntigos.put(rsAntigos.getInt("produto_id"), rsAntigos.getInt("quantidade"));
            }
            rsAntigos.close();
            psAntigos.close();

            for (Map.Entry<Integer, Integer> entry : p.getProdutos().entrySet()) {
                int produtoId = entry.getKey();
                int quantidade = entry.getValue();

                psPreco.setInt(1, produtoId);
                ResultSet rsPreco = psPreco.executeQuery();
                if (rsPreco.next()) {
                    int estoqueAtual = rsPreco.getInt("estoque");
                    double preco = rsPreco.getDouble("preco");

                    // Ajustar estoque considerando produtos antigos
                    int estoqueDisponivel = estoqueAtual + produtosAntigos.getOrDefault(produtoId, 0);

                    if (quantidade > estoqueDisponivel) {
                        throw new Exception("Produto ID " + produtoId + " não possui estoque suficiente!");
                    }

                    total += preco * quantidade;
                } else {
                    throw new Exception("Produto ID " + produtoId + " não encontrado!");
                }
                rsPreco.close();
            }
            psPreco.close();

            p.setTotal(total);

            // Atualizar pedido
            String sqlPedido = "UPDATE pedido SET cliente_id = ?, total = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlPedido);
            ps.setInt(1, p.getClienteId());
            ps.setDouble(2, p.getTotal());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
            ps.close();

            // Restaurar estoque antigo
            String sqlRestaurarEstoque = "UPDATE produto SET estoque = estoque + ? WHERE id = ?";
            PreparedStatement psRest = conn.prepareStatement(sqlRestaurarEstoque);
            for (Map.Entry<Integer, Integer> entry : produtosAntigos.entrySet()) {
                psRest.setInt(1, entry.getValue());
                psRest.setInt(2, entry.getKey());
                psRest.addBatch();
            }
            psRest.executeBatch();
            psRest.close();

            // Deletar produtos antigos
            String sqlDel = "DELETE FROM pedido_produto WHERE pedido_id = ?";
            PreparedStatement psDel = conn.prepareStatement(sqlDel);
            psDel.setInt(1, p.getId());
            psDel.executeUpdate();
            psDel.close();

            // Inserir produtos atualizados e atualizar estoque
            String sqlProduto = "INSERT INTO pedido_produto (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";
            PreparedStatement psProd = conn.prepareStatement(sqlProduto);

            String sqlAtualizaEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id = ?";
            PreparedStatement psEstoque = conn.prepareStatement(sqlAtualizaEstoque);

            for (Map.Entry<Integer, Integer> entry : p.getProdutos().entrySet()) {
                int produtoId = entry.getKey();
                int quantidade = entry.getValue();

                psProd.setInt(1, p.getId());
                psProd.setInt(2, produtoId);
                psProd.setInt(3, quantidade);
                psProd.addBatch();

                psEstoque.setInt(1, quantidade);
                psEstoque.setInt(2, produtoId);
                psEstoque.addBatch();
            }

            psProd.executeBatch();
            psProd.close();

            psEstoque.executeBatch();
            psEstoque.close();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void deletar(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        try {
            conn.setAutoCommit(false);

            // Restaurar estoque dos produtos antes de deletar
            String sqlProd = "SELECT produto_id, quantidade FROM pedido_produto WHERE pedido_id = ?";
            PreparedStatement psProd = conn.prepareStatement(sqlProd);
            psProd.setInt(1, id);
            ResultSet rs = psProd.executeQuery();
            Map<Integer, Integer> produtos = new HashMap<>();
            while (rs.next()) {
                produtos.put(rs.getInt("produto_id"), rs.getInt("quantidade"));
            }
            rs.close();
            psProd.close();

            String sqlRest = "UPDATE produto SET estoque = estoque + ? WHERE id = ?";
            PreparedStatement psRest = conn.prepareStatement(sqlRest);
            for (Map.Entry<Integer, Integer> entry : produtos.entrySet()) {
                psRest.setInt(1, entry.getValue());
                psRest.setInt(2, entry.getKey());
                psRest.addBatch();
            }
            psRest.executeBatch();
            psRest.close();

            // Deletar pedido
            String sql = "DELETE FROM pedido WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public Pedido getById(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        Pedido p = null;

        // Pedido + Cliente
        String sql = "SELECT p.id, p.cliente_id, p.data_pedido, p.total, c.nome AS cliente_nome " +
                     "FROM pedido p JOIN cliente c ON p.cliente_id = c.id WHERE p.id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            p = new Pedido();
            p.setId(rs.getInt("id"));
            p.setClienteId(rs.getInt("cliente_id"));
            p.setDataPedido(rs.getTimestamp("data_pedido"));
            p.setTotal(rs.getDouble("total"));
            p.setClienteNome(rs.getString("cliente_nome"));
        }
        rs.close();
        ps.close();

        if (p != null) {
            String sqlProd = "SELECT produto_id, quantidade FROM pedido_produto WHERE pedido_id = ?";
            PreparedStatement psProd = conn.prepareStatement(sqlProd);
            psProd.setInt(1, id);
            ResultSet rsProd = psProd.executeQuery();
            Map<Integer, Integer> produtos = new HashMap<>();
            while (rsProd.next()) {
                produtos.put(rsProd.getInt("produto_id"), rsProd.getInt("quantidade"));
            }
            p.setProdutos(produtos);
            rsProd.close();
            psProd.close();
        }

        conn.close();
        return p;
    }
}
