//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Repositorio;

import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import Common.*;

public class Repositorio{
	String nombre="";
	String ID="";
	public static int sesion = 0;
	public static IServidor servidor;
	ArrayList<Fichero> listaFicheros = new ArrayList<Fichero>();
	ArrayList<String> listaIDClientes = new ArrayList<String>();
	private static String URLServer="rmi://localhost:"+7000+"/Servidor";
	ServicioClOperadorImpl servicioClOp;
	ServicioSrOperadorImpl servicioSrOp;
	private static ServicioAutenticacionInterface servicioA;
	private static ServicioGestorInterface servicioG;
	
	//Constructor de Repositorio vacio
	public Repositorio()throws RemoteException{}
	
	//Constructor de repositorio que pide al servidor las url de los servicios de autenticacion y gestor
	//y despues utiliza dichos servicios remotamente
	public Repositorio(String in) throws NotBoundException, AlreadyBoundException, IOException{
		/*Registry registro = LocateRegistry.getRegistry(8000);
		servicioA = (ServicioAutenticacionInterface) registro.lookup(servidor.dameServicioAutenticacion());
		registro = LocateRegistry.getRegistry(9000);
		servicioG = (ServicioGestorInterface) registro.lookup(servidor.dameServicioGestor());*/
		menuPrincipal();
		}
	//Constructor de repositorio que asigna el nombre y el ID del repositorio
	public Repositorio(String nombre,String ID){
		this.nombre=nombre;
		this.ID=ID;
	}
	//Metodo que añade un cliente nuevo a la lista local del repositorio en cuestion
	public void nuevoCliente(String clID){
		listaIDClientes.add(clID);
	}
	
	//Metodo que devuelve el ID del repositorio
	public String getID(){return this.ID;}

	//MAIN
	public static void main(String[] args) throws NotBoundException, IOException, AlreadyBoundException {
		Registry registro = LocateRegistry.getRegistry(7000);
		try{
		servidor = (IServidor) registro.lookup(URLServer);
		registro = LocateRegistry.getRegistry(8000);
		servicioA = (ServicioAutenticacionInterface) registro.lookup(servidor.dameServicioAutenticacion());
		registro = LocateRegistry.getRegistry(9000);
		servicioG = (ServicioGestorInterface) registro.lookup(servidor.dameServicioGestor());
		//menuPrincipal();
		new Repositorio("inicio");
		}
		catch (Exception e){System.out.println("No se ha encontrado el servidor.");}
	}
	
	//Metodo que se ejecuta tras autenticar el repositorio, pone en marcha sus servicios
	public void iniciarRepositorio() throws NotBoundException, AlreadyBoundException, IOException{
		//En caso de que haya 2 repositorios en el mismo pc los servicios solo se levantaran 1 vez
		try{
			servicioClOp = new ServicioClOperadorImpl(11000);
			servicioSrOp = new ServicioSrOperadorImpl(11001);
		}
		catch (Exception e){}
		menuRepositorio();		
	}
	
	//Menu principal que muestra las opciones de autenticacion y registro del repositorio
	public void menuPrincipal() throws NotBoundException, AlreadyBoundException, IOException{
		this.ID="miID";
		try{
		int opt= GUI.menu("Menu principal Repositorios", new String[]{"Autenticarse","Registro nuevo", "Salir"});
			switch (opt){
				case 0: autenticarse(); break;
				case 1: registrarse(); break;
				case 2: System.exit(0);
			}
		}catch (Exception e){System.out.println("Ha ocurrido un error, vuelva a intentarlo."); menuPrincipal();}
	}
	//Metodo que pide el nombre del repositorio que se desea registrar y lo registra en la base de datos del servidor
	//Ademas crea la carpeta del repositorio en el pc
	public void registrarse() throws NotBoundException, AlreadyBoundException, IOException {
		String nombre = GUI.input("Registro nuevo", "Ingrese su nombre: ");
		boolean registrado=false;
		try{
			Registry registro = LocateRegistry.getRegistry(8000);
		
		servicioA = (ServicioAutenticacionInterface) registro.lookup(servidor.dameServicioAutenticacion());
		registrado = servicioA.registrarRep(nombre);
		}
		catch (Exception e){System.out.println(e);}
		if(registrado) {
			System.out.println("Repositorio registrado correctamente");
			//Creamos el directorio para almacenar los archivos
			String ruta = (getDirectorio()+"\\Repositorios\\"+nombre);
			File directorio = new File(ruta);
			directorio.mkdirs();
		}
		else{System.out.println("El nombre de repositorio ya esta siendo utilizado");}
		menuPrincipal();
	}
	
	//Metodo que devuelve el directorio actual
    private String getDirectorio() throws IOException
    {
    	String dir="";
        File miDir = new File (".");
        try {
            dir=miDir.getCanonicalPath();
        }
        catch(Exception e) {
           e.printStackTrace();
        }
        return dir;
     }
	
    //Metodo de autenticacion que pide el nombre del repositorio, comprueba que existe en la base de datos
    //y accede al menu interno del repositorio
	public void autenticarse() throws NotBoundException, AlreadyBoundException, IOException {
		String nombre = GUI.input("Autenticarse", "Ingrese su nombre: ");
		if(nombre != null && !nombre.isEmpty()){
			String miID = servicioA.autenticarRep(nombre);
			if(miID!=null){
				this.ID=miID;
				iniciarRepositorio();
			}
			else{menuPrincipal();}
		}
		
	}
	
	//Menu interno del repositorio al que se accede una vez autenticado, muestra las opciones de
	//Listar clientes, listar ficheros de un cliente, Salir
	public void menuRepositorio() throws RemoteException, NotBoundException{
		try{
			int opt= GUI.menu("Menu Repositorio", new String[]{"Listar Clientes","Listar ficheros del Cliente","Salir"});
				switch (opt){
					case 0: System.out.println("Listar Clientes");
							listarClientes(); 
							break;
					case 1: System.out.println("Listar ficheros del Cliente");
							listarFicherosCliente(GUI.input("Escriba el ID del Cliente"));
							break;
					case 2: System.exit(0);
				}
		}catch (Exception e){System.out.println("Ha ocurrido un error, vuelva a intentarlo."); menuRepositorio();}
	}
	
	//Metodo que imprime por pantalla los clientes pertenecientes al repositorio, para lo cual accede a la base de datos
	public void listarClientes() throws RemoteException, NotBoundException{
		System.out.println("Usuarios conectados:");
		String listas=servicioG.dameListaClientes(ID);		
		System.out.println(listas);
		menuRepositorio();
	}
	
	//Metodo que comprueba si un cliente pertenece a este repositorio.
	public boolean buscaCliente(String clID){
		for(String cl : listaIDClientes){
			if(cl.equals(clID)){
				return true;
			}
		}
		return false;
	}

	//Metodo que imprime por pantalla los ficheros de un cliente
	public void listarFicherosCliente(String clN) throws RemoteException, NotBoundException {
		System.out.println("Ficheros de: "+clN);
		String listaF = servicioG.listarFicheros(clN);
		System.out.println(listaF);
		menuRepositorio();
	}
	
	public void salir() throws RemoteException{}
	
	public boolean compruebaNombre(String nombre){
		if(this.nombre.equals(nombre)) return true;
		return false;		
	}
	public String toString(){
		return "Repositorio : "+this.nombre+" \t ID: "+this.ID;
	}
	public String getNombre(){
		return this.nombre;
	}
}
