package framework.view;

import framework.interfaces.IColleague;
import framework.interfaces.IMediator;

import java.util.ArrayList;
import java.util.List;

final public class RadioButtonMediator implements IMediator {
	List<RadioButtonTypeAccount> colleagues;
	
	public RadioButtonMediator() {
		this.colleagues = new ArrayList<>();
	}
	
	final public String getSelectedValue() {
		for(RadioButtonTypeAccount item: this.colleagues) {
			if(item.isSelected()) return item.getText();
		}
		return null;
	}
	
	final public int getNumberOfColleagues() {
		return this.colleagues.size();
	}
	
	@Override public void addColleague(IColleague colleague) {
		if(!(colleague instanceof RadioButtonTypeAccount)) return;
		this.colleagues.add((RadioButtonTypeAccount)colleague);
		colleague.setMediator(this);
	}

	@Override public void informOtherColleagues(IColleague colleague) {
		if(!(colleague instanceof RadioButtonTypeAccount)) return;
		for(RadioButtonTypeAccount item: this.colleagues) {
			if(colleague != item) item.update();
		}
	}

}
