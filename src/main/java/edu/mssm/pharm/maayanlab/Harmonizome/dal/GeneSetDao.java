package edu.mssm.pharm.maayanlab.Harmonizome.dal;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import edu.mssm.pharm.maayanlab.Harmonizome.model.GeneSet;
import edu.mssm.pharm.maayanlab.common.database.HibernateUtil;

public class GeneSetDao {

	@SuppressWarnings("unchecked")
	public static List<GeneSet> getAllFromNameFromDataset(String name) {
		return (List<GeneSet>) HibernateUtil
			.getCurrentSession()
			.createQuery(
                "SELECT geneSet FROM GeneSet AS geneSet " +
                "WHERE geneSet.nameFromDataset = :name"
            )
			.setString("name", name)
			.list();
	}

	public static GeneSet getFromNameAndDataset(String name, String datasetName) {
		return (GeneSet) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT geneSet FROM GeneSet AS geneSet " +
				"  JOIN geneSet.dataset AS dataset " +
				"WHERE geneSet.nameFromDataset = :name " +
				"  AND dataset.name = :datasetName"
			)
			.setString("name", name)
			.setString("datasetName", datasetName)
			.uniqueResult();
	}

	public static Pair<List<GeneSet>, List<GeneSet>> getFromGeneAndDataset(String geneSymbol, String datasetName) {
		List<GeneSet> pos = getFromDatasetAndGeneAndValue(datasetName, geneSymbol, 1);
		List<GeneSet> neg = getFromDatasetAndGeneAndValue(datasetName, geneSymbol, -1);
		return new ImmutablePair<List<GeneSet>, List<GeneSet>>(pos, neg);
	}

	@SuppressWarnings("unchecked")
	private static List<GeneSet> getFromDatasetAndGeneAndValue(String datasetName, String geneSymbol, int thresholdValue) {
		return (List<GeneSet>) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT geneSet FROM GeneSet AS geneSet " +
				"  JOIN geneSet.associations AS associations " +
				"  JOIN geneSet.dataset AS dataset " +
				"  JOIN associations.gene AS gene " +
				"WHERE dataset.name = :datasetName " +
				"  AND gene.symbol = :geneSymbol " +
				"  AND associations.thresholdValue = :thresholdValue"
			)
			.setString("datasetName", datasetName)
			.setString("geneSymbol", geneSymbol)
			.setInteger("thresholdValue", thresholdValue)
			.list();
	}

	/* For stats page
	 * --------------
	 */
	public static Long getCountByResource(String resourceName) {
		return (Long) HibernateUtil
			.getCurrentSession()
			.createQuery(
				"SELECT COUNT(geneSet) FROM GeneSet AS geneSet" +
				"  JOIN geneSet.dataset AS dataset " +
				"  JOIN dataset.resource AS resource " +
				"WHERE resource.name = :resourceName"
			)
			.setString("resourceName", resourceName)
			.uniqueResult();
	}

	public static Long getCountByDatasetGroup(String datasetGroupName) {
		return (Long) HibernateUtil
			.getCurrentSession()
			.createQuery(
                "SELECT COUNT(geneSet) FROM GeneSet AS geneSet" +
                "  JOIN geneSet.dataset AS dataset " +
                "  JOIN dataset.datasetGroup AS datasetGroup " +
                "WHERE datasetGroup.name = :datasetGroupName"
            )
			.setString("datasetGroupName", datasetGroupName)
			.uniqueResult();
	}
	
    public static Long getCountByAttributeGroup(String attributeGroupName) {
        return (Long) HibernateUtil
            .getCurrentSession()
            .createQuery(
                "SELECT COUNT(geneSet) FROM GeneSet AS geneSet" +
                "  JOIN geneSet.dataset AS dataset " +
                "  JOIN dataset.attributeGroup AS attributeGroup " +
                "WHERE attributeGroup.name = :attributeGroupName"
            )
            .setString("attributeGroupName", attributeGroupName)
            .uniqueResult();
    }

	public static Long getNumAssociationsFromGene(String geneSymbol) {
		return (Long) HibernateUtil
            .getCurrentSession()
            .createQuery(
                "SELECT COUNT(geneSet) FROM GeneSet AS geneSet" +
                "  JOIN geneSet.associations AS associations " +
                "  JOIN associations.gene AS gene " +
                "WHERE gene.symbol = :geneSymbol"
            )
            .setString("geneSymbol", geneSymbol)
            .uniqueResult();
	}
}