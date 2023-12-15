import java.util.ArrayList;
import java.util.Random;

public class Aparcamiento {
    public static void main(String[] args) throws Exception {
        int plazas = Integer.parseInt(args[0]);
        int totalCoches = Integer.parseInt(args[1]);
        Coche[] coches = new Coche[totalCoches];
        
        Parquing parquing = new Parquing(plazas);
        parquing.printPlazas();
        for (int i = 0; i < coches.length; i++) {
            coches[i] = new Coche(parquing);
            coches[i].start();
        }

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

    public synchronized void entrarAlParking(Coche coche) throws InterruptedException {
        while (!plazas.contains(null)) {
            wait();
        }
        // Entrar
        int plazaLibre = plazas.indexOf(null);
        plazas.set(plazaLibre, coche);

        printPlazas();
    }

    public synchronized void salirDelParking(Coche coche) {
        // Salimos
        int plazaLibre = plazas.indexOf(coche);
        plazas.set(plazaLibre, null);
        notifyAll();
        printPlazas();
    }
}

/**
 * Coche
 */
class Coche extends Thread {

    Parquing parquing;

    public Coche(Parquing parquing) {
        this.parquing = parquing;
    }

    @Override
    public void run() {
        Random r = new Random();
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
