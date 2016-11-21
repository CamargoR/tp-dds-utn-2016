package ar.edu.dds.tpa.persistencia;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.dds.tpa.model.BajaPuntoDeInteres;
import ar.edu.dds.tpa.model.PuntoDeInteres;

public class MapaEnMemoria implements Mapa {
	private List<PuntoDeInteres> puntosDeInteres;

	public MapaEnMemoria() {
		puntosDeInteres = new ArrayList<PuntoDeInteres>();
	}

	@Override
	public void agregar(PuntoDeInteres unPuntoDeInteres) {
		puntosDeInteres.add(unPuntoDeInteres);
	}

	@Override
	public void agregar(List<PuntoDeInteres> variosPuntosDeInteres) {
		puntosDeInteres.addAll(variosPuntosDeInteres);
	}

	@Override
	public void sacar(PuntoDeInteres unPuntoDeInteres) {
		puntosDeInteres.remove(unPuntoDeInteres);
	}

	@Override
	public void modificar(PuntoDeInteres unPuntoDeInteres, PuntoDeInteres puntoDeInteresModificado) {
		puntosDeInteres.remove(unPuntoDeInteres);
		puntosDeInteres.add(puntoDeInteresModificado);
	}

	@Override
	public List<PuntoDeInteres> obtenerPuntosDeInteres() {
		return puntosDeInteres;
	}

	public void darDeBaja(BajaPuntoDeInteres puntoADarDeBaja) {
		puntosDeInteres.stream().filter(puntoADarDeBaja::equals).findFirst()
				.ifPresent(punto -> punto.setFechaBaja(puntoADarDeBaja.getFechaBaja()));
	}

	@Override
	public List<PuntoDeInteres> obtenerPuntosDeInteresPorTipoYNombre(Class<? extends PuntoDeInteres> tipo, String nombre) {
		return puntosDeInteres
				.stream()
				.filter(poi -> tipo.isAssignableFrom(poi.getClass()) && poi.getNombre().contains(nombre))
				.collect(Collectors.toList());
	}

	@Override
	public PuntoDeInteres obtenerPorID(int id) {
		return puntosDeInteres.stream().filter(poi -> poi.getId() == id).findFirst().get();
	}
}