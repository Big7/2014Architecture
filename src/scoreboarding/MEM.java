package scoreboarding;

import simulator.Instructions;

public class MEM {
	private static MEM mem = null;

	Instructions post_MEM;

	public static MEM getInstance(){
		if(mem==null){
			mem = new MEM();
		}
		return mem;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
