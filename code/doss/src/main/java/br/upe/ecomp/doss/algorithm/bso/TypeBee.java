package br.upe.ecomp.doss.algorithm.bso;

public enum TypeBee {
       
    SCOUT( 0 ),
    ONLOOKER ( 1 ),
    FORAGER ( 2 );
    
    private int value;
    
    TypeBee( int value ) {  
        this.value = value;  
    } 
    
    public void setTypeBee(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return this.value;
    }
    
}
