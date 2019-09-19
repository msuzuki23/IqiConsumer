/**
* This Application will be run as a Service
* When Message is available on Queue (ActiveMQ)
* Workers will mine IQI, calling ConsumerIqiMiner
*
* @author: msuzuki
* @version: 1.0.0-SNAPSHOT
* @since: 2019-09-13
*/

package iqiConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class IqiConsumerApp {
	public static void main(String[] args) {
		
		System.out.println("Hi from Iqi Consumer App!");
		
		final int MAX_T = 3;
		ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
		
		// Call Log (System.err and System.out) redirect to a file
		Log log = new Log("IqiConsumerApp");
		log.redirect();
		
		// Pull Messages if There is an Idle Thread and 
		// if the there is a message to be Consumed
		while(true) {
			if (((ThreadPoolExecutor) pool).getActiveCount() < MAX_T) {
				MessagePuller task = new MessagePuller();
				String pulledTask = task.pull();
				if (pulledTask != null) {
					pool.execute(new ConsumerIqiMiner(pulledTask));
				} else {
					// Request Garbage Collector Cleaning
					System.gc();
					try {
						// Put Threads to Sleep (milliSeconds)
						Thread.sleep(1000 * 60 * 60);	// To sleep for 1 Hour = 1000 * 60 * 60
					} catch (InterruptedException e) {
						System.err.println("Consumer Thread got Interrupted on task: " + pulledTask);
						//e.printStackTrace();
						e.toString();
					}
				}
			}
		}
	}
}
