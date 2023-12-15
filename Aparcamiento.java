import java.util.ArrayList;
import java.util.Random;

public class Aparcamiento {
    public static void main(String[] args) throws Exception {
        // Variable para guardar las plazas que recibimos por args
        int plazas = Integer.parseInt(args[0]);
        // Variable para guardar el total de coches pasado por args
        int totalCoches = Integer.parseInt(args[1]);
        // Array de coches
        Coche[] coches = new Coche[totalCoches];
        // Creamos el objeto parquing
        Parquing parquing = new Parquing(plazas);
        parquing.printPlazas();
        // Creamos los coches de forma dinamica
        for (int i = 0; i < coches.length; i++) {
            coches[i] = new Coche(parquing);
            coches[i].start();
        }

        // Esperamos a que los hilos acaben
        for (int i = 0; i < coches.length; i++) {
            coches[i].join();
        }
    }
}

class Parquing {
    private int numCoches;
    private int plazasParking;
    private ArrayList<Coche> plazas;

    public Parquing(int plazasParking) {
        plazas = new ArrayList<>();
        for (int i = 0; i < plazasParking; i++) {
            plazas.add(null);
        }
    }

    public int getNumCoches() {
        return numCoches;
    }

    public int getPlazasParking() {
        return plazasParking;
    }

    public void printPlazas() {
        System.out.println("[");
        for (int i = 0; i < plazas.size(); i++) {
            if (plazas.get(i) == null) {
                System.out.println("-----------");
            } else {
                System.out.println(plazas.get(i).getName());
            }
            System.out.println(plazas.get(i));
            if (i != plazas.size() - 1) {
                System.out.println(", ");
            }
        }
        System.out.println("]");
    }

    // Metodo entrar al parking
    public synchronized void entrarAlParking(Coche coche) throws InterruptedException {
        // Comprobamos que mientras no haya una plaza null los hilos esperen
        while (!plazas.contains(null)) {
            wait(); // Dormimos los hilos
        }
        // Entrar
        int plazaLibre = plazas.indexOf(null); // Buscamos la plaza que es null
        plazas.set(plazaLibre, coche); // Hacemos un set para introducir un coche en ese espacio

        printPlazas();
    }

    // Metodo para salir del parking
    public synchronized void salirDelParking(Coche coche) {
        // Salimos
        int plazaLibre = plazas.indexOf(coche); // Buscamos la plaza donde este el coche
        plazas.set(plazaLibre, null); // Hacemos un set para ponerla a null donde estaba el coche
        notifyAll(); // Despertamos a todos los hilos
        printPlazas();
    }
}

class Coche extends Thread {

    Parquing parquing;

    public Coche(Parquing parquing) {
        this.parquing = parquing;
    }

    @Override
    public void run() {
        Random r = new Random();
        // Mientras los hilos no se interrumpan
        while (!Thread.interrupted()) {
            // paseamos
            System.out.println(getName() + " : Estoy paseando...");
            try {
                Thread.sleep(r.nextInt(2000, 40000));
                // entramos
                System.out.println("Intento entrar...");
                parquing.entrarAlParking(this);
                // compramos
                Thread.sleep(r.nextInt(2000, 4000));
                // salimos
                parquing.salirDelParking(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
