package interruption03;

import java.math.BigInteger;

/**
 * 
 * Cuando convierto un hilo a tipo DAEMON, 
 * que debe ser antes de arrancarlo con start(), 
 * este hilo ya no se comporta como los hilos de proceso normales. 
 * 
 * Los hilos de tipo DAEMON finalizan autom�ticamente cuando no queda 
 * ning�n hilo de proceso vivo (como p.ej. el hilo MAIN).
 * 
 * Los hilos de proceso, por el contrario, contin�an ejecut�ndose 
 * hasta que finalicen. Incluso aunque haya terminado el hilo MAIN. 
 *
 */
public class CuandoFinalizanLosDAEMON_3 {

	public static void main(String[] args) throws InterruptedException {
		BigInteger base = BigInteger.valueOf(100);
		BigInteger power = BigInteger.valueOf(7000);
		
		Thread hiloCalculo = new Thread(new LongComputationTask(base, power));
		Thread hiloDaemon = new Thread( () -> {
			while(true) {
				System.out.println("- - - El hilo DAEMON " + Thread.currentThread().getName() + " sigue activo.");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		hiloDaemon.setDaemon(true);
		
		hiloCalculo.start();
		hiloDaemon.start();
		
		Thread.sleep(50);
		hiloCalculo.interrupt();
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

//			Aqu� me ha pasado algo muy curioso, 
//			tengo dos bloques distintos para gestionar el thread.interrupt()
//			un try-catch y tambi�n un if
//			por tanto, dependiendo del azar, la interrupci�n la pod�a recoger cualquiera de los dos
//			en el try-catch hab�a olvidado poner el return 
//			as� que cuando entraba por el try-catch, el hilo segu�a y segu�a hasta el final
//			en vez de parar como yo quer�a
//			porque s�lo lanzo una interrupci�n desde el main
//			y �sta ya la hab�a tratado el catch de modo que no deten�a el hilo
			
			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return BigInteger.ZERO;	// --- me hab�a olvidado de este return !!!!
				}
				if(Thread.currentThread().isInterrupted()) {
					System.out.println("Detenido al calcular la potencia " + i);
					System.out.println("El c�lculo de la potencia se ha interrumpido prematuramente");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}
			return result;
		}
	}
}
