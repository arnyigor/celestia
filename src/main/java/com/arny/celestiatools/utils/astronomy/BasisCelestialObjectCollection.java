package com.arny.celestiatools.utils.astronomy;

import java.util.ArrayList;
import java.util.Iterator;

public class BasisCelestialObjectCollection implements Iterable<BasisCelestialObject> {
    protected ArrayList<BasisCelestialObject> basisCelestialObjects;
    private int stopIndex;

    public BasisCelestialObjectCollection() {
        this.stopIndex = 0;
        this.basisCelestialObjects = new ArrayList();
    }

    public BasisCelestialObjectCollection(int length) {
        this.stopIndex = 0;
        this.basisCelestialObjects = new ArrayList(length);
    }

    public BasisCelestialObjectCollection(BasisCelestialObjectCollection original) {
        this.stopIndex = 0;
        this.basisCelestialObjects = new ArrayList(original.size());
        this.basisCelestialObjects.addAll(original.basisCelestialObjects);
    }

    public BasisCelestialObjectCollection copy() {
        return new BasisCelestialObjectCollection(this);
    }

    public void add(BasisCelestialObject o) {
        this.basisCelestialObjects.add(o);
    }

    public void addAll(BasisCelestialObjectCollection collection) {
        this.basisCelestialObjects.addAll(collection.basisCelestialObjects);
    }

    public BasisCelestialObject get(int i) {
        try {
            return (BasisCelestialObject) this.basisCelestialObjects.get(i);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(BasisCelestialObject o) {
        int i = this.basisCelestialObjects.indexOf(o);
        if (i >= 0) {
            this.basisCelestialObjects.remove(i);
        }
    }

    public ArrayList<BasisCelestialObject> getAll() {
        return this.basisCelestialObjects;
    }

    public Iterator<BasisCelestialObject> iterator() {
        return this.basisCelestialObjects.iterator();
    }

    public void clear() {
        this.basisCelestialObjects.clear();
    }

    public BasisCelestialObject search(String key) {
        Iterator i$ = this.basisCelestialObjects.iterator();
        while (i$.hasNext()) {
            BasisCelestialObject bco = (BasisCelestialObject) i$.next();
            if (bco.search(key)) {
                return bco;
            }
        }
        return null;
    }

    public int getStopIndex() {
        return this.stopIndex;
    }

    public void setStopIndex(int stopIndex) {
        this.stopIndex = stopIndex;
    }

    public int size() {
        return this.basisCelestialObjects.size();
    }
}
