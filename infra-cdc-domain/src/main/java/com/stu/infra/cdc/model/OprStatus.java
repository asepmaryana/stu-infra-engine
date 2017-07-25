package com.stu.infra.cdc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="opr_status")
public class OprStatus implements java.io.Serializable {
		
	private static final long serialVersionUID = 8475189452799524645L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false, length=10)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "oprStatus", cascade = CascadeType.ALL)
	private Set<Node> nodes = new HashSet<Node>(0);
	
	public OprStatus() {}
	
	public OprStatus(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}
	
}
