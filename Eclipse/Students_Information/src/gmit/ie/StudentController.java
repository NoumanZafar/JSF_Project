package gmit.ie;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.exceptions.Neo4jException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

/**
 * This class is type of ManagedBean which SessionScoped as well.
 * 
 * @author Nouman
 *
 */
@ManagedBean
@SessionScoped
public class StudentController {
	/**
	 * Declare Objects of type Student_DAO,Neo4j_DAO and Student.
	 */
	private Student_DAO dao;
	private Neo4j_DAO neo_dao;
	private Student student;
	private List<Student> list;

	/**
	 * Instantiate the Objects
	 * 
	 * @throws Exception
	 */
	public StudentController() throws Exception {
		super();
		dao = new Student_DAO();
		neo_dao = new Neo4j_DAO();
		list = new ArrayList<Student>();
	}

	/**
	 * Get the list and return it.
	 * 
	 * @return List of type Students.
	 */
	public List<Student> getList() {
		return list;
	}

	/**
	 * Get the records from database and add them in the list created above.
	 * 
	 * @SQLException This throws exception of Type SQL if there is any error or
	 *               communication or any other problem relation to SQL.
	 */
	public void loadStudents() {
		try {
			list = dao.loadStudents();
		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication Problem ");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error : Can't Connect to MYSQL Database.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * AddStudent is a method which takes an Object of type Student and call it on
	 * Student_DAO where SQL query runs and add the object to Databases. This method
	 * adds the record into two different databases SQL and Neo4j. If neo4j
	 * connection is down the record in added in SQL only and the message is
	 * displayed about Ne04j Connection Failure.
	 * 
	 * @SQLException If there is any error like different input the required or
	 *               Communication error etc. then display a error message.
	 * @param s Of type Object Student.
	 * @return Navigate to the Students Page.
	 */
	public String addStudent(Student s) {
		try {
			dao.addStudent(s);
			neo_dao.addStudent_Neo(s);
		} catch (SQLIntegrityConstraintViolationException e) {
			if (e.getMessage().contains("Cannot add or update a child row")) {
				FacesMessage message = new FacesMessage("Error : Course " + s.getcID() + " Does not Exist.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			} else {
				FacesMessage message = new FacesMessage("Error : " + e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}

		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication problem");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;

		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error : " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;

		} catch (ServiceUnavailableException e) {
			FacesMessage message = new FacesMessage(
					"Error : Student " + s.getName() + " has not been added to Neo4j Database, as it is Offline.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Neo4jException e) {
			FacesMessage message = new FacesMessage("Error : " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		return "students.xhtml";
	}

	/**
	 * Set the value to the created object.
	 * 
	 * @param student of type Student Object
	 * @return Navigate to allStudentsDetails page.
	 */
	public String getStudentID(Student student) {
		this.student = student;
		return "allStudentDetails.xhtml";
	}

	/**
	 * This method calls a method from Student_DAO which returns the all details of
	 * a student.
	 */
	public void allStudentDetails() {
		try {
			/**
			 * If list is not empty. Otherwise display the message.
			 */
			if (this.student != null) {
				list = dao.allStudentDetails(this.student);
			} else {
				FacesMessage message = new FacesMessage("Error : NO STUDENT Chosen to show the Details.!");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication Problem ");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error : Can't Connect to MYSQL Database.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Delete the record from the database. which is basically an object so take the
	 * object needs to be deleted and pass it to Student_DAO to run on the SQL. and
	 * as well as to the Neo4j_DAO which runs session and delete the node from
	 * database.If In Neo4j node has any kind of relation then the process is
	 * stopped from both databases and display and message.
	 * 
	 * @param s Of type Object Student.
	 */
	public void deleteStudent(Student s) {
		try {
			neo_dao.deletStudent(s);
			dao.deleteStudent(s);
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = new FacesMessage("Error : Student " + s.getSid() + " Can't be Deleted.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication Problem ");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (SQLException e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage("Error : " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (ClientException e) {
			FacesMessage message = new FacesMessage(
					"Error : Student " + s.getSid() + " in Neo4j has Relationship whith other Node");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (ServiceUnavailableException e) {
			FacesMessage message = new FacesMessage(
					"Error : Student " + s.getName() + " can't be deleted as the Neo4j Connection is down.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Neo4jException e) {
			FacesMessage message = new FacesMessage("Error : " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
}
