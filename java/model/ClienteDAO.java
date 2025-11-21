package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.ConnectionFactory;

public class ClienteDAO {

    public void salvar(Cliente c) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO cliente (nome, email, senha, telefone) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, c.getNome());
        ps.setString(2, c.getEmail());
        ps.setString(3, c.getSenha());
        ps.setString(4, c.getTelefone());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void atualizar(Cliente c) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE cliente SET nome=?, email=?, senha=?, telefone=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, c.getNome());
        ps.setString(2, c.getEmail());
        ps.setString(3, c.getSenha());
        ps.setString(4, c.getTelefone());
        ps.setInt(5, c.getId());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void deletar(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "DELETE FROM cliente WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public Cliente getClienteById(int id) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM cliente WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Cliente c = null;
        if(rs.next()) {
            c = new Cliente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("telefone")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return c;
    }

    public List<Cliente> listar() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM cliente";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Cliente c = new Cliente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("telefone")
            );
            lista.add(c);
        }
        rs.close();
        ps.close();
        conn.close();
        return lista;
    }
    
    
    public Cliente getClienteByEmailAndSenha(String email, String senha) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM cliente WHERE email=? AND senha=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, senha);
        ResultSet rs = ps.executeQuery();
        Cliente c = null;
        if(rs.next()) {
            c = new Cliente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("telefone")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return c;
    }
    
    public Cliente getClienteByEmail(String email) throws Exception {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM cliente WHERE email=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        Cliente c = null;
        if(rs.next()) {
            c = new Cliente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("telefone")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return c;
    }


}
