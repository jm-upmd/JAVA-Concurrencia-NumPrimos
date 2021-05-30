import java.util.Scanner;


/**
 * Programa que arranca varios threads (hilos), cada uno de ellos calcula
 * el numero de primos comprendidos en un rango, cuyo tamaño es igual para todos los threads.  
 * El usuario especifa el número de threads a utilizar.  
 * El objetivo es comprobar que a mayor número de hilos el tiempo de compunto total
 * es menor.
 */
public class ThreadTest3 {
    
    private final static int MAX = 50_000_000;
    private final static int MAX_THREADS = 50;
    static int totalPrimes;


    /**
     * Cuando un thread de  esta clase sea ejecutado contará el número
     * de primos entre desde y hasta.  Imprimirá el resultado en
     * la salida estandart, su número de id y el tiempo consumido
     * entre el inicio y fin de su ejecución.
     */
    private static class CountPrimesThread extends Thread {
        int id;  // An id number for this thread; specified in the constructor.
        int desde,hasta; // Intervalo de numeros para calcular núm. de primos
        int count;
        public CountPrimesThread(int id, int desde, int hasta) {
        	this.id = id;
        	this.desde = desde;
        	this.hasta = hasta;
        }
        public void run() {
            long startTime = System.currentTimeMillis();
            
            count = countPrimes(desde,hasta);
            
            // Lamada a metodo sincronizado 
            sumaTotal(count);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Thread " + id + " contados " + 
                    count + " primos in " + (elapsedTime/1000.0) + " segundos.");
        }
    }


    /**
     * Arranca varios CountPrimesThread. El número de threads, entre 1 y 25,
     * es especificado por el usuario.
     */
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
        int numberOfThreads = 0;
        
        System.out.println("Número de procesadores: " + Runtime.getRuntime().availableProcessors());
        
        while (numberOfThreads < 1 || numberOfThreads > MAX_THREADS) {
            System.out.print("Cuantos hilos quieres usar  (de 1 a " + MAX_THREADS + " ) ?  ");
            numberOfThreads = sc.nextInt();
            if (numberOfThreads < 1 || numberOfThreads > MAX_THREADS)
                System.out.println("¡ Por favor introduce un número entre 1 y " + MAX_THREADS + " !");
        }
        
                      
        System.out.println("\nCreando " + numberOfThreads + " contador-primos hilos...");
        CountPrimesThread[] worker = new CountPrimesThread[numberOfThreads];
        
        int first = 2;  // Primer valor del rango
        int numsPerThread = MAX/numberOfThreads;  // Números en cada hilo
        int rest = MAX % numberOfThreads;  // Resto de hilos. Se añadirán al último hilo.
        int last;
        
        
        // Reparte los numeros entro los threads y al ultimo le asigna el resto si los hay
        
        Long time = System.currentTimeMillis();
        
        for (int i = 0; i < numberOfThreads; i++) {
        	last = numsPerThread*(i+1);  // Ultimo valor del rango
        	
        	// Añáde resto de números al último thread
        	if( rest != 0 && i == numberOfThreads -1 )
        		last+=rest; 
        	            
        	worker[i] = new CountPrimesThread( i, first, last );

        	// Primer valor del siguiente rango.
        	first = last + 1;
            
            
        }
        
        System.out.println("Creando y arrancando hilos");
       
        for (int i = 0; i < numberOfThreads; i++) {
            worker[i].start();
        }
        
        System.out.println("Esperando la terminación de ejecución de todos lo hilos...");
        
        for (int i = 0; i < numberOfThreads; i++) {
            try {
				worker[i].join();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }    
      
        System.out.println("Ejecución finalizada.");
       
       
         System.out.println("Total primos: " + totalPrimes);

         System.out.println("Tiempo(sg): " + ((System.currentTimeMillis() -time ) / 1_000.0));
      
    }
    
    
    // Método sincronizado. Asegura que no es accedido simultaneamente por dos threads.
    synchronized static private void sumaTotal(int n) {
    	totalPrimes+=n;
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


} // end class 

