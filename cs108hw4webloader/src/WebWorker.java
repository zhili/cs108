import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;


public class WebWorker extends Thread {
	String urlString;
	Semaphore rollSemaphore;
	WebFrame webFrame;
	int row;
	public WebWorker(String url, int row, WebFrame webFrame, Semaphore rollSemaphore) {
		this.urlString = url;
		this.rollSemaphore = rollSemaphore;
		this.webFrame = webFrame;
		this.row = row;
	}
	
	@Override
	public void run() {
		try {
			rollSemaphore.acquire();
			// actual printing here
			webFrame.increaseRunning();
			download();
			webFrame.decreaseRunning();
			rollSemaphore.release();
		} catch (InterruptedException e) {
			//e.printStackTrace();
			webFrame.runCompletedWithStatus(row, "Acquire Interrupt");
			
		} 
	}
	
	public void download()  {
		InputStream input = null;
		StringBuilder contents = null;
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
		
			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);
			
			connection.connect();
			input = connection.getInputStream();

			BufferedReader reader  = new BufferedReader(new InputStreamReader(input));
		
			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			while ((len = reader.read(array, 0, array.length)) > 0) {
				contents.append(array, 0, len);
				Thread.sleep(100);
			}

			// Successful download if we get here
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			final String status = sdf.format(now) + " " + contents.length() + "bytes";
			webFrame.runCompletedWithStatus(row, status);
		}
		// Otherwise control jumps to a catch...
		catch(MalformedURLException ignored) {
			webFrame.runCompletedWithStatus(row, "URL err");
		}
		catch(InterruptedException exception) {
			// YOUR CODE HERE
			// deal with interruption
			//exception.printStackTrace();
			webFrame.runCompletedWithStatus(row, "Sleep Interrupt");
		}
		catch(IOException ignored) {
			webFrame.runCompletedWithStatus(row, "IO err");
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try{
				if (input != null) input.close();
			}
			catch(IOException ignored) {}
		}
 
	}
}

