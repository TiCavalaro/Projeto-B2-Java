<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Pedido" %>
<jsp:include page="/includes/header.jsp" />

<%
    if(session.getAttribute("usuarioLogado") == null){
        response.sendRedirect("login.jsp");
        return;
    }

    List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");
    String msg = (String) request.getSession().getAttribute("msg");
    request.getSession().removeAttribute("msg");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pedidos - Loja Aquário</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(to bottom, #e0f7fa, #b2ebf2);
            min-height: 100vh;
        }
        .container-custom {
            background-color: #ffffffcc;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
            margin-top: 60px;
        }
        h2 { color: #00796b; }
        .btn-primary { background-color: #00acc1; border: none; }
        .btn-primary:hover { background-color: #00838f; }
        .btn-success { background-color: #26c6da; border: none; }
        .btn-success:hover { background-color: #00acc1; }
        .btn-danger { background-color: #e53935; border: none; }
        .btn-danger:hover { background-color: #c62828; }
        .btn-secondary { background-color: #b2ebf2; color: #00796b; }
        .btn-secondary:hover { background-color: #81d4fa; }
        table thead { background-color: #b2ebf2 !important; color: #00796b; }
    </style>
</head>
<body>
<div class="container container-custom">

    <% if(msg != null) { %>
        <div class="alert alert-info alert-dismissible fade show mt-3" role="alert">
            <%= msg %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Pedidos</h2>
        <a href="pedidos?action=editar" class="btn btn-success">Novo Pedido</a>
    </div>

    <table class="table table-striped table-hover shadow-sm rounded">
        <thead>
            <tr>
                <th>ID</th>
                <th>Cliente</th>
                <th>Data</th>
                <th>Total</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <% if(pedidos != null && !pedidos.isEmpty()) {
                for(Pedido p : pedidos) { %>
                    <tr>
                        <td><%= p.getId() %></td>
                        <td><%= p.getClienteNome() %></td>
                        <td><%= p.getDataPedido() %></td>
                        <td>R$ <%= String.format("%.2f", p.getTotal()) %></td>
                        <td>
                            <a href="pedidos?action=editar&id=<%= p.getId() %>" class="btn btn-sm btn-primary">Editar</a>
                            <a href="pedidos?action=deletar&id=<%= p.getId() %>" class="btn btn-sm btn-danger"
                               onclick="return confirm('Deseja realmente excluir este pedido?');">Excluir</a>
                        </td>
                    </tr>
            <%  } } else { %>
                <tr>
                    <td colspan="5" class="text-center">Nenhum pedido cadastrado.</td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <a href="index.jsp" class="btn btn-secondary mt-3">Voltar à Home</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
