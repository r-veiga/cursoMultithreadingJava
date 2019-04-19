package join04;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Usando .join() desde el hilo principal, contra otros hilos, 
 * se fuerza a que el hilo principal no finalice hasta que finalicen los demás hilos, 
 * el hilo principal espera a que estos otros hilos terminen primero.
 * 
 * @author r-veiga
 *
 */
public class HilosEsperandoseDebidoAPalabraJOIN {

	public static void main(String[] args) throws InterruptedException {
		List<Long> inputNumbers = Arrays.asList(100000L, 3435L, 5L, 2324L, 4656L, 23L, 2435L, 5566L);
		// Calculo el factorial de cada número
		
		List<FactorialThread> hilos = new ArrayList<>();
		
		for(long inputNumber : inputNumbers) {
			hilos.add(new FactorialThread(inputNumber));
		}
		
		for(Thread hilo : hilos) {
			hilo.setDaemon(true);
			hilo.start();
		}
		
		// Hace que el hilo espere a que termine
		for(Thread hilo : hilos) {
			hilo.join(5000);	// Lanza InterruptedException
//			hilo.join();		// Lanza InterruptedException
		}
		
//		Ahora mismo resulta que 
//		1 - imprime el factorial cuando al hilo le da tiempo a calcularlo 
//		2 - imprime el mensaje de que no le ha dado tiempo cuando salta el timeout del join y el hilo aún no había finalizado sus cálculos
//		3 - no imprime el factorial de 100000L, cuando el timeout es enorme 
//		    el hilo ha acabado el cálculo, pero no puede imprimir el valor del factorial (se ha solucionado haciendo un 
//		    substring)
		for(int i = 0; i < inputNumbers.size(); i++) {
			FactorialThread hiloFactorial = hilos.get(i);
			boolean threadIsFinished = hiloFactorial.isFinished();
			if(threadIsFinished) {
				imprimirFactorial(hiloFactorial);
			}
			else {
				imprimirFactorialNoPudoCalcularse(hiloFactorial, threadIsFinished);
			}
		}
	}

	private static void imprimirFactorialNoPudoCalcularse(FactorialThread hiloFactorial, boolean threadIsFinished) {
		System.out.println("Hilo " + hiloFactorial.getName() + " no ha finalizado, \"" 
				+ hiloFactorial.getName() + ".isFinished()=" + threadIsFinished
				+ "\". No se ha calculado el factorial de " + hiloFactorial.inputNumber);
	}

	private static void imprimirFactorial(FactorialThread hiloFactorial) {
		System.out.println("Hilo " + hiloFactorial.getName() + " ha finalizado."
				+ " El factorial de " + hiloFactorial.inputNumber + " es " + hiloFactorial.getResult().toString().substring(0,2));
	}
	
	public static class FactorialThread extends Thread {
		private long inputNumber;
		private BigInteger result = BigInteger.ZERO;
		private boolean isFinished = false;
		
		public FactorialThread(long inputNumber) {
			this.inputNumber = inputNumber;
		}

		@Override
		public void run() {
			System.out.println(this.getName() + " iniciado en " + System.currentTimeMillis() + " milisegundos");
			this.result = factorial(inputNumber);
			this.isFinished = true;
			System.out.println(this.getName() + " finalizado en " + System.currentTimeMillis() + " milisegundos");

		}

		private BigInteger factorial(long n) {
			BigInteger tempResult = BigInteger.ONE;
			
			for(long i = n ; i > 0 ; i-- ) {
				tempResult = tempResult.multiply(BigInteger.valueOf(i));
			}
			return tempResult;
		}
		
		public boolean isFinished() {
			return isFinished;
		}
		
		public BigInteger getResult() {
			return result;
		}
	}

}
