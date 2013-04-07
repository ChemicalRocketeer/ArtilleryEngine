package hellomisterme.artillery_engine;

public class GameLog {

	private String log = "Artillery Engine Game Log\n\n";

	public void log(String message) {
		log += message + "\n";
	}

	public String getLog() {
		return log;
	}
}
