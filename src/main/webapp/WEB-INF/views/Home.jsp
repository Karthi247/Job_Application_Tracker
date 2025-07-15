<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.UserEntity" %>
<%
    UserEntity user = (UserEntity) session.getAttribute("user");
    boolean loggedIn = user != null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Job Tracker | Home</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background-color: #f6f1ff;
        }

        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #ffffff;
            padding: 15px 60px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.08);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .navbar .logo {
            display: flex;
            align-items: center;
            font-size: 24px;
            color: #6c5ce7;
            font-weight: bold;
            text-decoration: none;
        }

        .navbar .logo img {
            height: 32px;
            margin-right: 10px;
        }

        .navbar .nav-links {
            display: flex;
            align-items: center;
        }

        .navbar .nav-links a {
            text-decoration: none;
            color: #2d3436;
            margin: 0 15px;
            font-size: 16px;
            font-weight: 500;
        }

        .navbar .nav-links a:hover {
            color: #6c5ce7;
        }


        .btn-login, .btn-signup {
            background-color: #6c5ce7;
            color: white !important;
            padding: 8px 16px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
            margin-left: 10px;
            transition: background-color 0.3s ease;
        }

        .btn-login:hover, .btn-signup:hover {
            background-color: #594ae2;
            color: white !important;
        }

        .btn-logout {
            background-color: #e74c3c;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            margin-left: 20px;
        }

        .btn-logout:hover {
            background-color: #c0392b;
        }

        .hero {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 60px 100px;
        }

        .hero-text {
            max-width: 50%;
        }

        .hero-text h1 {
            font-size: 52px;
            color: #6c5ce7;
            margin: 0;
        }

        .hero-text h2 {
            font-size: 48px;
            color: #2d3436;
            margin: 5px 0;
        }

        .hero-text p {
            font-size: 18px;
            color: #555;
            margin: 20px 0 30px;
        }

        .get-started-btn {
            background-color: #6c5ce7;
            color: white;
            padding: 14px 30px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            text-decoration: none;
        }

        .get-started-btn:hover {
            background-color: #594ae2;
            color: white;
        }

        .hero-image img {
            width: 480px;
            max-width: 100%;
        }
    </style>
</head>
<body>

<div class="navbar">
    <div class="logo">
        Job Tracker
    </div>
    <div class="nav-links">
        <a href="/home">Home</a>
        <a href="/dashboard-info">Dashboard</a>
        <a href="/track-info">Track</a>
        <a href="/applications">Applications</a>
        <a href="/profile">Profile</a>

        <% if (!loggedIn) { %>
            <a href="/login" class="btn-login">Login</a>
            <a href="/login/signup" class="btn-signup">Signup</a>
        <% } else { %>
            <form action="/logout" method="get">
                <button type="submit" class="btn-logout">Logout</button>
            </form>
        <% } %>
    </div>
</div>

<div class="hero">
    <div class="hero-text">
        <h1>Effortless</h1>
        <h2>Job Management</h2>
        <p>Manage applications, track progress, and stay organized every step of the way.</p>
        <a href="/dashboard-info" class="get-started-btn">Get Started</a>
    </div>
    <div class="hero-image">
        <img src="/images/jobtracker.png" alt="My Image">
    </div>
</div>

</body>
</html>
