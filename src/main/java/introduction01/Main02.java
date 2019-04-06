package introduction01;

public class Main02 {

	public static void main(String[] args) throws InterruptedException {		
		Thread hilo = new HiloNuevo();
		hilo.start();
	}

	// La clase Thread implementa el interfaz Runnable
	private static class HiloNuevo extends Thread {
		@Override
		public void run() {
			System.out.println(" >> Estamos en el hilo: " + Thread.currentThread().getName());
			System.out.println(" >> La prioridad del hilo actual es: " + Thread.currentThread().getPriority());
		}
	}
}
