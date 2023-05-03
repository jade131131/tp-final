package org.example.modelo;

import org.example.Resultado;

public class Partido {

    private Equipo equipo1;
    private Equipo equipo2;
    private int cantGoles1;
    private int cantGoles2;

    public Partido(Equipo equipo1, Equipo equipo2, int cantGoles1, int cantGoles2){
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.cantGoles1 = cantGoles1;
        this.cantGoles2 = cantGoles2;
    }

    public Equipo getEquipo1(){
        return equipo1;
    }
    public Equipo getEquipo2(){
        return equipo2;
    }
    public int getCantGoles1(){
        return cantGoles1;
    }
    public int getCantGoles2(){
        return cantGoles2;
    }

    public boolean igual(Partido p){
        return (this.equipo1.getNombre().equals(p.equipo1.getNombre()) && this.equipo2.getNombre().equals(p.equipo2.getNombre()))
                || (this.equipo1.getNombre().equals(p.equipo2.getNombre()) && this.equipo2.getNombre().equals(p.equipo1.getNombre()));
        // Asi chequear cuando un partido es igual a otro
    }

	public boolean juegan(String equipo1, String equipo2) {
		
		return (this.equipo1.getNombre().equals(equipo1) && this.equipo2.getNombre().equals(equipo2))
                || (this.equipo1.getNombre().equals(equipo2) && this.equipo2.getNombre().equals(equipo1));
		
	}

	public Resultado getResultado() {
		
	if(cantGoles1 > cantGoles2) {
		return Resultado.GANA1;
		
	} else if (cantGoles2 > cantGoles1) {
		return Resultado.GANA2;
		
	} else {
		return Resultado.EMPATE;
	}
  }
}