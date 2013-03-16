package hellomisterme.artillery_engine;

public class GameLog {

	private String log = "Artillery Engine Game\n";

	public void log(String message) {
		log += '\n' + message + '\n';
	}

	public String getLog() {
		return log;
	}
}
