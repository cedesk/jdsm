/*
Copyright (c) 2008, Roberto Milev
All rights reserved.

Redistribution and use in source and binary forms, with or 
without modification, are permitted provided that the following 
conditions are met:

    * Redistributions of source code must retain the above 
      copyright notice, this list of conditions and the following 
      disclaimer.
    * Redistributions in binary form must reproduce the above 
      copyright notice, this list of conditions and the following 
      disclaimer in the documentation and/or other materials 
      provided with the distribution.
    * Neither the name of the Carleton University nor the names 
      of its contributors may be used to endorse or promote 
      products derived from this software without specific prior 
      written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/
package edu.carleton.tim.jdsm.dependency.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.jscience.mathematics.number.Rational;

import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.dependency.Dependency;


/**
 * The Class ClusteredCost.
 * 
 * The metric provides a measure of modularity for the system represented by the
 * DSM transformed in a idealized modular form. In order to provide this metric
 * the method performs the following operations in this order: <br>
 * <br> 
 * - Computes vertical busses @see {@link #computeVerticalBusses(double)}<br> 
 * - Initiates clusters by placing each element in its own singleton cluster<br>
 * - Executes a stochastic clustering algorithm<br>
 * - Computes clustered cost<br>
 * - Computes relative clustered cost<br>
 *      
 * @author Roberto Milev
 */
