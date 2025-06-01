<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Lista de Categorias</title>
    <style>
        /* Common CSS from above */
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f4f7f6; color: #333; line-height: 1.6; }
        .page-header { background-color: #5DADE2; color: white; padding: 20px 40px; text-align: center; margin-bottom: 30px; }
        .page-header h1 { margin: 0; font-size: 2.2em; font-weight: 300; }
        .container { max-width: 800px; margin: 0 auto 30px auto; padding: 25px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
        .action-bar { margin-bottom: 20px; text-align: right; }
        .button, input[type="submit"], .cancel-button { text-decoration: none; font-size: 1em; color: #fff; background-color: #5DADE2; padding: 10px 20px; border: none; border-radius: 5px; transition: background-color 0.3s, transform 0.2s; margin-right: 10px; cursor: pointer; display: inline-block; }
        .button:hover, input[type="submit"]:hover, .cancel-button:hover { background-color: #3498DB; transform: translateY(-1px); }
        .button-add { background-color: #2ECC71; }
        .button-add:hover { background-color: #28B463; }
        .back-link { display: inline-block; margin-top:20px; color: #5DADE2; text-decoration: none; font-weight: bold; }
        .back-link:hover { text-decoration: underline; color: #3498DB; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
        th, td { border: 1px solid #e0e0e0; padding: 12px 15px; text-align: left; vertical-align: middle; }
        th { background-color: #A9CCE3; color: #2c3e50; font-weight: 600; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        tr:hover { background-color: #eef7ff; }
        .actions a { text-decoration: none; color: #3498DB; margin-right: 8px; font-weight: normal; padding: 4px 8px; border: 1px solid #A9CCE3; border-radius: 4px; transition: background-color 0.2s, color 0.2s; }
        .actions a:hover { background-color: #5DADE2; color: white; border-color: #5DADE2; }
        .error-message { color: #c0392b; background-color: #fADBD8; border: 1px solid #E74C3C; padding: 15px; margin-bottom: 20px; border-radius: 5px; text-align: left; }
    </style>
</head>
<body>
<div class="page-header">
    <h1>Lista de Categorias</h1>
</div>

<div class="container">
    <c:if test="${not empty errorMessage}">
        <div class="error-message"><c:out value="${errorMessage}" /></div>
    </c:if>

    <div class="action-bar">
        <a href="${pageContext.request.contextPath}/categorias/new" class="button button-add">Adicionar Nova Categoria</a>
    </div>

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
                       onclick="return confirm('Tem certeza que deseja excluir esta categoria? Se ela tiver tarefas associadas, a exclusão pode falhar ou as tarefas ficarão sem categoria.')">Excluir</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty listCategorias}">
            <tr>
                <td colspan="4" style="text-align:center; padding: 20px;">Nenhuma categoria encontrada.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <p><a href="${pageContext.request.contextPath}" class="back-link">&laquo; Voltar para Home</a></p>
</div>
</body>
</html>