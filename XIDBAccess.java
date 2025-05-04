package xinvoice;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;

public class XIDBAccess
{
	public static Connection con;
	public static Statement stmt;
	public static ResultSet rsltst;

	public XIDBAccess() throws Exception
	{
		String url = "jdbc:postgresql://localhost:5432/sadenko";
		String us = "user";
		String pw = "password";
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection( url , us , pw );
		stmt = con.createStatement();
	}


	public void setSQL( String sqlstr ) throws Exception
	{
		rsltst = stmt.executeQuery( sqlstr );
	}

	public void setUpdate( String sqlstr ) throws Exception
	{
		stmt.executeUpdate( sqlstr );
	}

	public boolean getRow() throws Exception
	{
		boolean rc = rsltst.next();
		return rc;
	}

	public String getClmn( String clmn ) throws Exception
	{
		String rc = "";
		rc = rsltst.getString( clmn );
		return rc;
	}

	public int getClmnAsInt( String clmn ) throws Exception
	{
		int rc = 0;
		rc = rsltst.getInt( clmn );
		return rc;
	}

	public long getClmnAsLong( String clmn ) throws Exception
	{
		long rc = 0;
		rc = rsltst.getLong( clmn );
		return rc;
	}

	public double getClmnAsDouble( String clmn ) throws Exception
	{
		double rc = 0;
		rc = rsltst.getDouble( clmn );
		return rc;
	}

	public Date getClmnAsDate( String clmn ) throws Exception
	{
		Date dt = new Date();
		Date tm = new Date();
		dt = rsltst.getDate( clmn );
		tm = rsltst.getTime( clmn );
		Date rc = null;
		if ( dt != null )
		{
			long ldt = dt.getTime();
			long ltm = tm.getTime();
			long lrc = ldt + ltm + 9 * 60 * 60 * 1000;
			rc = new Date( lrc );
		}
		return rc;
	}

	public boolean getClmnAsBoolean( String clmn ) throws Exception
	{
		boolean rc = false;
		rc = rsltst.getBoolean( clmn );
		return rc;
	}

	public void commit() throws Exception
	{
		con.commit();
	}

	public long copyIn( String tbl , String[] cols , String infile ) throws Exception
	{
		PGConnection pgcon = (PGConnection) con;
		CopyManager cm = pgcon.getCopyAPI();
		String sqlstr = "COPY " + tbl + " (";
		for ( int i = 0 ; i < cols.length ; i++ )
		{
			if ( i == 0 )
			{
				sqlstr = sqlstr + " " + cols[i];
			}
			else
			{
				sqlstr = sqlstr + " , " + cols[i];
			}
		}
		sqlstr = sqlstr + " ) FROM STDIN ( FORMAT CSV , HEADER )";
		CopyIn ci = cm.copyIn( sqlstr );

		FileInputStream fis = new FileInputStream( infile );
		InputStreamReader reader = new InputStreamReader( fis , "UTF-8" );
		int bufsize = 65536;
		char[] cbuf = new char[bufsize];
		int len;

		while ( ( len = reader.read( cbuf ) ) > 0 )
		{
			byte[] buf = (new String( cbuf , 0 , len ) ).getBytes("UTF-8");
			ci.writeToCopy( buf , 0 , buf.length );
		}
		long rc = ci.endCopy();

		reader.close();
		fis.close();
		return( rc );
	}

	public long copyInSJIS( String tbl , String[] cols , String infile ) throws Exception
	{
		PGConnection pgcon = (PGConnection) con;
		CopyManager cm = pgcon.getCopyAPI();
		String sqlstr = "COPY " + tbl + " (";
		for ( int i = 0 ; i < cols.length ; i++ )
		{
			if ( i == 0 )
			{
				sqlstr = sqlstr + " " + cols[i];
			}
			else
			{
				sqlstr = sqlstr + " , " + cols[i];
			}
		}
		sqlstr = sqlstr + " ) FROM STDIN ( FORMAT CSV , HEADER , QUOTE '\"' )";
		CopyIn ci = cm.copyIn( sqlstr );

		FileInputStream fis = new FileInputStream( infile );
		InputStreamReader reader = new InputStreamReader( fis , "SHIFT_JIS" );
		int bufsize = 65536;
		char[] cbuf = new char[bufsize];
		int len;

		while ( ( len = reader.read( cbuf ) ) > 0 )
		{
			byte[] buf = (new String( cbuf , 0 , len ) ).getBytes("UTF-8");
			ci.writeToCopy( buf , 0 , buf.length );
		}
		long rc = ci.endCopy();

		reader.close();
		fis.close();
		return( rc );
	}

	public long copyInFromStringArray( String tbl , String[] cols , String[][] recs ) throws Exception
	{
		PGConnection pgcon = (PGConnection) con;
		CopyManager cm = pgcon.getCopyAPI();
		String sqlstr = "COPY " + tbl + " (";
		for ( int i = 0 ; i < cols.length ; i++ )
		{
			if ( i == 0 )
			{
				sqlstr = sqlstr + " " + cols[i];
			}
			else
			{
				sqlstr = sqlstr + " , " + cols[i];
			}
		}
		sqlstr = sqlstr + " ) FROM STDIN ( FORMAT CSV )";
		CopyIn ci = cm.copyIn( sqlstr );

		for ( int i = 0 ; i < recs.length ; i++ )
		{
			String rec = String.join( "," ,recs[i] ) + "\n";
			byte[] buf = rec.getBytes("UTF-8");
			int len = rec.length();
			ci.writeToCopy( buf , 0 , len );
		}
		long rc = ci.endCopy();

		return( rc );
	}

	public void insert( String tblnm , String[] fldtbl , String[] vltbl) throws Exception
	{
		String fldstr = "";
		String vlstr = "";
		String comma = "";

		for ( int i = 0 ; i < vltbl.length ; i++ )
		{
			if ( vltbl[i] != null )
			{
				fldstr = fldstr + comma + fldtbl[i];
				vlstr = vlstr + comma + vltbl[i];
				comma = " , ";
			}
		}

		String sqlstr = "INSERT INTO " + tblnm + " ( " + fldstr + " ) VALUES ( " + vlstr + " )";
		this.setUpdate( sqlstr );
	}

	public void update( String tblnm , String wherestr ,
							String[] fldtbl , String[] orgtbl , String[] vltbl ) throws Exception
	{
		String updstr = "";
		String comma = "";
		boolean updflg = false;

		for ( int i = 0 ; i < vltbl.length ; i++ )
		{
			if ( orgtbl[i] == null )
			{
				if ( vltbl[i] != null )
				{
					updstr = updstr + comma + fldtbl[i] + " = " + vltbl[i];
					comma = " , ";
					updflg = true;
				}
			}
			else
			{
				if ( vltbl[i] == null )
				{
					updstr = updstr + comma + fldtbl[i] + " = " + vltbl[i];
					comma = " , ";
					updflg = true;
				}
				else
				{
					if ( !orgtbl[i].equals( vltbl[i] ) )
					{
						updstr = updstr + comma + fldtbl[i] + " = " + vltbl[i];
						comma = " , ";
						updflg = true;
					}
				}
			}
		}

		String sqlstr = "UPDATE " + tblnm + " SET " + updstr
						+ " WHERE " + wherestr;
		if ( updflg == true )	this.setUpdate( sqlstr );
	}

	public void close() throws Exception
	{
		if ( rsltst != null ) rsltst.close();
		if ( stmt != null ) stmt.close();
		if ( con != null ) con.close();
	}

}
