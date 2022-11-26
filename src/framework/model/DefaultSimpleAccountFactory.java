package framework.model;

public class DefaultSimpleAccountFactory {
	static public AccountFactory getCheckingAccountFactory() {
		return new DefaultAccountFactory();
	}
}
