import java.util.concurrent.Semaphore;

//Readers-Writers with Writer Priority

public class RW_Semaphore { 
	
	public static void main(String[] args) {

		Database d = new Database();

		Writer w1 = new Writer(d,1);
		Writer w2 = new Writer(d,10);
		Writer w3 = new Writer(d,100);
		Writer w4 = new Writer(d,1000);
		Writer w5 = new Writer(d,10000);

		Reader r1 = new Reader(d);
		Reader r2 = new Reader(d);
		Reader r3 = new Reader(d);
		Reader r4 = new Reader(d);
		Reader r5 = new Reader(d);
		
		w1.start();
		r1.start();
		r2.start();
		r3.start();
		w2.start();
		w3.start();
		w4.start();
		w5.start();
		r4.start();
		r5.start();
	}
}

class Reader extends Thread {
	Database d;

	public Reader(Database d) {
		this.d = d;
	}

	public void run() {

		for (int i = 0; i < 5; i++){		
			d.request_read();
			System.out.println(d.read());
			try { Thread.sleep(50); } //Reader takes a bit of time to read
			catch (Exception e) {}
			d.done_read();
			
		}
	}
}

class Writer extends Thread {

	Database d;
	int x;

	public Writer(Database d, int x) {
		this.d = d;
		this.x = x;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {			
			d.request_write();
			d.write(x);
			try { Thread.sleep(50); } //Writer takes a bit of time to write
			catch (Exception e) {}
			d.done_write();
		}
	}
}



class Database {
	
	int data = 0;
	int r = 0;   // # active readers
	int w = 0;   // # active writers (0 or 1)
	int ww = 0;  // # waiting writers
	int wr = 0;  // # waiting readers
	
	Semaphore s1 = new Semaphore(1); //Semaphore for translating synchronized methods
	Semaphore s2 = new Semaphore(0); //Semaphore for waiting readers
	Semaphore s3 = new Semaphore(0); //Semaphore for waiting writers
	
	public void request_read() {
		try {
			s1.acquire();				
			while (w == 1 || ww > 0) {	
				wr++;
				s1.release();	
				s2.acquire(); //acquire lock for s2(Reader)				
				s1.acquire();
				wr--;
			} 		
			r++;
			s1.release();
		} catch (Exception e) {}
	}

	public void done_read() {
		try {
			s1.acquire();
			r--;
			s1.release();
		} catch (Exception e) {}
		//Since Writers are given the priority, if there are waiting writers, release them
		if (ww > 0) {
			while (s3.hasQueuedThreads()) {
				s3.release();
			}
		} 
		else {
			while (s2.hasQueuedThreads()) {
				s2.release();
			}
		}
	}

	public void request_write() {
		try {
			s1.acquire();			
			while (r > 0 || w == 1) {	
				ww++; 
				s1.release();
				s3.acquire(); //acquire lock for s3(Writer)
				s1.acquire();
				ww--;
			}
			w = 1;
			s1.release();
		} catch (Exception e) {}
	}

	public void done_write() {
		try {
			s1.acquire();
			w = 0;
			s1.release();
		} 
		catch (Exception e) {}
		if (ww > 0) {
			while (s3.hasQueuedThreads()) {
				s3.release();
			}
		} 
		else {
			while (s2.hasQueuedThreads()) {
				s2.release();
			}
		}
	}
	
	int read() {
		return data;
	}

	void write(int x) {
		data = data + x;
	}
}

