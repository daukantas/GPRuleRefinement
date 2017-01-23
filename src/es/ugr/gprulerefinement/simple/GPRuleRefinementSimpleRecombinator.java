/*
 * BasicOrderRecombinator.java
 * 
 * Copyright (c) 2013, Pablo Garcia-Sanchez. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 * 
 * Contributors:
 */
package es.ugr.gprulerefinement.simple;

import java.util.ArrayList;
import java.util.List;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.elements.Crossover;
import es.ugr.osgiliath.evolutionary.elements.EvolutionaryBasicParameters;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.elements.Recombinator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Genome;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class GPRuleRefinementSimpleRecombinator extends OsgiliathService implements Recombinator{

	private FitnessCalculator fitnessCalculator;
	private Crossover crossover;
	/*
	 * This recombinator recombines the parents in order (given by Selector). Is the same that BasicOrderRecombinator
	 * BUT it generates a GPRuleRefinementIndividual 
	 * If the parents are an odd number, the last
	 * is not recombined. Returns a new offspring (in this implementation, all new).
	 * 
	 * @see es.ugr.osgiliath.evolutionary.elements.Recombinator#recombine(java.util.List)
	 */
	@Override
	
	public ArrayList<Individual> recombine(ArrayList<Individual> parents) {
		//System.out.println("SPX");
		ArrayList<Individual> offspring = new ArrayList<Individual>();
		double prob = (Double) this.getAlgorithmParameters().getParameter(EvolutionaryBasicParameters.CROSSOVER_PROB);
		//Collections.shuffle(parents); //TODO ojo!!!
		for(int i=0;i<parents.size()-1;i=i+2){
			if(Math.random()< prob){
				Individual father = parents.get(i);
				Individual mother = parents.get(i+1);			
				Genome fatherGenome = father.getGenome();
				Genome motherGenome = mother.getGenome();
		    
				//long tC = System.nanoTime();
				ArrayList<Genome> childs = this.crossover.cross(motherGenome, fatherGenome);
			    //long tC2 = System.nanoTime() - tC;
			    //System.out.println("C;"+tC2+";");
				for(Genome chG:childs){
					Individual ind = new GPRuleRefinementSimpleIndividual();
					ind.setGenome(chG);
					offspring.add(ind);				
				}
			}
			
			
		}
		
		if(this.fitnessCalculator!=null){
			List<Fitness> fits = this.fitnessCalculator.calculateFitnessForAll(offspring);
			
			int ind = 0;
			for(Fitness f:fits){
				offspring.get(ind).setFitness(f);
				ind++;
			}
		}
		return offspring;
	}
	/**
	 * Bind function to pick the FitnessCalculator service
	 * @param fitnessCalculator FitnessCalculator implementation
	 */
	public void setFitnessCalculator(FitnessCalculator fitnessCalculator){
		this.fitnessCalculator = fitnessCalculator;
	}
	/**
	 * Unbind function to release the FitnessCalculator service
	 * @param fitnessCalculator the implementation to release
	 */
	public void unsetFitnessCalculator(FitnessCalculator fitnessCalculator){
		this.fitnessCalculator = null;
	}
	
	public void setCrossover(Crossover cross){
		this.crossover = cross;
	}
	
	public void unsetCrossover(Crossover cross){
		this.crossover = null;
	}
	

}
