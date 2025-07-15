<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.ApplicationEntity" %>
<%
    ApplicationEntity app = (ApplicationEntity) request.getAttribute("application");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Application</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f6f1ff;
            margin: 0;
            padding: 0;
        }

        .form-container {
            max-width: 600px;
            background-color: #fff;
            margin: 60px auto;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            color: #6c5ce7;
            margin-bottom: 30px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2d3436;
        }

        input[type="text"],
        input[type="date"],
        select {
            width: 100%;
            padding: 10px 12px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #6c5ce7;
            color: #fff;
            font-size: 16px;
            font-weight: 600;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        button:hover {
            background-color: #5a4bd6;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            text-decoration: none;
            color: #6c5ce7;
            font-weight: 500;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Edit Application</h2>
    <form action="/applications/update" method="post">
        <input type="hidden" name="id" value="<%= app.getId() %>" />

        <label for="title">Job Title</label>
        <input type="text" id="title" name="title" value="<%= app.getTitle() %>" required />

        <label for="company">Company</label>
        <input type="text" id="company" name="company" value="<%= app.getCompany() %>" required />

        <label for="status">Status</label>
        <select name="status" id="status" required>
            <option value="Applied" <%= app.getStatus().equals("Applied") ? "selected" : "" %>>Applied</option>
            <option value="Interview Scheduled" <%= app.getStatus().equals("Interview Scheduled") ? "selected" : "" %>>Interview Scheduled</option>
            <option value="Offer Received" <%= app.getStatus().equals("Offer Received") ? "selected" : "" %>>Offer Received</option>
            <option value="Rejected" <%= app.getStatus().equals("Rejected") ? "selected" : "" %>>Rejected</option>
        </select>

        <label for="appliedDate">Applied Date</label>
        <input type="date" id="appliedDate" name="appliedDate" value="<%= app.getAppliedDate() %>" required />

        <button type="submit">Update Application</button>
    </form>

    <a href="/applications" class="back-link">← Back to Applications</a>
</div>

</body>
</html>
