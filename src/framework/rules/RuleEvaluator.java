package framework.rules;

import framework.interfaces.IAccount;
import framework.interfaces.IRule;

import java.util.ArrayList;
import java.util.List;

public class RuleEvaluator {
	private List<IRule> rules;
	
	public RuleEvaluator() {
		this.rules = new ArrayList<>();
	}
	
	public void addRule(IRule rule) {
		this.rules.add(rule);
	}
	
	public void evaluate(IAccount account, double amount) {
		for(IRule rule: this.rules) {
			if(rule.isApplicable(account, amount)) {
				rule.apply(account, amount);
			}
		}
	}
}
