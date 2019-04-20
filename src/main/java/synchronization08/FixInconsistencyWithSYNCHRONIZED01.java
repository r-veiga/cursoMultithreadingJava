package synchronization08;

/**
 * Es la soluci�n al ejemplo del paquete anterior "sharingresources07" que se 
 * dise�� para ser inconsistente porque sus operaciones no eran at�micas.
 * 
 * En este caso, solucionamos mediante el uso de la palabra clave SYNCHRONIZED 
 * en las operaciones al recurso compartido "InventoryCounter"
 *
 * Se usa SYNCHRONIZED a nivel de bloque de c�digo dentro de los m�todos, 
 * y no a nivel de m�todo. 
 * Es necesario que cree un objeto (uno o m�s) a modo de token.  
 * (como hice en la soluci�n previa que codifiqu� en otra clase en este mismo paquete)  
 */
public class FixInconsistencyWithSYNCHRONIZED01 {

	public static final int INVENTARIO_VACIO = 0;
	
	public static void main(String[] args) throws InterruptedException {

		InventoryCounter inventario = new InventoryCounter();		
		IncrementingThread hiloSumaUnidad;
		DecrementingThread hiloRestaUnidad;
		
		// -----------------------------------------------
		// -- INICIO1 - Los c�lculos saldr�an err�neos, pero est� corregido con synchronized
		// -- INICIO1 - porque se lanzan los hilos consecutivamente sin esperar
		// -----------------------------------------------
		inventario.setItems(INVENTARIO_VACIO);
		hiloSumaUnidad = new IncrementingThread(inventario);
		hiloRestaUnidad = new DecrementingThread(inventario);
		
		hiloSumaUnidad.start();
		hiloRestaUnidad.start();
		
		hiloSumaUnidad.join();
		hiloRestaUnidad.join();

		System.out.println("Aunque [hilo1-hilo2].start() y despu�s [hilo1-hilo2].join(), usando SYNCHRONIZED el inventario no sale err�neo: " + inventario.getItems());
		// -----------------------------------------------
		// -- FINAL1 - Los c�lculos saldr�an err�neos, pero est� corregido con synchronized
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
		private Object testigoDeBloqueo = new Object();

		public InventoryCounter() 			{ this.items = 0; }
		public InventoryCounter(int items) 	{ this.items = items; }
		
		public int getItems() {
			synchronized(testigoDeBloqueo) {
				return items; 
			}
		} 
		public void setItems(int items)	{ 
			synchronized(testigoDeBloqueo) {
				this.items = items; 
			}
		} 
		
		public int increment() { 
			synchronized(testigoDeBloqueo) {
				return ++items; 
			}
		} 
		
		public int decrement() { 
			synchronized(testigoDeBloqueo) {
				return --items; 
			}
		} 
	}
}
