<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Cliente" %>

<%
    Cliente usuarioLogado = (Cliente) session.getAttribute("usuarioLogado");
%>

<!DOCTYPE html>
<!-- Navbar global -->
<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #b2ebf2;">
    <div class="container">
        <a class="navbar-brand text-primary fw-bold" href="index.jsp">Loja Aquário</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">

                <% if (usuarioLogado != null) { %>
                    <li class="nav-item">
                        <a class="nav-link text-primary fw-semibold" href="clientes?action=listar">Clientes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-primary fw-semibold" href="#">Produtos</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-primary fw-semibold" href="#">Pedidos</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-primary fw-semibold" href="categorias?action=listar">Categorias</a>
                    </li>
                    
                    <li class="nav-item">
                        <span class="nav-link text-primary fw-semibold">Olá, <strong><%= usuarioLogado.getNome() %></strong></span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link btn btn-outline-danger btn-sm ms-2" href="logout.jsp">Sair</a>
                    </li>
                <% } %>

            </ul>
        </div>
    </div>
</nav>
<hr style="margin:0; border-top:2px solid #00acc1;">
