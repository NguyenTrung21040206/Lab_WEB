<%-- 
    Document   : student-list
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
    <title>Student List - MVC</title>

    <style>
        /* (GIỮ Y NGUYÊN STYLE BẠN ĐÃ VIẾT — KHÔNG THAY ĐỔI) */

        .pagination {
            margin-top: 25px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }

        .pagination a, .pagination strong {
            padding: 8px 14px;
            border-radius: 5px;
            text-decoration: none;
            color: white;
            background: #667eea;
            font-weight: 500;
        }

        .pagination strong {
            background: #764ba2;
        }

        .pagination a:hover {
            opacity: 0.8;
        }
    </style>

</head>
<body>

    <div class="container">

        <h1>📚 Student Management System</h1>
        <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>

        <!-- Success Message -->
        <c:if test="${not empty param.message}">
            <div class="message success">✅ ${param.message}</div>
        </c:if>

        <!-- Error Message -->
        <c:if test="${not empty param.error}">
            <div class="message error">❌ ${param.error}</div>
        </c:if>

        <!-- Add New -->
        <div style="margin-bottom: 20px;">
            <a href="student?action=new" class="btn btn-primary">➕ Add New Student</a>
        </div>

        <!-- Search Form -->
        <form action="student" method="GET" style="margin-bottom:20px; display:flex; gap:10px;">
            <input type="hidden" name="action" value="search">

            <input type="text" name="keyword"
                   placeholder="Search by name or code..."
                   value="${keyword}"
                   style="padding:10px; width:250px;">

            <button type="submit" class="btn btn-primary">Search</button>

            <a href="student?action=list" class="btn btn-secondary">Clear</a>
        </form>

        <!-- Student Table -->
        <c:choose>
            <c:when test="${not empty students}">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Student Code</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Major</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>${student.id}</td>
                                <td><strong>${student.studentCode}</strong></td>
                                <td>${student.fullName}</td>
                                <td>${student.email}</td>
                                <td>${student.major}</td>

                                <td>
                                    <div class="actions">
                                        <a href="student?action=edit&id=${student.id}" class="btn btn-secondary">✏️ Edit</a>
                                        <a href="student?action=delete&id=${student.id}" 
                                           class="btn btn-danger"
                                           onclick="return confirm('Are you sure?')">
                                           🗑️ Delete
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>

                </table>
            </c:when>

            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h3>No students found</h3>
                    <p>Start by adding a new student</p>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- PAGINATION (JSTL version - NO JAVA CODE) -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">

                <!-- Previous -->
                <c:if test="${currentPage > 1}">
                    <a href="student?action=${empty keyword ? 'list' : 'search'}&page=${currentPage - 1}&keyword=${keyword}">
                        Previous
                    </a>
                </c:if>

                <!-- Numbered Pages -->
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <strong>${i}</strong>
                        </c:when>
                        <c:otherwise>
                            <a href="student?action=${empty keyword ? 'list' : 'search'}&page=${i}&keyword=${keyword}">
                                ${i}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <!-- Next -->
                <c:if test="${currentPage < totalPages}">
                    <a href="student?action=${empty keyword ? 'list' : 'search'}&page=${currentPage + 1}&keyword=${keyword}">
                        Next
                    </a>
                </c:if>

            </div>
        </c:if>

    </div>

</body>
</html>
