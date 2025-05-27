<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Categorias</title>
    <style> /* Basic styling, improve as needed */
    body { font-family: sans-serif; margin: 20px; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
    a { text-decoration: none; color: blue; }
    .actions a { margin-right: 10px; }
    .container { max-width: 800px; margin: auto; }
    .add-button { margin-bottom: 15px; padding: 10px 15px; background-color: #4CAF50; color: white; border:none; cursor:pointer; }
    .error { color: red; margin-bottom: 15px;}
    </style>
</head>
<body>
<div class="container">
    <h1>Lista de Categorias</h1>
    <c:if test="${not empty errorMessage}">
        <p class="error">${errorMessage}</p>
    </c:if>
    <p>
        <a href="${pageContext.request.contextPath}/categorias/new" class="add-button">Adicionar Nova Categoria</a>
    </p>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="categoria" items="${listCategorias}">
            <tr>
                <td><c:out value="${categoria.id_Categoria}" /></td>
                <td><c:out value="${categoria.nome_Categoria}" /></td>
                <td><c:out value="${categoria.descricao_Categoria}" /></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/categorias/edit?id=${categoria.id_Categoria}">Editar</a>
                    <a href="${pageContext.request.contextPath}/categorias/delete?id=${categoria.id_Categoria}"
                       onclick="return confirm('Tem certeza que deseja excluir esta categoria?')">Excluir</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty listCategorias}">
            <tr>
                <td colspan="4">Nenhuma categoria encontrada.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <p style="margin-top: 20px;"><a href="${pageContext.request.contextPath}/">Voltar para Home</a></p>
</div>
</body>
</html>