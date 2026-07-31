package hr.tvz.revive.async;

import javafx.concurrent.Task;

public class AsinkroniZadaci {

    private static final int SIMULIRANO_KASNJENJE_MILISEKUNDI = 600;

    public Task<Void> stvoriZadatakSimulacijeObrade() {
        return new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(SIMULIRANO_KASNJENJE_MILISEKUNDI);
                return null;
            }
        };
    }

    public void pokreniZadatakUPozadini(Task<Void> zadatak) {
        Thread pozadinskaNit = new Thread(zadatak);
        pozadinskaNit.setDaemon(true);
        pozadinskaNit.start();
    }
}