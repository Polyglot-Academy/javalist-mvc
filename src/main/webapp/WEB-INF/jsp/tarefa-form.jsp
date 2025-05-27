<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${empty tarefa ? 'Adicionar Nova' : 'Editar'} Tarefa</title>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        .container { max-width: 600px; margin: auto; padding:20px; border:1px solid #ccc; border-radius:5px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], input[type="date"], textarea, select { width: 100%; padding: 8px; margin-bottom: 10px; border:1px solid #ccc; border-radius:3px; box-sizing: border-box;}
        input[type="submit"], .cancel-button { padding: 10px 15px; border:none; border-radius:3px; cursor:pointer; margin-right:10px;}
        input[type="submit"] { background-color: #4CAF50; color: white; }
        .cancel-button { background-color: #f44336; color:white; text-decoration: none;}
    </style>
</head>
<body>
<div class="container">
    <h1>${empty tarefa ? 'Adicionar Nova' : 'Editar'} Tarefa</h1>
    <form action="${pageContext.request.contextPath}/tarefas" method="post">
        <c:if test="${not empty tarefa}">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id_Tarefas" value="<c:out value='${tarefa.id_Tarefas}' />" />
        </c:if>
        <c:if test="${empty tarefa}">
            <input type="hidden" name="action" value="create" />
        </c:if>

        <p>
            <label for="titulo">Título:</label>
            <input type="text" id="titulo" name="titulo" value="<c:out value='${tarefa.titulo}' />" required>
        </p>
        <p>
            <label for="descricao">Descrição:</label>
            <textarea id="descricao" name="descricao" rows="3"><c:out value='${tarefa.descricao}' /></textarea>
        </p>
        <p>
            <label for="data_criacao">Data de Criação:</label>
            <input type="date" id="data_criacao" name="data_criacao" value="<c:out value='${tarefa.data_criacao}' />">
        </p>
        <p>
            <label for="data_conclusao">Data de Conclusão:</label>
            <input type="date" id="data_conclusao" name="data_conclusao" value="<c:out value='${tarefa.data_conclusao}' />">
        </p>
        <p>
            <label for="status">Status:</label>
            <select id="status" name="status">
                <c:forEach var="s" items="${statusOptions}">
                    <option value="${s}" ${tarefa.status == s ? 'selected' : ''}>${s}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="id_Categoria">Categoria:</label>
            <select id="id_Categoria" name="id_Categoria" required>
                <option value="">-- Selecione uma Categoria --</option>
                <c:forEach var="categoria" items="${listCategorias}">
                    <option value="${categoria.id_Categoria}" ${tarefa.id_Categoria == categoria.id_Categoria ? 'selected' : ''}>
                        <c:out value="${categoria.nome_Categoria}" />
                    </option>
                </c:forEach>
            </select>
        </p>
        <p>
            <input type="submit" value="Salvar">
            <a href="${pageContext.request.contextPath}/tarefas/list" class="cancel-button">Cancelar</a>
        </p>
    </form>
</div>
</body>
</html>