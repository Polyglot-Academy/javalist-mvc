<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Lista de Tarefas</title>
    <style>
        /* Common CSS from above */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .page-header {
            background-color: #5DADE2;
            color: white;
            padding: 20px 40px;
            text-align: center;
            margin-bottom: 30px;
        }

        .page-header h1 {
            margin: 0;
            font-size: 2.2em;
            font-weight: 300;
        }

        .container {
            max-width: 950px;
            margin: 0 auto 30px auto;
            padding: 25px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
        }

        .action-bar {
            margin-bottom: 20px;
            text-align: right;
        }

        .button, input[type="submit"], .cancel-button {
            text-decoration: none;
            font-size: 1em;
            color: #fff;
            background-color: #5DADE2;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s, transform 0.2s;
            margin-right: 10px;
            cursor: pointer;
            display: inline-block;
        }

        .button:hover, input[type="submit"]:hover, .cancel-button:hover {
            background-color: #3498DB;
            transform: translateY(-1px);
        }

        .button-add {
            background-color: #2ECC71;
        }

        .button-add:hover {
            background-color: #28B463;
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #5DADE2;
            text-decoration: none;
            font-weight: bold;
        }

        .back-link:hover {
            text-decoration: underline;
            color: #3498DB;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
        }

        th, td {
            border: 1px solid #e0e0e0;
            padding: 12px 15px;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background-color: #A9CCE3;
            color: #2c3e50;
            font-weight: 600;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #eef7ff;
        }

        .actions a {
            display: inline-block; /* Crucial for proper spacing and layout */
            text-decoration: none;
            color: #3498DB;
            background-color: #f0f8ff; /* Light background for the button itself */
            margin-right: 8px;
            margin-bottom: 5px; /* For spacing if links wrap on small screens */
            font-weight: normal;
            padding: 6px 12px; /* Adjusted padding for a better button feel */
            border: 1px solid #A9CCE3;
            border-radius: 4px;
            transition: background-color 0.2s, color 0.2s, border-color 0.2s;
        }

        .actions a:hover {
            background-color: #5DADE2;
            color: white;
            border-color: #5DADE2;
        }

        /* Optional: Remove margin from the last action link in a row if there are multiple */
        .actions a:last-child {
            margin-right: 0;
        }

        .error-message {
            color: #c0392b;
            background-color: #fADBD8;
            border: 1px solid #E74C3C;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            text-align: left;
        }
    </style>
</head>
<body>

<div class="page-header">
    <h1>Lista de Tarefas</h1>
</div>

<div class="container">
    <c:if test="${not empty errorMessage}">
        <div class="error-message"><c:out value="${errorMessage}"/></div>
    </c:if>

    <div class="action-bar">
        <a href="${pageContext.request.contextPath}/tarefas/new" class="button button-add">Adicionar Nova Tarefa</a>
    </div>

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
                <td><c:out value="${tarefa.id_Tarefas}"/></td>
                <td><c:out value="${tarefa.titulo}"/></td>
                <td><c:out value="${tarefa.descricao}"/></td>
                <td><c:out value="${tarefa.dataCriacaoFormatted_Display}"/></td>
                <td><c:out value="${tarefa.dataConclusaoFormatted_Display}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${tarefa.status == 1}">Não Iniciado</c:when>
                        <c:when test="${tarefa.status == 2}">Em Andamento</c:when>
                        <c:when test="${tarefa.status == 3}">Concluído</c:when>
                        <c:otherwise><c:out value="${tarefa.status}"/> (Desconhecido)</c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${tarefa.categoriaId}"/></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/tarefas/edit?id=${tarefa.id_Tarefas}">Editar</a>
                    <a href="${pageContext.request.contextPath}/tarefas/delete?id=${tarefa.id_Tarefas}"
                       onclick="return confirm('Tem certeza que deseja excluir esta tarefa?')">Excluir</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty listTarefas}">
            <tr>
                <td colspan="8" style="text-align:center; padding: 20px;">Nenhuma tarefa encontrada.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <p><a href="${pageContext.request.contextPath}" class="back-link">&laquo; Voltar para Home</a></p>
</div>
</body>
</html>