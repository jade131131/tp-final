package org.example.utilidades;

import org.example.Resultado;
import org.example.modelo.Fase;
import org.example.modelo.Partido;
import org.example.modelo.Persona;
import org.example.modelo.Pronostico;
import org.example.modelo.Ronda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectorDB {
    private List<Pronostico> pronosticos;
    private List<Persona> personas;
    private LectorCSV lectorCSV;

    public LectorDB(LectorCSV lectorCSV) {
        this.lectorCSV = lectorCSV;
        this.pronosticos = new ArrayList<>();
        this.personas = new ArrayList<>();
    }

    public void levantarPronosticos() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pronosticos?allowPublicKeyRetrieval=true&useSSL=false", "root", "1234");
            Statement stmt = con.createStatement();
            ResultSet resultado = stmt.executeQuery("select * from pronosticos");

            while(resultado.next()){
                // Instanciar un pronostico
                Fase fase = lectorCSV.obtenerFase(resultado.getInt("fase"));
                Ronda ronda = fase.obtenerRonda(resultado.getInt("ronda"));
                Persona persona = this.obtenerPersonas(resultado.getString("persona"));
                Partido partido = ronda.obtenerPartido(resultado.getString("equipo_1"), resultado.getString("equipo_2"));
                
               boolean gana1 = resultado.getBoolean("gana_1");
               boolean empata = resultado.getBoolean("empata");
               boolean gana2 = resultado.getBoolean("gana_2");

               Resultado res = null;
               
               if(gana1) {
            	   
            	   res = Resultado.GANA1;
            	   
               } else if(gana2) {
            	   
            	   res = Resultado.GANA2;
            	   
               } else if(empata) {
            	   
            	   res = Resultado.EMPATE;
               }
               

               
               if(res == null) {
            	   
            	   throw new RuntimeException("No se ha establecido un resultado");
               }
               
                Pronostico pronostico = new Pronostico(fase, ronda, persona, partido, res);
                this.agrearPronostico(pronostico);

                //Agregar el pronostico a la lista de pronosticos
            }


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }



    }

	private void agrearPronostico(Pronostico pronostico) {
		
		for(Pronostico p: this.pronosticos) {
			if(p.getFase().equals(pronostico.getFase()) && p .getPartido().equals(pronostico.getPartido())
					&& p.getRonda().equals(pronostico.getRonda())
					&& p .getPersona().equals(pronostico.getPersona())) {
				
				throw new RuntimeException("Ya hay un pronostico para el partido");
				
			}
			
		}
		
		this.pronosticos.add(pronostico);
	}
	

	private Persona obtenerPersonas(String nombrePersona) {
		
		Persona persona = null;
        for (Persona p : this.personas){
        	
            if (p.getNombre().equals(nombrePersona)){
                persona = p;
            }
        }
        
        if (persona == null){
            persona = new Persona(nombrePersona);
            this.personas.add(persona);
        }

        return persona;
		
	}


	
	
	public boolean acertoTodosLosPronosticosRonda(Persona p, Ronda ronda) {
		
		List<Pronostico> pronosticos1 = this.pronosticos
                .stream()
                .filter(pronostico -> pronostico.getRonda().equals(ronda) && pronostico.getPersona().equals(p))
                .toList();
		
		return pronosticos1.stream().allMatch(pronostico -> pronostico.fueAcertado());
	}
	
	

	public boolean acertoTodosLosPronosticosFase(Persona p, int fase ) {
		
		List<Pronostico> pronosticos1 = this.pronosticos
                .stream()
                .filter(pronostico -> pronostico.getFase().getNumeroFase() == fase && pronostico.getPersona().equals(p))
                .toList();
		
		return pronosticos1.stream().allMatch(pronostico -> pronostico.fueAcertado());
	}
	
	
	public List<Pronostico> getPronosticos() {
		return pronosticos;
	}

	public List<Persona> getPersonas() {
		return personas;
	}
	
	
}