public class ClusteredCost {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ClusteredCost.class);
	
	/** The dependency cost sum. */
	private long dependencyCostSum = 0;
	
	/** The lambda. */
	private int lambda = 2;
	
	/** The vertical busses. */
	protected List<String> verticalBusses = new ArrayList<String>();
	
	/** The dsm. */
	protected DesignStructureMatrix<Dependency> dsm;
	
	/**
	 * Compute clustered cost.
	 * 
	 * @param dsm
	 *            the DSM to be analyzed
	 * @param verticalBusTreshold
	 *            the vertical bus treshold
	 * 
	 * @return a structure containing all outputs of the analysis
	 */
	public static ClusteredCostResult computeClusteredCost(DesignStructureMatrix<Dependency> dsm, double verticalBusTreshold){
		logger.info("Started computing clustered cost.");
		ClusteredCost instance = new ClusteredCost(dsm.clone());
		instance.computeVerticalBusses(verticalBusTreshold);
		logger.info("Computed "+instance.verticalBusses.size()+" vertical busses.");
		instance.initClusters();
		logger.info("Initiated clusters.");
		logger.info("Started clustering algorithm.");
		instance.performClustering();
		logger.info("Finished clustering algorithm.");
		logger.info("Computed "+instance.dsm.getClusterStartPositionMappings().size()+" clusters.");

		long clusteredCost = instance.computeClusteredCost();
		double relativeClusteredCost = instance.computeRelativeClusteredCost();

		ClusteredCostResult result = instance.new ClusteredCostResult(instance.dsm, 
				instance.verticalBusses, clusteredCost, relativeClusteredCost);
		
		logger.info("Computed clustered cost: " + clusteredCost);
		logger.info("Computed relative clustered cost: " + relativeClusteredCost);
		return result;
	}

	/**
	 * Perform clustering. Executes a stochastic clustering algorithm to group elements into
	 * a clustering configuration that minimizes the overall cost of the DSM. 
	 * 
	 * @param dsm
	 *            the DSM to be analyzed
	 * @param verticalBusTreshold
	 *            the vertical bus treshold
	 * 
	 * @return a clustered DSM
	 */
	public static DesignStructureMatrix<Dependency> clusterDsm(DesignStructureMatrix<Dependency> dsm, double verticalBusTreshold){
		logger.info("Started computing clustered cost.");
		ClusteredCost instance = new ClusteredCost(dsm.clone());
		instance.computeVerticalBusses(verticalBusTreshold);
		logger.info("Computed "+instance.verticalBusses.size()+" vertical busses.");
		instance.initClusters();
		logger.info("Initiated clusters.");
		logger.info("Started clustering algorithm.");
		instance.performClustering();
		return instance.dsm;
	}

	/**
	 * Compute vertical busses.
	 * 
	 * @param treshold
	 *            the treshold
	 * 
	 * @return the list< string>
	 */
	public List<String> computeVerticalBusses(double treshold){
		int mapSize = dsm.getMap().length;
		List<String> result = new ArrayList<String>();
		
		for(int j = 0 ; j < mapSize; j++){
			double dependenciesCount = 0;
			for(int i = 0 ; i < mapSize; i++){
				if(dsm.getMap()[i][j].booleanValue())
					dependenciesCount++;
			}
			if((dependenciesCount / mapSize) > treshold){
					result.add(dsm.getPositionNameMappings().get(j));
			}
		}
		this.verticalBusses = result;
		return result;
	}

	/**
	 * Instantiates a new clustered cost.
	 * 
	 * @param dsm
	 *            the dsm
	 * 
	 * @throws JAXBException
	 *             the JAXB exception
	 */
	private ClusteredCost(DesignStructureMatrix<Dependency> dsm) {
		this.dsm = dsm;
	}

	/**
	 * Compute clustered cost.
	 * 
	 * @return the long
	 */
	private long computeClusteredCost(){
		long cost = 0;
		for (int i = 0; i < dsm.getMap().length; i++) {
			for (int j = 0; j < dsm.getMap().length; j++) {
				if(i != j){
					cost += computeDependencyCost(i, j);
				}
			}
		}
		return cost;
	}

	/**
	 * Compute optimal clustering.
	 * 
	 * @return the design structure matrix< dependency>
	 */
	private DesignStructureMatrix<Dependency> performClustering() {
		initClusters();
		Random random = new Random();
		dependencyCostSum = computeClusteredCost();
		int noImprovementCount = 0;
		int randomPosition;
		while(noImprovementCount < dsm.getMap().length){
			randomPosition = random.nextInt(dsm.getMap().length);
			if(!isVerticalBus(randomPosition)){
			String winningCluster = computeWinnigCluster(randomPosition);
				if(winningCluster != null) {
					moveToCluster(randomPosition, winningCluster);
					noImprovementCount = 0;
					logger.debug("Cost sum: " + dependencyCostSum+ "\tCluster count: " + dsm.getClusterStartPositionMappings().size() + "\tMoved element " + randomPosition + " to " + winningCluster);
				}
				else{
					noImprovementCount++;
				}
			}
		}
		return dsm;
	}	
	
	/**
	 * Compute dependency cost.
	 * 
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * 
	 * @return the double
	 */
	private double computeDependencyCost(int i, int j) {
		//if j is a vertical bus
		if(isVerticalBus(j)){
			return dsm.getMap()[i][j].longValue();
		}
		//if i and j are in same cluster
		else if(getCluster(i).equals(getCluster(j))) {
			return dsm.getMap()[i][j].longValue() * Math.pow(getClusterSize(getCluster(i)), lambda);
		}
		//if i and j are not in same cluster
		else{
			return dsm.getMap()[i][j].longValue() * Math.pow(dsm.getMap().length, lambda);
		}
	}
	
	/**
	 * Compute relative dependency cost.
	 * 
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * 
	 * @return the rational
	 */
	private Rational computeRelativeDependencyCost(int i, int j) {
		long N = dsm.getMap().length;
		long n = getClusterSize(getCluster(i));
		long dsmValue = dsm.getMap()[i][j].longValue();
		// if j is a vertical bus
		if (isVerticalBus(j)) {
			return Rational.valueOf(dsmValue, (long) Math.pow(N, 4));
		}
		// if i and j are in same cluster
		else if (getCluster(i).equals(getCluster(j))) {
			return Rational.valueOf(dsmValue *
					(long) Math.pow(n, 2),	(long) Math.pow(N, 4));
		}
		// if i and j are not in same cluster
		else {
			return Rational.valueOf(dsmValue, (long) Math.pow(N, 2));
		}
	}


	/**
	 * Compute relative clustered cost.
	 * 
	 * @return the double
	 */
	private double computeRelativeClusteredCost() {
		Rational cost = Rational.ZERO;
		for (int i = 0; i < dsm.getMap().length; i++) {
			for (int j = 0; j < dsm.getMap().length; j++) {
				if (i != j) {
					cost = cost.plus(computeRelativeDependencyCost(i, j));
				}
			}
		}
		logger.debug("Relative clustered cost: " + cost);
		return cost.doubleValue();
	}


	
	/**
	 * Compute marginal cost of change.
	 * 
	 * @param position
	 *            the position
	 * @param biddinCluster
	 *            the biddin cluster
	 * 
	 * @return the long
	 */
	private long computeMarginalCostOfChange(int position, String biddinCluster){
		long totalChangeInCost = 0;
		String sourceCluster = getCluster(position);
		if(!sourceCluster.equals(biddinCluster)) {
			int N = dsm.getMap().length;
			int m = getClusterSize(sourceCluster);
			int n = getClusterSize(biddinCluster) + 1; 
			
			for(int i = dsm.getClusterStartPositionMappings().get(biddinCluster); i <= dsm.getClusterEndPositionMappings().get(biddinCluster); i++){
				if(!isVerticalBus(i)){
					totalChangeInCost += dsm.getMap()[i][position].longValue() * (- Math.pow(N , lambda) + Math.pow(n, lambda));
					totalChangeInCost += dsm.getMap()[position][i].longValue() * (- Math.pow(N , lambda) + Math.pow(n, lambda));
				}
			}
			for(int i = dsm.getClusterStartPositionMappings().get(sourceCluster); i <= dsm.getClusterEndPositionMappings().get(sourceCluster); i++){
				if(!isVerticalBus(i)){
					totalChangeInCost += dsm.getMap()[position][i].longValue() * (- Math.pow(m, lambda) + Math.pow(N, lambda));
					totalChangeInCost += dsm.getMap()[i][position].longValue() * (- Math.pow(m, lambda) + Math.pow(N, lambda));
				}
			}
			for(int i = dsm.getClusterStartPositionMappings().get(sourceCluster); i <= dsm.getClusterEndPositionMappings().get(sourceCluster); i++){
				for(int j = dsm.getClusterStartPositionMappings().get(sourceCluster); j <= dsm.getClusterEndPositionMappings().get(sourceCluster); j++){
					if(i != position && j != position && !isVerticalBus(j)){
						totalChangeInCost += dsm.getMap()[i][j].longValue() * (- Math.pow(m, lambda) + Math.pow(m - 1, lambda));
					}
				}
			}
			for(int i = dsm.getClusterStartPositionMappings().get(biddinCluster); i <= dsm.getClusterEndPositionMappings().get(biddinCluster); i++){
				for(int j = dsm.getClusterStartPositionMappings().get(biddinCluster); j <= dsm.getClusterEndPositionMappings().get(biddinCluster); j++){
					if(i != position && j != position && !isVerticalBus(j)){
						totalChangeInCost += dsm.getMap()[i][j].longValue() * (- Math.pow(n -1, lambda) + Math.pow(n, lambda));
					}
				}
			}
		}		
		return totalChangeInCost;
	}
	
	/**
	 * Compute winnig cluster.
	 * 
	 * @param position
	 *            the position
	 * 
	 * @return the string
	 */
	private String computeWinnigCluster(int position){
		long maximumDecreaseInCost = 0;
		String winnignCluster = null;
		for(String clusterName: dsm.getClusterStartPositionMappings().keySet()){
			long marginalCostOfChange = computeMarginalCostOfChange(position, clusterName);
			if( marginalCostOfChange < maximumDecreaseInCost) {
				winnignCluster = clusterName;
				maximumDecreaseInCost = marginalCostOfChange;
			}
		}
		dependencyCostSum += maximumDecreaseInCost;
		return winnignCluster;
	}
	
	/**
	 * Gets the cluster.
	 * 
	 * @param position
	 *            the position
	 * 
	 * @return the cluster
	 */
	private String getCluster(int position) {
		for(String clusterName: dsm.getClusterStartPositionMappings().keySet()) {
			if(isInCluster(position, clusterName)) {
				return clusterName;
			}
		}		
		return null;
	}
	
	/**
	 * Gets the cluster size.
	 * 
	 * @param clusterName
	 *            the cluster name
	 * 
	 * @return the cluster size
	 */
	private int getClusterSize(String clusterName){
		return dsm.getClusterEndPositionMappings().get(clusterName) - 
			dsm.getClusterStartPositionMappings().get(clusterName) + 1;
	}
	
	/**
	 * Inits the clusters.
	 */
	private void initClusters(){
		dsm.resetClusters();
		//initially each class is in its own cluster
		for(int i = 0; i < dsm.getMap().length; i++){
			String clusterName = "cluster_" + i;
			dsm.getClusterStartPositionMappings().put(clusterName, i);
			dsm.getClusterEndPositionMappings().put(clusterName, i);
		}
	}

	/**
	 * Checks if is in cluster.
	 * 
	 * @param position
	 *            the position
	 * @param clusterName
	 *            the cluster name
	 * 
	 * @return true, if is in cluster
	 */
	private boolean isInCluster(int position, String clusterName) {
		return dsm.getClusterStartPositionMappings().get(clusterName) <= position && 
			position <= dsm.getClusterEndPositionMappings().get(clusterName); 
	}
	
	/**
	 * Checks if is vertical bus.
	 * 
	 * @param position
	 *            the position
	 * 
	 * @return true, if is vertical bus
	 */
	private boolean isVerticalBus(int position){
		return verticalBusses.contains(dsm.getPositionNameMappings().get(position));
	}
	
	/**
	 * Move to cluster.
	 * 
	 * @param position
	 *            the position
	 * @param targetCluster
	 *            the target cluster
	 */
	private void moveToCluster(int position, String targetCluster){
		String sourceCluster = getCluster(position);
		if(!sourceCluster.equals(targetCluster)) {
			
			 int sourceStart = dsm.getClusterStartPositionMappings().get(sourceCluster);
			 int targetStart = dsm.getClusterStartPositionMappings().get(targetCluster);
			 int sourceEnd = dsm.getClusterEndPositionMappings().get(sourceCluster);
			 int targetEnd = dsm.getClusterEndPositionMappings().get(targetCluster);
			 

			 if(sourceStart < targetStart){
				 updateClusterPositions(sourceEnd, targetStart, -1);
				 dsm.getClusterStartPositionMappings().put(targetCluster, targetStart - 1);
				 dsm.getClusterEndPositionMappings().put(sourceCluster, sourceEnd -1);
			 }
			 else {
				 updateClusterPositions(targetEnd, sourceStart, 1);
				 dsm.getClusterStartPositionMappings().put(sourceCluster, sourceStart + 1);
				 dsm.getClusterEndPositionMappings().put(targetCluster, targetEnd + 1);
			 }

			 dsm.shift(position, dsm.getClusterEndPositionMappings().get(targetCluster));

			 if(getClusterSize(sourceCluster) == 0){
				 dsm.getClusterStartPositionMappings().remove(sourceCluster);
				 dsm.getClusterEndPositionMappings().remove(sourceCluster);
			 }
		}
	}
	
	/**
	 * Update cluster positions.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param endIndex
	 *            the end index
	 * @param offset
	 *            the offset
	 */
	private void updateClusterPositions(int startIndex, int endIndex, int offset) {
		for(String clusterName: dsm.getClusterStartPositionMappings().keySet()) {
			int startPosition = dsm.getClusterStartPositionMappings().get(clusterName);
			if(startPosition > startIndex && startPosition < endIndex){
				dsm.getClusterStartPositionMappings().put(clusterName, startPosition + offset);
			}
			int endPosition = dsm.getClusterEndPositionMappings().get(clusterName);
			if(endPosition > startIndex && endPosition < endIndex){
				dsm.getClusterEndPositionMappings().put(clusterName, endPosition + offset);
			}
		}
	}

	/**
	 * The Class ClusteredCostResult.
	 * 
	 * @author Roberto Milev
	 */
	public class ClusteredCostResult{


		/**
		 * Instantiates a new clustered cost result.
		 * 
		 * @param dsm
		 *            the dsm
		 * @param verticalBusses
		 *            the vertical busses
		 * @param clusteredCost
		 *            the clustered cost
		 * @param relativeClusteredCost
		 *            the relative clustered cost
		 */
		public ClusteredCostResult(DesignStructureMatrix<Dependency> dsm,
				List<String> verticalBusses, long clusteredCost,
				double relativeClusteredCost) {
			super();
			this.dsm = dsm;
			this.verticalBusses = verticalBusses;
			this.clusteredCost = clusteredCost;
			this.relativeClusteredCost = relativeClusteredCost;
		}

		/** The dsm. */
		private DesignStructureMatrix<Dependency> dsm;
		
		/** The vertical busses. */
		private List<String> verticalBusses = new ArrayList<String>();
		
		/** The clustered cost. */
		private long clusteredCost = 0;
		
		/** The relative clustered cost. */
		private double relativeClusteredCost = 0;

		/**
		 * Gets the dsm.
		 * 
		 * @return the dsm
		 */
		public DesignStructureMatrix<Dependency> getDsm() {
			return dsm;
		}

		/**
		 * Gets the vertical busses.
		 * 
		 * @return the vertical busses
		 */
		public List<String> getVerticalBusses() {
			return verticalBusses;
		}

		/**
		 * Gets the clustered cost.
		 * 
		 * @return the clustered cost
		 */
		public long getClusteredCost() {
			return clusteredCost;
		}

		/**
		 * Gets the relative clustered cost.
		 * 
		 * @return the relative clustered cost
		 */
		public double getRelativeClusteredCost() {
			return relativeClusteredCost;
		}
	}
}
