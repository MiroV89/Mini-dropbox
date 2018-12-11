//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Servidor;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;


import Common.*;


public class Servidor implements IServidor {

	private ServicioAutenticacionInterface ServicioAutenticacion;
	private ServicioDatosInterface servicioDatos;
	private ServicioGestorInterface servicioGestor;
	private String urlSA="rmi://localhost:"+8000+"/ServicioAutenticacion";
	private String urlG="rmi://localhost:"+9000+"/ServicioGestor";
	//private String urlD="rmi://localhost:"+10000+"/ServicioDatos";
	
	//MAIN
	public static void main(String[] args) throws Exception {
		Utils.setCodeBase(IServidor.class);
		try{
			Servidor servidor = new Servidor(); 
			IServidor remoteS = (IServidor)UnicastRemoteObject.exportObject(servidor, 7000);
			String URL_nombre=("rmi://localhost:"+7000+"/Servidor");
			
			Registry registry = LocateRegistry.createRegistry(7000);
			registry.rebind(URL_nombre, remoteS); 
	
			System.out.println("Servidor en funcionamiento.");
			servidor.menuServidor();
			
			registry.unbind(URL_nombre);
			UnicastRemoteObject.unexportObject(servidor, true);
			System.out.println("Servidor Terminado");
		}
		catch (ExportException e){System.out.println("Error: El servidor ya está ejecutandose o el puerto esta ocupado");}
	}
	
	//Constructor del servidor que pone en marcha todos sus servicios y les proporciona la base de datos
	public Servidor() throws RemoteException, NotBoundException{ 
		this.servicioDatos=new ServicioDatosImpl();
		servicioDatos.iniciar(); 
		this.ServicioAutenticacion=new ServicioAutenticacionImpl();
		ServicioAutenticacion.iniciar();
		this.servicioGestor=new ServicioGestorImpl();
		servicioGestor.iniciar();
		ServicioAutenticacion.dameDatos(servicioDatos);
		servicioGestor.dameDatos(servicioDatos);
	}
	//Menu del servidor que muestra las opciones de listar clientes,repositorios o parejas
	public void menuServidor() throws NotBoundException, IOException{
		try{
		int opt= GUI.menu("Menu Servidor", new String[]{"Listar clientes","Listar Repositorios","Listar Parejas Repositorio-cliente", "Salir"});
			switch (opt){
				case 0: System.out.println("Listar clientes");
						System.out.print(dameListaClientes());
						System.out.println();
						menuServidor();
						break;
				case 1: System.out.println("Lista de Repositorios:");
						System.out.println(listarRepositorios());
						System.out.println();
						menuServidor(); 
						break;
				case 2: System.out.println("Listar Parejas Repositorio-cliente");
						System.out.println(listarParejas());
						menuServidor(); 
						break;
				case 3: System.exit(0);
			}
		}catch (Exception e){System.out.println("Ha ocurrido un error, vuelva a intentarlo."); menuServidor();}
	}
	//Metodo que imprime por pantalla una lista de clientes, para lo cual accede a la base de datos
	public String dameListaClientes() throws RemoteException{
		return servicioDatos.listarClientes();
	}
	//Metodo que imprime por pantalla una lista de repositorios, para lo cual accede a la base de datos
	public String listarRepositorios() throws RemoteException {
		return servicioDatos.listarRepositorios();
	}
	//Metodo que imprime por pantalla una lista de parejas, para lo cual accede a la base de datos
	public String listarParejas() throws RemoteException{
		return servicioDatos.listarParejas();
	}

	//Metodo que devuelve la url del servicio de autenticacion
	@Override
	public String dameServicioAutenticacion() throws RemoteException{
		return urlSA;
	}
	//Metodo que devuelve la url del servicio gestor
	@Override
	public String dameServicioGestor() throws RemoteException{
		return urlG;
	}
}

