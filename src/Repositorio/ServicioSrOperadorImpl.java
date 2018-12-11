//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Repositorio;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Common.*;
import Common.Utils;

public class ServicioSrOperadorImpl implements ServicioSrOperadorInterface{
	//Constructor vacio
	public ServicioSrOperadorImpl(){}
	//Constructor que exporta remotamente el servicio
	public ServicioSrOperadorImpl(int puerto) throws RemoteException{
		Utils.setCodeBase(ServicioSrOperadorInterface.class);
		String URL_nombre=("rmi://localhost:"+puerto+"/ServicioSrOp");
		ServicioSrOperadorImpl servicioSrOp = new ServicioSrOperadorImpl();
		ServicioSrOperadorInterface remote = (ServicioSrOperadorInterface)UnicastRemoteObject.exportObject(servicioSrOp, puerto);
		Registry registry = LocateRegistry.createRegistry(puerto);
		registry.rebind(URL_nombre, remote);
		//System.out.println("Servicio SR-OP arrancado. "+URL_nombre);
	}

	//Metodo encargado de la descarga de ficheros
	@Override
	public void bajar(String ruta, String clID, String clURL, String archivoNombre,String repID) throws NotBoundException, IOException {	
		String url=clURL+"/ServicioDC";
		String rutaRep = (getDirectorio()+"\\Repositorios\\"+repID+"\\"+clID);
		Fichero fich = new Fichero(rutaRep+"\\"+archivoNombre,clID);
		Registry registro = LocateRegistry.getRegistry(15000);
		ServicioDiscoClienteInterface servDC = (ServicioDiscoClienteInterface) registro.lookup(url);
		servDC.descarga(ruta,fich,archivoNombre);
	}
	
	//Metodo que devuelve el directorio actual
	private String getDirectorio() throws IOException
	{
		String dir="";
	   File miDir = new File (".");
	   try {
	       dir=miDir.getCanonicalPath();
	   }
	   catch(Exception e) {
	      e.printStackTrace();
	   }
	   return dir;
	}

}
