<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>JavaList - Início</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }
        .header {
            background-color: #5DADE2; /* A nice blue */
            color: white;
            padding: 25px 40px;
            text-align: center;
            border-bottom: 5px solid #3498DB;
        }
        .header h1 {
            margin: 0;
            font-size: 2.5em;
            font-weight: 300;
        }
        .container {
            max-width: 1100px;
            margin: 30px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .navigation-links {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
        }
        .nav-button {
            text-decoration: none;
            font-size: 1.1em;
            color: #fff;
            background-color: #5DADE2;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s, transform 0.2s;
            margin: 5px 10px; /* Added margin for better spacing on wrap */
            display: inline-block;
        }
        .nav-button:hover {
            background-color: #3498DB;
            transform: translateY(-2px);
        }
        h2 {
            color: #2c3e50;
            border-bottom: 2px solid #5DADE2;
            padding-bottom: 10px;
            margin-top: 0;
            font-weight: 400;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
            box-shadow: 0 2px 3px rgba(0,0,0,0.05);
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px 15px; /* Adjusted padding */
            text-align: left;
            vertical-align: middle; /* Added for better alignment */
        }
        th {
            background-color: #A9CCE3; /* Lighter blue for headers */
            color: #2c3e50;
            font-weight: 600;
        }
        tr:nth-child(even) { background-color: #f9f9f9; }
        tr:hover { background-color: #eef7ff; } /* Lighter hover */
        .actions a {
            text-decoration: none;
            color: #3498DB;
            margin-right: 10px;
            font-weight: bold;
            padding: 5px 8px; /* Added padding for better click area */
            border-radius: 3px;
            transition: background-color 0.2s, color 0.2s;
        }
        .actions a:hover {
            background-color: #3498DB;
            color: white;
        }
        .no-tasks {
            text-align: center;
            padding: 20px;
            color: #777;
            font-style: italic;
            background-color: #fdfefe;
            border: 1px dashed #ddd;
            border-radius: 5px;
        }
        .error-message {
            color: #c0392b;
            background-color: #fADBD8;
            border: 1px solid #E74C3C;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="header">
    <h1>Bem-vindo ao JavaList</h1>
</div>

<div class="container">
    <c:if test="${not empty errorMessage}">
        <div class="error-message"><c:out value="${errorMessage}" /></div>
    </c:if>

    <div class="navigation-links">
        <a href="${pageContext.request.contextPath}/categorias/list" class="nav-button">Gerenciar Categorias</a>
        <a href="${pageContext.request.contextPath}/tarefas/list" class="nav-button">Gerenciar Todas as Tarefas</a>
        <a href="${pageContext.request.contextPath}/tarefas/new" class="nav-button" style="background-color: #2ECC71;">+ Nova Tarefa</a>
    </div>

    <h2>Tarefas Atuais (Não Iniciadas ou Em Andamento)</h2>

    <c:choose>
        <c:when test="${not empty pendingTarefas}">
            <table>
                <thead>
                <tr>
                    <th>Título</th>
                    <th>Categoria</th>
                    <th>Status</th>
                    <th>Data de Criação</th>
                    <th>Data de Conclusão Prevista</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tarefa" items="${pendingTarefas}">
                    <tr>
                        <td><c:out value="${tarefa.titulo}" /></td>
                        <td>
                            <c:set var="categoriaNome" value="N/A" />
                            <c:if test="${not empty tarefa.categoriaId}">
                                <c:forEach var="cat" items="${listCategorias}">
                                    <c:if test="${cat.id_Categoria == tarefa.categoriaId}">
                                        <c:set var="categoriaNome" value="${cat.nome_Categoria}" />
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <c:out value="${categoriaNome}" />
                        </td>
                        <td>
                            <c:choose>
                                <%-- CORRECTED status mapping based on new info (1=NI, 2=EA) --%>
                                <c:when test="${tarefa.status == 1}">Não Iniciado</c:when>
                                <c:when test="${tarefa.status == 2}">Em Andamento</c:when>
                                <c:otherwise><c:out value="${tarefa.status}" /> (Status Desconhecido)</c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${tarefa.dataCriacaoFormatted_Display}" /></td>
                        <td><c:out value="${tarefa.dataConclusaoFormatted_Display}" /></td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/tarefas/edit?id=${tarefa.id_Tarefas}">Editar</a>
                            <a href="${pageContext.request.contextPath}/tarefas/avancarStatus?id=${tarefa.id_Tarefas}">Avançar Status</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-tasks">Nenhuma tarefa não iniciada ou em andamento encontrada.</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>