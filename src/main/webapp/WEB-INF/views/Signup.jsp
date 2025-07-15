<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
    <%@ include file="Styles.jsp" %>
</head>
<body>
<div class="form-container">
    <h2>Sign Up</h2>
     <form action="/register" method="post">
        <div class="input-container">
            <input type="text" name="name" placeholder=" " required />
            <label>Full Name</label>
        </div>
        <div class="input-container">
            <input type="email" name="email" placeholder=" " required />
            <label>Email</label>
        </div>
        <div class="input-container">
            <input type="text" name="phone" placeholder=" " pattern="[0-9]{10}" required />
            <label>Phone Number</label>
        </div>
        <div class="input-container">
            <input type="password" name="password" placeholder=" " required />
            <label>Password</label>
        </div>
        <div class="input-container">
            <input type="password" name="confirmPassword" placeholder=" " required />
            <label>Re-enter Password</label>
        </div>

        <div class="link">
            Already have an account? <a href="/login">Login</a>
        </div>

        <button type="submit" class="btn">Sign Up</button>
    </form>
    
</div>
</body>
</html>
