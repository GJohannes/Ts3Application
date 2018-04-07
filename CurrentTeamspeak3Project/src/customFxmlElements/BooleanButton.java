package customFxmlElements;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

public class BooleanButton extends Button{
	private boolean active;
	private String activeText = "Active text not yet set";
	private String deactiveText = "Deactive text not yet set";
	
	
	public BooleanButton () {
		EventHandler<MouseEvent> handle = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY) {
					toggle();
				}
			}
		};
		
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, handle);
		this.setDeActive();
	}
	
	public void toggle() {
		if(active) {
			this.setDeActive();
		} else {
			this.setActive();
		}
	}

	public boolean isNowActive() {
		return active;
	}

	public void setActive() {
		this.active = true;
		this.setText(activeText);
		this.setStyle("-fx-background-color: #7FE817; -fx-background-radius: 9,8,5,4,3;");
	}

	public void setDeActive(){
		this.active = false;
		this.setText(deactiveText);
		this.setStyle("-fx-background-color: #F75D59; -fx-background-radius: 9,8,5,4,3;");
	}
	
	public String getActiveText() {
		return activeText;
	}

	public void setActiveText(String activeText) {
		this.activeText = activeText;
		this.refreshState();
	}

	public String getDeActiveText() {
		return deactiveText;
	}

	public void setDeActiveText(String deactiveText) {
		this.deactiveText = deactiveText;
		this.refreshState();
	}
	
	// called after a text is set so that new text is appropriately shown 
	public void refreshState() {
		if(active) {
			this.setActive();
		} else {
			this.setDeActive();
		}
	}
	
}
