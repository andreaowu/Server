import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Threadpool {
	/* List of threads in the threadpool */
	protected Thread threads[] = null;
	/* List of tasks to be run */
	protected Queue<Runnable> tasks = null;
	/* Lock for the queue */
	protected static Lock taskLock;
	/* Condition for the lock for the queue */
	protected static Condition hasTask;

	/**
	 * Initialize the number of threads required in the threadpool.
	 * Initialize all variables.
	 * 
	 * @param size How many threads in the thread pool.
	 */
	public Threadpool(int size) {
		this.threads = new Thread[size];
		this.tasks = new LinkedList<Runnable>();
		taskLock = new ReentrantLock();
		hasTask = taskLock.newCondition();
		for (int i = 0; i < size; i++) {
			/* Create worker threads */
			Thread nThread = new WorkerThread(this);
			threads[i] = nThread;
			threads[i].start();
		}
	}

	/**
	 * Add a job to the queue of tasks that has to be executed. As soon as a
	 * thread is available, it will retrieve tasks from this queue and start
	 * processing.
	 * 
	 * @param r job that has to be executed asynchronously
	 * @throws InterruptedException
	 */
	public void addToQueue(Runnable r) {

		/* Lock queue so a thread doesn't try to pop off the first task
		 * when another thread is adding a task onto the queue */
		taskLock.lock();

		/* Add the task to the queue */
		this.tasks.add(r);

		/* Wake up a waiting thread to give a job */
		hasTask.signal();

		/* Allow changes to the queue now */
		taskLock.unlock();
	}

	/**
	 * Block until a job is available in the queue and retrieve the job
	 * 
	 * @return A runnable task that has to be executed
	 * @throws InterruptedException
	 */
	public synchronized Runnable getJob() throws InterruptedException {

		/* Wait for a task if there aren't any available right now */
		if (this.tasks.peek() == null) {
			try {
				taskLock.lock();
				hasTask.await();
			} catch (InterruptedException e) {
				throw e;
			} finally {
				/* Make sure the lock gets unlocked */
				taskLock.unlock();
			}
		}

		/* Lock queue so other jobs can't be added to the queue
		 * and other threads can't run the first job in the queue */
		taskLock.lock();

		/* Take the first task in the queue */
		Runnable runThis = this.tasks.poll();

		/* Release the lock after popping off the first task */
		taskLock.unlock();

		return runThis;
	}
}

/**
 * The worker threads that make up the thread pool.
 */
class WorkerThread extends Thread {
	/**
	 * The constructor.
	 * 
	 * @param o
	 *            the thread pool
	 */
	protected Threadpool threadPool;

	WorkerThread(Threadpool o) {
		super();
		this.threadPool = o;
	}

	/**
	 * Scan for and execute tasks.
	 */
	public void run() {
		Runnable task = null;
			try {
				task = threadPool.getJob();
				task.run();
			} catch (InterruptedException e) {
			}
	}
}