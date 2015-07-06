package newtest;

public class Controller {
	private Database db;
	private static Controller controller;
	
	private Controller() {
		db = new Database();
	}
	
	public static Controller getController() {
		if(controller == null) {
			controller = new Controller();
		}
		return controller;
	}
	
}