package Servidor;

import java.io.Serializable;
import java.util.ArrayList;

public class MetaDatosRep implements Serializable{
	private static final long serialVersionUID = -8018205146037573416L;
	private String RepNombre="";
	private String RepID="";
	private String RepFecha="";
	private ArrayList<MetaDatosCl> listaCl = new ArrayList<MetaDatosCl>();
	public MetaDatosRep(String repN,String repID, String repF){
		this.RepNombre=repN;
		this.RepID=repID;
		this.RepFecha=repF;
	}
	
	public String getNombre(){return RepNombre;}
	public String getFecha(){return RepFecha;}
	public String getID(){return RepID;}
	
	//Metodo que actualiza la base de datos tras subir un fichero
	public void subirFichero(String clID, String FN, String FP, long Fpe, String FF){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCID().equals(clID)){
				cl.nuevoFichero(FN, FP, Fpe, FF);
			}
		}
	}
	//Metodo que actualiza la base de datos tras borrar un fichero
	public void borrarFichero(String clID,String FN){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCID().equals(clID)){
				cl.borrarfichero(FN);
			}
		}
	}
	
	//Metodo que comprueba si existe un cliente buscando su ID
	public boolean buscaCliente(String clID){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCID().equals(clID)){
				return true;
			}
		}
		return false;
	}
	//Metodo que comprueba si existe un cliente buscando su nombre
	public boolean buscaClienteN(String clN){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCN().equals(clN)){
				return true;
			}
		}
		return false;
	}
	
	//Metodo que devuelve el ID del cliente si dicho cliente pertenece a este repositorio
	public String dameID(String nombre, String pass){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCN().equals(nombre)){
				if(cl.compruebaPass(pass)){return cl.getCID();}
			}
		}
		return null;
	}
	
	//Metodo que añade un nuevo cliente a la base de datos
	public void nuevoCliente(String CN, String CID, String CF, String CP){
		listaCl.add(new MetaDatosCl(CN, CID, CF, CP));
	}
	//Metodo que devuelve una lista de clientes de este repositorio
	public String listarClientes(){
		String listaC="";
		for(MetaDatosCl cl : listaCl){
			listaC=listaC+cl.toString()+"\n";
		}
		return listaC;
	}
	//Metodo que devuelve una lista de ficheros del cliente y una lista de los ficheros compartidos por otros
	public String listarFicherosCl(String clN){
		String listaF="";
		String fichC="\t=== FICHEROS COMPARTIDOS ===\n";
		for(MetaDatosCl cl : listaCl){
			if(cl.getCN().equals(clN)){
				listaF=listaF+cl.listarFicheros()+"\n";
			}
		}
		String compartidos = listarCompartidos();
		if(compartidos.equals("") || compartidos.isEmpty()) return listaF;
		return listaF+fichC+compartidos;
	}
	
	//Metodo que devuelve una lista de los ficheros compartidos
	public String listarCompartidos(){
		String listaComp="";
		for(MetaDatosCl cl : listaCl){
			listaComp=listaComp+cl.listarCompartidos();
		}
		return listaComp;
	}
	
	//Metodo que comparte un fichero
	public boolean compartirFichero(String FN,String clID){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCID().equals(clID)){
				if(cl.compartirFichero(FN)){
					return true;
				}
			}
		}
		return false;
	}
	
	//Metodo que deja de compartir un fichero
	public boolean noCompartir(String FN,String clID){
		for(MetaDatosCl cl: listaCl){
			if(cl.getCID().equals(clID)){
				if(cl.noCompartir(FN)){
					return true;
				}
			}
		}
		return false;
	}
	
	//Metodo que devuelve la cantidad de clientes que tiene el repositorio
	public int cantidadClientes(){
		return listaCl.size();
	}
	
	public String toString(){
		return String.format("\n%-10s  "+"\t%-15s  ",RepNombre,RepFecha);
		//return this.RepNombre+"\t"+this.RepFecha;
	}
	
	
}
