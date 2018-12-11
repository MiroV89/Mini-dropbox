//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////

package Cliente;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

import Common.*;

public class Cliente {
	private String nombre=null;
	@SuppressWarnings("unused")
	private String pass=null;
	private String ID="";
	public String miURL="";
	public static IServidor servidor;
	@SuppressWarnings("unused")
	private ServicioDiscoClienteInterface servicioDC;
	private ServicioAutenticacionInterface servicioA;
	private ServicioGestorInterface servicioG;
	private ServicioClOperadorInterface servicioClOp;
	private static String URLServer="rmi://localhost:"+7000+"/Servidor";
	private String carpetaPorDefecto="C:/Descargas";
	private String nombreRep;

	//Constructor del Cliente que pide al servidor las URL de los servicios Gestor y autenticacion
	public Cliente() throws NotBoundException, IOException{
		try{
		Registry registro = LocateRegistry.getRegistry(8000);
		servicioA = (ServicioAutenticacionInterface) registro.lookup(servidor.dameServicioAutenticacion());
		registro = LocateRegistry.getRegistry(9000);
		servicioG = (ServicioGestorInterface) registro.lookup(servidor.dameServicioGestor());
		//Ejecutamos el menu principal
		menuPrincipal();
		}
		catch (Exception exception) {System.out.println("Servidor deconectado.");}
		}
	//Constructor que asigna el nombre y contraseña al cliente
	public Cliente(String nombre, String pass){
		this.nombre=nombre;
		this.pass=pass;
	}
	//Constructor que asigna nombre, contraseña e identificador al cliente
	public Cliente(String nombre, String pass, String ID){
		this.nombre=nombre;
		this.pass=pass;
		this.ID=ID;
	}
	
	//2
	public static void main(String[] args) throws NotBoundException, IOException {
		Registry registro = LocateRegistry.getRegistry(7000);
		try{
		servidor = (IServidor) registro.lookup(URLServer);
		new Cliente();}
		catch (Exception e){System.out.println("No se ha encontrado el servidor.");}
	}
	
	//Menu principal que muestra las opciones de autenticacion y registro.
	public void menuPrincipal() throws NotBoundException, IOException{
		try{
		int opt= GUI.menu("Menu principal Cliente", new String[]{"Autenticarse","Registro nuevo", "Salir"});
			switch (opt){
				case 0: 	
					try{
						autenticarse(); 
					}
					catch (Exception e){
						System.out.println("Ha ocurrido un error, puede que no haya repositorios en marcha.");
						System.out.println("Intentelo de nuevo mas tarde.");
						menuPrincipal();
					}
					break;				
				case 1: registrarse(); break;
				case 2: System.exit(0);
			}
		}catch (Exception e){System.out.println("Ha ocurrido un error con el registro, vuelva a intentarlo."); menuPrincipal();}
	}
	
	//Metodo de registro, pide los datos de nombre y contraseña para el nuevo cliente y accede al servicio de Autenticacion
	private void registrarse() throws NotBoundException, IOException {
		String nombre = GUI.input("Registro nuevo", "Ingrese su nombre: ");
		String pass = GUI.input("Ingrese contraseña: ");
		boolean registrado=servicioA.registrarCl(nombre, pass);
		if(!registrado){System.out.println("Nombre de usuario no disponible");}
		menuPrincipal();
	}
	
	//Metodo de autenticacion, pide los datos de nombre y contraseña del cliente y accede al servicio de Autenticacion
	private void autenticarse() throws NotBoundException, IOException {
		String nombre = GUI.input("Autenticarse", "Ingrese su nombre: ");
		String pass = GUI.input("Ingrese contraseña: ");
		ID=servicioA.autenticarCl(nombre, pass);
		if(ID==null){
			System.out.println("Datos incorrectos, intentelo de nuevo");
			menuPrincipal();
		}
		else {
			nombreRep=servicioG.dimeREP(ID);
			miURL="rmi://localhost:15000/"+ID;
			this.nombre=nombre;
			this.pass=pass;
			String urlSCLOP=("rmi://localhost:11000/ServicioClOp");
			Registry registro = LocateRegistry.getRegistry(11000);
			this.servicioClOp = (ServicioClOperadorInterface) registro.lookup(urlSCLOP);
			//En caso de que mas de un cliente se conecte desde el mismo pc utilizaremos el mismo servicio.
			try{
			servicioDC = new ServicioDiscoClienteImpl(this.ID,15000);			
			}
			catch (Exception e){}
			menuUsuario();
		}
	}
	
