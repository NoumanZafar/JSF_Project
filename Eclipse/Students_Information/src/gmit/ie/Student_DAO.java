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
	/**
	 * Create the Variables to connect to database and to store the data coming back
	 * from the database.
	 */
	private DataSource mysql;
	private Connection conn;
	private PreparedStatement prest;
	private ResultSet rs;
	private Student student;
	private Courses course;
	private List<Student> list;

	/**
	 * In the Constructor build the SQL Database connection.
	 * 
	 * @throws Exception
	 */
	public Student_DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/students";
		mysql = (DataSource) context.lookup(jndiName);
	}

	/**
	 * This method brings the records of students from SQL database and store them
	 * in the list.
	 * 
	 * @return List with the Students Records.
	 * @throws SQLException
	 */
	public List<Student> loadStudents() throws SQLException {
		list = new ArrayList<Student>();
		conn = mysql.getConnection();
		String query = "select * from student";
		prest = conn.prepareStatement(query);
		rs = prest.executeQuery();

		/**
		 * Iterate till the last record in the database and add the data in the list of
		 * type Students.
		 */
		while (rs.next()) {
			student = new Student(
					rs.getString("sid"),
					rs.getString("cID"), 
					rs.getString("name"),
					rs.getString("address"));
			list.add(student);
		}

		/**
		 * Close the Connection,PreparedStatement and ResultSet. And return the list.
		 */
		prest.close();
		conn.close();
		rs.close();
		return list;
	}

	/**
	 * Take a student object and add that into database.
	 * 
	 * @param s Of type Student
	 * @throws SQLException
	 */
	public void addStudent(Student s) throws SQLException {
		conn = mysql.getConnection();
		String query = "insert into student values(?,?,?,?)";
		prest = conn.prepareStatement(query);
		prest.setString(1, s.getSid());
		prest.setString(2, s.getcID());
		prest.setString(3, s.getName());
		prest.setString(4, s.getAddress());
		prest.executeUpdate();
		prest.close();
		conn.close();
	}

	/**
	 * This method get the student and display ALL details of that student in new
	 * page.
	 * 
	 * @param s Of type Student.
	 * @return list with all the details of the student.
	 * @throws SQLException
	 */
	public List<Student> allStudentDetails(Student s) throws SQLException {
		list = new ArrayList<Student>();
		conn = mysql.getConnection();
		String query = "select s.sid,s.name,c.* from student s join course c on s.cID=c.cID where s.sid=?";
		prest = conn.prepareStatement(query);
		prest.setString(1, s.getSid());
		rs = prest.executeQuery();

		/**
		 * Iterate over the database table.
		 */
		while (rs.next()) {
			course = new Courses(
					rs.getString("cID"),
					rs.getString("cName"),
					rs.getInt("duration"));
			student = new Student();
			student.setSid(rs.getString("sid"));
			student.setName(rs.getString("name"));
			student.setCourse(course);
			list.add(student);
		}

		/**
		 * Close the Connection,PreparedStatement and ResultSet. And return the list.
		 */
		conn.close();
		rs.close();
		prest.close();
		return list;
	}

	/**
	 * Delete a student from database.
	 * 
	 * @param s of type Student.
	 * @throws SQLException
	 */
	public void deleteStudent(Student s) throws SQLException {
		conn = mysql.getConnection();
		String query = "delete from student where sid=?";
		prest = conn.prepareStatement(query);
		prest.setString(1, s.getSid());
		prest.executeUpdate();
		conn.close();
		prest.close();
	}
}