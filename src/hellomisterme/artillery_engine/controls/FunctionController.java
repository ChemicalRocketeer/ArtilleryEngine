package hellomisterme.artillery_engine.controls;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.controls.Keyboard.Control;

import java.awt.event.KeyEvent;

public class FunctionController implements Tick {

	protected Control control;
	protected ControlResponder responder;
	protected boolean alreadyPressed;

	public FunctionController(Control control, ControlResponder responder) {
		this.control = control;
		this.responder = responder;
		alreadyPressed = control.pressed();
	}

	@Override
	public void tick() {
		if (control.pressed()) {
			if (shouldRespond())
				respond();
			alreadyPressed = true;
		} else {
			alreadyPressed = false;
		}
	}

	protected boolean shouldRespond() {
		return !alreadyPressed;
	}

	protected void respond() {
		responder.respondToControl(this);
	}

	public static interface ControlResponder {
		public void respondToControl(FunctionController controller);
	}

	public static void main(String[] args) {
		Keyboard board = new Keyboard();
		System.out.println("initializing");
		FunctionController troll = new FunctionController(Control.UP, new ControlResponder() {
			public void respondToControl(FunctionController controller) {
				System.out.println("responded");
			}
		});

		System.out.println("pressing");
		board.keyPressed(KeyEvent.VK_W);
		troll.tick();
		System.out.println("pressing");
		board.keyPressed(KeyEvent.VK_W);
		troll.tick();
		System.out.println("pressing");
		board.keyPressed(KeyEvent.VK_W);
		troll.tick();
		System.out.println("releasing");
		board.keyReleased(KeyEvent.VK_W);
		troll.tick();
		System.out.println("pressing");
		board.keyPressed(KeyEvent.VK_W);
		troll.tick();
	}
}
