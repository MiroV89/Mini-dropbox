//////////////////////////////////////////
//Nombre: Miroslav Krasimirov Vladimirov//
//Centro Asociado: Cantabria			//
//NIE: X4780953N						//
//Email: mkrasimir4@alumno.uned.es 		//
//		 miro.kv89@gmail.com		   	//
//////////////////////////////////////////
package Common;

public class Utils {
	
	public static final String CODEBASE = "java.rmi.server.codebase";
	
	
	public static void setCodeBase(Class<?> c) {
		String ruta = c.getProtectionDomain().getCodeSource()
					   .getLocation().toString();
		
		String path = System.getProperty(CODEBASE);
		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;  
		}
		
		System.setProperty(CODEBASE, ruta);
	}
}
