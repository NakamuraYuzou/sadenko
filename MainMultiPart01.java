package test01;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.UUID;

public class MainMultiPart01
{

	public static void main(String[] args) throws Exception
	{
		String inf = "C:\\WORK\\inf.pdf";
		String host = "http://localhost:8080/test/TestFileUpload";
//		String host = "http://localhost:8080/test/TestMsgView";
		String CRLF = "\r\n";
		File file = new File( inf );
		String boundary = UUID.randomUUID().toString();

		URL url = new URL( host );
		HttpURLConnection con = ( HttpURLConnection ) url.openConnection();

		con.setDoOutput( true );
		con.setRequestMethod("POST");
		con.setRequestProperty( "Content-Type" , "multipart/form-data;boundary=" + boundary );

		byte[] filebyte = Files.readAllBytes( file.toPath() );

		DataOutputStream request = new DataOutputStream( con.getOutputStream() );
		String config = "";
		request.writeBytes( "--" + boundary + CRLF );
		config = "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + CRLF + CRLF;
		request.writeBytes( config );
		request.write( filebyte );
		request.writeBytes( CRLF );
		request.writeBytes( "--" + boundary + "--" );
		request.flush();
		request.close();

		InputStream is;
		int code = con.getResponseCode();
		System.out.println( "CODE = " + code );
		if ( code == 200 )
		{
			is = con.getInputStream();
		}
		else
		{
			is = con.getErrorStream();
		}
		if ( is != null )
		{
			InputStreamReader isr = new InputStreamReader( is , "UTF-8" );
			BufferedReader br = new BufferedReader( isr );
			String line = "";
			while( ( line = br.readLine() ) != null )
			{
				System.out.println( line );
			}
		}

		con.disconnect();
	}

}
