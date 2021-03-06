package edu.mssm.pharm.maayanlab.Harmonizome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "gene_synonym")
public class GeneSynonym {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "symbol", unique = true)
	private String symbol;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gene_fk")
	private Gene gene;

	public GeneSynonym() {
	}

	public GeneSynonym(String symbol, Gene gene) {
		this.symbol = symbol;
		this.gene = gene;
	}

	public int getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Gene getGene() {
		return gene;
	}

	public void setGene(Gene gene) {
		this.gene = gene;
	}
}