package com.arny.celestiatools.utils.astro;

/**
 * Player Class
 */
class OrbitPlayer implements Runnable {
	OrbitViewer orbitViewer;

	/**
	 * Constructor
	 */
	public OrbitPlayer(OrbitViewer orbitViewer) {
		this.orbitViewer = orbitViewer;
	}

	/**
	 * Play forever
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
			ATime atime = orbitViewer.getAtime();
            double ed = orbitViewer.getEsDistance();
            orbitViewer.DynamicTimeStep(ed);
            orbitViewer.minEdistance(ed,atime);
            orbitViewer.averageEdistance(ed);
            atime.changeDate(orbitViewer.timeStep, orbitViewer.playDirection);
            orbitViewer.setNewDate(atime);
		}
	}
}
