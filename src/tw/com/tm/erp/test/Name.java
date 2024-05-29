package tw.com.tm.erp.test;

public class Name {

	private String path;//宣告
	
	public Name(String s) {
		System.out.println(path);
		System.out.println("call string constructor");
		path = s;
		System.out.println(path);
	}
	
	public Name() {
		
	}
	
	public void setPath(String s) {
		path = s;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Name n = new Name("/ERP/abc.txt");
		System.out.println(n);
	}

}
