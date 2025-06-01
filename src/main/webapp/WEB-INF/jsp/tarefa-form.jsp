<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${empty tarefa.id_Tarefas || tarefa.id_Tarefas == 0 ? 'Adicionar Nova' : 'Editar'} Tarefa</title>
    <style>
        /* Common CSS from above */
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f4f7f6; color: #333; line-height: 1.6; }
        .page-header { background-color: #5DADE2; color: white; padding: 20px 40px; text-align: center; margin-bottom: 30px; }
        .page-header h1 { margin: 0; font-size: 2.2em; font-weight: 300; }
        .container { max-width: 700px; margin: 0 auto 30px auto; padding: 25px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
        .action-bar { margin-bottom: 20px; text-align: right; } /* Not used here, but kept for consistency */
        .button, input[type="submit"], .cancel-button { text-decoration: none; font-size: 1em; color: #fff; background-color: #5DADE2; padding: 10px 20px; border: none; border-radius: 5px; transition: background-color 0.3s, transform 0.2s; margin-right: 10px; cursor: pointer; display: inline-block; }
        .button:hover, input[type="submit"]:hover, .cancel-button:hover { background-color: #3498DB; transform: translateY(-1px); }
        input[type="submit"] { background-color: #2ECC71; } /* Save button green */
        input[type="submit"]:hover { background-color: #28B463; }
        .cancel-button { background-color: #E74C3C; }
        .cancel-button:hover { background-color: #C0392B; }
        .error-message { color: #c0392b; background-color: #fADBD8; border: 1px solid #E74C3C; padding: 15px; margin-bottom: 20px; border-radius: 5px; text-align: left; }
        label { display: block; margin-bottom: 6px; font-weight: 600; color: #555; }
        input[type="text"], input[type="date"], textarea, select { width: 100%; padding: 10px; margin-bottom: 15px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 1em; }
        textarea { min-height: 80px; resize: vertical; }
        .form-actions { margin-top: 20px; text-align: left; }
        .back-link { display: inline-block; margin-top:20px; color: #5DADE2; text-decoration: none; font-weight: bold; }
        .back-link:hover { text-decoration: underline; color: #3498DB; }
    </style>
</head>
<body>
<div class="page-header">
    <h1>${empty tarefa.id_Tarefas || tarefa.id_Tarefas == 0 ? 'Adicionar Nova' : 'Editar'} Tarefa</h1>
</div>

<div class="container form-container">
    <c:if test="${not empty errorMessage}">
        <div class="error-message"><c:out value="${errorMessage}" /></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/tarefas" method="post">
        <c:if test="${not empty tarefa.id_Tarefas && tarefa.id_Tarefas != 0}">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id_Tarefas" value="<c:out value='${tarefa.id_Tarefas}' />" />
        </c:if>
        <c:if test="${empty tarefa.id_Tarefas || tarefa.id_Tarefas == 0}">
            <input type="hidden" name="action" value="create" />
        </c:if>

        <div>
            <label for="titulo">Título:</label>
            <input type="text" id="titulo" name="titulo" value="<c:out value='${tarefa.titulo}' />" required>
        </div>
        <div>
            <label for="descricao">Descrição:</label>
            <textarea id="descricao" name="descricao" rows="3"><c:out value='${tarefa.descricao}' /></textarea>
        </div>
        <div>
            <label for="data_criacao">Data de Criação:</label>
            <input type="date" id="data_criacao" name="data_criacao" value="<c:out value='${tarefa.dataCriacaoForForm}' />">
        </div>
        <div>
            <label for="data_conclusao">Data de Conclusão:</label>
            <input type="date" id="data_conclusao" name="data_conclusao" value="<c:out value='${tarefa.dataConclusaoForForm}' />">
        </div>
        <div>
            <label for="status">Status:</label>
            <select id="status" name="status">
                <%-- Assuming status 1=NI, 2=EA, 3=C based on previous context --%>
                <option value="1" ${tarefa.status == 1 ? 'selected' : ''}>Não Iniciado</option>
                <option value="2" ${tarefa.status == 2 ? 'selected' : ''}>Em Andamento</option>
                <option value="3" ${tarefa.status == 3 ? 'selected' : ''}>Concluído</option>
            </select>
        </div>
        <div>
            <label for="id_Categoria">Categoria:</label>
            <select id="id_Categoria" name="id_Categoria" required>
                <option value="">-- Selecione uma Categoria --</option>
                <c:forEach var="cat" items="${listCategorias}">
                    <option value="${cat.id_Categoria}" ${tarefa.categoriaId == cat.id_Categoria ? 'selected' : ''}>
                        <c:out value="${cat.nome_Categoria}" />
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-actions">
            <input type="submit" value="Salvar">
            <a href="${pageContext.request.contextPath}/tarefas/list" class="cancel-button">Cancelar</a>
        </div>
    </form>
    <p><a href="${pageContext.request.contextPath}" class="back-link">&laquo; Voltar para Home</a></p>
</div>
</body>
</html>