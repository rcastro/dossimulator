package br.upe.ecomp.doss.algorithm.pso;

public abstract class AbstractPSO extends PSO {

    public abstract PSOParticle newParticle();

    public void reInitParticles(double percent, PSOParticle[] particles) {
        if (getIterations() > 0 && getIterations() % getProblem().getChangeStep() == 0) {
            int newPerticlesNum = (int) Math.floor(getSwarmSize() * (percent / 100));
            for (int i = 0; i < newPerticlesNum; i++) {
                particles[i] = newParticle();
            }
        }
    }

}
