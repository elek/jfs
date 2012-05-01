package net.anzix.jfs.gdocs.model;

import com.google.api.client.util.Key;

public class Category {

	@Key("@scheme")
	public String scheme;

	@Key("@label")
	public String label;
	
	@Key("@term")
	public String term;

	public Category(){
		
	}
	public Category(String scheme, String label) {
		super();
		this.scheme = scheme;
		this.label = label;
	}
	
	
}
