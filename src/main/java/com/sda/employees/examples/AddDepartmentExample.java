package com.sda.employees.examples;
import java.sql.*;
import java.time.DayOfWeek;

public class AddDepartmentExample {
    static final String url = "jdbc:mysql://localhost/employees";
    static final String user = "root";
    static final String password = "";

    static final String insertEmp = "INSERT INTO employees" +
            "(emp_no, birth_date, first_name, last_name, gender, hire_date) " +
            "VALUES(?, ?, ?, ?, ?, ?)";

    static final String insertDept = "INSERT INTO departments" +
            "(dept_no, dept_name) VALUES(?, ?)";

    static final String insertDeptEmp = "INSERT INTO dept_emp" +
            "(dept_no, emp_no, from_date, to_date) VALUES(?, ?, ?, ?)";

    static final String insertDeptManager = "INSERT INTO dept_manager" +
            "(dept_no, emp_no, from_date, to_date) VALUES(?, ?, ?, ?)";

    public static void main(String[] args) throws SQLException {
        Date hireDate = Date.valueOf("2018-02-10");
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try{
                conn.setAutoCommit(false);
                try (PreparedStatement stmt = conn.prepareStatement(insertEmp)) {

                    stmt.setInt(1, 700000);
                    stmt.setDate(2, Date.valueOf("1999-05-06"));
                    stmt.setString(3, "John");
                    stmt.setString(4, "Smith");
                    stmt.setString(5, "M");
                    stmt.setDate(6, hireDate);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        throw new RuntimeException("Cannot add an employee");
                    }

                    stmt.setInt(1, 700001);
                    stmt.setDate(2, Date.valueOf("1999-07-06"));
                    stmt.setString(4, "Doe");
                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        throw new RuntimeException("Cannot add an employee");
                    }
                }
                try (PreparedStatement stmt = conn.prepareStatement(insertDept)) {
                    stmt.setString(1, "d256");
                    stmt.setString(2, "It");
                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        throw new RuntimeException("Cannot add a department");
                    }
                }
                try (PreparedStatement stmt = conn.prepareStatement(insertDeptEmp)) {
                    stmt.setString(1, "d256");
                    stmt.setInt(2, 700000);
                    stmt.setDate(3, hireDate);
                    stmt.setDate(4, Date.valueOf("9999-01-01"));
                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        throw new RuntimeException("Cannot add a department");
                    }
                    stmt.setInt(2, 700001);
                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        throw new RuntimeException("Cannot add a department");
                    }
                }
                try (PreparedStatement stmt = conn.prepareStatement(insertDeptManager)) {
                    stmt.setInt(2, 700001);
                    stmt.setString(1, "d256");
                    stmt.setDate(3, hireDate);
                    stmt.setDate(4, Date.valueOf("9999-01-01"));
                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        throw new RuntimeException("Cannot add a department");
                    }
                }
                conn.commit();
                conn.setAutoCommit(true);
            }catch(SQLException ex){
                ex.printStackTrace();
                conn.rollback();
            }
        }
    }
}
