package Common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioClOperadorInterface extends Remote{
	public boolean subirArchivo(Fichero archivo, String string, String id) throws RemoteException, IOException;
	public boolean borrarArchivo(String nombreF, String string, String id) throws RemoteException, IOException;
}
