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

public interface ServicioSrOperadorInterface extends Remote {
	public void bajar(String ruta, String clID, String clURL, String archivo,String repID) throws RemoteException, NotBoundException, IOException;

}
