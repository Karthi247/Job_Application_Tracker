<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, com.pratice.Job_Application_Tracker.Entity.TrackEntity, com.pratice.Job_Application_Tracker.Entity.UserEntity" %>
<%
    UserEntity user = (UserEntity) session.getAttribute("user");
    boolean loggedIn = user != null;
    List<TrackEntity> tracks = (List<TrackEntity>) request.getAttribute("tracks");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Track List</title>
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

        h2 {
            color: #2d3436;
            font-size: 28px;
            margin-bottom: 20px;
        }

        .btn-add {
            background-color: #6c5ce7;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
        }

        .btn-add:hover {
            background-color: #5a4bd1;
        }

        table {
            width: 100%;
            margin-top: 30px;
            border-collapse: collapse;
            background-color: #fff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        th, td {
            padding: 14px 16px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            background-color: #f8f6ff;
            color: #6c5ce7;
            font-weight: 600;
        }

        tr:hover {
            background-color: #f9f9f9;
        }

        .no-data {
            color: #999;
            margin-top: 20px;
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
            <form action="/logout" method="get" style="display:inline;">
                <button type="submit" class="btn-logout">Logout</button>
            </form>
        <% } %>
    </div>
</div>



<div class="container">
    <h2>Track List</h2>

    <form action="track-form" method="get">
        <button class="btn-add" type="submit">+ Add New Track</button>
    </form>

    <% if (tracks != null && !tracks.isEmpty()) { %>
        <table>
            <tr>
                <th>Title</th>
                <th>Status</th>
                <th>Interview Date</th>
                <th>Company</th>
                <th>Position</th>
                <th>Progress</th>
                <th>Notes</th>
                <th>Updated At</th>
            </tr>
            <% for (TrackEntity track : tracks) { %>
                <tr>
                    <td><%= track.getTitle() %></td>
                    <td><%= track.getStatus() %></td>
                    <td><%= track.getInterviewDate() %></td>
                    <td><%= track.getCompany() %></td>
                    <td><%= track.getPosition() %></td>
                    <td><%= track.getProgress() %></td>
                    <td><%= track.getNotes() %></td>
                    <td><%= track.getUpdatedAt() %></td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <p class="no-data">No track data available.</p>
    <% } %>
</div>

</body>
</html>
