package gmit.ie;

import java.util.*;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class StudentController {
	private Student_DAO dao;
	private List<Student> list;
	
	public StudentController() throws Exception {
		super();
		dao=new Student_DAO();
		list=new ArrayList<Student>();
	}

	public List<Student> getList() {
		return list;
	}
	
	public void loadStudents() throws Exception {
		list=dao.loadStudents();
	}
}
