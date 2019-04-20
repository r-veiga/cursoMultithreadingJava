package sharingresources07;

public class SharingWhenOperationsAreNotAtomicBringsInconsistency {

	public static final int INVENTARIO_VACIO = 0;
	
	public static void main(String[] args) throws InterruptedException {

		InventoryCounter inventario = new InventoryCounter();		
		IncrementingThread hiloSumaUnidad;
		DecrementingThread hiloRestaUnidad;
		
		// -----------------------------------------------
		// -- INICIO1 - Los cálculos saldrán erróneos
		// -- INICIO1 - porque se lanzan los hilos consecutivamente sin esperar
		// -----------------------------------------------
		inventario.setItems(INVENTARIO_VACIO);
		hiloSumaUnidad = new IncrementingThread(inventario);
		hiloRestaUnidad = new DecrementingThread(inventario);
		
		hiloSumaUnidad.start();
		hiloRestaUnidad.start();
		
		hiloSumaUnidad.join();
		hiloRestaUnidad.join();

		System.out.println("Cuando [hilo1-hilo2].start() y después [hilo1-hilo2].join(), el inventario sale erróneo: " + inventario.getItems());
		// -----------------------------------------------
		// -- FINAL1 - Los cálculos saldrán erróneos
		// -- FINAL1 - porque se lanzan los hilos consecutivamente sin esperar
		// -----------------------------------------------
		
		// -----------------------------------------------
		// -- INICIO2 - Los cálculos saldrán correctos
		// -- INICIO2 - porque se espera a que termine cada hilo
		// -----------------------------------------------
		inventario.setItems(INVENTARIO_VACIO);
		hiloSumaUnidad = new IncrementingThread(inventario);
		hiloRestaUnidad = new DecrementingThread(inventario);
		
		hiloSumaUnidad.start();
		hiloSumaUnidad.join();
		
		hiloRestaUnidad.start();
		hiloRestaUnidad.join();
		
		System.out.println("Cuando hilo1.[start-join]() y después hilo2.[start-join](), el inventario sale correcto: " + inventario.getItems());
		// -----------------------------------------------
		// -- FINAL2 - Los cálculos saldrán correctos
		// -- FINAL2 - porque se espera a que termine cada hilo
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

		public InventoryCounter() 			{ }
		public InventoryCounter(int items) 	{ this.items = items; }
		
		public int getItems() 			{ return items; } 
		public void setItems(int items) { this.items = items; }
		
		public int increment() { return ++items; }
		public int decrement() { return --items; }
	}
}
