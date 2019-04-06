package interruption03;

public class Examen {
	  public static void main(String [] args) {
	        Thread thread = new Thread(new SleepingThread());
	        thread.start();
	        thread.interrupt();
	    }
	 
	    private static class SleepingThread implements Runnable {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(10);
	                } catch (InterruptedException e) {
	                }
	            }
	        }
	    }
}
