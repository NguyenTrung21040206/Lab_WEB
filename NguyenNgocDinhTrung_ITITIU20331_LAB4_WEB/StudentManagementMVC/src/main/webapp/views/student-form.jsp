<%-- 
    Document   : student-form
    Created on : Nov 15, 2025
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${student != null}">Edit Student</c:when>
            <c:otherwise>Add New Student</c:otherwise>
        </c:choose>
    </title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 600px;
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
            text-align: center;
        }
        .form-group { margin-bottom: 25px; }
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
            font-size: 14px;
        }
        input[type="text"], input[type="email"], select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        input:focus, select:focus { outline: none; border-color: #667eea; }
        .required { color: #dc3545; }
        .button-group { display: flex; gap: 15px; margin-top: 30px; }
        .btn {
            flex: 1;
            padding: 14px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            text-align: center;
            display: inline-block;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover { background-color: #5a6268; }
        .info-text { font-size: 12px; color: #666; margin-top: 5px; }
        .alert { padding: 15px; margin-bottom: 20px; border-radius: 5px; }
        .alert-error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    </style>
</head>

<body>
    <div class="container">
        <h1>
            <c:choose>
                <c:when test="${student != null}">✏️ Edit Student</c:when>
                <c:otherwise>➕ Add New Student</c:otherwise>
            </c:choose>
        </h1>

        <!-- Hiển thị lỗi hoặc thông báo thành công -->
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">❌ ${param.error}</div>
        </c:if>
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">✅ ${param.message}</div>
        </c:if>

        <form action="student" method="POST">
            <!-- Hidden field for action -->
            <input type="hidden" name="action" value="${student != null ? 'update' : 'insert'}">
            <!-- Hidden field for ID (update) -->
            <c:if test="${student != null}">
                <input type="hidden" name="id" value="${student.id}">
            </c:if>

            <!-- Student Code -->
            <div class="form-group">
                <label for="studentCode">Student Code <span class="required">*</span></label>
                <input type="text" id="studentCode" name="studentCode"
                       value="${param.studentCode != null ? param.studentCode : (student != null ? student.studentCode : '')}"
                       ${student != null ? 'readonly' : 'required'}
                       placeholder="e.g., SV001, IT123">
                <p class="info-text">Format: 2 letters + 3+ digits</p>
            </div>

            <!-- Full Name -->
            <div class="form-group">
                <label for="fullName">Full Name <span class="required">*</span></label>
                <input type="text" id="fullName" name="fullName"
                       value="${param.fullName != null ? param.fullName : (student != null ? student.fullName : '')}"
                       required
                       placeholder="Enter full name">
            </div>

            <!-- Email -->
            <div class="form-group">
                <label for="email">Email <span class="required">*</span></label>
                <input type="email" id="email" name="email"
                       value="${param.email != null ? param.email : (student != null ? student.email : '')}"
                       required
                       placeholder="student@example.com">
            </div>

            <!-- Major -->
            <div class="form-group">
                <label for="major">Major <span class="required">*</span></label>
                <select id="major" name="major" required>
                    <option value="">-- Select Major --</option>
                    <option value="Computer Science"
                        ${ (param.major == 'Computer Science') || (student.major == 'Computer Science' && param.major == null) ? 'selected' : '' }>
                        Computer Science
                    </option>
                    <option value="Information Technology"
                        ${ (param.major == 'Information Technology') || (student.major == 'Information Technology' && param.major == null) ? 'selected' : '' }>
                        Information Technology
                    </option>
                    <option value="Software Engineering"
                        ${ (param.major == 'Software Engineering') || (student.major == 'Software Engineering' && param.major == null) ? 'selected' : '' }>
                        Software Engineering
                    </option>
                    <option value="Business Administration"
                        ${ (param.major == 'Business Administration') || (student.major == 'Business Administration' && param.major == null) ? 'selected' : '' }>
                        Business Administration
                    </option>
                </select>
            </div>

            <!-- Buttons -->
            <div class="button-group">
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${student != null}">💾 Update Student</c:when>
                        <c:otherwise>➕ Add Student</c:otherwise>
                    </c:choose>
                </button>
                <a href="student?action=list" class="btn btn-secondary">❌ Cancel</a>
            </div>
        </form>
    </div>
                        <script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    form.addEventListener('submit', function(e) {
        const studentCode = document.getElementById('studentCode').value.trim();
        const fullName = document.getElementById('fullName').value.trim();
        const email = document.getElementById('email').value.trim();
        const major = document.getElementById('major').value;

        const codeRegex = /^[A-Z]{2}[0-9]{3,}$/;
        const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;

        let errors = [];

        if (!codeRegex.test(studentCode)) {
            errors.push("Student code must be 2 uppercase letters + 3+ digits (e.g., SV001).");
        }
        if (fullName === "") {
            errors.push("Full Name is required.");
        }
        if (!emailRegex.test(email)) {
            errors.push("Invalid email format.");
        }
        if (major === "") {
            errors.push("Please select a Major.");
        }

        if (errors.length > 0) {
            e.preventDefault(); // Ngăn form submit
            alert(errors.join("\n"));
            return false;
        }
    });
});
</script>
</body>
</html>
