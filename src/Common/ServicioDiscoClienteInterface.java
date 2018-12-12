package Common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDiscoClienteInterface extends Remote {


	void descarga(String ruta, Fichero fich,String archivoNombre) throws RemoteException , IOException;
}
