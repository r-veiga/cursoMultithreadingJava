package join04;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ComplexCalculation02 {
	
	public static final BigInteger ONE = BigInteger.ONE;

	public static void main(String[] args) throws InterruptedException {
		
		BigInteger base1 = BigInteger.valueOf(2);
		BigInteger power1 = BigInteger.valueOf(8);
		
		BigInteger base2 = BigInteger.valueOf(9);
		BigInteger power2 = BigInteger.valueOf(3);
		
//		PowerCalculatingThread hilo1 = new PowerCalculatingThread(base1,power1);
//		hilo1.run();
//		System.out.println(hilo1.getResult());
//		
//		PowerCalculatingThread hilo2 = new PowerCalculatingThread(base2,power2);
//		hilo2.run();
//		System.out.println(hilo2.getResult());
		
		ComplexCalculation02 calculadora = new ComplexCalculation02();
		
		calculadora.calculateResult(base1, power1, base2, power2);
	}
	
	
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        System.out.println("Launching calculateResult(" + base1 + ", " + power1 + ", " + base2 + ", " + power2 + ");");
    	
    	BigInteger result;
        
        PowerCalculatingThread sumando1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread sumando2 = new PowerCalculatingThread(base2, power2);
		
		sumando1.start();
		sumando2.start();
		
		sumando1.join();
		sumando2.join();
		
		result = sumando1.getResult().add(sumando2.getResult());
		
		showResultInSysOut(base1, power1, base2, power2, result, sumando1, sumando2);
				
		return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = ONE;
        private BigInteger base;
        private BigInteger power;
    
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
    
        @Override
        public void run() {
        	
        	for(BigInteger bi = ONE ; bi.compareTo(power) <= 0 ; bi = bi.add(ONE)) {
        		result = result.multiply(base);
        		System.out.println("\t" 
        				+ bi + ".compareTo(" + power + ") = " + bi.compareTo(power)
        				+ "  ==>  result=" + result);
        	} 
        }
    
        public BigInteger getResult() { return result; }
    }
    
    private void showResultInSysOut(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2, BigInteger result,
    		PowerCalculatingThread sumando1, PowerCalculatingThread sumando2) {
    	System.out.println("calculateResult(...) => " + base1 + "^" + power1 + " + " + base2 + "^" + power2 + " = " 
    			+ sumando1.getResult() + " + " + sumando2.getResult() + " = " 
    			+ result);
    }
    
}
