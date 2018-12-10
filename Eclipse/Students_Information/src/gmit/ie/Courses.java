package gmit.ie;

import javax.faces.bean.ManagedBean;

/**
 * Courses is a managedBean which holds the Course Attributes.
 * 
 * @author Nouman
 *
 */
@ManagedBean
public class Courses {
	/**
	 * Course Attributes.
	 */
	private String cID;
	private String cName;
	private int duration;
	private Student student;

	public Courses() {
		super();
	}

	/**
	 * 
	 * @param cID      - Course ID
	 * @param cName    - Course Name
	 * @param duration - Duration of the course.
	 */
	public Courses(String cID, String cName, int duration) {
		super();
		this.cID = cID;
		this.cName = cName;
		this.duration = duration;
	}

	/**
	 * Getters and setters for the attributes.
	 */
	public String getcID() {
		return cID;
	}

	public void setcID(String cID) {
		this.cID = cID;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}
