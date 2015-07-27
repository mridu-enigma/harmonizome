package edu.mssm.pharm.maayanlab.Harmonizome.dal;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import edu.mssm.pharm.maayanlab.Harmonizome.model.Gene;
import edu.mssm.pharm.maayanlab.Harmonizome.model.GeneSynonym;
import edu.mssm.pharm.maayanlab.common.database.HibernateUtil;

public class GeneDAO {

	@SuppressWarnings("unchecked")
	public static List<String> getSymbols() {
		return (List<String>) HibernateUtil
			.getCurrentSession()
			.createQuery("SELECT gene.symbol FROM Gene AS gene")
			.list();
	}

	public static Gene getBySymbol(String symbol) {
		Criteria criteria = HibernateUtil.getCurrentSession()
			.createCriteria(Gene.class)
			.add(Restrictions.eq("symbol", symbol).ignoreCase());
		return (Gene) criteria.uniqueResult();
	}
	
	public static Gene getBySynonymSymbol(String symbol) {
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(GeneSynonym.class).add(Restrictions.eq("symbol", symbol).ignoreCase());
		GeneSynonym geneSynonym = (GeneSynonym) criteria.uniqueResult();
		return (geneSynonym == null ? null : geneSynonym.getGene());
	}
	
	public static Long getCountByDataset(String datasetName) {
		return (Long) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT COUNT(gene) FROM Gene AS gene " +
				"JOIN gene.features AS feats " +
				"JOIN feats.attribute AS attr " +
				"JOIN attr.dataset AS dataset " +
				"WHERE dataset.name = :datasetName"
			)
			.setString("datasetName", datasetName)
			.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public static List<Gene> getFromDatasetAndAttributeAndValue(String datasetName, String attributeName, int thresholdValue) {
		return (List<Gene>) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT gene FROM Gene AS gene " +
				"JOIN gene.features AS feats " +
				"JOIN feats.attribute AS attr " +
				"JOIN attr.dataset AS dataset " +
				"WHERE dataset.name = :datasetName AND attr.nameFromDataset = :attributeName AND feats.thresholdValue = :thresholdValue"
			)
			.setString("datasetName", datasetName)
			.setString("attributeName", attributeName)
			.setInteger("thresholdValue", thresholdValue)
			.list();
	}

	@SuppressWarnings("unchecked")
	public static List<Gene> getFromDataset(String datasetName) {
		return (List<Gene>) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT gene FROM Gene AS gene " +
				"JOIN gene.features AS feats " +
				"JOIN feats.dataset AS dataset " +
				"WHERE dataset.name = :datasetName"
			)
			.setString("datasetName", datasetName)
			.list();
	}

	@SuppressWarnings("unchecked")
	public static List<Gene> getByAttributeAndDatasetAndValue(String attributeName, String datasetName, int thresholdValue) {
		return (List<Gene>) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT gene FROM Gene AS gene " +
				"JOIN gene.features AS features " +
				"JOIN features.attribute AS attribute " +
				"JOIN features.dataset AS dataset " +
				"WHERE attribute.name = :attributeName AND dataset.name = :datasetName AND features.thresholdValue = :thresholdValue"
			)
			.setString("datasetName", datasetName)
			.setString("attributeName", attributeName)
			.setInteger("thresholdValue", thresholdValue)
			.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Gene> getByAttribute(String attributeName) {
		return (List<Gene>) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT gene FROM Gene AS gene " +
				"JOIN gene.features AS features " +
				"JOIN features.attribute AS attribute " +
				"WHERE attribute.nameFromDataset = :attributeName"
			)
			.setString("attributeName", attributeName)
			.list();
	}
	
	public static List<Gene> getByWordInSymbol(String query) {
		return GenericDAO.getBySubstringInField(Gene.class, "gene", "symbol", query);
	}

	public static List<Gene> getByWordInSymbolButIgnoreExactMatch(String query, int idToIgnore) {
		return GenericDAO.getBySubstringInFieldButIgnoreId(Gene.class, "gene", "symbol", query, idToIgnore);
	}
	
	public static List<String> getSuggestions(String query) {
		return GenericDAO.getSuggestions("gene", "symbol", query);
	}
}
