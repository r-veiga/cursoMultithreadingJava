package interruption03;

import java.math.BigInteger;

/**
 * 
 * Cuando convierto un hilo a tipo DAEMON, 
 * que debe ser antes de arrancarlo con start(), 
 * este hilo ya no se comporta como los hilos de proceso normales. 
 * 
 * Los hilos de tipo DAEMON finalizan automáticamente cuando no queda 
 * ningún hilo de proceso vivo (como p.ej. el hilo MAIN).
 * 
 * Los hilos de proceso, por el contrario, continúan ejecutándose 
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

//			Aquí me ha pasado algo muy curioso, 
//			tengo dos bloques distintos para gestionar el thread.interrupt()
//			un try-catch y también un if
//			por tanto, dependiendo del azar, la interrupción la podía recoger cualquiera de los dos
//			en el try-catch había olvidado poner el return 
//			así que cuando entraba por el try-catch, el hilo seguía y seguía hasta el final
//			en vez de parar como yo quería
//			porque sólo lanzo una interrupción desde el main
//			y ésta ya la había tratado el catch de modo que no detenía el hilo
			
			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return BigInteger.ZERO;	// --- me había olvidado de este return !!!!
				}
				if(Thread.currentThread().isInterrupted()) {
					System.out.println("Detenido al calcular la potencia " + i);
					System.out.println("El cálculo de la potencia se ha interrumpido prematuramente");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}
			return result;
		}
	}
}
