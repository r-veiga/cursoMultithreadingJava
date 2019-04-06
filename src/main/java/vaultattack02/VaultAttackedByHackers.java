package vaultattack02;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaultAttackedByHackers {

	public static final int MAX_VALUE = 9999;
	
	public static void main(String[] args) {
		Random random = new Random();
		Vault vault = new Vault(random.nextInt(MAX_VALUE));
		
		List<Thread> threads = new ArrayList<>();
		threads.add(new AscendingHackerThread(vault));
		threads.add(new DescendingHackerThread(vault));
		threads.add(new PoliceThread());
		
		for(Thread thread : threads) {
			thread.start();
		}
	}
	
	private static class Vault {
		private int password; 
		public Vault (int password) {
			this.password = password;
		}
		public boolean isPasswordCorrect(int guess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
			return this.password == guess;
		}
	}

	public static abstract class HackerThread extends Thread { 
		protected Vault vault; 
		public HackerThread (Vault vault) {
			this.vault = vault;
			this.setName(this.getClass().getSimpleName());
			this.setPriority(Thread.MAX_PRIORITY);
		}
		@Override
		public void start() {
			System.out.println("Comienza el hilo " + this.getName());
			super.start();
		}
	}
	
	private static class AscendingHackerThread extends HackerThread {
		public AscendingHackerThread(Vault vault) {
			super(vault);
		}
		@Override
		public void run() {
			for(int i = 0; i < MAX_VALUE; i++) {
				if(vault.isPasswordCorrect(i)) {
					System.out.println( this.getName() + " ha encontrado la contaseña " + i);
					System.exit(0);
				}
			}
		}
	}
	
	private static class DescendingHackerThread extends HackerThread {
		public DescendingHackerThread(Vault vault) {
			super(vault);
		}
		@Override
		public void run() {
			for(int i = MAX_VALUE ; i >= 0 ; i--) {
				if(vault.isPasswordCorrect(i)) {
					System.out.println( this.getName() + " ha encontrado la contaseña " + i);
					System.exit(0);
				}
			}
		}
	}
	private static class PoliceThread extends Thread {
		@Override
		public void run() { 
			for(int i = 10 ; i > 0 ; i--) {
				try {
					Thread.sleep(1000);
					System.out.println("Quedan " + i + " segundos para que la policía intervenga");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			System.out.println("La policía ha intervenido antes de que los ladrones abran la caja"); 
		}
	}
}
