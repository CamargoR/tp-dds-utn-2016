package ar.edu.dds.tpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.edu.dds.tpa.model.*;
import ar.edu.dds.tpa.model.usuario.Terminal;
import ar.edu.dds.tpa.persistencia.repository.historial.HistorialDeBusquedaEnMemoria;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class HistorialDeBusquedasTest {

	private HistorialDeBusquedaEnMemoria historialDeBusqueda;

	private LocalDate cuatroDeFebreroDe2016;
	private LocalDate diezDeEneroDe2016;

	private Busqueda busquedaParadaCinco;
	private Busqueda busquedaLibreria;
	private Busqueda busquedaParadaDos;

	private LocalComercial localComercial;
	private ParadaDeColectivo paradaDeColectivo;
	private Banco banco;
	private List<PuntoDeInteres> resultadosDeBusqueda1;
	private List<PuntoDeInteres> resultadosDeBusqueda2;
	private List<PuntoDeInteres> resultadosDeBusqueda3;

	private List<LocalDate> fechasDeBusqueda;

	private Terminal terminal;

	@Before
	public void inicializar() {

		cuatroDeFebreroDe2016 = LocalDate.of(2016, Month.FEBRUARY, 4);
		diezDeEneroDe2016 = LocalDate.of(2016, Month.JANUARY, 10);

		fechasDeBusqueda = Arrays.asList(cuatroDeFebreroDe2016, diezDeEneroDe2016);

		terminal = new Terminal();

		localComercial = new LocalComercial();
		paradaDeColectivo = new ParadaDeColectivo();
		banco = new Banco();
		resultadosDeBusqueda1 = new ArrayList<PuntoDeInteres>();
		resultadosDeBusqueda2 = new ArrayList<PuntoDeInteres>();
		resultadosDeBusqueda3 = new ArrayList<PuntoDeInteres>();
		resultadosDeBusqueda1.add(banco);
		resultadosDeBusqueda1.add(paradaDeColectivo);
		resultadosDeBusqueda2.add(localComercial);
		resultadosDeBusqueda2.add(banco);
		resultadosDeBusqueda3.add(paradaDeColectivo);
		resultadosDeBusqueda3.add(banco);
		resultadosDeBusqueda3.add(localComercial);

		busquedaParadaCinco = new Busqueda(terminal, "Florida",new PuntosDeInteresEncontrados(resultadosDeBusqueda1) , cuatroDeFebreroDe2016, 5.0);
		busquedaLibreria = new Busqueda(terminal, "Ahorro",new PuntosDeInteresEncontrados(resultadosDeBusqueda2) , cuatroDeFebreroDe2016, 5.0);
		busquedaParadaDos = new Busqueda(terminal, "Subte",new PuntosDeInteresEncontrados(resultadosDeBusqueda3) , diezDeEneroDe2016, 5.0);

		historialDeBusqueda = new HistorialDeBusquedaEnMemoria();

		historialDeBusqueda.registrarBusqueda(busquedaParadaCinco);
		historialDeBusqueda.registrarBusqueda(busquedaLibreria);
		historialDeBusqueda.registrarBusqueda(busquedaParadaDos);
	}

	@Test
	public void filtradoDeFechasDeBusquedasDistintas() {
		Assert.assertTrue(historialDeBusqueda.fechasDeBusquedas().containsAll(fechasDeBusqueda));
	}

	@Test
	public void resultadosTotalesSonSiete() {
		Assert.assertEquals(7, historialDeBusqueda.cantidadDeResultadosTotales());
	}

	@Test
	public void enCuatroDeFebreroSeRealizaronDosBusquedas() {
		Assert.assertEquals(2, historialDeBusqueda.cantidadDeBusquedasEnUnaFecha(cuatroDeFebreroDe2016));
	}
}