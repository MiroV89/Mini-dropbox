//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Cliente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Common.*;

public class ServicioDiscoClienteImpl implements ServicioDiscoClienteInterface{
	//Constructor vacio del servicio disco del cliente
	public ServicioDiscoClienteImpl(){}
	//Construcotr del servicio disco del cliente que exporta remotamente el servicio
	//Para generar la url utiliza un puerto proporcionado y el ID del cliente
	public ServicioDiscoClienteImpl(String ID, int puerto) throws IOException, NotBoundException {
		Utils.setCodeBase(ServicioDiscoClienteInterface.class);

		String URL_nombre=("rmi://localhost:"+puerto+"/"+ID+"/ServicioDC");
		ServicioDiscoClienteImpl ServicioDC = new ServicioDiscoClienteImpl();
		ServicioDiscoClienteInterface remote = (ServicioDiscoClienteInterface)UnicastRemoteObject.exportObject(ServicioDC, puerto);
		Registry registry = LocateRegistry.createRegistry(puerto);
		registry.rebind(URL_nombre, remote);		

	}
	//Metodo descarga pide la ruta de guardado, el nombre del fichero y guarda en esa ruta el fichero 
	//que queremos descargar
	@Override
	public void descarga(String ruta, Fichero fich, String archivoNombre) throws IOException{
		 File directorio = new File(ruta);
		 directorio.mkdirs();
		 File archivo = new File(ruta);
		 archivo.createNewFile();
		 OutputStream out = new FileOutputStream(ruta+"/"+archivoNombre);
		 fich.escribirEn(out);
	}
}
