package model;

import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void salvar(Categoria c) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, c.getNome());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void atualizar(Categoria c) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE categoria SET nome=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, c.getNome());
        ps.setInt(2, c.getId());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void deletar(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "DELETE FROM categoria WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public Categoria getCategoriaById(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM categoria WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Categoria c = null;
        if (rs.next()) {
            c = new Categoria();
            c.setId(rs.getInt("id"));
            c.setNome(rs.getString("nome"));
        }
        rs.close();
        ps.close();
        conn.close();
        return c;
    }

    public List<Categoria> listar() throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM categoria";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Categoria> lista = new ArrayList<>();
        while (rs.next()) {
            Categoria c = new Categoria();
            c.setId(rs.getInt("id"));
            c.setNome(rs.getString("nome"));
            lista.add(c);
        }
        rs.close();
        ps.close();
        conn.close();
        return lista;
    }
}
