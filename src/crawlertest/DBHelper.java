package crawlertest;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DBHelper {
	private static final String driver ="com.mysql.jdbc.Driver";
	private static final String url="jdbc:mysql://localhost:3306/webcrawler?useUnicode=true&characterEncoding=UTF-8";
	private static final String username="root";
	private static final String password="root";
	
	private static java.sql.Connection conn=null;
	
	static{
		try{
			Class.forName(driver);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		if(conn==null)
		{
			try {
				conn=DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (Connection) conn;
		}
		return (Connection) conn;
	}
	
	public static void insert(String sql,Connection conn)
	{
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		
		String str="insert into test_two (item1) values ('fu')";
		String str2="µ„∆¿(16)";
		String sql_2="insert into chart_two (username,shop,time,comment_all,kouwei,huanjing,fuwu,url) values ('fu','f','t','a','f','cx','sd','f')";

		System.out.println(str2.substring(3).replace(")", ""));
//		try
//		{
//			Connection conn=DBHelper.getConnection();
//			if(conn!=null)
//			{
//				insert(sql_2,conn);
//				System.out.println("success");
//			}
//			else
//			{
//				System.out.println("fail");
//			}
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		
	}

}
