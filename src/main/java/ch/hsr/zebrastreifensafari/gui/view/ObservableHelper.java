package ch.hsr.zebrastreifensafari.gui.view;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class ObservableHelper extends Observable {

    private CreateUpdateGUI observable;

    public ObservableHelper(CreateUpdateGUI observable) {
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

    public CreateUpdateGUI getObservable() {
        return observable;
    }
}
