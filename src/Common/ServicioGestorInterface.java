//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Common;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioGestorInterface extends Remote {
	public String dimeREP(String nombreCliente) throws RemoteException;
	public void dameDatos(ServicioDatosInterface servicioDatos)throws RemoteException;
	public void iniciar()throws RemoteException, NotBoundException;
	public void archivoSubido(String iD, String obtenerNombre, String nombre, long obtenerPeso, String localeString) throws RemoteException;
	public void bajarFichero(String ruta, String nombreRep, String iD, String miURL, String archivo)throws RemoteException, NotBoundException, IOException;
	public void archivoBorrado(String iD, String nombreF)throws RemoteException;
	public String dameListaClientes()throws RemoteException;
	public String dameListaClientes(String iD)throws RemoteException;
	public String listarFicheros(String nombre)throws RemoteException;
	public boolean compartirFichero(String nombreFichero, String id, String repID)throws RemoteException;
	public boolean noCompartir(String nombreFichero, String id)throws RemoteException;
	
}
