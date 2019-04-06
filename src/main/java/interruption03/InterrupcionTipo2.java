package interruption03;

import java.math.BigInteger;

/**
 * 
 * El segundo método para controlar que se envían interrupciones al hilo que consume mucho tiempo
 * es (en vez de try-catch a una InterruptionException), 
 * busco un punto en el hilo donde se consuma mucho tiempo, y ahí planto un IF preguntando si se 
 * ha lanzado ya la interrupción: "Thread.getCurrentThread().isInterrupted()"
 *
 */
public class InterrupcionTipo2 {

	public static void main(String[] args) throws InterruptedException {
		BigInteger base = BigInteger.valueOf(100);
		BigInteger power = BigInteger.valueOf(7000); 
		Thread hilo = new Thread(new LongComputationTask(base, power));
		hilo.start();
		Thread.currentThread().sleep(5);
		hilo.interrupt();
	}

	private static class LongComputationTask implements Runnable {
		private BigInteger base;
		private BigInteger power;
		
		public LongComputationTask(BigInteger base, BigInteger power) {
			this.base = base;
			this.power = power;
		}

		@Override
		public void run() {
			System.out.println("La potencia de " + base + " elevado a " + power + " es: " + pow(base,power));
		}

		private BigInteger pow(BigInteger base, BigInteger power) {
			BigInteger result = BigInteger.ONE;

			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				if(Thread.currentThread().isInterrupted()) {
					System.out.println(i);
					System.out.println("El cálculo de la potencia se ha interrumpido prematuramente");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}
			return result;
		}
	}
}
