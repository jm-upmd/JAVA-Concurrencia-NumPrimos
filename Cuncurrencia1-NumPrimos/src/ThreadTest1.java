import java.util.Scanner;



/**
* Un programa que inicia varios subprocesos, cada uno de los cuales realiza el
* mismo cálculo. El usuario especifica el número de subprocesos.
* El objetivo es ver que los hilos terminan en un orden indeterminado y que la
* gestión de los hilos invierte tiempo adicional al propio calculo.
*/

public class ThreadTest1 {
    
    private final static int MAX = 5_000_000;
    private final static int MAX_THREADS = 25;



    
    private static class CountPrimesThread extends Thread {
        int id;  // An id number for this thread; specified in the constructor.
        public CountPrimesThread(int id) {
            this.id = id;
        }
        public void run() {
            long startTime = System.currentTimeMillis();
            int count = countPrimes(2,MAX);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Thread " + id + " cuenta " + 
                    count + " primos en " + (elapsedTime/1000.0) + " segundos.");
        }
    }


    /**
    * Inicia varios CountPrimesThreads. El número de subprocesos, entre 1 y 25,
    * lo especifica el usuario.
    */
    
    public static void main(String[] args) {
     	
    	Scanner sc = new Scanner(System.in);
        int numberOfThreads = 0;
        while (numberOfThreads < 1 || numberOfThreads > 25) {
            System.out.print("¿Cuantos threads quieres usar  (de 1 a " + MAX_THREADS + ") ?  ");
            numberOfThreads = sc.nextInt();
            if (numberOfThreads < 1 || numberOfThreads > 25)
                System.out.println("Por favor escribe un número entre 1 y" + MAX_THREADS + "!");
        }
        System.out.println("\nCreando " + numberOfThreads + " threads cuenta-primos...");
        CountPrimesThread[] worker = new CountPrimesThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++)
            worker[i] = new CountPrimesThread( i );
        for (int i = 0; i < numberOfThreads; i++)
            worker[i].start();
        System.out.println("Los threads han sido creados y arrancados");
    }


    /**
    * Calcula y devuelve el número de números primos en el rango
    * min a max, inclusive.
    */
    
    private static int countPrimes(int min, int max) {
        int count = 0;
        for (int i = min; i <= max; i++)
            if (isPrime(i))
                count++;
        return count;
    }


    /**
    * Comprueba si x es un número primo.
    * Se supone que x es mayor que 1.
    */
    private static boolean isPrime(int x) {
    	//Un assert cuya expresión se evalúa como falso produce una excepción del tipo java.lang.AssertionError 
    	//pero para ello se han de habilitar en tiempo de ejecución como el parámetro -ea de la máquina virtual
        assert x > 1: String.format("El número dado no cumple la precondición (%d)", x);
        int top = (int)Math.sqrt(x);
        for (int i = 2; i <= top; i++)
            if ( x % i == 0 )
                return false;
        return true;
    }


} 

