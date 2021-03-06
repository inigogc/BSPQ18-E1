package es.deusto.bspq.cinema.server.jdo.DAO;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.api.jdo.JDOQuery;

import es.deusto.bspq.cinema.server.jdo.data.Employee;
import es.deusto.bspq.cinema.server.jdo.data.Film;
import es.deusto.bspq.cinema.server.jdo.data.Member;
import es.deusto.bspq.cinema.server.jdo.data.Session;


public class ManagerDAO implements IManagerDAO {
	
private PersistenceManagerFactory pmf;
	
	public ManagerDAO() {
		pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
	}
	
	private void storeObject(Object object) {
		PersistenceManager pm = pmf.getPersistenceManager();
	    Transaction tx = pm.currentTransaction();
	    try {
	       tx.begin();
	       pm.makePersistent(object);
	       tx.commit();
	    } catch (Exception ex) {
	    	System.out.println("   $ Error storing an object: " + ex.getMessage());
	    } finally {
	    	if (tx != null && tx.isActive()) {
	    		tx.rollback();
	    	}
    		pm.close();
	    }
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Film> getFilms() {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		ArrayList <Film> films = new ArrayList<Film>();
		
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
			tx.begin();			
			Query<?> q = pm.newQuery("SELECT FROM " + Film.class.getName());
			List <Film> result = (List<Film>) q.execute();
			
			System.out.println("All films retrieved.");
			
			for (int i = 0; i < result.size(); i++) {
				films.add(new Film());
				films.get(i).copyFilm(result.get(i));
			}
			
			tx.commit();			
		} catch (Exception ex) {
	    	System.out.println("   $ Error retrieving all films: " + ex.getMessage());
	    } finally {
	    	if (tx != null && tx.isActive()) {
	    		tx.rollback();
	    	}
    		pm.close(); 
	    }
	    				
		return films;
	}


	public void deleteAllFilms() {	
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		
		try {		
			tx.begin();
			JDOQuery<Film> query = (JDOQuery<Film>) pm.newQuery(Film.class);
			System.out.println(" * '" + query.deletePersistentAll() + "' films deleted from the DB.");
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning the DB: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
	}
	
	public void deleteFilm(Film film) {		
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
			
		try {		
			tx.begin();

			Query<Film> query = pm.newQuery(Film.class, "title =='"+film.getTitle()+"'");

			Collection<?> result = (Collection<?>) query.execute();

			Film f = (Film) result.iterator().next();

			query.close(result);

			pm.deletePersistent(f);
			   
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning a film: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
	}
	
	public Film getFilm(String name){
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		Film film = new Film();
	    
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
	    	tx.begin();
	    	Query <?> query = pm.newQuery("SELECT FROM " + Film.class.getName() + " WHERE title == '" + name + "'");
	    	query.setUnique(true);
	    	Film result = (Film) query.execute();
	    	film.copyFilm(result);
 	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error retrieving a film: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		 }
	   		pm.close();
	     }
		
	    return film;
	}
	
	
	
	public static void main(String[] args) {
		IManagerDAO dao= new ManagerDAO();
		
		if (args.length != 3) {
			System.out.println("Attention: arguments missing");
			System.exit(0);
		}

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		
	}

	
	public void storeFilm(Film film) {
		System.out.println("   * Storing a film: " + film.getTitle());
		 this.storeObject(film);
		
	}

	
	public void storeSession(Session session) {
		System.out.println("   * Storing a session: " + session.getRoom().getRoomNumber()+" - "+session.getDate().toString()+" "+session.getHour().toString());
		 this.storeObject(session);
	}

	
	public void storeMember(Member member) {
		System.out.println("   * Storing a member: " + member.getEmail());
		 this.storeObject(member);
		
	}

	
	public void updateFilm(Film film) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
	    Transaction tx = pm.currentTransaction();
	    
