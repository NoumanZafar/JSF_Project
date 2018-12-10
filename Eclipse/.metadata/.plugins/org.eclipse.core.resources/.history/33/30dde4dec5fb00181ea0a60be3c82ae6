package gmit.ie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Student_DAO {
	private DataSource mysql;
	private Connection conn;
	private PreparedStatement prest;
	private ResultSet rs;
	private Student student;
	private List<Student> list;

	public Student_DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/students";
		mysql = (DataSource) context.lookup(jndiName);
	}

	public List<Student> loadStudents() throws Exception {
		list = new ArrayList<Student>();
		conn = mysql.getConnection();
		String query = "select * from student";
		prest = conn.prepareStatement(query);
		rs = prest.executeQuery();

		while (rs.next()) {
			student = new Student(
					rs.getString("sid"),
					rs.getString("cID"),
					rs.getString("name"),
					rs.getString("address"));
			
			list.add(student);
		}
		prest.close();
		conn.close();
		rs.close();
		return list;
	}
}
