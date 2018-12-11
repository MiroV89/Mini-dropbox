//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Repositorio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Common.*;
import Common.Utils;

public class ServicioClOperadorImpl implements ServicioClOperadorInterface{
	//Constructor vacio
	public ServicioClOperadorImpl(){}
	//Constructor que exporta remotamente el servicio
	public ServicioClOperadorImpl(int puerto) throws IOException, NotBoundException{
		Utils.setCodeBase(ServicioClOperadorInterface.class);
		String URL_nombre=("rmi://localhost:"+puerto+"/ServicioClOp");
		ServicioClOperadorInterface servicioClOp = new ServicioClOperadorImpl();
		ServicioClOperadorInterface remote = (ServicioClOperadorInterface)UnicastRemoteObject.exportObject(servicioClOp, puerto);
		Registry registry = LocateRegistry.createRegistry(puerto);
		registry.rebind(URL_nombre, remote);
		//System.out.println("Servicio CL-OP arrancado. "+URL_nombre);
	}

	//Metodo que se encarga de la subida de archivos
	@Override
	public boolean subirArchivo(Fichero file, String repID, String clID) throws IOException {
		 String ruta = (getDirectorio()+"\\Repositorios\\"+repID+"\\"+clID);
		 File directorio = new File(ruta);
		 directorio.mkdirs();
		 File fich = new File(ruta+"\\"+file.obtenerNombre());
		 if(fich.exists()) return false;
		 OutputStream out = new FileOutputStream(ruta+"\\"+file.obtenerNombre());
		 file.escribirEn(out);
		return true;
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
	
    //Metodo que se encarga del borrado de archivos
	@Override
	public boolean borrarArchivo(String nombreF, String repID, String clID) throws RemoteException,IOException {
		 String ruta = (getDirectorio()+"\\Repositorios\\"+repID+"\\"+clID+"\\"+nombreF);
		 File fichero = new File(ruta);
		 if(fichero.delete()) return true;
		return false;
	}

}
