import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* 
 * Problem statement:
 * A multi-threaded (e.g. file-based) web server with thread-pooling
 * implemented in Java.
 */

public class Server {

	public static void main(String[] args) throws IOException {

		/* Sanitize arguments */
		if (args[0] == null) {
			throw new IOException("Missing Argument: pass in root directory for files");
		} else {
			File givenDir = new File(args[0]);
			
			/* Check whether given directory exists */
			if (!givenDir.exists()) {
				throw new IOException("Bad Argument: given directory does not exist");
			}
			
			/* If given directory exists, take out slash at the end if there is one */
			if (args[0].endsWith("/")) {
				args[0] = args[0].substring(0, args[0].length() - 1);
			}
		}
		
		/* Start the ServerSocket */
		ServerSocket ss = new ServerSocket(8080);
		Socket s;
		/* Let's set up 10 threads */
		Threadpool threadpool = new Threadpool(10);

		/* Keep running the server */
		while (true) {
			/* Start the ServerSocket if it's closed */
			if (ss.isClosed())
				ss = new ServerSocket(8080);

			/* Connect to the socket */
			s = ss.accept();
			Runnable r = new ClientHandler(s, args[0]);

			/* Add new task to the threadpool */
			threadpool.addToQueue(r);

			/* Always close the ServerSocket */
			ss.close();
		}
	}
}