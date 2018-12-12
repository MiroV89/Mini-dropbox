package Servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import Common.*;

public class ServicioDatosImpl implements ServicioDatosInterface {
	private static ArrayList<MetaDatosRep> listaRep= new ArrayList<MetaDatosRep>();
	
	//Metodo que inicia la base de datos, importandola desde el pc
	//Tambien exporta la base de datos remotamente
	public void iniciar() throws RemoteException, NotBoundException{
		importarBBDD();
		Utils.setCodeBase(ServicioDatosInterface.class);
		ServicioDatosImpl servicioD = new ServicioDatosImpl();
		ServicioDatosInterface remoteS = (ServicioDatosInterface)UnicastRemoteObject.exportObject(servicioD, 10000);
		String URL_nombre=("rmi://localhost:"+10000+"/ServicioDatos");
		
		Registry registry = LocateRegistry.createRegistry(10000);
		registry.rebind(URL_nombre, remoteS);
		
	//	registry.unbind(URL_nombre);
	//	UnicastRemoteObject.unexportObject(servicioD, true);
	}
	
	//Metodo que busca el repositorio al que pertenece un cliente y devuelve su ID
	public String buscaRepCl(String clID)throws RemoteException{
		for(MetaDatosRep rep: listaRep){
			rep.toString();
			if(rep.buscaCliente(clID)){
				return rep.getID();
			}
		}
		return null;
	}

	//Metodo que devuelve una lista de todos los clientes
	public String listarClientes(){
		String cadena= "Nombre:\t Fecha de registro:\n";
		for(MetaDatosRep rep: listaRep){
			cadena=cadena+rep.listarClientes();
		}
		return cadena;
	}
	//Metodo que devuelve una lista de clientes pertenecientes a un repositorio
	public String listarClientes(String repID){
		String cadena= "Nombre:\t Fecha de registro:\n";
		for(MetaDatosRep rep: listaRep){
			if(repID.equals(rep.getID()))
			cadena=cadena+rep.listarClientes();
		}
		return cadena;
	}
	
	//Metodo que devuelve una lista de parejas
	public String listarParejas(){
		String cadena= "";
		for(MetaDatosRep rep: listaRep){
			String cadenaRep="Repositorio: "+rep.getNombre()+"\t Activo desde:"+rep.getFecha()+"\nClientes:\nNombre:\t Fecha de registro:\n"+rep.listarClientes()+"\n";
			cadena=cadena+cadenaRep;
		}
		return cadena;
	}
	//Metodo que devuelve por pantalla una lista con los ficheros de un cliente
	public String listarMisFicheros(String clN){
		String misFich="";
		for (MetaDatosRep rep: listaRep ){
				if(rep.buscaClienteN(clN)){
					misFich=rep.listarFicherosCl(clN);
				}
			}
		return misFich;
	}
	//Metodo que devuelve una lista de repositorios
	public String listarRepositorios(){
		String cadena= String.format("\n%-10s  "+"\t%-15s  ","Nombre","Fecha registro:");
		for(MetaDatosRep rep: listaRep){
			cadena=cadena+"\n"+rep.toString();
		}
		return cadena;
	}
	
	//Metodo que comprueba los datos de un cliente al autenticarse y devuelve su id
	@Override
	public String consultaCl(String nombre, String pass) throws RemoteException {
		String clID="";
		for(MetaDatosRep rep : listaRep){
			if(rep.buscaClienteN(nombre)){
				clID=rep.dameID(nombre, pass);				
			}
		}
		return clID;
		
	}
	//Metodo que comprueba si existe el nombre de un repositorio y devuelve su id
	@Override
	public String consultaRep(String nombre) throws RemoteException {
		String repID=null;
		for(MetaDatosRep rep : listaRep){
			if(rep.getNombre().equals(nombre)){
		        repID = rep.getID();									
			}
		}
		return repID;
		
	}
	//Metodo que registra un nuevo repositorio
	@Override
	public boolean nuevoRep(String nombre) throws RemoteException {
		String existe = consultaRep(nombre);
		if(existe==null){
			Date fechaActual=new Date();
	        @SuppressWarnings("deprecation")
			MetaDatosRep rep = new MetaDatosRep(nombre, nombre, fechaActual.toLocaleString());
	        listaRep.add(rep);
	        guardarBBDD();
	        return true;
		}
		return false;
	}
	
	//Metodo que registra un nuevo cliente
	@SuppressWarnings("deprecation")
	@Override
	public boolean nuevoCl(String clNombre,String pass) throws RemoteException {
		boolean existe=false;
		for(MetaDatosRep rep: listaRep){
			if(rep.buscaClienteN(clNombre)){existe=true;}
		}
		if(!existe){
			try{
				Random random = new Random();
				int size = listaRep.size();
				int aleatorio = random.nextInt(size);
				Date fechaActual=new Date(); 
				listaRep.get(aleatorio).nuevoCliente(clNombre,clNombre,fechaActual.toLocaleString(),pass);
				guardarBBDD();
			}
			catch (IllegalArgumentException ia){System.out.println("No existen repositorios en linea.");}
		}
		return !existe;
	}
	
	//Metodo que marca un fichero como compartido
	public boolean compartirFichero(String FN, String clID, String repID){
		for(MetaDatosRep rep: listaRep){
			if(rep.getID().equals(repID)){
				rep.compartirFichero(FN, clID);
				return true;
			}
		}
		return false;
	}
		
	//Metodo que deja de compartir un fichero
	public boolean noCompartir(String FN, String clID, String repID){
		for(MetaDatosRep rep: listaRep){
			if(rep.getID().equals(repID)){
				return rep.noCompartir(FN, clID);
			}
		}
		return false;
	}

	//Metodo que actualiza la base de datos al subir un fichero
	public void archivoSubido(String clID, String FN, String FP, long FPe, String FF){
		for(MetaDatosRep rep:listaRep){
			if(rep.buscaCliente(clID)){
				rep.subirFichero(clID, FN, FP, FPe, FF);
			}
		}
		guardarBBDD();
	}
	//Metodo que actualiza la base de datos al borrar un fichero
	public void archivoBorrado(String clID, String nombreFichero){
		for(MetaDatosRep rep:listaRep){
			if(rep.buscaCliente(clID)){
				rep.borrarFichero(clID, nombreFichero);;
			}
		}
		guardarBBDD();
	}
	
	//Metodo que guarda la base de datos en el pc
	private void guardarBBDD(){
		try{
	         FileOutputStream fos= new FileOutputStream("basedatos");
	         ObjectOutputStream oos= new ObjectOutputStream(fos);
	         oos.writeObject(listaRep);
	         oos.close();
	         fos.close();
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }
	}
	//Metodo que importa la base de datos
	@SuppressWarnings("unchecked")
	private void importarBBDD(){
		try
        {
            FileInputStream fis = new FileInputStream("basedatos");
            ObjectInputStream ois = new ObjectInputStream(fis);
            listaRep = (ArrayList<MetaDatosRep>) ois.readObject();
            ois.close();
            fis.close();
        }
		catch(FileNotFoundException fnfe){
            System.out.println("No se ha encontrado base de datos\nCreando nueva base de datos...\n");
            guardarBBDD();
            return;
		}
		catch(IOException ioe){
             ioe.printStackTrace();
             return;
        }
		catch(ClassNotFoundException c){
             System.out.println("Clase no encontrada\n");
             c.printStackTrace();
             return;
        }
		catch (Exception e){System.out.println("Ha ocurrido un error inesperado al cargar la base de datos\n");}
	}

}
