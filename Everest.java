import java.util.Random;

public class Everest {
    public static void main(String[] args) throws InterruptedException {
        Cima cima = new Cima(57);

        Helicoptero h1 = new Helicoptero(cima, 5);
        Helicoptero h2 = new Helicoptero(cima, 3);
        Helicoptero h3 = new Helicoptero(cima, 1);

        h1.start();
        h2.start();
        h3.start();

        h1.join();
        h2.join();
        h3.join();

        System.out.println("Rescate finalizado con éxito. 0 bajas.");
    }
}

/**
 * Cima
 */
class Cima {

    private int atrapados;

    public Cima(int atrapados) {
        this.atrapados = atrapados;
    }

    public int getAtrapados() {
        return atrapados;
    }

    public synchronized int rescatar(Helicoptero helicoptero) throws InterruptedException {
        System.out.println(helicoptero.getName() + " : estoy en la cima. Procedo al rescate");
        Thread.sleep(new Random().nextInt(2000, 4000));
        int rescatados = 0;
        if (helicoptero.capacidad > atrapados) {
            // Solor atrapados
            rescatados = atrapados;
        } else {
            rescatados = helicoptero.capacidad;
        }

        // Los rescatados
        atrapados -= rescatados;
        System.out.println(helicoptero.getName() + " : estoy en la cima. He rescatado a " + rescatados + " atrapados. Quedan " + atrapados);
        return rescatados;
    }
}

/**
 * Helicoptero
 */
class Helicoptero extends Thread {
    Cima cima;
    int capacidad;
    public Helicoptero(Cima cima, int capacidad) {
        this.cima = cima;
        this.capacidad = capacidad;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (cima.getAtrapados() > 0) {
            try {
                // Ida
                System.out.println(getName() + " : vamos a la cima");
                Thread.sleep(r.nextInt(2000, 4000));
                // Carga
                int rescatados = cima.rescatar(this);
                // Vuelta
                System.out.println(getName() + " : vamos a la base");
                Thread.sleep(r.nextInt(2000, 4000));
                // Descarga
                System.out.println(getName() + ": estoy en la base, he rescatado a " + rescatados);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}