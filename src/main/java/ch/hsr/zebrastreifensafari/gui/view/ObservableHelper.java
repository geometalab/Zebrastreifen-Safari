package ch.hsr.zebrastreifensafari.gui.view;

import ch.hsr.zebrastreifensafari.gui.CreateEditGUI;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class ObservableHelper extends Observable {

    private final CreateEditGUI observable;

    public ObservableHelper(CreateEditGUI observable) {
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

    public CreateEditGUI getObservable() {
        return observable;
    }
}
