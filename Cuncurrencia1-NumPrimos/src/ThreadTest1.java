import java.util.Scanner;



/**
* Un programa que inicia varios subprocesos, cada uno de los cuales realiza el
* mismo cálculo. El usuario especifica el número de subprocesos.
* El objetivo es ver que los hilos terminan en un orden indeterminado.
*/

public class ThreadTest1 {
    
    private final static int MAX = 5_000_000;


    /* Cuando se ejecute un hilo que pertenece a esta clase, contará el
    * número de primos entre 2 y MAX. Imprimirá el resultado
    * a la salida estándar, junto con su número de identificación y el tiempo transcurrido
    * entre el inicio y el final del cálculo.
    */
    
    private static class CountPrimesThread extends Thread {
        int id;  // An id number for this thread; specified in the constructor.
        public CountPrimesThread(int id) {
            this.id = id;
        }
        public void run() {
            long startTime = System.currentTimeMillis();
            int count = countPrimes(2,MAX);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Thread " + id + " counted " + 
                    count + " primes in " + (elapsedTime/1000.0) + " seconds.");
        }
    }


    /**
    * Inicie varios CountPrimesThreads. El número de subprocesos, entre 1 y 25,
    * lo especifica el usuario.
    */
    
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
        int numberOfThreads = 0;
        while (numberOfThreads < 1 || numberOfThreads > 25) {
            System.out.print("How many threads do you want to use  (from 1 to 25) ?  ");
            numberOfThreads = sc.nextInt();
            if (numberOfThreads < 1 || numberOfThreads > 25)
                System.out.println("Please enter a number between 1 and 25 !");
        }
        System.out.println("\nCreating " + numberOfThreads + " prime-counting threads...");
        CountPrimesThread[] worker = new CountPrimesThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++)
            worker[i] = new CountPrimesThread( i );
        for (int i = 0; i < numberOfThreads; i++)
            worker[i].start();
        System.out.println("Threads have been created and started.");
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

