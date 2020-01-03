<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>SQL Tags</title>
    </head>
    <body>
        <h2>Ejemplo SELECT</h2>
            <sql:setDataSource var="dataSource" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/jstl_tags" user="root" password="" scope="session"/>
            <sql:query var = "qry" dataSource="${dataSource}">
            SELECT id_usuario, username FROM usuarios
        </sql:query>
        <table border="1">
            <c:forEach var="row" items="${qry.rows}">
                <tr>
                    <td><c:out value="${row.id_usuario}"/></td>
                    <td><c:out value="${row.username}"/></td>
                </tr>
            </c:forEach>
        </table>

        <h2>Ejemplo INSERT</h2>
        <form action="sql_tags.jsp" method="post">
            <input type="hidden" value="insertar" name="valor"/>
            <input type="submit" value="Insert"/>
            <c:if test="${param.valor eq 'insertar'}">
                <sql:update dataSource="${dataSource}">
                INSERT INTO usuarios(username, userpass, nombre, apellido) VALUES( "JavaCuriosities","JavaCuriosities", "Pedro", "Picapiedra")
                </sql:update>
            </c:if>
        </form>
    </body>
</html>
