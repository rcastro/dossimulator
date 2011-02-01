package br.upe.ecomp.doss.algorithm.chargedpso;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import br.upe.ecomp.doss.algorithm.clanpso.ClanPSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.core.annotation.Parameter;

public class ChargedClanPSO extends ClanPSO {

    @Parameter(name = "Particle charge")
    private double particleCharge;

    @Parameter(name = "Percentage of charged particles")
    private double percentageChargedParticles;

    public ChargedClanPSO() {
        super();
    }

    @Override
    public String getName() {
        return "Clan Charged PSO";
    }

    @Override
    public String getDescription() {
        return "An implementation of the Charged Clan PSO algorithm.";
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

        chargedParticle.updateVelocity(getInertiaWeight(), bestParticleNeighborhood.getBestPosition(), getC1(),
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
}
