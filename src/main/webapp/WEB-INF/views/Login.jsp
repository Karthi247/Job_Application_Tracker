<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%@ include file="Styles.jsp" %>
    <script>
        function resetForm() {
            document.getElementById("loginForm").reset();
        }
    </script>
</head>
<body>
<div class="form-container">
    <h2>Login</h2>
    <form id="loginForm" action="/loginsu" method="get">
    <div class="input-container">
        <input type="text" name="identifier" placeholder=" " required />
        <label>Email or Phone Number</label>
    </div>

    <div class="input-container">
        <input type="password" name="password" placeholder=" " required />
        <label>Password</label>
    </div>

    <div class="link">
        Don't have an account? <a href="/signup">Sign up</a>
    </div>

    <button type="submit" class="btn">Login</button>
    <button type="button" class="btn btn-reset" onclick="document.getElementById('loginForm').reset()">Reset</button>
</form>

    
</div>
</body>
</html>
