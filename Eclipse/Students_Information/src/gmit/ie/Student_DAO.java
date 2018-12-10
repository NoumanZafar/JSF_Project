package gmit.ie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Student_DAO {
	private DataSource mysql;
	private Connection conn;
	private PreparedStatement prest;
	private ResultSet rs;
	private Student student;
	private Courses course;
	private List<Student> list;

	public Student_DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/students";
		mysql = (DataSource) context.lookup(jndiName);
	}

	public List<Student> loadStudents() throws SQLException {
		try {
			list = new ArrayList<Student>();
			conn = mysql.getConnection();
			String query = "select * from student";
			prest = conn.prepareStatement(query);
			rs = prest.executeQuery();

			while (rs.next()) {
				student = new Student(rs.getString("sid"), rs.getString("cID"), rs.getString("name"),
						rs.getString("address"));
				list.add(student);
			}
		} finally {
			prest.close();
			conn.close();
			rs.close();
		}
		return list;
	}

	public void addStudent(Student s) throws SQLException {
		try {
			conn = mysql.getConnection();
			String query = "insert into student values(?,?,?,?)";
			prest = conn.prepareStatement(query);
			prest.setString(1, s.getSid());
			prest.setString(2, s.getcID());
			prest.setString(3, s.getName());
			prest.setString(4, s.getAddress());
			prest.executeUpdate();
		} finally {
			prest.close();
			conn.close();
		}
	}

	public List<Student> allStudentDetails(Student s) throws SQLException {
		try {
			list = new ArrayList<Student>();
			conn = mysql.getConnection();
			String query = "select s.sid,s.name,c.* from student s join course c on s.cID=c.cID where s.sid=?";
			prest = conn.prepareStatement(query);
			prest.setString(1, s.getSid());
			rs = prest.executeQuery();
			while (rs.next()) {
				course = new Courses(rs.getString("cID"), rs.getString("cName"), rs.getInt("duration"));
				student = new Student();
				student.setSid(rs.getString("sid"));
				student.setName(rs.getString("name"));
				student.setCourse(course);
				list.add(student);
			}
		} finally {
			conn.close();
			rs.close();
			prest.close();
		}
		return list;
	}

	public void deleteStudent(Student s) throws SQLException {
		try {
			conn = mysql.getConnection();
			String query = "delete from student where sid=?";
			prest = conn.prepareStatement(query);
			prest.setString(1, s.getSid());
			prest.executeUpdate();
		} finally {
			conn.close();
			prest.close();
		}
	}
}
