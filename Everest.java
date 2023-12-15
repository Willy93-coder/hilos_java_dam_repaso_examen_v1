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

class Cima {

    private int atrapados;

    public Cima(int atrapados) {
        this.atrapados = atrapados;
    }

    public int getAtrapados() {
        return atrapados;
    }

    // Metodo para rescatar personas - Setter de los atrapados
    public synchronized int rescatar(Helicoptero helicoptero) throws InterruptedException {
        // Mostramos que hilo esta en la cima
        System.out.println(helicoptero.getName() + " : estoy en la cima. Procedo al rescate");
        // Dormimos el hilo entre 2 a 4 segundos
        Thread.sleep(new Random().nextInt(2000, 4000));
        // Variable interna para saber cuantas personas hemos rescatado
        int rescatados = 0;
        // Comprobamos si la capacidad del Helicoptero es mayor a los atrapados - Asi evitamos que pueda llevarse mas gente de la que queda atrapada
        if (helicoptero.capacidad > atrapados) {
            // Solor atrapados
            rescatados = atrapados; // Rescatados es igual a atrapados - Si el Helicoptero tiene capacidad para 5 y solo quedan 2, se lleva 2
        } else {
            rescatados = helicoptero.capacidad; // Se lleva su capacidad si hay mas personas que sitios
        }

        // Los rescatados modifican el total de atrapados
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
        // Mientras que en la cima queden personas los Helicopteros siguen subiendo a la montania
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