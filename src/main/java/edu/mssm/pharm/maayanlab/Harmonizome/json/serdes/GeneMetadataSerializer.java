package edu.mssm.pharm.maayanlab.Harmonizome.json.serdes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.mssm.pharm.maayanlab.Harmonizome.model.Attribute;
import edu.mssm.pharm.maayanlab.Harmonizome.model.Dataset;
import edu.mssm.pharm.maayanlab.Harmonizome.model.Feature;
import edu.mssm.pharm.maayanlab.Harmonizome.model.Gene;
import edu.mssm.pharm.maayanlab.Harmonizome.model.GeneSet;
import edu.mssm.pharm.maayanlab.Harmonizome.model.GeneSynonym;
import edu.mssm.pharm.maayanlab.Harmonizome.model.HgncRootFamily;
import edu.mssm.pharm.maayanlab.Harmonizome.model.Protein;

public class GeneMetadataSerializer implements JsonSerializer<Gene> {

	public JsonElement serialize(final Gene gene, final Type type, final JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		result.add("symbol", new JsonPrimitive(gene.getSymbol()));
		
		JsonArray synonyms = new JsonArray();
		for (GeneSynonym syn : gene.getSynonyms()) {
			synonyms.add(new JsonPrimitive(syn.getSymbol()));
		}
		SerDesUtil.add(result, "synonyms", synonyms);
		
		SerDesUtil.add(result, "name", gene.getName());
		SerDesUtil.add(result, "description", gene.getDescription());
		SerDesUtil.add(result, "ncbiEntrezGeneId", gene.getNcbiEntrezGeneId());
		SerDesUtil.add(result, "ncbiEntrezGeneUrl", gene.getNcbiEntrezGeneUrl());
		
		JsonArray proteins = new JsonArray();
		for (Protein protein : gene.getProteins()) {
			proteins.add(context.serialize(protein, Protein.class));
		}
		SerDesUtil.add(result, "proteins", proteins);

		JsonArray hgncRootFamilies = new JsonArray();
		for (HgncRootFamily family : gene.getHgncRootFamilies()) {
			hgncRootFamilies.add(context.serialize(family, HgncRootFamily.class));
		}
		SerDesUtil.add(result, "hgncRootFamilies", hgncRootFamilies);
		
		List<GeneSet> geneSets = new ArrayList<GeneSet>();
		for (Feature feature : gene.getFeatures()) {
			Attribute attribute = feature.getAttribute();
			Dataset dataset = attribute.getDataset();
			GeneSet geneSet = new GeneSet(attribute, dataset);
			geneSets.add(geneSet);
		}
		result.add("geneSets", context.serialize(geneSets));
		
		return result;
	}
}