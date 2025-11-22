<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Pedido, model.Cliente, model.Produto, java.util.List, java.util.Map" %>
<jsp:include page="/includes/header.jsp" />

<%
    if(session.getAttribute("usuarioLogado") == null){
        response.sendRedirect("login.jsp");
        return;
    }

    Pedido p = (Pedido) request.getAttribute("pedido");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Produto> produtos = (List<Produto>) request.getAttribute("produtos");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulário Pedido - Loja Aquário</title>
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
            max-width: 700px;
        }
        h1 { color: #00796b; }
        .btn-primary { background-color: #00acc1; border: none; }
        .btn-primary:hover { background-color: #00838f; }
        .btn-secondary { background-color: #b2ebf2; color: #00796b; }
        .btn-secondary:hover { background-color: #81d4fa; }
        label { font-weight: 600; color: #00796b; }
        .input-group-text { font-weight: 600; }
    </style>
</head>
<body>
<div class="container container-custom">
    <h1 class="mb-4 text-center"><%= (p != null && p.getId() > 0) ? "Editar Pedido" : "Novo Pedido" %></h1>

    <form action="pedidos" method="post">
        <input type="hidden" name="id" value="<%= (p != null) ? p.getId() : "" %>"/>

        <div class="mb-3">
            <label>Cliente</label>
            <select name="cliente_id" class="form-select" required>
                <option value="">Selecione um cliente</option>
                <% for(Cliente c : clientes) { %>
                    <option value="<%= c.getId() %>" 
                        <%= (p != null && p.getClienteId() == c.getId()) ? "selected" : "" %>>
                        <%= c.getNome() %>
                    </option>
                <% } %>
            </select>
        </div>

        <label class="form-label mt-3">Produtos</label>
        <% for(Produto prod : produtos) { 
                if(prod.getEstoque() > 0) { // só exibe se houver estoque
                    int qtd = 0;
                    if(p != null && p.getProdutos() != null && p.getProdutos().containsKey(prod.getId())){
                        qtd = p.getProdutos().get(prod.getId());
                    }
        %>
            <div class="input-group mb-2">
                <span class="input-group-text"><%= prod.getNome() %> (R$ <%= String.format("%.2f", prod.getPreco()) %>) - Estoque: <%= prod.getEstoque() %></span>
                <input type="hidden" name="produto_id" value="<%= prod.getId() %>"/>
                <input type="number" name="quantidade" min="0" max="<%= prod.getEstoque() %>" class="form-control" value="<%= qtd %>"/>
            </div>
        <%  } 
           } %>

        <div class="d-flex justify-content-between mt-3">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a href="pedidos?action=listar" class="btn btn-secondary">Voltar</a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
