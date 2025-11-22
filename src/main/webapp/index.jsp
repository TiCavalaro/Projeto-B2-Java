<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Cliente, model.Produto, model.ProdutoDAO" %>
<jsp:include page="/includes/header.jsp" />

<%
    // Verifica se o usuário está logado
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Pega os produtos: se não vier do servlet, carrega diretamente do DAO
    List<Produto> produtos = (List<Produto>) request.getAttribute("produtos");
    if (produtos == null) {
        try {
            ProdutoDAO dao = new ProdutoDAO();
            produtos = dao.listar();
        } catch (Exception e) {
            produtos = null;
            out.println("<p class='text-danger'>Erro ao carregar produtos: " + e.getMessage() + "</p>");
        }
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loja Aquário - Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="container mt-5">
        <div class="jumbotron text-center p-5 bg-light rounded-4 shadow-sm">
            <h1 class="display-4">Bem-vindo à Loja Aquário!</h1>
            <p class="lead">Aqui você encontra os melhores produtos para seu aquário e pet shop.</p>
            <hr class="my-4">
            <p>Comece gerenciando clientes, produtos e pedidos.</p>
            <a class="btn btn-primary btn-lg" href="clientes?action=listar" role="button">Ver Clientes</a>
        </div>
    </div>

    <!-- Seção de Produtos -->
    <div class="container mt-5">
        <h2 class="mb-4 text-center">Produtos Cadastrados</h2>
        <div class="row g-4">
            <% if (produtos != null && !produtos.isEmpty()) {
                   for (Produto p : produtos) { %>
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="card card-product border rounded-3 shadow-sm p-3">
                        <div class="card-body text-center">
                            <h5 class="card-title"><%= p.getNome() %></h5>
                            <p class="card-text">R$ <%= String.format("%.2f", p.getPreco()) %></p>
                            <p class="card-text"><%= p.getEstoque() %> em estoque</p>
                            <span class="badge bg-secondary"><%= p.getCategoriaNome() != null ? p.getCategoriaNome() : "Sem Categoria" %></span>
                        </div>
                    </div>
                </div>
            <%   }
               } else { %>
            <div class="col-12">
                <p class="text-center fs-4 text-muted">Nenhum produto cadastrado.</p>
            </div>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
