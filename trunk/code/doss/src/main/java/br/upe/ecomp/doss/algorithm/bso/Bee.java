package br.upe.ecomp.doss.algorithm.bso;

import br.upe.ecomp.doss.algorithm.Particle;

public class Bee extends Particle{

    private TypeBee type;
    
    public Bee(int dimensions) {
        super(dimensions);
        // TODO Auto-generated constructor stub
    }
    
    public TypeBee getType() {
        return type;
    }

    public void setType(TypeBee type) {
        this.type = type;
    }

}


