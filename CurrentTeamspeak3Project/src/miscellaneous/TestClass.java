package miscellaneous;

public class TestClass {

	public void test() {
		Number number = Math.random() * 1000;
		int i = number.intValue();
		while(true){
			System.out.println(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
