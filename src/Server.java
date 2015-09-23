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
			Runnable r = new ClientHandler(s);

			/* Add new task to the threadpool */
			threadpool.addToQueue(r);

			/* Always close the ServerSocket */
			ss.close();
		}
	}
}