package join04;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

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
		}
		
		// No se me muestra el factorial cuando es muy grande y el hilo sigue colgado		
		for(int i = 0; i < inputNumbers.size(); i++) {
			FactorialThread hiloFactorial = hilos.get(i);
			if(hiloFactorial.isFinished()) {
				System.out.println("Hilo " + hiloFactorial.getName() + " ha finalizado."
						+ " El factorial de " + hiloFactorial.inputNumber + " es " + hiloFactorial.getResult());
			}
			else {
//				System.out.println("Hilo " + hiloFactorial.getName() + " continúa activo."
//						+ " No se ha calculado el factorial de " + hiloFactorial.inputNumber);
				System.out.println("No se ha calculado el factorial de " + inputNumbers.get(i));
			}
		}
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
			this.result = factorial(inputNumber);
			this.isFinished = true;
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
