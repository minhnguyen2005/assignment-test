<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Register</title></head>
<body>
    <h2>Registration Form</h2>
    <form action="UserController" method="post">
        Name: <input type="text" name="name" required><br>
        Email: <input type="email" name="email" required><br>
        Password: <input type="password" name="password" required><br>
        Address: <input type="text" name="address"><br>
        Phone Number: <input type="text" name="phoneNumber"><br>
        <input type="submit" value="Register">
    </form>
</body>
</html>
