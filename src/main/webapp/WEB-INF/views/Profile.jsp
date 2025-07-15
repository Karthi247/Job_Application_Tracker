<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.UserEntity" %>
<%
    UserEntity user = (UserEntity) session.getAttribute("user");
    boolean loggedIn = user != null;

    if (!loggedIn) {
        response.sendRedirect("/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Profile | Job Tracker</title>
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
            font-size: 24px;
            color: #6c5ce7;
            font-weight: bold;
            text-decoration: none;
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

        .container {
            padding: 50px 60px;
            max-width: 900px;
            margin: auto;
        }

        h1 {
            color: #2d3436;
            font-size: 32px;
            margin-bottom: 30px;
        }

        .profile-card {
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .profile-row {
            margin-bottom: 20px;
        }

        .profile-label {
            color: #6c5ce7;
            font-weight: bold;
            font-size: 16px;
        }

        .profile-value {
            font-size: 18px;
            color: #2d3436;
        }
    </style>
</head>
<body>


<div class="navbar">
    <div class="logo">Job Tracker</div>
    <div class="nav-links">
        <a href="/home">Home</a>
        <a href="/dashboard-info">Dashboard</a>
        <a href="/track-info">Track</a>
        <a href="/applications">Applications</a>
        <a href="/profile">Profile</a>
        <form action="/logout" method="get" style="display:inline;">
            <button class="btn-logout" type="submit">Logout</button>
        </form>
    </div>
</div>


<div class="container">
    <h1>Your Profile</h1>
    <div class="profile-card">
        <div class="profile-row">
            <div class="profile-label">Full Name:</div>
            <div class="profile-value"><%= user.getName() %></div>
        </div>
        <div class="profile-row">
            <div class="profile-label">Email:</div>
            <div class="profile-value"><%= user.getEmail() %></div>
        </div>
        <div class="profile-row">
            <div class="profile-label">Phone:</div>
            <div class="profile-value"><%= user.getPhone() %></div>
        </div>
        <%-- Add more fields if needed, like Resume URL, LinkedIn, etc. --%>
    </div>
</div>

</body>
</html>