	    try {
	    	tx.begin();
	    	pm.makePersistent(film);
	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error updating a film: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		   	}
	   		pm.close();
	     }
		
	}

	
	public void updateSession(Session session) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
	    Transaction tx = pm.currentTransaction();
	    
	    try {
	    	tx.begin();
	    	pm.makePersistent(session);
	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error updating a session: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		   	}
	   		pm.close();
	     }
		
	}

	
	public void updateMember(Member member) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
	    Transaction tx = pm.currentTransaction();
	    
	    try {
	    	tx.begin();
	    	pm.makePersistent(member);
	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error updating a member: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		   	}
	   		pm.close();
	     }
		
	}


	public void deleteAllSessions() {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		
		try {		
			tx.begin();
			JDOQuery<Session> query = (JDOQuery<Session>) pm.newQuery(Session.class);
			System.out.println(" * '" + query.deletePersistentAll() + "' sessions deleted from the DB.");
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning the DB: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
		
	}

	
	public void deleteSession(Session session) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
			
		try {		
			tx.begin();

			Query<Session> query = pm.newQuery(Session.class, "hour =='"+session.getHour()+"'");

			Collection<?> result = (Collection<?>) query.execute();

			Film f = (Film) result.iterator().next();

			query.close(result);

			pm.deletePersistent(f);
			   
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning a film: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
		
	}

	
	public void deleteAllMembers() {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		
		try {		
			tx.begin();
			JDOQuery<Member> query = (JDOQuery<Member>) pm.newQuery(Member.class);
			System.out.println(" * '" + query.deletePersistentAll() + "' members deleted from the DB.");
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning the DB: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
		
	}

	
	public void deleteMember(Member member) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
			
		try {		
			tx.begin();

			Query<Member> query = pm.newQuery(Member.class, "email =='"+member.getEmail()+"'");

			Collection<?> result = (Collection<?>) query.execute();

			Member m = (Member) result.iterator().next();

			query.close(result);

			pm.deletePersistent(m);
			   
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning a member: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
		
	}


	public void storeEmployee(Employee employee) {
		System.out.println("   * Storing an employee: " + employee.getUsername());
		 this.storeObject(employee);
	}

	
	public ArrayList<Member> getMembers() {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		ArrayList <Member> members = new ArrayList<Member>();
		
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
			tx.begin();			
			Query<?> q = pm.newQuery("SELECT FROM " + Member.class.getName());
			List <Member> result = (List<Member>) q.execute();
			
			System.out.println("All members retrieved.");
			
			for (int i = 0; i < result.size(); i++) {
				members.add(new Member());
				members.get(i).copyMember(result.get(i));
			}
			
			tx.commit();			
		} catch (Exception ex) {
	    	System.out.println("   $ Error retrieving all members: " + ex.getMessage());
	    } finally {
	    	if (tx != null && tx.isActive()) {
	    		tx.rollback();
	    	}
    		pm.close(); 
	    }
	    				
		return members;
	}


	public Member getMember(String email) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		Member member = new Member();
	    
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
	    	tx.begin();
	    	Query <?> query = pm.newQuery("SELECT FROM " + Member.class.getName() + " WHERE email == '" + email + "'");
	    	query.setUnique(true);
	    	Member result = (Member) query.execute();
	    	member.copyMember(result);
 	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error retrieving a member: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		 }
	   		pm.close();
	     }
		
	    return member;
	}

	
	public ArrayList<Session> getSessions() {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		ArrayList <Session> sessions = new ArrayList<Session>();
		
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
			tx.begin();			
			Query<?> q = pm.newQuery("SELECT FROM " + Session.class.getName());
			List <Session> result = (List<Session>) q.execute();
			
			System.out.println("All sessions retrieved.");
			
			for (int i = 0; i < result.size(); i++) {
				sessions.add(new Session());
				sessions.get(i).copySession(result.get(i));
			}
			
			tx.commit();			
		} catch (Exception ex) {
	    	System.out.println("   $ Error retrieving all sessions: " + ex.getMessage());
	    } finally {
	    	if (tx != null && tx.isActive()) {
	    		tx.rollback();
	    	}
    		pm.close(); 
	    }
	    				
		return sessions;
	}

	@Override
	public Session getSession(java.sql.Date date, Time hour) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		Session session = new Session();
	    
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
	    	tx.begin();
	    	Query <?> query = pm.newQuery("SELECT FROM " + Session.class.getName() + " WHERE date == '" + date + "' AND hour=='"+hour+"'");
	    	query.setUnique(true);
	    	Session result = (Session) query.execute();
	    	session.copySession(result);
 	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error retrieving a session: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		 }
	   		pm.close();
	     }
		
	    return session;
	}

	
	public ArrayList<Employee> getEmployees() {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		ArrayList <Employee> employees = new ArrayList<Employee>();
		
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
			tx.begin();			
			Query<?> q = pm.newQuery("SELECT FROM " + Employee.class.getName());
			List <Employee> result = (List<Employee>) q.execute();
			
			System.out.println("All employees retrieved.");
			
			for (int i = 0; i < result.size(); i++) {
				employees.add(new Employee());
				employees.get(i).copyEmployee(result.get(i));
			}
			
			tx.commit();			
		} catch (Exception ex) {
	    	System.out.println("   $ Error retrieving all employees: " + ex.getMessage());
	    } finally {
	    	if (tx != null && tx.isActive()) {
	    		tx.rollback();
	    	}
    		pm.close(); 
	    }
	    				
		return employees;
	}

	
	public Employee getEmployee(String username) {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		Employee employee = new Employee();
	    
		pm.getFetchPlan().setMaxFetchDepth(3);
		
		try {
	    	tx.begin();
	    	Query <?> query = pm.newQuery("SELECT FROM " + Employee.class.getName() + " WHERE username == '" + username + "'");
	    	query.setUnique(true);
	    	Employee result = (Employee) query.execute();
	    	employee.copyEmployee(result);
 	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error retrieving an employee: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		 }
	   		pm.close();
	     }
		
	    return employee;
	}

	
	public void updateEmployee(Employee employee) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
	    Transaction tx = pm.currentTransaction();
	    
	    try {
	    	tx.begin();
	    	pm.makePersistent(employee);
	    	tx.commit();
	     } catch (Exception ex) {
	    	 System.out.println("   $ Error updating an employee: " + ex.getMessage());
	     } finally {
		   	if (tx != null && tx.isActive()) {
		   		tx.rollback();
		   	}
	   		pm.close();
	     }
	}

	
	public void deleteAllEmployees() {
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		
		try {		
			tx.begin();
			JDOQuery<Employee> query = (JDOQuery<Employee>) pm.newQuery(Employee.class);
			System.out.println(" * '" + query.deletePersistentAll() + "' employees deleted from the DB.");
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning the DB: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
		
	}

	
	public void deleteEmployee(Employee employee) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
			
		try {		
			tx.begin();

			Query<Employee> query = pm.newQuery(Employee.class, "username =='"+employee.getUsername()+"'");

			Collection<?> result = (Collection<?>) query.execute();

			Employee e = (Employee) result.iterator().next();

			query.close(result);

			pm.deletePersistent(e);
			   
			tx.commit();
		} catch (Exception ex) {
			System.out.println("   $ Error cleaning an employee: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (pm != null && !pm.isClosed()) {
				pm.close();
			}
		}
		
	}

}