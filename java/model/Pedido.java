package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Pedido {

    private int id;
    private int clienteId;
    private String clienteNome; 
    private Date dataPedido;
    private double total;

    private Map<Integer, Integer> produtos = new HashMap<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public Date getDataPedido() { return dataPedido; }
    public void setDataPedido(Date dataPedido) { this.dataPedido = dataPedido; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Map<Integer, Integer> getProdutos() { return produtos; }
    public void setProdutos(Map<Integer, Integer> produtos) { this.produtos = produtos; }
}
