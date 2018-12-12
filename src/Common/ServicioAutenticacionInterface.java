package Common;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ServicioAutenticacionInterface extends Remote {

	public String autenticarCl(String nombre, String pass) throws RemoteException;

	public String autenticarRep(String nombre) throws RemoteException;

	public boolean registrarRep(String nombre) throws RemoteException;

	public boolean registrarCl(String nombre, String pass) throws RemoteException;

	public void dameDatos(ServicioDatosInterface servicioDatos)throws RemoteException;

	public void iniciar()throws RemoteException, NotBoundException;
	

}
