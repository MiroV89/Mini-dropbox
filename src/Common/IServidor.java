//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServidor extends Remote {

	public String dameServicioAutenticacion() throws RemoteException;
	public String dameServicioGestor() throws RemoteException;

}