/**
 * com.beyondspock.jmx package contains the JMX support classes and interfaces
 */
package com.beyondspock.jmx;

/**
 * TerraNova class represents the JMX link to the application
 * 
 * @author Fabio Riberto
 *
 */
public class TerraNova implements TerraNovaMBean {

	/**
	 * threadCount member
	 *
	 */
	private int threadCount;

	/**
	 * TerraNova constructor
	 * 
	 * @param TerraNova
	 */
	public TerraNova(int numThreads) {
		this.threadCount = numThreads;
	}

	@Override
	public void setThreadCount(int noOfThreads) {
	}

	@Override
	public int getThreadCount() {
		return 0;
	}

	@Override
	public String doConfig() {
		return "Number of Threads: ".concat(String.valueOf(this.threadCount));
	}

}
