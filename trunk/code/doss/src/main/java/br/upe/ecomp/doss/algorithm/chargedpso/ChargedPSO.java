/**
 * Copyright (C) 2010
 * Swarm Intelligence Team (SIT)
 * Department of Computer and Systems
 * University of Pernambuco
 * Brazil
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package br.upe.ecomp.doss.algorithm.chargedpso;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import br.upe.ecomp.doss.algorithm.pso.PSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public abstract class ChargedPSO extends PSO {

    @Parameter(name = "Particle charge")
    private double particleCharge;

    @Parameter(name = "Percentage of charged particles")
    private double percentageChargedParticles;

    /**
     * Creates a new instance of this class.
     */
    public ChargedPSO() {
        super();
    }

    @Override
    protected void createParticles() {
        ChargedPSOParticle[] particles = new ChargedPSOParticle[getSwarmSize()];
        double chargedParticles = Math.ceil((getSwarmSize() * percentageChargedParticles) / 100);

        double[] position;
        for (int i = 0; i < getSwarmSize(); i++) {
            position = super.getInitialPosition();

            ChargedPSOParticle particle = new ChargedPSOParticle(getDimensions());
            particle.updateCurrentPosition(position, getProblem().getFitness(position));
            particle.updateBestPosition(position.clone(), particle.getCurrentFitness());
            particle.setVelocity(getInitialVelocity());

            particles[i] = particle;
        }

        chargeParticles(particles, chargedParticles);

        setParticles(particles);
    }

    private void chargeParticles(ChargedPSOParticle[] particles, double chargedParticlesLenght) {
        RandomData random = new RandomDataImpl();

        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < particles.length; i++) {
            indexes.add(i);
        }

        int index;
        int randomNumber;
        for (int i = 0; i < chargedParticlesLenght; i++) {
            randomNumber = random.nextInt(0, indexes.size() - 1);
            index = indexes.remove(randomNumber);
            particles[index].setCharge(particleCharge);
        }
    }

    @Override
    protected void updateParticleVelocity(PSOParticle currentParticle, int index) {
        ChargedPSOParticle chargedParticle = (ChargedPSOParticle) currentParticle;
        PSOParticle bestParticleNeighborhood = getTopology().getBestParticleNeighborhood(this, index);

        chargedParticle.updateVelocity(getInertialWeight(), bestParticleNeighborhood.getBestPosition(), getC1(),
                getC2(), calculateAcceleration(chargedParticle));
    }

    private double[] calculateAcceleration(ChargedPSOParticle particle) {
        double pcore = 1;
        double p = Math.sqrt(3) * getProblem().getUpperBound(0);

        ChargedPSOParticle[] particles = (ChargedPSOParticle[]) getParticles();
        double[] acceleration = new double[particle.getDimensions()];

        double[] particlesDifference;
        double module;
        for (ChargedPSOParticle currParticle : particles) {
            if (!currParticle.equals(particle)) {
                particlesDifference = calculateDifference(particle, currParticle);
                module = calculateModule(particlesDifference);
                if (module > pcore && module < p) {
                    double chargeInffluence = (particle.getCharge() * currParticle.getCharge()) / (Math.pow(module, 3));
                    for (int i = 0; i < acceleration.length; i++) {
                        acceleration[i] += chargeInffluence * particlesDifference[i];
                    }
                }
            }
        }
        return acceleration;
    }

    private double[] calculateDifference(ChargedPSOParticle particle1, ChargedPSOParticle particle2) {
        double[] particle1Position = particle1.getCurrentPosition();
        double[] particle2Position = particle2.getCurrentPosition();

        double[] difference = new double[particle1Position.length];
        for (int i = 0; i < particle1Position.length; i++) {
            difference[i] = particle1Position[i] - particle2Position[i];
        }
        return difference;
    }

    private double calculateModule(double[] vector) {
        double module = 0;
        for (int i = 0; i < vector.length; i++) {
            module += vector[i];
        }
        return module;
    }

    public double getParticleCharge() {
        return particleCharge;
    }

    public void setParticleCharge(double particleCharge) {
        this.particleCharge = particleCharge;
    }
}
