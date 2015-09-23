# Server
Multi-threaded server with a threadpool

## Usage
### Start the server
In the command line:<br>
javac Server.java<br>
java Server<br>

In Eclipse:<br>
Import the project into Eclipse.<br>
Click the green "Run" button or go to Run -> Run in the menu (on Mac).

### Get a page
There is only one page that can be served. Type "http://localhost:8080/samplepage.html" into your browser. You should see "Hello World!" show up on your browser page.

### Stop the server
In the command line:<br>
Exit out (ctrl + c on Mac).<br>
*or*<br>
Do "lsof -i:8080" command<br>
Find the job that is getting run by java, and copy its PID.<br>
Do "kill [PID]"

In Eclipse:<br>
To stop, click the red square "Stop" button on the Console.

## Code Design
### Server.java:
This is where "main" is, and it makes all initializations: SocketServer to handle connection requests, Socket to receive and send data to the client, Runnable ClientHandler to process and construct the actual data from/to the client, and Threadpool to manage threads.

### Threadpool.java:
The Threadpool manages the tasks and makes sure each thread gets one task to run. There is a WorkerThread class, which extends the Thread class, from which Threads are made for the Threadpool. When a new task comes in, it gets added to the queue, and an available thread takes and executes the task. If there are no tasks, the threads wait.

### ClientHandler.java:
Implements Runnable and are the tasks given to the threads in Threadpool. It looks at the data sent from the client, processes it, and sends back appropriate data back to the client. For this implementation, "samplepage.html" gets read on the server side and gets sent back to the client.
