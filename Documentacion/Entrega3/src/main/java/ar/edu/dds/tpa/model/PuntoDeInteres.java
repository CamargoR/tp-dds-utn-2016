package ar.edu.dds.tpa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.dds.tpa.geolocalizacion.Posicion;

public abstract class PuntoDeInteres {
	private String nombre;
	private Posicion coordenadas;
	private List<String> palabrasClave;
	private LocalDateTime fechaBaja;

	public PuntoDeInteres(String nombre, Posicion coordenadas) {
		this.nombre = nombre;
		this.coordenadas = coordenadas;
		palabrasClave = new ArrayList<String>();
		agregarPalabraClave(nombre);
	}
	
	public String getNombre() {
		return nombre;
	}

	public Posicion getCoordenadas() {
		return coordenadas;
	}
	
	public List<String> getPalabrasClave() {
		return palabrasClave;
	}
	
	public void agregarPalabraClave(String unaPalabraClave) {
		palabrasClave.add(unaPalabraClave.toLowerCase());
	}
	
	public boolean estaCercaDe(Posicion unaPosicion) {
		return coordenadas.distanciaA(unaPosicion) <= 0.5;
	}

	public abstract boolean estaDisponibleEn(LocalDateTime unDiaYHorario);
	
	public boolean contienePalabraClave(String unaPalabra) {
		return palabrasClave.stream().anyMatch(unaPalabraClave -> unaPalabraClave.contains(unaPalabra.toLowerCase()));
	}
	
	public void borrarPalabrasClaves(){
		palabrasClave = new ArrayList<String>();
		agregarPalabraClave(nombre);

	}

	public LocalDateTime getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDateTime fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	@Override
	public boolean equals(Object otro) {		
		//Nos referimos al mismo punto de interes si ambos objetos son exactamente el mismo, o si tienen el mismo nombre y la ubicacion es aproximadamente la misma con un leve margen de error (2mm)
		return otro != null &&
			   (otro == this ||
				   (Math.abs(this.coordenadas.distanciaA(((PuntoDeInteres)otro).getCoordenadas())) <= 0.0000002 
				   && this.getNombre().equals(((PuntoDeInteres)otro).getNombre())));
	}

	public boolean estaActivo() {
		return fechaBaja == null || fechaBaja.isAfter(LocalDateTime.now());
	}
}