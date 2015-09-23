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

		/* Make sure the directory of the to-be-served file is correct */
		if (args[0] == null) {
			/* A directory wasn't given */
			throw new IOException("Provide directory of to-be-served file!");
		} else {
			/* Check whether the given directory exists */
			File givenDir = new File(args[0]);
			if (!givenDir.exists()) {
				throw new IOException("Provided directory does not exist!");
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