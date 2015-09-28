# Server
Multi-threaded server with a threadpool

## Usage
### Inputs and Outputs
You will need to give an input to the program. The input is a local directory from where you would like the program to search from. For example, if I give "/Users/andreaowu/Desktop" as the input, the server will look in this directory for pages.


### Start the server
In the command line:<br>
javac Server.java<br>
java Server [directory]<br>

In Eclipse:<br>
Import the project into Eclipse.<br>
Click the dropdown arrow to the right of the green "Run" button and select "Run Configurations" *or* go to Run -> Run Configurations in the menu (on Mac).<br>
After pulling up the Run Configurations box, click on the "Arguments" tab and type in your directory there.

### Get a page
Open a browser. Type in "localhost:8080/[name of page]". 
For example, if I pass in the directory "/Users/andreaowu/Desktop" as the input and I put "samplepage.html" in that local directory, I will open a browser and type in "localhost:8080/samplepage.html".<br>
The server will either return the page if it is found or a 404 not found error code to the client (browser in this case).

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
