<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Track</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background-color: #f6f1ff;
        }

        .container {
            max-width: 700px;
            margin: 50px auto;
            background-color: #fff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        h2 {
            text-align: center;
            color: #2d3436;
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
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            margin-bottom: 20px;
            font-size: 14px;
            box-sizing: border-box;
        }

        textarea {
            resize: vertical;
        }

        .btn-group {
            text-align: center;
        }

        button {
            padding: 10px 20px;
            background-color: #6c5ce7;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            margin: 0 10px;
        }

        button:hover {
            background-color: #5a4bd1;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Add Track Info</h2>

    <form method="post" action="add-track">
        <label>Title:</label>
        <input type="text" name="title" required>

        <label>Status:</label>
        <input type="text" name="status" required>

        <label>Company:</label>
        <input type="text" name="company" required>

        <label>Position:</label>
        <input type="text" name="position">

        <label>Job Title:</label>
        <input type="text" name="jobTitle">

        <label>Progress:</label>
        <input type="text" name="progress">

        <label>Stage:</label>
        <input type="text" name="stage">

        <label>Notes:</label>
        <textarea name="notes" rows="4"></textarea>

        <label>Interview Date:</label>
        <input type="date" name="interviewDate" required>

        <div class="btn-group">
            <button type="submit">Save</button>
            <button type="button" onclick="window.location.href='track-info'">Back</button>
        </div>
    </form>
</div>
</body>
</html>
