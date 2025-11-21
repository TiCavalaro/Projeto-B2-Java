<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Cliente" %>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clientes - Loja Aquário</title>
    <!-- Bootstrap CSS -->
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
        h2 {
            color: #00796b;
        }
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
    <jsp:include page="/includes/header.jsp" />

    <div class="container container-custom">

        <%-- Mensagem de sucesso ou erro --%>
        <%
            String msg = (String) request.getAttribute("msg");
        %>
        <% if(msg != null) { %>
            <div class="alert alert-info alert-dismissible fade show mt-3" role="alert">
                <%= msg %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Clientes</h2>
            <a href="clientes?action=editar" class="btn btn-success">Adicionar Cliente</a>
        </div>

        <table class="table table-striped table-hover shadow-sm rounded">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Telefone</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <% if(clientes != null && !clientes.isEmpty()) {
                        for(Cliente c : clientes) { %>
                        <tr>
                            <td><%= c.getId() %></td>
                            <td><%= c.getNome() %></td>
                            <td><%= c.getEmail() %></td>
                            <td><%= c.getTelefone() %></td>
                            <td>
                                <a href="clientes?action=editar&id=<%= c.getId() %>" class="btn btn-sm btn-primary">Editar</a>
                                <a href="clientes?action=deletar&id=<%= c.getId() %>" class="btn btn-sm btn-danger"
                                   onclick="return confirm('Deseja realmente deletar este cliente?');">Deletar</a>
                            </td>
                        </tr>
                <%  }
                   } else { %>
                    <tr>
                        <td colspan="5" class="text-center">Nenhum cliente cadastrado.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <a href="index.jsp" class="btn btn-secondary mt-3">Voltar à Home</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
