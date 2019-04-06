package interruption03;

public class InterrupcionTipo1 {

	public static void main(String[] args) {

		Thread hilo = new Thread(new BlockingClass());
		hilo.start();
		hilo.interrupt();
	}

	private static class BlockingClass implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				System.out.println("Saliendo del hilo bloqueante de 50 seg.");
			}
		}
	}
}
