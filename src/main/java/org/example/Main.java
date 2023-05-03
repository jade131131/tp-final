package org.example;

import org.example.modelo.Fase;
import org.example.modelo.Persona;
import org.example.modelo.Pronostico;
import org.example.modelo.Ronda;
import org.example.utilidades.LectorCSV;
import org.example.utilidades.LectorDB;

public class Main {
    public static void main(String[] args) {

    	Integer puntajePorPartido = 2;
    	Integer puntajeExtraRonda = 10;
    	Integer puntajeExtraFase = 20;
    	
        LectorCSV lectorCSV = new LectorCSV(args[0]);
        lectorCSV.levantarResultados();

        LectorDB lectorDB = new LectorDB(lectorCSV);
        lectorDB.levantarPronosticos();

        calcularPuntos(lectorCSV, lectorDB, puntajePorPartido, puntajeExtraRonda, puntajeExtraFase);
    }

	private static void calcularPuntos(LectorCSV lectorCSV, LectorDB lectorDB,Integer puntajePorPartido, Integer puntajeExtraRonda,Integer puntajeExtraFase) {
		
		for(Pronostico p : lectorDB.getPronosticos()) {
			if(p.fueAcertado()) {
				p.getPersona().sumarPuntos(puntajePorPartido);
				p.getPersona().agregarAcierto();
			}
		}
		
		for(Persona p : lectorDB.getPersonas()) {
			for(Fase fase : lectorCSV.getFases()){
				if(lectorDB.acertoTodosLosPronosticosFase(p, fase.getNumeroFase())) {
					p.sumarPuntos(puntajeExtraFase);
				}

				for(Ronda ronda : fase.getRondas()){
					if(lectorDB.acertoTodosLosPronosticosRonda(p, ronda)) {
						p.sumarPuntos(puntajeExtraRonda);
					}
				}
		  	}
	    }
		
		for(Persona p : lectorDB.getPersonas()) {
			System.out.println("Nombre: " + p.getNombre());
			System.out.println("Aciertos: " + p.getCantAciertos());
			System.out.println("Puntaje: " + p.getPuntaje());
			System.out.println("----------------------------------------------: ");
		}
	}
}