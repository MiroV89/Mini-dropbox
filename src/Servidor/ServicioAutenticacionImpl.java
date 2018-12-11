//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Servidor;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import Common.*;


public class ServicioAutenticacionImpl implements ServicioAutenticacionInterface {

	private static ServicioDatosInterface servicioDatos;
	
	//Metodo que inicia el servicio de autenticacion y lo exporta remotamente
	public void iniciar() throws RemoteException, NotBoundException{
		Utils.setCodeBase(ServicioAutenticacionInterface.class);
		Registry registry = LocateRegistry.createRegistry(8000);
		ServicioAutenticacionImpl servicioA = new ServicioAutenticacionImpl();
		ServicioAutenticacionInterface remoteS = (ServicioAutenticacionInterface)UnicastRemoteObject.exportObject(servicioA, 8000);
		String URL_nombre=("rmi://localhost:"+8000+"/ServicioAutenticacion");
		registry.rebind(URL_nombre, remoteS);
		
		//registry.unbind(URL_nombre);
		//UnicastRemoteObject.unexportObject(servicioA, true);
	}
	
	//Metodo encargado de autenticar un cliente, comprueba que existe el nombre del cliente y coincide con la contraseña
		@Override
		public String autenticarCl(String nombre, String pass) throws RemoteException {
			String auten=servicioDatos.consultaCl(nombre, pass);
			return auten;
		}
		//Metodo encargado de autenticar un repositorio, comprueba que existe en la base de datos
		public String autenticarRep(String nombre) throws RemoteException {
			String auten=servicioDatos.consultaRep(nombre);
			return auten;
		}
		//Metodo encargado de registrar un cliente
		@Override
		public boolean registrarCl(String nombre, String pass) throws RemoteException {
			return servicioDatos.nuevoCl(nombre, pass);
		}
		//Metodo encargado de registrar un repositorio
		@Override
		public boolean registrarRep(String nombre) throws RemoteException {
			boolean as=false;
			try{as= servicioDatos.nuevoRep(nombre);}
			catch (Exception exception) {System.out.println(exception);}
			return as;
		}
		//Metodo encargado de recibir la base de datos desde el servidor
		@SuppressWarnings("static-access")
		@Override
		public void dameDatos(ServicioDatosInterface servicioDatos) throws RemoteException{
			this.servicioDatos=servicioDatos;
			
		}


	}

