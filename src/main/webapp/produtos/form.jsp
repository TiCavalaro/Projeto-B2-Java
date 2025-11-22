<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Produto, model.Categoria, java.util.List" %>
<jsp:include page="/includes/header.jsp" />

<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Produto p = (Produto) request.getAttribute("produto");
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulário Produto - Loja Aquário</title>
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
            max-width: 600px;
        }
        h1 { color: #00796b; }
        .btn-primary { background-color: #00acc1; border: none; }
        .btn-primary:hover { background-color: #00838f; }
        .btn-secondary { background-color: #b2ebf2; color: #00796b; }
        .btn-secondary:hover { background-color: #81d4fa; }
        label { font-weight: 600; color: #00796b; }
    </style>
</head>
<body>

<div class="container container-custom">
    <h1><%= (p != null && p.getId() > 0) ? "Editar Produto" : "Novo Produto" %></h1>
    <form action="produtos" method="post">
        <input type="hidden" name="id" value="<%= (p != null) ? p.getId() : "" %>"/>

        <div class="mb-3">
            <label>Nome</label>
            <input type="text" name="nome" class="form-control" value="<%= (p != null) ? p.getNome() : "" %>" required/>
        </div>

        <div class="mb-3">
            <label>Preço</label>
            <input type="number" step="0.01" name="preco" class="form-control" value="<%= (p != null) ? p.getPreco() : "" %>" required/>
        </div>

        <div class="mb-3">
            <label>Estoque</label>
            <input type="number" name="estoque" class="form-control" value="<%= (p != null) ? p.getEstoque() : "" %>" required/>
        </div>

        <div class="mb-3">
            <label>Categoria</label>
            <select name="categoria_id" class="form-select" required>
                <option value="">Selecione...</option>
                <% for(Categoria cat : categorias) { %>
                    <option value="<%= cat.getId() %>" <%= (p != null && p.getCategoriaId() == cat.getId()) ? "selected" : "" %>>
                        <%= cat.getNome() %>
                    </option>
                <% } %>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Salvar</button>
        <a href="produtos?action=listar" class="btn btn-secondary">Voltar</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
