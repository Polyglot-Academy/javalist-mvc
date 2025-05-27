<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gerenciador de Tarefas</title>
    <style>
        body { font-family: sans-serif; margin: 40px; text-align: center; }
        h1 { color: #333; }
        ul { list-style-type: none; padding: 0; }
        li { margin: 15px; }
        a { text-decoration: none; font-size: 1.2em; color: #007bff; padding:10px 20px; border:1px solid #007bff; border-radius:5px; transition: all 0.3s;}
        a:hover { background-color: #007bff; color:white; }
    </style>
</head>
<body>
<h1>Bem-vindo ao Gerenciador de Tarefas</h1>
<ul>
    <li><a href="${pageContext.request.contextPath}/categorias/list">Gerenciar Categorias</a></li>
    <li><a href="${pageContext.request.contextPath}/tarefas/list">Gerenciar Tarefas</a></li>
</ul>
</body>
</html>