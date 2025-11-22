<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Categoria" %>
<jsp:include page="/includes/header.jsp" />

<%
    // Verifica login
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Categoria c = (Categoria) request.getAttribute("categoria");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulário Categoria - Loja Aquário</title>
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
    <h1><%= (c != null && c.getId() > 0) ? "Editar Categoria" : "Nova Categoria" %></h1>
    <form action="categorias" method="post">
        <input type="hidden" name="id" value="<%= (c != null) ? c.getId() : "" %>"/>

        <div class="mb-3">
            <label>Nome</label>
            <input type="text" name="nome" class="form-control" value="<%= (c != null) ? c.getNome() : "" %>" required/>
        </div>

        <button type="submit" class="btn btn-primary">Salvar</button>
        <a href="categorias?action=listar" class="btn btn-secondary">Voltar</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
