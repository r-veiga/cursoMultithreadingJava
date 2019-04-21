package atomic09;

import java.util.Random;

public class AtomicVolatileAndMetrics {

	public static class MetricsPrinter extends Thread {
		private Metrics metrics;

		MetricsPrinter(Metrics metrics) {
			this.metrics = metrics;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				double currentAverage = metrics.getAverage();
				System.out.println("La media en este momento es de: " + currentAverage);
			}
		}
	}

	public static class BusinessLogic extends Thread {
		private Metrics metrics;
		private Random random = new Random();
		
		BusinessLogic(Metrics metrics) {
			this.metrics = metrics;
		}
				
		@Override
		public void run() {
			while(true) {
				long start = System.currentTimeMillis();
				
				try {
					Thread.sleep(random.nextInt(10));
				} 
				catch(InterruptedException e) { }
				
				long end = System.currentTimeMillis();
				metrics.addSample(end - start);
			}
		}
	}
	
	public static class Metrics {
		
//		el tipo double necesita sincronización porque la parte entera va por un lado y la decimal por otro
//		-------------------------------------------------------------
		private volatile double average = 0.0;
		private long count = 0;
		
//		addSample no es un método atómico, se protee con synchronized
//		-------------------------------------------------------------
		public synchronized void addSample(long sample) {
			double currentSum = average * count; 
			count++;
			average = (currentSum + sample) / count;
		}
		
		public double getAverage() { 
			return average;
		}
	}
	
	public static void main(String[] args) {
		Metrics metrics = new Metrics();
		
		BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
		BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);
		MetricsPrinter metricsPrinterThread = new MetricsPrinter(metrics);
		
		businessLogicThread1.start();
		businessLogicThread2.start();
		metricsPrinterThread.start();
		
		// Se espera visualizar una media de unos 5 milisegundos, por el "sleep(random(de 0 a 10 ms))"
	}

}
