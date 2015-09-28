import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

class ClientHandler implements Runnable {

	/* Connected client socket */
	private Socket client;
	/* Given directory for serving files */
	private String dir;

	public ClientHandler(Socket client, String dir) {
		this.client = client;
		this.dir = dir;
	}

	@Override
	public void run() {
		try {
			 /* Read and process the input gotten from the client */
			InputStream msg = client.getInputStream();
			BufferedReader clientReader = new BufferedReader(new InputStreamReader(msg));
			String line = clientReader.readLine();
			String req[] = line.split(" "); 
			
			/* See whether it's a GET request */
			if (req[0].equals("GET")) {
				OutputStream resp = client.getOutputStream();
				String fileName = dir + req[1];
				File givenDir = new File(fileName);
				
				if (givenDir.exists()) {
					/* Prepare the response */
					resp.write(("HTTP/1.0 200 OK\n\n" + 
							"Content-type: text/html\r\n\r\n").getBytes());
					resp.write("\r\n".getBytes());
					
					/* Read the requested file and then write the file to the response */
					BufferedReader br = new BufferedReader(new FileReader(givenDir));
					line = br.readLine();
					while (line != null) {
						resp.write((line + "\n").getBytes());
						line = br.readLine();
					}
					br.close();
					
				} else {
					/* Send back a 404 error */
					resp.write(("HTTP/1.0 404 Not Found\r\n" +
					          "Content-type: text/html\r\n\r\n" +
					          "<html><head></head><body>"+ 
					          fileName + " not found</body></html>\n").getBytes());
				}
				
				/* Send the response to the client */
				resp.flush();
			}
			clientReader.close();
			msg.close();
		} catch (IOException e) {
		} finally {
			/* Always close the client socket */
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}
}