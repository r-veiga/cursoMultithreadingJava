package synchronization08;

/**
 * Es la solución al ejemplo del paquete anterior "sharingresources07" que se 
 * diseñó para ser inconsistente porque sus operaciones no eran atómicas.
 * 
 * En este caso, solucionamos mediante el uso de la palabra clave SYNCHRONIZED 
 * en las operaciones al recurso compartido "InventoryCounter"
 *
 */
public class FixInconsistencyWithSYNCHRONIZED02 {

	public static final int INVENTARIO_VACIO = 0;
	
	public static void main(String[] args) throws InterruptedException {

		InventoryCounter inventario = new InventoryCounter();		
		IncrementingThread hiloSumaUnidad;
		DecrementingThread hiloRestaUnidad;
		
		// -----------------------------------------------
		// -- INICIO1 - Los cálculos saldrían erróneos, pero está corregido con synchronized
		// -- INICIO1 - porque se lanzan los hilos consecutivamente sin esperar
		// -----------------------------------------------
		inventario.setItems(INVENTARIO_VACIO);
		hiloSumaUnidad = new IncrementingThread(inventario);
		hiloRestaUnidad = new DecrementingThread(inventario);
		
		hiloSumaUnidad.start();
		hiloRestaUnidad.start();
		
		hiloSumaUnidad.join();
		hiloRestaUnidad.join();

		System.out.println("Aunque [hilo1-hilo2].start() y después [hilo1-hilo2].join(), usando SYNCHRONIZED el inventario no sale erróneo: " + inventario.getItems());
		// -----------------------------------------------
		// -- FINAL1 - Los cálculos saldrían erróneos, pero está corregido con synchronized
		// -- FINAL1 - porque se lanzan los hilos consecutivamente sin esperar
		// -----------------------------------------------
	}
	
	public static class IncrementingThread extends Thread {
		private InventoryCounter miInventario;
		
		public IncrementingThread(InventoryCounter miInventario) {
			this.miInventario = miInventario;
		}
		
		public InventoryCounter getMiInventario() {
			return miInventario;
		}
		
		public void setMiInventario(InventoryCounter miInventario) {
			this.miInventario = miInventario;
		}
		
		@Override
		public void run() {
			for(int i = 0 ; i < 10000 ; i++) { miInventario.increment(); }
		}
	}

	public static class DecrementingThread extends Thread {
		private InventoryCounter miInventario;

		public DecrementingThread(InventoryCounter miInventario) {
			this.miInventario = miInventario;
		}

		@Override
		public void run() {
			for(int i = 0 ; i < 10000 ; i++) { miInventario.decrement(); }
		}
	}
	
	private static class InventoryCounter {
		private int items;

		public InventoryCounter() 			{ this.items = 0; }
		public InventoryCounter(int items) 	{ this.items = items; }
		
		public synchronized int getItems() 				{ return items; } 
		public synchronized void setItems(int items)	{ this.items = items; }
		
		public synchronized int increment() { return ++items; }
		public synchronized int decrement() { return --items; }
	}
}
