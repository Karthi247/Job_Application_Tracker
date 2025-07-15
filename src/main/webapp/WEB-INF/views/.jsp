<%@ page import="java.util.List" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.ApplicationEntity" %>
<%@ page import="com.pratice.Job_Application_Tracker.Entity.TrackEntity" %>

<%
    List<ApplicationEntity> applications = (List<ApplicationEntity>) request.getAttribute("applications");
    TrackEntity track = (TrackEntity) request.getAttribute("track");
%>

<h2>Create Track Entry</h2>
<form action="save-track" method="post">
    <label for="applicationId">Select Application:</label>
    <select name="applicationId" required>
        <% for (ApplicationEntity app : applications) { %>
            <option value="<%= app.getId() %>">
                <%= app.getCompany() %> - <%= app.getTitle() %>
            </option>
        <% } %>
    </select>
    <br><br>

    <label for="interviewDate">Interview Date:</label>
    <input type="date" name="interviewDate" value="<%= track.getInterviewDate() != null ? track.getInterviewDate() : "" %>" required>
    <br><br>

    <input type="submit" value="Save Track">
</form>
