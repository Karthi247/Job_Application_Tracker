<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Application Form</title></head>
<body>
    <h2>${application.id == null ? "Add" : "Edit"} Application</h2>
    <form action="/applications/save" method="post">
        <input type="hidden" name="id" value="${application.id}" />
        <label>Title:</label><input type="text" name="title" value="${application.title}" required /><br>
        <label>Company:</label><input type="text" name="company" value="${application.company}" required /><br>
        <label>Status:</label>
        <select name="status">
            <option value="Applied" ${application.status == 'Applied' ? "selected" : ""}>Applied</option>
            <option value="Interviewing" ${application.status == 'Interviewing' ? "selected" : ""}>Interviewing</option>
            <option value="Offer" ${application.status == 'Offer' ? "selected" : ""}>Offer</option>
            <option value="Rejected" ${application.status == 'Rejected' ? "selected" : ""}>Rejected</option>
        </select><br>
        <button type="submit">Save</button>
    </form>
</body>

<style>
    body {
        margin: 0;
        font-family: 'Segoe UI', sans-serif;
        background-color: #f6f1ff;
        padding: 40px;
    }

    h2 {
        text-align: center;
        color: #6c5ce7;
        font-size: 28px;
        margin-bottom: 30px;
    }

    form {
        max-width: 500px;
        margin: 0 auto;
        background-color: #ffffff;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    label {
        display: block;
        margin-bottom: 10px;
        font-weight: 600;
        color: #2d3436;
    }

    input[type="text"], select {
        width: 100%;
        padding: 12px;
        border: 1px solid #ccc;
        border-radius: 6px;
        margin-bottom: 20px;
        font-size: 15px;
    }

    button {
        width: 100%;
        background-color: #6c5ce7;
        color: white;
        padding: 12px;
        border: none;
        border-radius: 6px;
        font-size: 16px;
        cursor: pointer;
    }

    button:hover {
        background-color: #5a4bdc;
    }
</style>

</html>
