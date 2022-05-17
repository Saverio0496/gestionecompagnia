package it.prova.gestionecompagnia.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.connection.MyConnection;
import it.prova.gestionecompagnia.dao.Constants;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAO;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAO;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class TestGestioneCompagnia {

	public static void main(String[] args) {
		CompagniaDAO compagniaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;

		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			compagniaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testInsertCompagnia(compagniaDAOInstance);
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testDeleteCompagnia(compagniaDAOInstance);
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testUpdateCompagnia(compagniaDAOInstance);

			testFindByIdPerCompagnia(compagniaDAOInstance);

			testFindByExampleCompagnia(compagniaDAOInstance);

			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			testInsertImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			testDeleteImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			testUpdateImpiegato(impiegatoDAOInstance);

			testFindByIdPerImpiegato(impiegatoDAOInstance);

			testFindByExampleImpiegato(impiegatoDAOInstance);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("Inzio testInsertCompagnia");
		int quanteCompagnieInserite = compagniaDAOInstance.insert(new Compagnia("Alitalia", 600000, new Date()));
		if (quanteCompagnieInserite < 1)
			throw new RuntimeException("testInsertCompagnia fallito!");

		System.out.println("Fine testInserCompagnia!");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("Inizio testDeleteCompagnia");
		List<Compagnia> elencoCompagniePresenti = compagniaDAOInstance.list();
		int numeroCompagniePresentiPrimaDellaRimozione = elencoCompagniePresenti.size();
		if (numeroCompagniePresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteCompagnia fallito! Non ci sono compagnie sul database!");

		Compagnia compagniaDaEliminare = compagniaDAOInstance.list().get(4);
//		if (compagniaDAOInstance.findByIdEager(compagniaDaEliminare)) {
		int quanteCompagnieEliminate = compagniaDAOInstance.delete(compagniaDaEliminare);
		if (quanteCompagnieEliminate < 1)
			throw new RuntimeException("Cancellazione fallita!");
		System.out.println("Sono stati eleminate " + quanteCompagnieEliminate + " compagnie!");
		System.out.println("Fine testDeleteCompagnia!");
//		} else {
//			throw new RuntimeException("Ci sono ancora impiegati assunti in questa azienda!");
//		}
	}

	private static void testUpdateCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("Inzio testUpdateCompagnia");

		Compagnia compagniaDaAggiornare = compagniaDAOInstance.list().get(0);
		compagniaDaAggiornare.setFatturatoAnnuo(750000);

		compagniaDAOInstance.update(compagniaDaAggiornare);
		System.out.println(compagniaDaAggiornare);

		System.out.println("Fine testUpdateCompagnia!");
	}

	private static void testFindByIdPerCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("Inizio testFindByIdPerCompagnia");
		List<Compagnia> elencoCompagniePresenti = compagniaDAOInstance.list();
		if (elencoCompagniePresenti.size() < 1)
			throw new RuntimeException("testFindByIdPerCompagnia fallito! Non ci sono compagnie sul database!");

		Compagnia primoDellaLista = elencoCompagniePresenti.get(0);

		Compagnia elementoCheRicercoColDAO = compagniaDAOInstance.get(primoDellaLista.getId());
		if (elementoCheRicercoColDAO == null)
			throw new RuntimeException("testFindByIdPerCompagnia fallito!");

		System.out.println(elementoCheRicercoColDAO);
		System.out.println("Fine testFindById!");
	}

	private static void testFindByExampleCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("Inizio testFindByExample");
		List<Compagnia> result = null;
		List<Compagnia> elencoCompagniePresenti = compagniaDAOInstance.list();
		if (elencoCompagniePresenti.size() < 1)
			throw new RuntimeException("testFindByExample fallito! Non ci sono compagnie sul database!");

		Compagnia compagniaPerTest = new Compagnia();
		compagniaPerTest.setRagioneSociale("vue");
		compagniaPerTest.setFatturatoAnnuo(900000);
		compagniaPerTest.setDataFondazione(null);
		try {
			result = compagniaDAOInstance.findByExample(compagniaPerTest);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Compagnia compagniaItem : result)
			System.out.println(compagniaItem);
		System.out.println("Fine testFindByExample!");

	}

	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("Inzio testInsertImpiegato");
		Compagnia compagniaId = new Compagnia();
		compagniaId.setId(2L);
		int quantiImpiegatiInseriti = impiegatoDAOInstance
				.insert(new Impiegato("Luigi", "Carolis", "LGICRL67A45F501H", new Date(), new Date()));
		if (quantiImpiegatiInseriti < 1)
			throw new RuntimeException("testInsertImpiegato fallito!");

		System.out.println("Fine testInserCompagnia!");
	}

	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("Inizio testDeleteImpiegato");
		List<Impiegato> elencoImpiegatiPresenti = impiegatoDAOInstance.list();
		int numeroImpiegatiPresentiPrimaDellaRimozione = elencoImpiegatiPresenti.size();
		if (numeroImpiegatiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteImpiegato fallito! Non ci sono impiegati sul database!");

		Impiegato impiegatoDaEliminare = impiegatoDAOInstance.list().get(4);
		int quanteImpiegatiEliminati = impiegatoDAOInstance.delete(impiegatoDaEliminare);
		if (quanteImpiegatiEliminati < 1)
			throw new RuntimeException("Cancellazione fallita!");
		System.out.println("Sono stati eleminati " + quanteImpiegatiEliminati + " impiegati!");
		System.out.println("Fine testDeleteImpiegato!");
	}

	private static void testUpdateImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("Inzio testUpdateImpiegato");

		Impiegato impiegatoDaAggiornare = impiegatoDAOInstance.list().get(0);
		impiegatoDaAggiornare.setCognome("Forti");

		impiegatoDAOInstance.update(impiegatoDaAggiornare);
		System.out.println(impiegatoDaAggiornare);

		System.out.println("Fine testUpdateImpiegato!");
	}

	private static void testFindByIdPerImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("Inizio testFindByIdPerImpiegato");
		List<Impiegato> elencoImpiegatiPresenti = impiegatoDAOInstance.list();
		if (elencoImpiegatiPresenti.size() < 1)
			throw new RuntimeException("testFindByIdPerImpiegato fallito! Non ci sono impiegati sul database!");

		Impiegato primoDellaLista = elencoImpiegatiPresenti.get(0);

		Impiegato elementoCheRicercoColDAO = impiegatoDAOInstance.get(primoDellaLista.getId());
		if (elementoCheRicercoColDAO == null)
			throw new RuntimeException("testFindByIdPerImpiegato fallito!");

		System.out.println(elementoCheRicercoColDAO);
		System.out.println("Fine testFindById!");
	}

	private static void testFindByExampleImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("Inizio testFindByExample");
		List<Impiegato> result = null;
		List<Impiegato> elencoCompagniePresenti = impiegatoDAOInstance.list();
		if (elencoCompagniePresenti.size() < 1)
			throw new RuntimeException("testFindByExample fallito! Non ci sono impiegati sul database!");

		Impiegato impiegatoPerTest = new Impiegato();
		impiegatoPerTest.setNome("ve");
		impiegatoPerTest.setCognome("lli");
		impiegatoPerTest.setCodiceFiscale("96E");
		impiegatoPerTest.setDataNascita(null);
		impiegatoPerTest.setDataAssunzione(null);
		try {
			result = impiegatoDAOInstance.findByExample(impiegatoPerTest);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Impiegato impiegatoItem : result)
			System.out.println(impiegatoItem);
		System.out.println("Fine testFindByExample!");

	}

}
