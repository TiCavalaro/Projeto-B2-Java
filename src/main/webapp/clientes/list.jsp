<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Cliente" %>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Clientes</title>
</head>
<body>
    <h1>Lista de Clientes</h1>
    <a href="clientes/form.jsp">Novo Cliente</a>
    <table border="1">
        <tr>
            <th>ID</th><th>Nome</th><th>Email</th><th>Telefone</th><th>Ações</th>
        </tr>
        <%
            if(clientes != null){
                for(Cliente c : clientes){
        %>
        <tr>
            <td><%= c.getId() %></td>
            <td><%= c.getNome() %></td>
            <td><%= c.getEmail() %></td>
            <td><%= c.getTelefone() %></td>
            <td>
                <a href="clientes?action=editar&id=<%=c.getId()%>">Editar</a>
                <a href="clientes?action=deletar&id=<%=c.getId()%>">Deletar</a>
            </td>
        </tr>
        <% } } %>
    </table>
</body>
</html>
