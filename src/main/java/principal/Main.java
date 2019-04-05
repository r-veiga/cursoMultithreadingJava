package principal;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		// Primer hilo, creado sin lambdas
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(" >> Estamos en el hilo: " + Thread.currentThread().getName());
				System.out.println(" >> La prioridad del hilo actual es: " + Thread.currentThread().getPriority());
			}
		});

		// Segundo hilo, creado con lambdas
		Thread hiloLanzaExcepcion = new Thread( () -> {
			System.out.println(" ** " + Thread.currentThread().getName() + " ** Hilo que lanza excepción forzada");
			System.out.println(" ** La prioridad del hilo actual es: " + Thread.currentThread().getPriority());
		});

		hilo.setName("Nuevo hilo de trabajo");
		hiloLanzaExcepcion.setName("Nuevo hilo de trabajo que lanza excepción");
		hilo.setPriority(Thread.MAX_PRIORITY);

		System.out.println("Estamos en el hilo: " + Thread.currentThread().getName() + " antes de iniciar nuevo hilo");

		hilo.start();
		hiloLanzaExcepcion.start();

		System.out.println("Estamos en el hilo: " + Thread.currentThread().getName() + " después de iniciar nuevo hilo");

		Thread.sleep(15000);
	}

}
