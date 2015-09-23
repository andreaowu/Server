import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

class ClientHandler implements Runnable {

	/* Connected client socket */
	private Socket client;

	public ClientHandler(Socket client) {
		this.client = client;
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
				
				/* Prepare the response */
				resp.write("HTTP/1.0 200 OK\n\n".getBytes());
				resp.write("\r\n".getBytes());

				/* Read the requested file and then write the file to the response */
				BufferedReader br = new BufferedReader(new FileReader(req[1].substring(1)));
				line = br.readLine();
				while (line != null) {
					resp.write((line + "\n").getBytes());
					line = br.readLine();
				}
				/* Send the response to the client */
				resp.flush();
				br.close();
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