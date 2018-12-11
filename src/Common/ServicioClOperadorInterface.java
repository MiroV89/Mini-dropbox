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

public interface ServicioClOperadorInterface extends Remote{
	public boolean subirArchivo(Fichero archivo, String string, String id) throws RemoteException, IOException;
	public boolean borrarArchivo(String nombreF, String string, String id) throws RemoteException, IOException;
}
