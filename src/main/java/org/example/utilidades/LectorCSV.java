package org.example.utilidades;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.example.modelo.Equipo;
import org.example.modelo.Fase;
import org.example.modelo.Partido;
import org.example.modelo.Ronda;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    List<Fase> fases;
    List<Equipo> equipos;
    String rutaResultados;

    public List<Fase> getFases() {
        return fases;
    }

    public LectorCSV(String rutaResultados){
        this.fases = new ArrayList<>();
        this.equipos = new ArrayList<>();
        this.rutaResultados = rutaResultados;
    }

    public void levantarResultados() {
        CSVParser parser = null;
        CSVReader lector = null;

        try {
            parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();
            lector = new CSVReaderBuilder(new FileReader(this.rutaResultados))
                    .withCSVParser(parser)
                    .withSkipLines(1)
                    .build();

            String[] fila;

            while((fila = lector.readNext()) != null){
                Fase fase;
                try{
                    fase = this.obtenerFase(Integer.parseInt(fila[0]));

                } catch (RuntimeException e){
                    fase = new Fase(Integer.parseInt(fila[0]));
                    this.agregarFase(fase);
                }

                Ronda ronda;
                try{
                    ronda = fase.obtenerRonda(Integer.parseInt(fila[2]));
                } catch(RuntimeException e){
                    ronda = new Ronda(Integer.parseInt(fila[2]));
                    fase.agregarRonda(ronda);
                }

                Equipo equipo1;

                try {
                    equipo1 = this.obtenerEquipo(fila[3]);
                } catch (RuntimeException e){
                    equipo1 = new Equipo(fila[3]);
                    this.agregarEquipo(equipo1);
                }

                Equipo equipo2;

                try {
                    equipo2 = this.obtenerEquipo(fila[6]);
                } catch (RuntimeException e){
                    equipo2 = new Equipo(fila[6]);
                    this.agregarEquipo(equipo2);
                }

                Partido partido = new Partido(equipo1, equipo2, Integer.parseInt(fila[4]), Integer.parseInt(fila[5]));

                ronda.agregarPartido(partido);
            }

        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void agregarFase(Fase fase) {
		
    	for(Fase f : this.fases) {
    		if(f.getNumeroFase() == fase.getNumeroFase()) {
    			throw new RuntimeException("La fase ya existe");
    			
    		}
    	}
    	
		this.fases.add(fase);
	}

    private void agregarEquipo(Equipo equipo) {

        for(Equipo e : this.equipos) {
            if(e.getNombre().equals(equipo.getNombre())) {
                throw new RuntimeException("El equipo ya existe");

            }
        }

        this.equipos.add(equipo);
    }
    

	private Equipo obtenerEquipo(String nombreEquipo) {

        Equipo equipo = null;

        for (Equipo e : this.equipos){
            if (e.getNombre().equals(nombreEquipo)){
                equipo = e;
            }
        }

        if (equipo == null){
            equipo = new Equipo(nombreEquipo);
            this.equipos.add(equipo);
        }

        return equipo;
    }

    public Fase obtenerFase(int numeroFase) {
        Fase fase = null;
        for (Fase f : this.fases){
            if (f.getNumeroFase() == numeroFase){
                fase = f;
            }
        }
        
        if (fase == null){
            throw new RuntimeException("La fase no existe"); // Hay que crear y enlazar nuestras excepciones
        }

        return fase;
    }
}
