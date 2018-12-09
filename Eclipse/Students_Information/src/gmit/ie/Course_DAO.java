package gmit.ie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Course_DAO {
	private DataSource mysql;
	private Connection conn;
	private PreparedStatement prest;
	private ResultSet rs;
	private Courses course;
	private Student student;
	private List<Courses> list;
	
	public Course_DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/students";
		mysql = (DataSource) context.lookup(jndiName);
	}
	
	public List<Courses> loadCourses() throws SQLException {
		list=new ArrayList<Courses>();
		conn=mysql.getConnection();
		String query="select * from course";
		prest=conn.prepareStatement(query);
		rs=prest.executeQuery();
		
		while(rs.next()) {
			course=new Courses(
					rs.getString("cID"),
					rs.getString("cName"),
					rs.getInt("duration"));
			list.add(course);
		}
		conn.close();
		prest.close();
		rs.close();
		return list;
	}

	public List<Courses> courseStudentDetails(Courses c) throws SQLException {
		list=new ArrayList<Courses>();
		conn=mysql.getConnection();
		String query=" select c.*,s.name,s.address from course c join student s on c.cID=s.cID where c.cID=?";
		prest=conn.prepareStatement(query);
		prest.setString(1, c.getcID());
		rs=prest.executeQuery();
		while(rs.next()) {
			student=new Student();
			student.setName(rs.getString("name")); 
			student.setAddress(rs.getString("address"));
			course=new Courses(
					rs.getString("cID"),
					rs.getString("cName"),
					rs.getInt("duration"));
			course.setStudent(student);
			list.add(course);
		}
		conn.close();
		prest.close();
		rs.close();
		return list;
	}
	
	public void deleteCourse(Courses c) throws SQLException {
		conn=mysql.getConnection();
		String query="delete from course where cID=?";
		prest=conn.prepareStatement(query);
		prest.setString(1, c.getcID());
		prest.executeUpdate();
		conn.close();
		prest.close();
	}
	
	public void addCourse(Courses c) throws SQLException {
		conn=mysql.getConnection();
		String query="insert into course values(?,?,?)";
		prest=conn.prepareStatement(query);
		prest.setString(1, c.getcID());
		prest.setString(2, c.getcName());
		prest.setInt(3, c.getDuration());
		prest.executeUpdate();
		
		prest.close();
		conn.close();
	}
}