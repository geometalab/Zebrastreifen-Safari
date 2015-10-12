package ch.hsr.zebrastreifensafari.gui.view;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class ObservableHelper extends Observable {

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }

}
