package model;

import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(Produto p) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getNome());
        ps.setDouble(2, p.getPreco());
        ps.setInt(3, p.getEstoque());
        ps.setInt(4, p.getCategoriaId());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void atualizar(Produto p) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE produto SET nome=?, preco=?, estoque=?, categoria_id=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getNome());
        ps.setDouble(2, p.getPreco());
        ps.setInt(3, p.getEstoque());
        ps.setInt(4, p.getCategoriaId());
        ps.setInt(5, p.getId());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void deletar(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "DELETE FROM produto WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public Produto getProdutoById(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT p.*, c.nome as categoria_nome FROM produto p JOIN categoria c ON p.categoria_id=c.id WHERE p.id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Produto p = null;
        if(rs.next()) {
            p = new Produto();
            p.setId(rs.getInt("id"));
            p.setNome(rs.getString("nome"));
            p.setPreco(rs.getDouble("preco"));
            p.setEstoque(rs.getInt("estoque"));
            p.setCategoriaId(rs.getInt("categoria_id"));
            p.setCategoriaNome(rs.getString("categoria_nome"));
        }
        rs.close();
        ps.close();
        conn.close();
        return p;
    }

    public List<Produto> listar() throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT p.*, c.nome as categoria_nome FROM produto p JOIN categoria c ON p.categoria_id=c.id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Produto> lista = new ArrayList<>();
        while(rs.next()) {
            Produto p = new Produto();
            p.setId(rs.getInt("id"));
            p.setNome(rs.getString("nome"));
            p.setPreco(rs.getDouble("preco"));
            p.setEstoque(rs.getInt("estoque"));
            p.setCategoriaId(rs.getInt("categoria_id"));
            p.setCategoriaNome(rs.getString("categoria_nome"));
            lista.add(p);
        }
        rs.close();
        ps.close();
        conn.close();
        return lista;
    }
}
