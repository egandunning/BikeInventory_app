package plu.bikecoop;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

	public static void main(String[] apples){
		Utilities util = new Utilities();
		
		// test open
		System.out.println("before opening: "+util.getConn());
		util.openDB();
		System.out.println("after opening: "+util.getConn());
		// test open complete
		
		//test getRentalInfo
		ResultSet r = util.getRentalInfo("2015", "summer", "Cool", "Dude");
		try {
			while(r.next()){
				System.out.print(r.getString(1)+'\t');
				System.out.print(r.getString(2)+'\t');
				System.out.print(r.getString(3)+'\t');
				System.out.print(r.getString(4)+'\t');
				System.out.print(r.getString(5)+'\t');
				System.out.print(r.getString(6)+'\t');
				System.out.print(r.getString(7)+'\t');
				System.out.print(r.getString(8)+'\t');
				System.out.println(r.getString(9)+'\t');
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		r=null;
		//test getRentalInfo complete
		
		//test getRepairInfo
		util.getRepairInfo("GB02B36142");
		util.getRepairInfo("Schwinn", "Mirada Sport");
		util.getRepairInfo("Jimmy", "Jamestown");
		//test getRepairInfo complete
		
		//test unpaidRental
		r=util.unpaidRental();
		try {
			while(r.next()){
				System.out.print(r.getString(1)+'\t');
				System.out.print(r.getString(2)+'\t');
				System.out.print(r.getString(3)+'\t');
				System.out.println(r.getString(4)+'\t');
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		r=null;
		//test unpaidRental complete
		
		//test bikesCheckedOutBy
		r = util.bikesCheckedOutBy("Nate", "Narwhal");
		try {
			while(r.next()){
				System.out.print(r.getString(1)+'\t');
				System.out.print(r.getString(2)+'\t');
				System.out.print(r.getString(3)+'\t');
				System.out.println(r.getString(4)+'\t');
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		r=null;
		//test bikesCheckedOutBy complete
		
		// test close
		System.out.println("before closing: "+util.getConn());
		util.closeDB();
		System.out.println("after closing: "+util.getConn());
		// test close complete
	}//main
}
