package ar.edu.dds.tpa;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.dds.tpa.model.Banco;
import ar.edu.dds.tpa.model.Buscador;
import ar.edu.dds.tpa.model.CGP;
import ar.edu.dds.tpa.model.LocalComercial;
import ar.edu.dds.tpa.model.ParadaDeColectivo;
import ar.edu.dds.tpa.model.PuntoDeInteres;
import ar.edu.dds.tpa.model.Rubro;
import ar.edu.dds.tpa.model.Servicio;
import ar.edu.dds.tpa.persistencia.repository.MapaEnMemoria;
import ar.edu.dds.tpa.persistencia.repository.historial.HistorialDeBusquedaEnMemoria;
import ar.edu.dds.tpa.procesos.ActualizarPOIs;

public class ActualizarPOIsTest {

	private Banco unBanco;
	private CGP unCGP;
	private LocalComercial kioscoDeDario;
	private LocalComercial libreriaEscolar;
	private ParadaDeColectivo paradaDel114EnRivadaviaYNazca;
	private ParadaDeColectivo paradaDel114EnPasajeMozart;
	private ParadaDeColectivo paradaDel114EnPrimeraJunta;
	private MapaEnMemoria mapa;
	private List<PuntoDeInteres> baseDeConocimientosDePuntosDeInteres;
	private Buscador buscador;

	private List<PuntoDeInteres> puntosDeInteresEncontrados;

	private ActualizarPOIs proceso1;

	@Before
	public void inicializar() {
		unBanco = new Banco("HSBC", null, null);

		unCGP = new CGP("CGP Balvanera", null, null);
		unCGP.agregarServicio(new Servicio("Rentas"));

		kioscoDeDario = new LocalComercial("El matutino", null, new Rubro("Kiosco de diario", 0.0), null);

		libreriaEscolar = new LocalComercial("El ateneo", null, new Rubro("Libreria Escolar", 0.0), null);

		paradaDel114EnRivadaviaYNazca = new ParadaDeColectivo("144 Rivadavia y Nazca", null, null);
		paradaDel114EnPasajeMozart = new ParadaDeColectivo("144 Pasaje Mozart ", null, null);
		paradaDel114EnPrimeraJunta = new ParadaDeColectivo("144 Primera Junta", null, null);

		mapa = new MapaEnMemoria();

		mapa.agregar(paradaDel114EnRivadaviaYNazca);
		mapa.agregar(paradaDel114EnPasajeMozart);
		mapa.agregar(paradaDel114EnPrimeraJunta);
		mapa.agregar(kioscoDeDario);
		mapa.agregar(libreriaEscolar);
		mapa.agregar(unBanco);
		mapa.agregar(unCGP);

		baseDeConocimientosDePuntosDeInteres = new ArrayList<>();
		baseDeConocimientosDePuntosDeInteres.add(paradaDel114EnRivadaviaYNazca);
		baseDeConocimientosDePuntosDeInteres.add(paradaDel114EnPasajeMozart);
		baseDeConocimientosDePuntosDeInteres.add(paradaDel114EnPrimeraJunta);
		baseDeConocimientosDePuntosDeInteres.add(kioscoDeDario);
		baseDeConocimientosDePuntosDeInteres.add(libreriaEscolar);
		baseDeConocimientosDePuntosDeInteres.add(unCGP);

		unBanco.agregarPalabraClave("pago");
		unBanco.agregarPalabraClave("pesos");
		unBanco.agregarPalabraClave("dolares");

		buscador = new Buscador(mapa, new HistorialDeBusquedaEnMemoria());

		puntosDeInteresEncontrados = new ArrayList<>();

		proceso1 = new ActualizarPOIs(buscador, "resources/localesComercialesAActualizar.txt");
		proceso1.ejecutar();
	}

	@Test
	public void buscarPorTextoLibreDesactualizado1() {
		puntosDeInteresEncontrados.add(unBanco);
		Assert.assertNotEquals(puntosDeInteresEncontrados, buscador.buscar("pago", null));
	}

	@Test
	public void buscarPorTextoLibreDesactualizado2() {
		puntosDeInteresEncontrados.add(unBanco);
		Assert.assertNotEquals(puntosDeInteresEncontrados, buscador.buscar("pesos", null));
	}

	@Test
	public void buscarPorTextoLibreDesactualizado3() {
		puntosDeInteresEncontrados.add(unBanco);
		Assert.assertNotEquals(puntosDeInteresEncontrados, buscador.buscar("dolares", null));
	}

	@Test
	public void buscarPorTextoLibreActualizado1() {
		puntosDeInteresEncontrados.add(unBanco);
		Assert.assertEquals(puntosDeInteresEncontrados, buscador.buscar("euros", null));
	}

	@Test
	public void buscarPorTextoLibreActualizado2() {
		puntosDeInteresEncontrados.add(unBanco);
		Assert.assertEquals(puntosDeInteresEncontrados, buscador.buscar("buitre", null));
	}

	@Test
	public void buscarPorTextoLibreActualizado3() {
		puntosDeInteresEncontrados.add(unBanco);
		Assert.assertEquals(puntosDeInteresEncontrados, buscador.buscar("millones", null));
	}
}