<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.UserEntity" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.DashboardEntity, com.pratice.Job_Application_Tracker.Entity.ApplicationEntity, java.util.*" %>
<%
    UserEntity user = (UserEntity) session.getAttribute("user");
    boolean loggedIn = user != null;
    DashboardEntity dashboard = (DashboardEntity) request.getAttribute("dashboard");
    List<ApplicationEntity> recentApps = (List<ApplicationEntity>) request.getAttribute("recentApps");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Job Tracker | Dashboard</title>
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
            color: white;
            padding: 8px 16px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
            margin-left: 10px;
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
        }
        h1 {
            color: #2d3436;
            font-size: 32px;
            margin-bottom: 10px;
        }
        .cards {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 30px;
            margin-bottom: 40px;
        }
        .card {
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            flex: 1 1 250px;
            transition: transform 0.2s ease;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .card-title {
            font-size: 18px;
            font-weight: 600;
            color: #6c5ce7;
            margin-bottom: 10px;
        }
        .card-value {
            font-size: 32px;
            color: #2d3436;
        }
        .recent-applications {
            margin-top: 40px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            padding: 30px;
        }
        .recent-applications h2 {
            color: #2d3436;
            font-size: 24px;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }
        .applications-table {
            width: 100%;
            border-collapse: collapse;
        }
        .applications-table th,
        .applications-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }
        .applications-table th {
            background-color: #f8f6ff;
            color: #6c5ce7;
            font-weight: 600;
        }
        .applications-table tr:hover {
            background-color: #f9f9f9;
        }
        .status {
            font-weight: 500;
        }
        .status-interview { color: #e67e22; }
        .status-applied { color: #3498db; }
        .status-rejected { color: #e74c3c; }
        .status-offer { color: #2ecc71; }
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
            <form action="/logout" method="get" style="display:inline;">
                <button type="submit" class="btn-logout">Logout</button>
            </form>
        <% } %>
    </div>
</div>


<div class="container">
    <h1>Dashboard</h1>
    <div class="cards">
        <% if (dashboard != null) { %>
            <div class="card">
                <div class="card-title">Total Applications</div>
                <div class="card-value"><%= dashboard.getTotalApplied() %></div>
            </div>
            <div class="card">
                <div class="card-title">Interviews Scheduled</div>
                <div class="card-value"><%= dashboard.getInterviewsScheduled() %></div>
            </div>
            <div class="card">
                <div class="card-title">Offers Received</div>
                <div class="card-value"><%= dashboard.getOffersReceived() %></div>
            </div>
            <div class="card">
                <div class="card-title">Rejections</div>
                <div class="card-value"><%= dashboard.getRejections() %></div>
            </div>
        <% } else { %>
            <div>No dashboard data available.</div>
        <% } %>
    </div>

    
    <div class="recent-applications">
        <h2>Recent Applications</h2>
        <table class="applications-table">
            <tr>
                <th>Title</th>
                <th>Company</th>
                <th>Status</th>
                <th>Applied Date</th>
            </tr>
            <% if (recentApps != null && !recentApps.isEmpty()) {
                for (ApplicationEntity app : recentApps) { %>
                <tr>
                    <td><%= app.getTitle() %></td>
                    <td><%= app.getCompany() %></td>
                    <td class="status 
                        <%= app.getStatus().equalsIgnoreCase("Interview Scheduled") ? "status-interview" :
                             app.getStatus().equalsIgnoreCase("Offer Received") ? "status-offer" :
                             app.getStatus().equalsIgnoreCase("Rejected") ? "status-rejected" :
                             "status-applied" %>">
                        <%= app.getStatus() %>
                    </td>
                    <td><%= app.getAppliedDate() %></td>
                </tr>
            <% } } else { %>
                <tr><td colspan="4">No recent applications found.</td></tr>
            <% } %>
        </table>
    </div>
</div>

</body>
</html>
