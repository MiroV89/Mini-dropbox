package Servidor;

import java.io.Serializable;
import java.util.ArrayList;

public class MetaDatosCl implements Serializable{
	private static final long serialVersionUID = -3818143902035809531L;
	private String CNombre;
	private String CID;
	private String CFecha;
	private String Cpass;
	private ArrayList<MetaDatosF> listaF;
	//Constructor de los metadatos de clientes
	public MetaDatosCl(String CN, String CID, String CF,String Cpass){
		this.CNombre=CN;
		this.CID=CID;
		this.CFecha=CF;
		this.Cpass=Cpass;
		listaF= new ArrayList<MetaDatosF>();
	}
	
	//Metodo que comparte un fichero
	public boolean compartirFichero(String FN){
		for(MetaDatosF f:listaF){
			if(f.getNombre().equals(FN)){
				f.compartir();
				return true;
			}
		}
		return false;
	}
	
	//Metodo que deja de compartir un fichero
	public boolean noCompartir(String FN){
		for(MetaDatosF f:listaF){
			if(f.getNombre().equals(FN)){
				f.noCompartir();
				return true;
			}
		}
		return false;
	}
	
	//Metodo que devuelve una lista de archivos compartidos
	public String listarCompartidos(){
		String listaComp="";
		boolean clienteEscrito=false;
		for(MetaDatosF f : listaF){
			if(f.esCompartido()){
				if(!clienteEscrito){					
					listaComp=listaComp+String.format("\n%-10s  "+"\t%-15s  "+"\t %-22s\n",
							"Nombre","Propietario","Fecha de subida:");}
				listaComp=listaComp+f.toString()+"\n";
				clienteEscrito=true;
			}
		}
		return listaComp;
	}
	
	//Metodo que añade un nuevo fichero a la base de datos
	public void nuevoFichero(String FN, String FP, long Fpe, String FF){
		listaF.add(new MetaDatosF(FN, CNombre, Fpe, FF));
	}
	//Metodo que borra un fichero de la base de datos
	public void borrarfichero(String FN){
		MetaDatosF fichAux=new MetaDatosF();
		for(MetaDatosF f:listaF){
			if(FN.equals(f.getNombre())){
				fichAux=f;
			}
		}
		listaF.remove(fichAux);
	}
	//Metodo que devuelve una lista de ficheros del cliente
	public String listarFicheros(){
		String listaFicheros="";
		boolean clienteEscrito=false;
		for(MetaDatosF f : listaF){
			if(!clienteEscrito){				
				listaFicheros=listaFicheros+String.format("\n%-10s  "+"\t%-15s  "+"\t %-22s\n",
						"Nombre","Propietario","Fecha de subida:");}
			listaFicheros=listaFicheros+f.toString()+"\n";
			clienteEscrito=true;
		}
		return listaFicheros;
	}

	//Metodo que comprueba la contraseña de un cliente
	public boolean compruebaPass(String pass){
		if(pass.equals(this.Cpass)) return true;
		return false;
		}
	//Metodo que indica la cantidad de ficheros que tiene un cliente
	public int cantidadArchivos(){
		return listaF.size();
	}
	public String getCN(){return CNombre;}
	public String getCID(){return CID;}
	public String getCF(){return CFecha;}
	
	public String toString(){
		return CNombre+"\t"+CFecha;
	}
}
