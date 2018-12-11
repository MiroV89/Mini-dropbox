//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDiscoClienteInterface extends Remote {


	void descarga(String ruta, Fichero fich,String archivoNombre) throws RemoteException , IOException;
}
