<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Register</title>
    <style type="text/css">
        .error {
            color: RED;
        }

        td {
            padding: 15px;
        }
    </style>
</head>
<body>
<form:form action="${pageContext.request.contextPath}/register"
           commandName="userModel" autocomplete="off">
    <table>
        <tr>
            <td><form:label path="name">Name:</form:label></td>
            <td><form:input path="name"/></td>
            <td><form:errors path="name" cssClass="error"></form:errors></td>
        </tr>
        <tr>
            <td><form:label path="password">Password:</form:label></td>
            <td><form:password path="password"/></td>
            <td><form:errors path="password" cssClass="error"></form:errors></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form:form>
</body>
</html>