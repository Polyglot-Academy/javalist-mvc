<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${empty categoria.id_Categoria || categoria.id_Categoria == 0 ? 'Adicionar Nova' : 'Editar'} Categoria</title>
    <style>
        /* Common CSS from above */
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f4f7f6; color: #333; line-height: 1.6; }
        .page-header { background-color: #5DADE2; color: white; padding: 20px 40px; text-align: center; margin-bottom: 30px; }
        .page-header h1 { margin: 0; font-size: 2.2em; font-weight: 300; }
        .container { max-width: 700px; margin: 0 auto 30px auto; padding: 25px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
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
    <h1>${empty categoria.id_Categoria || categoria.id_Categoria == 0 ? 'Adicionar Nova' : 'Editar'} Categoria</h1>
</div>

<div class="container form-container">
    <c:if test="${not empty errorMessage}">
        <div class="error-message"><c:out value="${errorMessage}" /></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/categorias" method="post">
        <c:if test="${not empty categoria.id_Categoria && categoria.id_Categoria != 0}">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id_Categoria" value="<c:out value='${categoria.id_Categoria}' />" />
        </c:if>
        <c:if test="${empty categoria.id_Categoria || categoria.id_Categoria == 0}">
            <input type="hidden" name="action" value="create" />
        </c:if>

        <div>
            <label for="nome_Categoria">Nome da Categoria:</label>
            <input type="text" id="nome_Categoria" name="nome_Categoria" value="<c:out value='${categoria.nome_Categoria}' />" required>
        </div>
        <div>
            <label for="descricao_Categoria">Descrição:</label>
            <textarea id="descricao_Categoria" name="descricao_Categoria" rows="4"><c:out value='${categoria.descricao_Categoria}' /></textarea>
        </div>
        <div class="form-actions">
            <input type="submit" value="Salvar Categoria">
            <a href="${pageContext.request.contextPath}/categorias/list" class="cancel-button">Cancelar</a>
        </div>
    </form>
    <p><a href="${pageContext.request.contextPath}" class="back-link">&laquo; Voltar para Home</a></p>
</div>
</body>
</html>