	//El menu del usuario ya autenticado
	public void menuUsuario() throws NotBoundException, IOException{
		int opt=0;
		try{
		opt= GUI.menu("Menu Usuario: "+this.ID, new String[]{"Subir ficheros","Bajar ficheros","Borrar ficheros","Compartir ficheros",
				"Listar ficheros","Mostrar lista de clientes", "Salir"});
			switch (opt){
				case 0: String ruta=GUI.input("Escriba la ruta del fichero que desea subir: ");
					//Una vez obtenida la ruta, listamos los archivos en dicho directorio.
						File directorio = new File(ruta);
						File[] listaArchivos = directorio.listFiles();
						String posiblesArchivos="";
						for (int i=0; i<listaArchivos.length; i++){
							if(listaArchivos[i].isFile()){
								posiblesArchivos=posiblesArchivos+"\n- "+listaArchivos[i].getName();
							}
						}
						System.out.println("Los siguientes archivos se encuentran en dicha ruta:\n"+posiblesArchivos);
						
						String nombre=GUI.input("\nEscriba el nombre del fichero que desea subir: ");
						subir(ruta,nombre);
						menuUsuario(); 
						break;
				case 1: System.out.println("Los ficheros que puede descargar son los siguientes:\n");
						listarFicheros();
						String FN=GUI.input("Escriba el nombre del fichero que desea descargar: ");
						String clID=GUI.input("Si el fichero es suyo pulse intro, en caso contrario escriba el nombre del cliente al que pertenece: ");
						bajar(carpetaPorDefecto,clID,FN);
						menuUsuario(); 
						break;
				case 2: listarFicheros();
						String FBorrar=GUI.input("(Los archivos compartidos no pueden ser eliminados)\n"
								+ "Escriba el nombre del fichero que desea eliminar: ");
						borrar(FBorrar);
						menuUsuario(); 
						break;
				case 3: listarFicheros();
						String Fcomp=GUI.input("Escriba el nombre del fichero que desea compartir: ");
						if(compartir(Fcomp)) System.out.print("Archivo "+Fcomp+" compartido con exito.\n");
						else System.out.println("El archivo no pudo compartirse\n");
						menuUsuario();
						break;
				case 4: System.out.println("Listar Ficheros");
						listarFicheros();
						menuUsuario();
						break;
				case 5: listarClientes();
						menuUsuario(); 
						break;
				case 6: System.exit(0);
			}
		}catch (Exception e){System.out.println("Ha ocurrido un error, vuelva a intentarlo."); menuUsuario();}
	}
	
	//Metodo para la subida de archivos, pide la ruta y el nombre del fichero
	@SuppressWarnings("deprecation")
	public void subir(String ruta, String nombre) throws NotBoundException, IOException{
		Fichero archivo = new Fichero(ruta,nombre, this.nombre);
		boolean subido = servicioClOp.subirArchivo(archivo,nombreRep, this.ID);
		Date fechaActual= new Date();
		if(subido){
			System.out.println("Archivo "+nombre+" subido con exito.\n");
			servicioG.archivoSubido(ID,archivo.obtenerNombre(),this.nombre,archivo.obtenerPeso(),fechaActual.toLocaleString());
		}	
		else{
			System.out.println("Compruebe que el fichero no exista y vuelva a intentarlo.\n");
		}
	}
	
	//Metodo de bajada de ficheros, pide la ruta de descarga (en este caso utilizamos por defecto la carpeta C:/Descargas),
	//el ID del cliente y el nombre del fichero
	public void bajar(String ruta,String clID, String archivo) throws NotBoundException, IOException{
		if(clID==null || clID.isEmpty()) clID=this.ID;
		try{
			servicioG.bajarFichero(ruta, nombreRep, clID , "rmi://localhost:15000/"+clID, archivo);
			System.out.println("La descarga se ha completado\nPuede encontrar el archivo en:\n"+carpetaPorDefecto+"\n");
		}
		catch (Exception e){System.out.println("Ha ocurrido un error en la descarga, intentelo mas tarde.\n"); e.printStackTrace();}
	}
	
	//Metodo de borrado de ficheros, pide el nombre del fichero que se va a eliminar
	public void borrar(String nombreF) throws IOException, NotBoundException{
		boolean borrado = servicioClOp.borrarArchivo(nombreF,nombreRep, this.ID);
		if(borrado){
			servicioG.archivoBorrado(ID,nombreF);
			System.out.println("Archivo \""+nombreF+"\" borrado con exito.");
		}
		else System.out.println("Archivo no borrado");
	}
	
	//Metodo compartir permite que el fichero sea listado y descargado por otros clientes
	public boolean compartir(String nombreFichero) throws RemoteException{
		return servicioG.compartirFichero(nombreFichero,this.ID,nombreRep);
	}
	//Metodo no compartir hace que el fichero no pueda ser listado o descargado por otros clientes.
	//Por el momento este metodo no va a ser utilizado.
	public boolean noCompartir(String nombreFichero) throws RemoteException{
		return servicioG.noCompartir(nombreFichero,this.ID);
	}
		
	//Metodo que imprime por pantalla todos los usuarios conectados	
	public void listarClientes() throws NotBoundException, IOException{
		System.out.println("Usuarios conectados: ");
		String listasCl=servicioG.dameListaClientes();
		System.out.println(listasCl);
	}
	
	//Metodo que imprime por pantalla los ficheros del cliente, así como los ficheros compartidos de otros clientes
	public void listarFicheros() throws NotBoundException, IOException{
		System.out.println("\t=== MIS FICHEROS ===");
		String listasF=servicioG.listarFicheros(this.nombre);
		System.out.println(listasF);
	}
	
	public void salir() throws RemoteException{}
	
	public String toString(){
		return "Cliente: "+this.nombre;
	}

}
