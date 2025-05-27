<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${empty categoria ? 'Adicionar Nova' : 'Editar'} Categoria</title>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        .container { max-width: 500px; margin: auto; padding:20px; border:1px solid #ccc; border-radius:5px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], textarea { width: 100%; padding: 8px; margin-bottom: 10px; border:1px solid #ccc; border-radius:3px; box-sizing: border-box;}
        input[type="submit"], .cancel-button { padding: 10px 15px; border:none; border-radius:3px; cursor:pointer; margin-right:10px;}
        input[type="submit"] { background-color: #4CAF50; color: white; }
        .cancel-button { background-color: #f44336; color:white; text-decoration: none;}
    </style>
</head>
<body>
<div class="container">
    <h1>${empty categoria ? 'Adicionar Nova' : 'Editar'} Categoria</h1>
    <form action="${pageContext.request.contextPath}/categorias" method="post">
        <c:if test="${not empty categoria}">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id_Categoria" value="<c:out value='${categoria.id_Categoria}' />" />
        </c:if>
        <c:if test="${empty categoria}">
            <input type="hidden" name="action" value="create" />
        </c:if>

        <p>
            <label for="nome_Categoria">Nome:</label>
            <input type="text" id="nome_Categoria" name="nome_Categoria" value="<c:out value='${categoria.nome_Categoria}' />" required>
        </p>
        <p>
            <label for="descricao_Categoria">Descrição:</label>
            <textarea id="descricao_Categoria" name="descricao_Categoria" rows="4"><c:out value='${categoria.descricao_Categoria}' /></textarea>
        </p>
        <p>
            <input type="submit" value="Salvar">
            <a href="${pageContext.request.contextPath}/categorias/list" class="cancel-button">Cancelar</a>
        </p>
    </form>
</div>
</body>
</html>