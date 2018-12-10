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
	/**
	 * Driver and Session to connect to Neo4j Database.
	 */
	private Driver driver;
	private Session session;

	public Neo4j_DAO() throws Exception {
		super();
	}

	/**
	 * Add the nodes of type :STUDENT in the database where all nodes has name and
	 * address attribute.This method is a Transaction Function.
	 * 
	 * @User neo4j to connect to database.
	 * @Password neo4jdb is the password.
	 * 
	 * @param s Of type Student.
	 * @throws Neo4jException
	 */
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

	/**
	 * This method take an Student Object and delete the related Node from database.
	 * 
	 * @param s Of type Student.
	 * @throws Neo4jException
	 */
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
