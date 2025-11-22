<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Categoria" %>
<jsp:include page="/includes/header.jsp" />

<%
    // Verifica se o usuário está logado
    if(session.getAttribute("usuarioLogado") == null){
        response.sendRedirect("login.jsp");
        return;
    }

    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    String msg = (String) request.getAttribute("msg");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categorias - Loja Aquário</title>
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
        <h2>Categorias</h2>
        <a href="categorias?action=editar" class="btn btn-success">Adicionar Categoria</a>
    </div>

    <table class="table table-striped table-hover shadow-sm rounded">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <% if(categorias != null && !categorias.isEmpty()) {
                   for(Categoria c : categorias) { %>
                        <tr>
                            <td><%= c.getId() %></td>
                            <td><%= c.getNome() %></td>
                            <td>
                                <a href="categorias?action=editar&id=<%= c.getId() %>" class="btn btn-sm btn-primary">Editar</a>
                                <a href="categorias?action=deletar&id=<%= c.getId() %>" class="btn btn-sm btn-danger"
                                   onclick="return confirm('Deseja realmente deletar esta categoria?');">Deletar</a>
                            </td>
                        </tr>
            <%       }
               } else { %>
                <tr>
                    <td colspan="3" class="text-center">Nenhuma categoria cadastrada.</td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <a href="index.jsp" class="btn btn-secondary mt-3">Voltar à Home</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
