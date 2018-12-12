package Common;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioSrOperadorInterface extends Remote {
	public void bajar(String ruta, String clID, String clURL, String archivo,String repID) throws RemoteException, NotBoundException, IOException;

}
