<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, com.pratice.Job_Application_Tracker.Entity.ApplicationEntity, com.pratice.Job_Application_Tracker.Entity.UserEntity" %>
<%
    List<ApplicationEntity> applications = (List<ApplicationEntity>) request.getAttribute("applications");
    UserEntity user = (UserEntity) session.getAttribute("user");
    boolean loggedIn = user != null;
    String userName = loggedIn ? user.getName() : "Guest";
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Applications</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f6f1ff;
            margin: 0;
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
        }

        h2 {
            color: #2d3436;
            font-size: 28px;
            margin-bottom: 20px;
        }

        .btn {
            background-color: #6c5ce7;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            margin-right: 5px;
        }

        .btn-delete {
            background-color: #e74c3c;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            margin-top: 20px;
        }

        th, td {
            padding: 14px;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #f0eaff;
            color: #6c5ce7;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
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
        <% if (loggedIn) { %>
            <form action="/logout" method="get" style="display:inline;">
                <button class="btn-logout" type="submit">Logout</button>
            </form>
        <% } else { %>
            <a href="/login">Login</a>
            <a href="/login/signup">Signup</a>
        <% } %>
    </div>
</div>


<div class="container">
    <div class="top-bar">
        <h2><%= userName %>'s Applications</h2>
        <a class="btn" href="/applications/add">+ Add Application</a>
    </div>

    <table>
        <tr>
            <th>Title</th>
            <th>Company</th>
            <th>Status</th>
            <th>Applied Date</th>
            <th>Actions</th>
        </tr>

        <%
            if (applications != null && !applications.isEmpty()) {
                for (ApplicationEntity app : applications) {
        %>
        <tr>
            <td><%= app.getTitle() %></td>
            <td><%= app.getCompany() %></td>
            <td><%= app.getStatus() %></td>
            <td><%= app.getAppliedDate() %></td>
            <td>
                <a class="btn" href="/applications/edit/<%= app.getId() %>">Edit</a>
                <a class="btn btn-delete" href="/applications/delete/<%= app.getId() %>" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="5">No applications found.</td>
        </tr>
        <%
            }
        %>
    </table>
</div>

</body>
</html>
