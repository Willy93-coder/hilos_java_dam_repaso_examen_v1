public class CenaFilosofos {
    public static void main(String[] args) {
        Mesa mesa = new Mesa(5);
        Filosofo[] filosofos = new Filosofo[5];

        for (int i = 0; i < filosofos.length; i++) {
            filosofos[i] = new Filosofo(mesa, i);
            filosofos[i].start();
        }

        for (int i = 0; i < filosofos.length; i++) {
            try {
                filosofos[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}

class Mesa {

    // Creamos el array de booleanos
    private boolean[] tenedores;

    // Pasamos al constructor cuantos tenedores hay
    public Mesa(int tenedores) {
        this.tenedores = new boolean[tenedores];
    }

    // Metodo para coger el tenedor izquierdo
    public int tenedorIzquierda(int indComensal) {
        return indComensal;  // El tenedor izquierdo es el mismo que comensal
    }

    // Metodo para coger el tenedor derecho
    public int tenedorDerecha(int indComensal) {
        if (indComensal == 0) {
            return tenedores.length - 1; // Aqui la posicion del comensal es 0 y tenemos que coger el tenedor en la posicion 4
        } else {
            return indComensal - 1; // El tenedor derecho es el comensal menos 1
        }
    }

    // Medodo para coger el tenedor
    public synchronized void cogerTenedores(int indComensal) {
        // Mientras uno de las posiciones de los tenedores tenedores este en true algun comensal no podra comer porque un tenedor que puede usar esta cogido
        while (tenedores[tenedorIzquierda(indComensal)] || tenedores[tenedorDerecha(indComensal)]) {
            try {
                wait(); // Los mandamos a esperar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        // Asignamos las posiciones a true
        tenedores[tenedorIzquierda(indComensal)] = true;
        tenedores[tenedorDerecha(indComensal)] = true;
    }

    // Metodo para dejar los tenedores
    public synchronized void dejarTenedores(int indComensal) {
        // Asignamos las posiciones a false para liberar todos los tenedors
        tenedores[tenedorIzquierda(indComensal)] = false;
        tenedores[tenedorDerecha(indComensal)] = false;
        notifyAll();
    }

}

class Filosofo extends Thread {

    private Mesa mesa;
    private int indComensal;

    public Filosofo(Mesa mesa, int indComensal) {
        this.mesa = mesa;
        this.indComensal = indComensal;
    }

    // Metodo de empezar comer
    public void comiendo() {
        System.out.println(getName() + " estoy comiendo...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    // Metodo de dejar de comer
    public void pensando() {
        System.out.println(getName() + " pensando...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Mientras no se interrumpan los filosofos seguiran comiendo y dejando de comer
        while (!Thread.interrupted()) {
            // Piensan
            pensando();
            // Coger tenedores
            mesa.cogerTenedores(indComensal);
            // Comiendo
            comiendo();
            System.out.println("Filosofo " + getName() + " deja de comer.");
            // Dejar tenedores
            mesa.dejarTenedores(indComensal);
        }
    }
}
