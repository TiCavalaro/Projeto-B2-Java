<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Loja Aquário</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(to bottom, #e0f7fa, #b2ebf2);
            margin: 0;
            padding: 0;
            min-height: 100vh;
        }

        .login-wrapper {
            display: flex;
            justify-content: center;
            align-items: center;
            height: calc(100vh - 60px); /* Ajusta altura considerando navbar */
        }

        .login-card {
            background-color: #ffffffcc;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
            width: 350px;
        }

        h2 {
            color: #00796b;
            margin-bottom: 20px;
            text-align: center;
        }

        .btn-primary {
            background-color: #00acc1;
            border: none;
            width: 100%;
        }

        .btn-primary:hover {
            background-color: #00838f;
        }
    </style>
</head>
<body>

    <!-- Navbar incluída -->
    <jsp:include page="/includes/header.jsp" />

    <!-- Wrapper centraliza o card -->
    <div class="login-wrapper">
        <div class="login-card">
            <h2>Login</h2>

            <%-- Mensagem de erro --%>
            <%
                String erro = (String) request.getAttribute("erro");
                if(erro != null) {
            %>
                <div class="alert alert-danger" role="alert">
                    <%= erro %>
                </div>
            <% } %>

            <form action="login" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="senha" class="form-label">Senha</label>
                    <input type="password" class="form-control" id="senha" name="senha" required>
                </div>
                <button type="submit" class="btn btn-primary">Entrar</button>
            </form>

            <p class="mt-3 text-center">
                <a href="clientes?action=editar">Não possui conta? Cadastre-se</a>
            </p>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
