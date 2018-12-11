//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Common;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDatosInterface extends Remote{

	boolean nuevoRep(String nombre) throws RemoteException;

	boolean nuevoCl(String clNombre, String pass) throws RemoteException;

	String consultaRep(String nombre) throws RemoteException;

	String consultaCl(String nombre, String pass) throws RemoteException;

	String buscaRepCl(String clID) throws RemoteException;

	String listarClientes() throws RemoteException;

	String listarClientes(String repID)throws RemoteException;

	String listarMisFicheros(String clN) throws RemoteException;

	String listarRepositorios() throws RemoteException;

	String listarParejas() throws RemoteException;

	void iniciar()throws RemoteException, NotBoundException;

	void archivoSubido(String clID, String fN, String fP, long fpe, String fF)throws RemoteException;

	void archivoBorrado(String clID, String nombreFichero)throws RemoteException;

	boolean compartirFichero(String fN, String clID, String repID)throws RemoteException;

	boolean noCompartir(String fN, String clID, String repID)throws RemoteException;


}
