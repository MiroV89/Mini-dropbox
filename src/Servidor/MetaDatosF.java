package Servidor;

import java.io.Serializable;

public class MetaDatosF implements Serializable{
	private static final long serialVersionUID = -6125379299801400151L;
	private String FNombre;
	private String FPropietario;
	private long Fpeso;
	private String FFecha;
	private boolean compartido=false;
	
	public MetaDatosF(String FN, String FP, long Fpe, String FF){
		this.FNombre=FN;
		this.FPropietario=FP;
		this.setFpeso(Fpe);
		this.FFecha=FF;
	}
	public MetaDatosF(){}
	
	public String getNombre(){return FNombre;}
	
	public String toString(){
		return String.format("%-10s  "+"\t%-15s  "+"\t %-22s\n",
			FNombre,FPropietario,FFecha);
	}
	
	public void compartir(){this.compartido=true;}
	public boolean esCompartido(){return compartido;}
	public void noCompartir(){this.compartido=false;}
	public long getFpeso() {
		return Fpeso;
	}
	public void setFpeso(long fpeso) {
		Fpeso = fpeso;
	}
}
