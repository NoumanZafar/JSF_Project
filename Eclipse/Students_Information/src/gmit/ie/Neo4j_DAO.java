package gmit.ie;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.exceptions.Neo4jException;

public class Neo4j_DAO {
	private Driver driver;
	private Session session;

	public Neo4j_DAO() throws Exception{
		super();
	}

	public void addStudent_Neo(Student s) throws Neo4jException {
		driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jdb"));
		session = driver.session();
		session.writeTransaction(new TransactionWork<String>() {
			@Override
			public String execute(Transaction tx) {
				tx.run("create (:STUDENT{name:{name},address:{address}})",
						parameters("name", s.getName(), "address", s.getAddress()));
				return null;
			}
		});
	}

	public void deletStudent(Student s) throws Neo4jException {
		driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jdb"));
		session = driver.session();
		session.writeTransaction(new TransactionWork<String>() {
			@Override
			public String execute(Transaction tx) {
				tx.run("match (n:STUDENT) where n.name={name} and n.address={address} delete n",
						parameters("name", s.getName(), "address", s.getAddress()));
				return null;
			}
		});
	}
}