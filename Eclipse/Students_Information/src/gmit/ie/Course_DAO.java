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
	/**
	 * Create the Variables to connect to database and to store the data coming back
	 * from the database.
	 */
	private DataSource mysql;
	private Connection conn;
	private PreparedStatement prest;
	private ResultSet rs;
	private Courses course;
	private Student student;
	private List<Courses> list;

	/**
	 * In the Constructor build the SQL Database connection.
	 * 
	 * @throws Exception
	 */
	public Course_DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/students";
		mysql = (DataSource) context.lookup(jndiName);
	}

	/**
	 * This method fetch the Courses data from the database. And this method is
	 * called whenever the page is loaded by the user.
	 * 
	 * @return The list of records from database.
	 * @throws SQLException
	 */
	public List<Courses> loadCourses() throws SQLException {
		list = new ArrayList<Courses>();
		conn = mysql.getConnection();
		String query = "select * from course";
		prest = conn.prepareStatement(query);
		rs = prest.executeQuery();

		/**
		 * Run the loop until the last record in the database.
		 */
		while (rs.next()) {
			course = new Courses(
					rs.getString("cID"), 
					rs.getString("cName"),
					rs.getInt("duration"));
			list.add(course);
		}

		/**
		 * Close the connection,PreparedStatement and ResultSet and return the List.
		 */
		conn.close();
		prest.close();
		rs.close();
		return list;
	}

	/**
	 * This method return the details of the students belongs to the chosen course.
	 * 
	 * @param c Is a type of Object Courses
	 * @return List of Records
	 * @throws SQLException
	 */
	public List<Courses> courseStudentDetails(Courses c) throws SQLException {
		list = new ArrayList<Courses>();
		conn = mysql.getConnection();
		String query = " select c.*,s.name,s.address from course c join student s on c.cID=s.cID where c.cID=?";
		prest = conn.prepareStatement(query);
		prest.setString(1, c.getcID());
		rs = prest.executeQuery();
		while (rs.next()) {
			student = new Student();
			student.setName(rs.getString("name"));
			student.setAddress(rs.getString("address"));
			course = new Courses(
					rs.getString("cID"), 
					rs.getString("cName"), 
					rs.getInt("duration"));
			course.setStudent(student);
			list.add(course);
		}

		/**
		 * Close the connection,PreparedStatement and ResultSet and return the List.
		 */
		conn.close();
		prest.close();
		rs.close();
		return list;
	}

	/**
	 * Delete the chosen course and refresh the page.
	 * 
	 * @param c Is a type of Object Courses.
	 * @throws SQLException
	 */
	public void deleteCourse(Courses c) throws SQLException {
		conn = mysql.getConnection();
		String query = "delete from course where cID=?";
		prest = conn.prepareStatement(query);
		prest.setString(1, c.getcID());
		prest.executeUpdate();

		/**
		 * Close the Connection and PreparedStatement.
		 */
		conn.close();
		prest.close();
	}

	/**
	 * Get the details from TextFields and add a new Course in Database.
	 * 
	 * @param c Is a type of Object Courses
	 * @throws SQLException
	 */
	public void addCourse(Courses c) throws SQLException {
		conn = mysql.getConnection();
		String query = "insert into course values(?,?,?)";
		prest = conn.prepareStatement(query);
		prest.setString(1, c.getcID());
		prest.setString(2, c.getcName());
		prest.setInt(3, c.getDuration());
		prest.executeUpdate();

		/**
		 * Close the Connection and PreparedStatement.
		 */
		prest.close();
		conn.close();
	}
}
