package it.prova.gestionecompagnia.dao.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;

public interface CompagniaDAO extends IBaseDAO<Compagnia> {

	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date dataInput) throws Exception;

	public List<Compagnia> findAllByRagioneSocialeContiene(String input) throws Exception;

	public List<Compagnia> findAllByCodiceFiscaleImpiegatoContiene(String input) throws Exception;

	public void findByIdEager(Compagnia compagniaInput) throws Exception;

}
