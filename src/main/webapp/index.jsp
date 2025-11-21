<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:include page="/includes/header.jsp" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loja Aquário - Home</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>


    <!-- Hero Section -->
    <div class="container mt-5">
        <div class="jumbotron text-center p-5 bg-light rounded-4 shadow-sm">
            <h1 class="display-4">Bem-vindo à Loja Aquário!</h1>
            <p class="lead">Aqui você encontra os melhores produtos para seu aquário e pet shop.</p>
            <hr class="my-4">
            <p>Comece gerenciando clientes, produtos e pedidos.</p>
            <a class="btn btn-primary btn-lg" href="clientes?action=listar" role="button">Ver Clientes</a>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
