package ch.hsr.zebrastreifensafari.service;

import ch.hsr.zebrastreifensafari.gui.modify.ModifyGUI;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class ObservableHelper extends Observable {

    private final ModifyGUI observable;

    public ObservableHelper(ModifyGUI observable) {
        this.observable = observable;
    }

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

    public ModifyGUI getObservable() {
        return observable;
    }
}
