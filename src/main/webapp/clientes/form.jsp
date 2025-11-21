<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Cliente" %>
<%
    Cliente c = (Cliente) request.getAttribute("cliente");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Formulário Cliente</title>
</head>
<body>
    <h1><%= (c != null && c.getId() > 0) ? "Editar Cliente" : "Novo Cliente" %></h1>
    <form action="clientes" method="post">
        <input type="hidden" name="id" value="<%= (c!=null) ? c.getId() : "" %>"/>
        Nome: <input type="text" name="nome" value="<%= (c!=null) ? c.getNome() : "" %>" required/><br/>
        Email: <input type="email" name="email" value="<%= (c!=null) ? c.getEmail() : "" %>" required/><br/>
        Senha: <input type="password" name="senha" value="<%= (c!=null) ? c.getSenha() : "" %>" required/><br/>
        Telefone: <input type="text" name="telefone" value="<%= (c!=null) ? c.getTelefone() : "" %>"/><br/>
        <input type="submit" value="Salvar"/>
    </form>
    <a href="clientes?action=listar">Voltar</a>
</body>
</html>
