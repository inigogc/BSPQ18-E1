package es.deusto.bspq.cinema.server.jdo.DAO;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import es.deusto.bspq.cinema.server.jdo.data.Employee;
import es.deusto.bspq.cinema.server.jdo.data.Film;
import es.deusto.bspq.cinema.server.jdo.data.Member;
import es.deusto.bspq.cinema.server.jdo.data.Session;

public interface IManagerDAO {
	
	public void storeFilm(Film film);
	public void storeSession(Session session);
	public void storeMember(Member member);
	public void storeEmployee(Employee employee);
	
	public ArrayList<Film> getFilms();
	public Film getFilm (String name);
	public ArrayList<Member> getMembers();
	public Member getMember (String email);
	public ArrayList<Session> getSessions();
	public Session getSession (Date date, Time hour);
	public ArrayList<Employee> getEmployees();
	public Employee getEmployee (String username);
	
	
	public void updateFilm(Film film);
	public void updateSession(Session session);
	public void updateMember(Member member);
	public void updateEmployee(Employee employee);
	
	public void deleteAllFilms();
	public void deleteFilm(Film film);
	public void deleteAllSessions();
	public void deleteSession(Session session);
	public void deleteAllMembers();
	public void deleteMember(Member member);
	public void deleteAllEmployees();
	public void deleteEmployee(Employee employee);
	
}
