package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL driver
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC",
                    "root", "123456");
            studentDAO = new StudentDAO(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new": showNewForm(request, response); break;
            case "edit": showEditForm(request, response); break;
            case "delete": deleteStudent(request, response); break;
            case "search": searchStudents(request, response); break;
            default: listStudents(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "insert": insertStudent(request, response); break;
            case "update": updateStudent(request, response); break;
        }
    }

    // ========================= LIST =========================
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pageParam = request.getParameter("page");
        int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

        int recordsPerPage = 10;
        int offset = (currentPage - 1) * recordsPerPage;

        List<Student> students = studentDAO.getStudentsPaginated(recordsPerPage, offset);
        int totalRecords = studentDAO.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("students", students);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // ========================= SEARCH =========================
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        String pageParam = request.getParameter("page");
        int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

        int recordsPerPage = 10;
        int offset = (currentPage - 1) * recordsPerPage;

        List<Student> students = studentDAO.searchStudentsPaginated(keyword, recordsPerPage, offset);
        int totalRecords = studentDAO.getTotalSearchRecords(keyword);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("students", students);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // ========================= NEW / EDIT FORM =========================
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);
        request.setAttribute("student", existingStudent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // ========================= INSERT =========================
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        String error = validateForm(studentCode, fullName, email, major, true);
        if (error != null) {
            redirectWithError(response, "new", 0, error, studentCode, fullName, email, major);
            return;
        }

        Student student = new Student(studentCode, fullName, email, major);
        if (studentDAO.addStudent(student)) {
            response.sendRedirect("student?action=list&message=" + encode("Student added successfully"));
        } else {
            redirectWithError(response, "new", 0, "Failed to add student", studentCode, fullName, email, major);
        }
    }

    // ========================= UPDATE =========================
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        String error = validateForm(studentCode, fullName, email, major, false);
        if (error != null) {
            redirectWithError(response, "edit", id, error, studentCode, fullName, email, major);
            return;
        }

        Student student = new Student(id, studentCode, fullName, email, major);
        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=" + encode("Student updated successfully"));
        } else {
            redirectWithError(response, "edit", id, "Failed to update student", studentCode, fullName, email, major);
        }
    }

    // ========================= DELETE =========================
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=" + encode("Student deleted successfully"));
        } else {
            response.sendRedirect("student?action=list&error=" + encode("Failed to delete student"));
        }
    }

    // ========================= VALIDATION =========================
    private String validateForm(String studentCode, String fullName, String email, String major, boolean isNew) {
        if (studentCode == null || !studentCode.matches("[A-Z]{2}[0-9]{3,}")) {
            return "Student code must be 2 uppercase letters + 3+ digits";
        }
        if (isNew && studentDAO.existsStudentCode(studentCode)) return "Student code already exists";
        if (fullName == null || fullName.trim().isEmpty()) return "Full name is required";
        if (email == null || email.trim().isEmpty()) return "Email is required";
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) return "Invalid email format";
        if (isNew && studentDAO.existsEmail(email)) return "Email already exists";
        if (major == null || major.trim().isEmpty()) return "Major is required";
        return null;
    }

    // ========================= REDIRECT WITH ERROR =========================
    private void redirectWithError(HttpServletResponse response, String action, int id,
                                   String error, String studentCode, String fullName,
                                   String email, String major) throws IOException {
        String url = "student?action=" + action;
        if (id > 0) url += "&id=" + id;
        url += "&error=" + encode(error)
                + "&studentCode=" + encode(studentCode)
                + "&fullName=" + encode(fullName)
                + "&email=" + encode(email)
                + "&major=" + encode(major);
        response.sendRedirect(url);
    }

    private String encode(String value) {
        if (value == null) return "";
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
