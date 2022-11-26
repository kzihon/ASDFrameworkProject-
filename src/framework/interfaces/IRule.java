package framework.interfaces;

public interface IRule {
	boolean isApplicable(IAccount account, double amount);
	void apply(IAccount account, double amount);
}
