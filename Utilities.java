package plu.bikecoop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import plu.bikecoop.Sanitizer;

/**
 * This class provides methods to update, view, and delete data in the Bike Inventory database.
 * @author Egan Dunning
 *
 */
public class Utilities {
	
	private Connection conn = null;
	private boolean loggedIn = false;
	private String user = "";
	
	public boolean isLoggedIn(){return loggedIn;}
	
	public String getUser(){return user;}
	
	public Connection getConn(){return conn;}
	
	public void setConn(Connection c){conn = c;}
	
	/**
	 * Open the database with set username and password
	 */
	public void openDB(){
		loggedIn = false;
		user = "";
		// load the mySQL JDBC driver
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			System.out.println("Unable to load Driver");
		}
		
		// connect to the database
		
		String url = "";
		String username = "";
		String password = "";
		
		
		try{
			conn = DriverManager.getConnection(url, username, password);
		}catch(SQLException e){
			System.out.println("Error connecting to the database.\n"+e.toString());
		}
	}//openDB
	
	/**
	 * User may login with login information contained in the user table.
	 * @param password of user.
	 * @param username of user.
	 * @return 1 given successful login, 0 if unsuccessful
	 */
	public int login(String password, String username){
		String sql = "select name "
				+ "from user "
				+ "where password=password('"+password+"') and username=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, username);
			ResultSet r = p.executeQuery();
			password=null;
			r.next(); // move pointer to first element in result set
			user = r.getString(1);
			loggedIn = true;
		}catch(SQLException e){
			password=null;
			System.out.println("login failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
		return 1;
	}//login
	
	/**
	 * Log out user and close the database.
	 */
	public void closeDB(){
		loggedIn = false;
		user = "";
		try{
			// check if database is already closed
			if(conn != null)
				// close database
				conn.close();
			conn = null;
		}catch(SQLException e){
			System.out.println("Failed to close the database.\n"+e.toString());
		}
	}//closeDB

////report operations
	
	/**
	 * Provides a list of bicycles listed by brand, model, price, serial number.
	 * Used to look up serial numbers more quickly, used as a reference on some
	 * pages.
	 * @return ResultSet containing (brand, model, price, serial_num) of every bicycle in the database.
	 */
	public ResultSet serialReference(){
		String sql = "select brand, model, price, serial_num "
				+ "from bicycle;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("serialReference() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//serialReference
	
	/**
	 * Provides a list of bicycles listed by brand, model, price, serial number.
	 * Used to look up serial numbers more quickly, used as a reference on some
	 * pages.
	 * @return ResultSet containing (brand, model, price, serial_num) of every bicycle in the database.
	 */
	public ResultSet serialArchivedReference(){
		String sql = "select brand, model, price, serial_num "
				+ "from bicycle_archive;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("serialArchivedReference() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//serialArchivedReference
	
	/**
	 * 
	 * @return ResultSet containing (type, brand, model, serial_num, notes, price) of every bicycle in the database.
	 */
	public ResultSet getBicycles(){
		String sql = "select * "
				+ "from bicycle;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getBicycles() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getBicycles
	
	public ResultSet getArchivedBicycles(){
		String sql = "select * "
				+ "from bicycle_archive;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getBicycles() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getArchivedBicycles
	
	/**
	 * Checks to see if the given first name, last name pair is present in the
	 * renter table. Used to check if a person has information in the renter
	 * table.
	 * @param fname of potential renter.
	 * @param lname of potential renter.
	 * @return true if (fname, lname) is a key for a tuple in the renter table.
	 */
	public boolean isInRenter(String fname, String lname){
		String sql = "select fname, lname "
				+ "from renter "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, fname);
			p.setString(2, lname);
			ResultSet r = p.executeQuery();
			r.next();
			return r.getString(1).equals(fname) && r.getString(2).equals(lname);
		}catch(SQLException e){
			System.out.println("isInRenter("+fname+", "+lname+") Query failed. sql: "+sql);
			e.printStackTrace();
			return false;
		}
	}//isInRenter
	
	/**
	 * 
	 * @return ResultSet containing (email, phone, plu id, address, first name, last name) in that order.
	 */
	public ResultSet getRenters(){
		String sql = "select * "
				+ "from renter;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRenters() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRenters
	
	/**
	 * 
	 * @param fname
	 * @param lname
	 * @return ResultSet containing (email, phone, plu_id, address, first name, last name) in that order of renter with fname, lname.
	 */
	public ResultSet getRenter(String fname, String lname){
		String sql = "select * "
				+ "from renter "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, fname);
			p.setString(2, lname);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRenter(String fname, String lname) Query failed. sql : "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRenter
	
	/**
	 * 
	 * @param serial
	 * @return ResultSet containing (type, brand, model, serial_num, notes, price) of the bicycle with the given serial number.
	 */
	public ResultSet getBicycleInfo(String serial){
		String sql = "select * "
				+ "from bicycle "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, serial);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getBikeInfo(String serial) Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getBikeInfo
	
	/**
	 * 
	 * @param serial
	 * @return ResultSet containing (type, brand, model, serial_num, notes, price, add_date) of the bicycle with the given serial number.
	 */
	public ResultSet getArchivedBicycleInfo(String serial){
		String sql = "select * "
				+ "from bicycle_archive "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, serial);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getArchivedBikeInfo(String serial) Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getArchivedBikeInfo
	
	/**
	 * 
	 * @return ResultSet containing (checkout_date, term, email, fname, lname, brand, model, paid, helmet, key_num, serial_num, details) of each unique rental in the database.
	 */
	public ResultSet getRentals(){
		String sql = "select checkout_date, term, email, fname, lname, brand, model, paid, helmet, key_num, serial_num, details "
				+ "from bicycle, renter, checked_out_to "
				+ "where bike_num=serial_num and renter_fname=fname and renter_lname=lname;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRentals() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRentals
	
	/**
	 * @param date
	 * @param term
	 * @param email
	 * @param fname
	 * @param lname
	 * @param serial
	 * @return ResultSet containing (checkout_date, term, email, fname, lname, brand, model, paid, helmet, key_num, serial_num, details) of specified rental.
	 */
	public ResultSet getRental(String date, String term, String fname, String lname, String serial){
		String sql = "select checkout_date, term, email, fname, lname, brand, model, paid, helmet, key_num, serial_num, details "
				+ "from bicycle, renter, checked_out_to "
				+ "where checkout_date=? and term=? and renter_fname=? and renter_lname=? and serial_num=? and bike_num=serial_num and renter_fname=fname and renter_lname=lname;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, date);
			p.setString(2, term);
			p.setString(3, fname);
			p.setString(4, lname);
			p.setString(4, serial);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRentals() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRentals
	
	/**
	 * Retrieves rental information for a given rental. Parameters compose the
	 * primary key for the checked_out_to table.
	 * @param date of rental.
	 * @param term of rental.
	 * @param fname first name of renter.
	 * @param lname last name of renter.
	 * @return ResultSet containing rental information and bicycle information.
	 */
	public ResultSet getRentalInfo(String year, String term, String fname, String lname){
		
		year = Sanitizer.cleanSql(year);
		
		String sql = "select paid, email, brand, model, type, phone, key_num, helmet, details "
				+ "from checked_out_to, renter, bicycle "
				+ "where bike_num=serial_num and renter_fname=fname and renter_lname=lname and checkout_date like '"+year+"%' and term=? and fname=? and lname=?; ";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, term);
			p.setString(2, fname);
			p.setString(3, lname);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRentalInfo(String year, String term, String fname, String lname) Query failed. sql:"+sql);
			e.printStackTrace();
			return null;
		}
	}//getRentalInfo
	
	/**
	 * Retrieves all repair information for a given brand and model of bicycle.
	 * Since (brand, model) is not unique, this query may return repair
	 * information for more than one bicycle. 
	 * @param brand of bicycle.
	 * @param model of bicycle.
	 * @return ResultSet containing all repair information for a given brand
	 * and model of bicycle. Repair information includes date repaired and
	 * description of repair.
	 */
	public ResultSet getRepairInfo(String brand, String model){
		String sql = "select repair_date, description "
				+ "from repair_history, bicycle "
				+ "where bike_repaired=serial_num and brand=? and model=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, brand);
			p.setString(2, model);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRepairInfo(String brand, String model) Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRepairInfo
	
	/**
	 * Retrieves all repair information for a specific bicycle. Nearly all
	 * bicycles have a serial number, we may assume all serial numbers are
	 * unique.
	 * @param serial number of bicycle.
	 * @return ResultSet containing all repair information for a bicycle with
	 * a given serial number. Repair information includes date repaired and
	 * description of repair.
	 */
	public ResultSet getRepairInfo(String serial){
		String sql = "select repair_date, description "
				+ "from repair_history "
				+ "where bike_repaired=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, serial);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRepairInfo(String serial) Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRepairInfo
	
	/**
	 * Retrieves all repair information for a bicycle which was rented during a
	 * given year and term by a given renter. Since a bicycle may not be
	 * checked out by more than one person at the same time, the returned
	 * repair history belongs to one bicycle.
	 * @param fname first name of renter.
	 * @param lname last name of renter.
	 * @param year of rental.
	 * @param term of rental.
	 * @return ResultSet containing all repair information for a bicycle that
	 * was checked out by a given renter at a given time. Repair information
	 * includes date repaired and description of repair.
	 */
	public ResultSet getRepairInfo(String fname, String lname, String year, String term){
		
		
		
		String sql = "select repair_date, description "
				+ "from repair_history, checked_out_to "
				+ "where bike_num=bike_repaired and renter_fname=? and renter_lname=? and checkout_date like '"+year+"%' and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, fname);
			p.setString(2, lname);
			p.setString(3, term);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("getRepairInfo(String fname, String lname, String year, String term) Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//getRepairInfo
	
	/**
	 * Retrieves a list of all renters who have not yet paid for their bicycle
	 * rental.
	 * @return First and last name of all renters who have not paid for a
	 * rental, and the date and term in which the rental took place.
	 */
	public ResultSet unpaidRental(){
		String sql = "select renter_fname, renter_lname, checkout_date, term "
				+ "from checked_out_to "
				+ "where paid=false;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("unpaidRental() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//unpaidRental
	
	/**
	 * Retrieves a list of bicycles checked out by a given renter.
	 * @param fname first name of renter.
	 * @param lname last name of renter.
	 * @return ResultSet containing the brand, model, type and notes
	 * corresponding to a bike renter by a specific renter.
	 */
	public ResultSet bikesCheckedOutBy(String fname, String lname){
		String sql = "select brand, model, type, notes "
				+ "from checked_out_to, bicycle "
				+ "where serial_num=bike_num and renter_fname=? and renter_lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, fname);
			p.setString(2, lname);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("bikesCheckedOutBy(String fname, String lname) Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//bikesCheckedOutBy
	
	/**
	 * Retrieves a list of bikes that do not have rental information stored in
	 * the database.
	 * @return ResultSet containing the brand, model, type and notes
	 * corresponding to bikes with no rental information.
	 */
	public ResultSet neverCheckedOut(){
		String sql = "select brand, model, type, notes "
				+ "from bicycle "
				+ "where serial_num not in ( "
				+    "select bike_num "
				+    "from checked_out_to);";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			return p.executeQuery();
		}catch(SQLException e){
			System.out.println("neverCheckedOut() Query failed. sql: "+sql);
			e.printStackTrace();
			return null;
		}
	}//neverCheckedOut
	
////insert operations
	
	/**
	 * Insert a row into the bicycle table.
	 * @param type of bicycle. Can be road, mountain, cruiser or hybrid.
	 * @param brand name of bicycle.
	 * @param model name of bicycle.
	 * @param serial number of bicycle. Primary key for bicycle table.
	 * @param notes any safety information regarding the bicycle, or any other
	 * important information.
	 * @param price the price in dollars of the bicycle.
	 * @return number of rows inserted into the bicycle table.
	 */
	public int insertBicycle(String type, String brand, String model, String serial, String notes, String price){
		String sql = "insert into bicycle "
				+ "values(?,?,?,?,?,?);";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, type);
			p.setString(2, brand);
			p.setString(3, model);
			p.setString(4, serial);
			p.setString(5, notes);
			p.setString(6, price);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("insertBicycle(String type, String brand, String model, String serial, String notes, String price) Insert failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Insert a row into the bicycle table.
	 * @param type of bicycle. Can be road, mountain, cruiser or hybrid.
	 * @param brand name of bicycle.
	 * @param model name of bicycle.
	 * @param serial number of bicycle. Primary key for bicycle table.
	 * @param notes any safety information regarding the bicycle, or any other
	 * important information.
	 * @param price the price in dollars of the bicycle.
	 * @return number of rows inserted into the bicycle table.
	 */
	public int insertArchivedBicycle(String type, String brand, String model, String serial, String notes, String price){
		String sql = "insert into bicycle_archive "
				+ "values(?,?,?,?,?,?, curdate());";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, type);
			p.setString(2, brand);
			p.setString(3, model);
			p.setString(4, serial);
			p.setString(5, notes);
			p.setString(6, price);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("insertArchivedBicycle(String type, String brand, String model, String serial, String notes, String price) Insert failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}//insertArchivedBicycle

	/**
	 * Copies a bicycle record from bicycle into bicycle_archive
	 * @param serial number of bicycle to copy
	 * @return true if record was copied into archive
	 */
	public boolean insertArchivedBicycleExisting(String serial) {
		
		String sql = "INSERT INTO bicycle_archive SELECT bicycle.*, curdate() FROM bicycle WHERE serial_num = ?;";
		
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, serial);
			return p.execute();
		} catch(SQLException e) {
			System.out.println("insertArchivedBicycleExisting(String serial) failed. sql = " + sql);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Insert a row into the renter table.
	 * @param email the renter's email address.
	 * @param phone the renter's 10 digit phone number, numbers only.
	 * @param pluId the renter's PLU ID number, if renter has a PLU ID card.
	 * @param address the address of the renter.
	 * @param fname the renter's first name.
	 * @param lname the renter's last name.
	 * @return number of rows inserted into the renter database.
	 */
	public int insertRenter(String email, String phone, String pluId, String address, String fname, String lname){
		String sql = "insert into renter "
				+ "values(?,?,?,?,?,?);";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, email);
			p.setString(2, phone);
			p.setString(3, pluId);
			p.setString(4, address);
			p.setString(5, fname);
			p.setString(6, lname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("insertRenter(String email, String phone, String pluId, String address, String fname, String lname) Insert failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Insert a row into the checked_out_by table.
	 * @param date the date the rental agreement began.
	 * @param paid "1" if renter has paid for this rental, "0" if not.
	 * @param details any notes regarding the rental, such as duration of
	 * rental or to record partial payment.
	 * @param helmet "1" if renter checked out a helmet, false if not.
	 * @param keyNum the number of the key corresponding to the lock that the
	 * renter has checked out.
	 * @param bikeNum the serial number of the bicycle that was checked out.
	 * @param fname renter's first name.
	 * @param lname renter's last name.
	 * @return number of rows inserted into checked_out_by.
	 */
	public int insertRent(String date, String paid, String term, String details, String helmet, String keyNum, String bikeNum, String fname, String lname){
		String sql = "insert into checked_out_to "
				+ "values(?,?,?,?,?,?,?,?,?);";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, date);
			p.setString(2, paid);
			p.setString(3, term);
			p.setString(4, details);
			p.setString(5, helmet);
			p.setString(6, keyNum);
			p.setString(7, bikeNum);
			p.setString(8, fname);
			p.setString(9, lname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("insertRent(String date, boolean paid, String details, boolean helmet, String keyNum, String bikeNum, String fname, String lname) Insert failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * insert row into repair_history table.
	 * @param date the date of repair.
	 * @param description a description of what was repaired, what parts were
	 * used and any other important information.
	 * @param bikeNum the serial number of the bike that was repaired.
	 * @return number of rows inserted into the repair_history table.
	 */
	public int insertRepair(String date, String description, String bikeNum){
		String sql = "insert into repair_history "
				+ "values(?,?,?);";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, date);
			p.setString(2, description);
			p.setString(3, bikeNum);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("insertRepair(String date, String description, String bikeNum) Insert failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Add user to database
	 * @param name
	 * @param username
	 * @param password
	 * @return
	 */
	public int insertUser(String name, String username, String password) {
		String sql = "INSERT INTO user VALUES(?,?,PASSWORD(?));";
		
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, name);
			p.setString(2, username);
			p.setString(3, password);
			return p.executeUpdate();
			
		}catch(SQLException e){
			System.out.println("insertUser(String name, String username, String password) Insert failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
////update operations
	
	//updates concerning bicycle table
	
	/**
	 * Update the bicycle type
	 * @param type new bicycle type
	 * @param serial primary key for bicycle table
	 * @return number of rows updated
	 */
	public int updateBicycleType(String type, String serial){
		String sql ="update bicycle "
				+ "set type=? "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, type);
			p.setString(2, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateBicycleType(String type, String serial) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the bicycle brand
	 * @param brand new bicycle brand
	 * @param serial primary key for bicycle table
	 * @return number of rows updated
	 */
	public int updateBicycleBrand(String brand, String serial){
		String sql ="update bicycle "
				+ "set brand=? "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, brand);
			p.setString(2, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateBicycleBrand(String brand, String serial) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the bicycle model
	 * @param model new bicycle model
	 * @param serial primary key for bicycle table
	 * @return number of rows updated
	 */
	public int updateBicycleModel(String model, String serial){
		String sql ="update bicycle "
				+ "set model=? "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, model);
			p.setString(2, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateBicycleModel(String model, String serial) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the bicycle serial number
	 * @param newSerial new primary key for bicycle table
	 * @param currentSerial primary key for bicycle table
	 * @return number of rows updated
	 */
	public int updateBicycleSerial(String newSerial, String currentSerial){
		String sql ="update bicycle "
				+ "set serial_num=? "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, newSerial);
			p.setString(2, currentSerial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateBicycleSerial(String newSerial, String currentSerial) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the notes concerning the bicycle 
	 * @param note new bicycle note
	 * @param serial primary key for bicycle table
	 * @return number of rows updated
	 */
	public int updateBicycleNotes(String note, String serial){
		String sql ="update bicycle "
				+ "set notes=? "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, note);
			p.setString(2, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateBicycleNotes(String note, String serial) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the price of the bicycle 
	 * @param price new bicycle price
	 * @param serial primary key for bicycle table
	 * @return number of rows updated
	 */
	public int updateBicyclePrice(String price, String serial){
		String sql ="update bicycle "
				+ "set price=? "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, price);
			p.setString(2, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateBicyclePrice(String price, String serial) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	//updates concerning renter table
	
	/**
	 * Update the renter's email address
	 * @param email renter's new email address
	 * @param fname first part of primary key
	 * @param lname last part of primary key
	 * @return number of rows updated
	 */
	public int updateRenterEmail(String email, String fname, String lname){
		String sql = "update renter "
				+ "set email=? "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, email);
			p.setString(2, fname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRenterEmail(String email, String fname, String lname) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the renter's phone number
	 * @param phone new phone number
	 * @param fname first part of primary key
	 * @param lname last part of primary key
	 * @return number of rows updated
	 */
	public int updateRenterPhone(String phone, String fname, String lname){
		String sql = "update renter "
				+ "set phone=? "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, phone);
			p.setString(2, fname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRenterPhone(String phone, String fname, String lname) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the renter's PLU ID number
	 * @param id new PLU ID number
	 * @param fname first part of primary key
	 * @param lname last part of primary key
	 * @return number of rows updated
	 */
	public int updateRenterPluId(String id, String fname, String lname){
		String sql = "update renter "
				+ "set plu_id=? "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, id);
			p.setString(2, fname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRenterPluId(String id, String fname, String lname) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the renter's address
	 * @param address the renter's new address
	 * @param fname first part of primary key
	 * @param lname last part of primary key
	 * @return number of rows updated
	 */
	public int updateRenterAddress(String address, String fname, String lname){
		String sql = "update renter "
				+ "set address=? "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, address);
			p.setString(2, fname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRenterAddress(String address, String fname, String lname) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the renter's first name
	 * @param newFname new first name
	 * @param fname first part of primary key
	 * @param lname last part of primary key
	 * @return number of rows updated
	 */
	public int updateRenterFirstName(String newFname, String fname, String lname){
		String sql = "update renter "
				+ "set fname=? "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, newFname);
			p.setString(2, fname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRenterFirstName(String newFname, String fname, String lname) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update the renter's last name
	 * @param newLname new last name
	 * @param fname first part of primary key
	 * @param lname last part of primary key
	 * @return number of rows updated
	 */
	public int updateRenterLastName(String newLname, String fname, String lname){
		String sql = "update renter "
				+ "set lname=? "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, newLname);
			p.setString(2, fname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRenterLastName(String newLname, String fname, String lname) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	//updates for checked_out_to
	
	/**
	 * Update start date of rental
	 * @param date new start date of rental
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalDate(String date, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set checkout_date=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, date);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalDate(String date, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update payment status of rental
	 * @param paid "1" or "0". "1" for paid, "0" for unpaid or partially paid.
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalPaid(String paid, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set paid=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, paid);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalPaid(boolean paid, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update term of rental
	 * @param term new term of rental
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalTerm(String newTerm, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set term=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, newTerm);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalTerm(String newTerm, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update details concerning rental
	 * @param details new details of rental
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalDetails(String details, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set details=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, details);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalDetails(String details, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update helmet checkout status of rental
	 * @param helmet "1" if renter checked out a helmet, "0" if not.
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalHelmet(String helmet, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set helmet=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, helmet);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalHelmet(boolean helmet, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update key number of the checked out lock.
	 * @param keyNum new lock key number
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalKeyNum(String keyNum, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set key_num=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, keyNum);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalKeyNum(String keyNum, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update the serial number of the checked out bicycle
	 * @param bikeNum new bicycle serial number
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalSerial(String bikeNum, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set bike_num=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, bikeNum);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalBike(String bikeNum, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}
	
	/**
	 * Update first name of renter
	 * @param fname new first name of renter
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalFname(String fname, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set renter_fname=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, fname);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalFname(String fname, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}

	/**
	 * Update last name of renter
	 * @param lname new last name of renter
	 * @param bike_num part of primary key
	 * @param checkout_date part of primary key
	 * @param renter_fname part of primary key
	 * @param renter_lname part of primary key
	 * @param term part of primary key
	 * @return number of rows updated
	 */
	public int updateRentalLname(String lname, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term){
		String sql="update checked_out_to "
				+ "set renter_lname=? "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, lname);
			p.setString(2, bike_num);
			p.setString(3, checkout_date);
			p.setString(4, renter_fname);
			p.setString(5, renter_lname);
			p.setString(6, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRentalLname(String lname, String bike_num, String checkout_date, String renter_fname, String renter_lname, String term) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;			
		}
	}

	//updates for repair_history table
	
	/**
	 * Update date of repair
	 * @param date new date of repair
	 * @param bike_repaired part of primary key
	 * @param repair_date part of primary key
	 * @return number of rows updated
	 */
	public int updateRepairDate(String date, String bike_repaired, String repair_date){
		String sql = "update repair_history "
				+ "set repair_date=? "
				+ "where bike_repaired=? and repair_date=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, date);
			p.setString(2, bike_repaired);
			p.setString(3, repair_date);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRepairDate(String date, String bike_repaired, String repair_date) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Update description of repair
	 * @param description new repair description
	 * @param bike_repaired part of primary key
	 * @param repair_date part of primary key
	 * @return number of rows updated
	 */
	public int updateRepairDescription(String description, String bike_repaired, String repair_date){
		String sql = "update repair_history "
				+ "set description=? "
				+ "where bike_repaired=? and repair_date=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, description);
			p.setString(2, bike_repaired);
			p.setString(3, repair_date);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRepairDescription(String description, String bike_repaired, String repair_date) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update serial number of repaired bicycle
	 * @param bikeNum new bicycle serial number
	 * @param bike_repaired part of primary key
	 * @param repair_date part of primary key
	 * @return number of rows updated
	 */
	public int updateRepairBike(String bikeNum, String bike_repaired, String repair_date){
		String sql = "update repair_history "
				+ "set bike_repaired=? "
				+ "where bike_repaired=? and repair_date=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, bikeNum);
			p.setString(2, bike_repaired);
			p.setString(3, repair_date);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("updateRepairBike(String bike, String bike_repaired, String repair_date) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	//updates for user
	
	public int updatePassword(String username, String currentPass, String password) {
		
		String sql = "UPDATE user SET password=PASSWORD(?) WHERE username=? AND password=PASSWORD(?);";
		
		try {
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, password);
			p.setString(2, username);
			p.setString(3, currentPass);
			
			return p.executeUpdate();
		} catch(SQLException e) {
			System.out.println("updatePassword(String username, String currentPass, String password) Update failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}

////delete operations
	
	/**
	 * Delete a row from the bicycle table
	 * @param serial the serial number of bicycle to be deleted
	 * @return number of rows deleted
	 */
	public int deleteBicycle(String serial){
		String sql = "delete from bicycle "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("deleteBicycle(String serial) Delete failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Delete a row from the bicycle table
	 * @param serial the serial number of bicycle to be deleted
	 * @return number of rows deleted
	 */
	public int deleteArchivedBicycle(String serial){
		String sql = "delete from bicycle_archive "
				+ "where serial_num=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, serial);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("deleteArchivedBicycle(String serial) Delete failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}//deleteArchivedBicycle

	/**
	 * Delete a row from the renter table
	 * @param fname part of primary key of renter to be deleted
	 * @param lname part of primary key
	 * @return number of rows deleted
	 */
	public int deleteRenter(String fname, String lname){
		String sql = "delete from renter "
				+ "where fname=? and lname=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, fname);
			p.setString(2, lname);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("deleteRenter(String fname, String lname) Delete failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Delete a row from the checked_out_to table
	 * @param bikeNum part of primary key of rental to be deleted
	 * @param date part of primary key
	 * @param fname part of primary key
	 * @param lname part of primary key
	 * @param term part of primary key
	 * @return number of rows deleted
	 */
	public int deleteRental(String bikeNum, String date, String fname, String lname, String term){
		String sql = "delete from checked_out_to "
				+ "where bike_num=? and checkout_date=? and renter_fname=? and renter_lname=? and term=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, bikeNum);
			p.setString(2, date);
			p.setString(3, fname);
			p.setString(4, lname);
			p.setString(5, term);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("deleteRental(String bikeNum, String date, String fname, String lname, String term) Delete failed. sql: "+ sql);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Delete a row from the repair_history table
	 * @param bikeNum part of primary key of repair to be deleted
	 * @param date part of primary key
	 * @return number of rows deleted
	 */
	public int deleteRepair(String bikeNum, String date){
		String sql = "delete from repair_history "
				+ "where bike_repaired=? and repair_date=?;";
		try{
			PreparedStatement p = conn.prepareStatement(sql);
			p.clearParameters();
			p.setString(1, bikeNum);
			p.setString(2, date);
			return p.executeUpdate();
		}catch(SQLException e){
			System.out.println("deleteRepair(String bikeNum, String date) Delete failed. sql: "+sql);
			e.printStackTrace();
			return 0;
		}
	}
	
}//Utilities