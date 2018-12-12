package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServidor extends Remote {

	public String dameServicioAutenticacion() throws RemoteException;
	public String dameServicioGestor() throws RemoteException;

}
