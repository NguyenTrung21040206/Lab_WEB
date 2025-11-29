package com.student.dao;

import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private Connection conn;

    public StudentDAO(Connection conn) {
        this.conn = conn;
    }

    // ===== Get total records =====
    public int getTotalRecords() {
        String sql = "SELECT COUNT(*) FROM students";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // ===== Pagination =====
    public List<Student> getStudentsPaginated(int limit, int offset) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("student_code"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("major")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ===== Search + Pagination =====
    public List<Student> searchStudentsPaginated(String keyword, int limit, int offset) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE full_name LIKE ? OR student_code LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("student_code"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("major")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ===== Total records for search =====
    public int getTotalSearchRecords(String keyword) {
        String sql = "SELECT COUNT(*) FROM students WHERE full_name LIKE ? OR student_code LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // ===== Get by ID =====
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("student_code"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("major")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ===== Add student =====
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getMajor());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ===== Update student =====
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_code=?, full_name=?, email=?, major=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getMajor());
            stmt.setInt(5, student.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ===== Delete student =====
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ===== Check student code exists =====
    public boolean existsStudentCode(String studentCode) {
        String sql = "SELECT 1 FROM students WHERE student_code=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ===== Check email exists =====
    public boolean existsEmail(String email) {
        String sql = "SELECT 1 FROM students WHERE email=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
