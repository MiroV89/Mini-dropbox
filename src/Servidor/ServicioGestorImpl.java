//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Servidor;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Common.*;

public class ServicioGestorImpl implements ServicioGestorInterface {

	private static ServicioDatosInterface servicioDatos = new ServicioDatosImpl();
	ServicioSrOperadorInterface servicioSrOp;

	public void iniciar() throws RemoteException, NotBoundException{
		Utils.setCodeBase(ServicioGestorInterface.class);
		Registry registry = LocateRegistry.createRegistry(9000);
		ServicioGestorImpl servicioG = new ServicioGestorImpl();
		ServicioGestorInterface remoteS = (ServicioGestorInterface)UnicastRemoteObject.exportObject(servicioG, 9000);
		String URL_nombre=("rmi://localhost:"+9000+"/ServicioGestor");
		registry.rebind(URL_nombre, remoteS);
		
		//registry.unbind(URL_nombre);
		//UnicastRemoteObject.unexportObject(servicioG, true);
	}
	
	//Metodo que pide el servicio de base de datos
	@SuppressWarnings("static-access")
	@Override
	public void dameDatos(ServicioDatosInterface datos){
		this.servicioDatos=datos;
	}
	
	//Metodo que se encarga de devolver el ID del repositorio al que pertenece el cliente que le decimos
	@Override
	public String dimeREP(String clID) throws RemoteException {
		String tieneRep=servicioDatos.buscaRepCl(clID);
		return tieneRep;
	}
	
	//Metodo encargado de descargar un fichero en una ruta especifica
	public void bajarFichero(String ruta,String nombreRep, String clID, String clURL, String archivo) throws NotBoundException, IOException{
		try{
			Registry registro = LocateRegistry.getRegistry(11001);
			String URL_nombre=("rmi://localhost:11001/ServicioSrOp");
			servicioSrOp = (ServicioSrOperadorInterface) registro.lookup(URL_nombre);

			servicioSrOp.bajar(ruta,clID,clURL,archivo,nombreRep);
		}
		catch (Exception e){System.out.println("Error al conectar con el servicio.");}
		
	}
	//Metodo que actualiza la base de datos tras subir un archivo
	public void archivoSubido(String clID,String FN,String FP, long Fpe, String FF) throws RemoteException{
		servicioDatos.archivoSubido(clID,FN,FP,Fpe,FF);
	}
	//Metodo que actualiza la base de datos tras borrar un archivo
	public void archivoBorrado(String clID,String nombreFichero) throws RemoteException{
		servicioDatos.archivoBorrado(clID,nombreFichero);
	}
	
	//Metodo que devuelve una lista de todos los clientes
	public String dameListaClientes() throws RemoteException{
		return servicioDatos.listarClientes();
	}
	//Metodo que devuelve una lista de clientes pertenecientes a un repositorio
	public String dameListaClientes(String repID) throws RemoteException{
		return servicioDatos.listarClientes(repID);
	}
	//Metodo que lista los ficheros de un cliente
	public String listarFicheros(String clN) throws RemoteException {
		return servicioDatos.listarMisFicheros(clN);
	}
	//Metodo que hace que un fichero sea compartido
	public boolean compartirFichero(String FN, String clID,String repID) throws RemoteException{
		return servicioDatos.compartirFichero(FN,clID,repID);
	}
	//Metodo que hace que el fichero deje de ser compartido
	public boolean noCompartir(String FN, String clID) throws RemoteException{
		String repID=servicioDatos.buscaRepCl(clID);
		return servicioDatos.noCompartir(FN,clID,repID);
	}
}
