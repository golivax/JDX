package br.usp.ime.jdx.entity;

import org.apache.commons.lang3.StringUtils;

public class Package {

	private String FQN;
	
	public Package(String FQN){
		this.FQN = FQN;
	}

	public String getName() {
		return StringUtils.substringAfterLast(FQN, ".");
	}

	public String getFQN() {
		return FQN;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FQN == null) ? 0 : FQN.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Package other = (Package) obj;
		if (FQN == null) {
			if (other.FQN != null)
				return false;
		} else if (!FQN.equals(other.FQN))
			return false;
		return true;
	}
	
	public String toString(){
		return FQN;
	}
	
}
