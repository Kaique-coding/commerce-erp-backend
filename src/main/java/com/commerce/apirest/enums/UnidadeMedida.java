package com.commerce.apirest.enums;

import lombok.Getter;

@Getter 
public enum UnidadeMedida {

	UNIDADE(1,"Unidade","UN"),
	CAIXA(2,"Caixa","CX"),
	METRO(3,"Metro","M"),
	LITRO(4,"Litro","L"),
	QUILOGRAMA(5,"Quilograma","KG"),
	FARDO(6,"Fardo","FD");
	
	private int codigo;
	private String descricao;
	private String sigla;
	
	private UnidadeMedida(int codigo, String descricao, String sigla) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	
}
