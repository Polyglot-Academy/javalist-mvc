<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- For date formatting --%>
<html>
<head>
    <title>Tarefas</title>
    <style> /* Basic styling, improve as needed */
    body { font-family: sans-serif; margin: 20px; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
    a { text-decoration: none; color: blue; }
    .actions a { margin-right: 10px; }
    .container { max-width: 950px; margin: auto; }
    .add-button { margin-bottom: 15px; padding: 10px 15px; background-color: #4CAF50; color: white; border:none; cursor:pointer; }
    .error { color: red; margin-bottom: 15px;}
    </style>
</head>
<body>
<div class="container">
    <h1>Lista de Tarefas</h1>
    <c:if test="${not empty errorMessage}">
        <p class="error">${errorMessage}</p>
    </c:if>
    <p>
        <a href="${pageContext.request.contextPath}/tarefas/new" class="add-button">Adicionar Nova Tarefa</a>
    </p>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Descrição</th>
            <th>Data Criação</th>
            <th>Data Conclusão</th>
            <th>Status</th>
            <th>ID Categoria</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="tarefa" items="${listTarefas}">
            <tr>
                <td><c:out value="${tarefa.id_Tarefas}" /></td>
                <td><c:out value="${tarefa.titulo}" /></td>
                <td><c:out value="${tarefa.descricao}" /></td>
                <td>
                    <fmt:parseDate value="${tarefa.data_criacao}" pattern="yyyy-MM-dd" var="parsedCriacaoDate" type="date" />
                    <fmt:formatDate value="${parsedCriacaoDate}" type="date" pattern="dd/MM/yyyy" />
                </td>
                <td>
                    <fmt:parseDate value="${tarefa.data_conclusao}" pattern="yyyy-MM-dd" var="parsedConclusaoDate" type="date" />
                    <fmt:formatDate value="${parsedConclusaoDate}" type="date" pattern="dd/MM/yyyy" />
                </td>
                <td><c:out value="${tarefa.status}" /></td>
                <td><c:out value="${tarefa.id_Categoria}" /></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/tarefas/edit?id=${tarefa.id_Tarefas}">Editar</a>
                    <a href="${pageContext.request.contextPath}/tarefas/delete?id=${tarefa.id_Tarefas}"
                       onclick="return confirm('Tem certeza que deseja excluir esta tarefa?')">Excluir</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty listTarefas}">
            <tr>
                <td colspan="8">Nenhuma tarefa encontrada.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <p style="margin-top: 20px;"><a href="${pageContext.request.contextPath}/">Voltar para Home</a></p>
</div>
</body>
</html>