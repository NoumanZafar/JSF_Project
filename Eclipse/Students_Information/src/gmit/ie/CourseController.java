package gmit.ie;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

/**
 * courseController is a ManagedBean which is SessionScoped the means HTTP
 * requests scope is there until the session is not complete.
 * 
 * @author Nouman
 *
 */
@ManagedBean
@SessionScoped
public class CourseController {
	/**
	 * Get the Objects of Course_DAO and Courses. Make a list to add the results.
	 */
	private Course_DAO dao;
	private List<Courses> list;
	private Courses course;

	/**
	 * In constructor Instantiate the DAO and List Objects.
	 * 
	 * @throws Exception
	 */
	public CourseController() throws Exception {
		super();
		dao = new Course_DAO();
		list = new ArrayList<Courses>();
	}

	/**
	 * Get the list.
	 * 
	 * @return List
	 */
	public List<Courses> getList() {
		return list;
	}

	/**
	 * Get the records from database and add them in the list created above.
	 * 
	 * @SQLException This throws exception of Type SQL if there is any error or
	 *               communication or any other problem relation to SQL.
	 */
	public void loadCourses() {
		try {
			list = dao.loadCourses();
		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication Problem ");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error : Can't Connect to MYSQL Database.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * 
	 * @param course of type Object Courses.
	 * @return Navigate to the courseStudent Page.
	 */
	public String getID(Courses course) {
		this.course = course;
		return "courseStudent.xhtml";
	}

	/**
	 * This method calls another method from Course_DAO class and keep the results
	 * in list.
	 * 
	 * @SQLException This throws exception of Type SQL if there is any error or
	 *               communication or any other problem relation to SQL.
	 */
	public void courseStudentDetails() {
		try {
			/**
			 * If list is not empty the call the method from Course_DAO otherwise display
			 * the User readable message.
			 */
			if (this.course != null) {
				list = dao.courseStudentDetails(this.course);
			} else {
				FacesMessage message = new FacesMessage("Error : NO Course Chosen to show the Details...!");
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
	 * object needs to be deleted and pass it to Courses_DAO to run on the SQL
	 * 
	 * @param c Of type Object Courses.
	 */
	public void deleteCourse(Courses c) {
		try {
			dao.deleteCourse(c);
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = new FacesMessage(
					"Error : Course " + c.getcID() + " Can't be Deleted as there are Student Associated.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication Problem ");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (SQLException e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage("Error : " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Add a course take an Object of type Course and call it on Courses_DAO where
	 * SQL query runs and add the object to Databases.
	 * 
	 * @SQLException If there is any error like different input the required or
	 *               Communication error etc. then display a error message.
	 * @param c Of type Object Courses.
	 * @return Navigate to the Courses Page.
	 */
	public String addCourse(Courses c) {
		try {
			dao.addCourse(c);
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = new FacesMessage("Error : Course " + c.getcID() + " Already Exists");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;

		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error : Communication problem");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;

		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error : " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		return "courses.xhtml";
	}
}
