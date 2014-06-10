package scoreboarding;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import simulator.Instructions;
import simulator.MIPSsim;

public class Pipeline extends MIPSsim{
	//indicate the register is ready
	static boolean[] flag = new boolean[32];
	//Indicates, for each register, which function unit will write results into it
	static String[] result = new String[32];
	FetchDecode ifunit;
	Issue issue;
	public static FunctionalUnit alu1;
	public static FunctionalUnit alu2;
	MEM mem;
	WriteBack wb;


	public Pipeline(){
		super();
		for(int i=0;i<32;i++){
			flag[i] = false;//all registers are empty
			result[i]=null;
		}
	}

	@Override
	public void simulation(int pc) throws FileNotFoundException{
		ifunit = FetchDecode.getInstance(instr_list);
		issue = Issue.getInstance();
		//alu = ALU.getInstance();
		alu1 = new FunctionalUnit("alu1");
		alu2 = new FunctionalUnit("alu2");
		mem = MEM.getInstance();
		wb = WriteBack.getInstance();
		END=false;
		PrintWriter pw=new PrintWriter("simulation.txt");
		Instructions ins ;

		while(pc!=0){
			cycle++;

			//write results
			wb.alu1_wb(alu1);
			wb.alu2_wb(alu2);
			//read operands & execution
			alu1.caculate();
			alu2.caculate();
			//issue
			issue.check(alu1,alu2);
			//fetch & decode
			pc = ifunit.IF(pc);
			System.out.println("pc: "+pc);
			print(pw);


		}
		pw.close();
	}



	private void print(PrintWriter pw) {
		pw.print("--------------------"+"\n");
		System.out.print("--------------------"+"\n");
		pw.print("Cycle:"+MIPSsim.cycle+"\n");
		System.out.print("Cycle:"+MIPSsim.cycle+"\n");
		pw.print("\n");
		System.out.print("\n");
		ifunit.print(pw);
		issue.print("alu1",pw);
		alu1.print(pw);
		wb.print(pw);
		issue.print("alu2",pw);
		alu2.print(pw);
		pw.print("\n");
		System.out.print("\n");
		pw.print("Registers");
		System.out.print("Registers");

		for(int i=0;i<32;i++)
		{
			if(i==0){
				pw.print("\n"+"R00:");
				System.out.print("\n"+"R00:");
			}
			if(i==8)
			{
				pw.print("\n"+"R08:");
				System.out.print("\n"+"R08:");
			}
			if(i==16)
			{
				pw.print("\n"+"R16:");
				System.out.print("\n"+"R16:");
			}
			if(i==24)
			{
				pw.print("\n"+"R24:");
				System.out.print("\n"+"R24:");
			}
			pw.print("\t"+MIPSsim.Register[i]);
			System.out.print("\t"+MIPSsim.Register[i]);
		}


		pw.print("\n");
		System.out.print("\n");
		pw.print("\n");
		System.out.print("\n");
		pw.print("Data");
		System.out.print("Data");

		int i=0;

		for(Entry<Integer, Integer> entry:MIPSsim.Data.entrySet()){
			if((i%8)==0)
			{
				pw.print("\n"+entry.getKey()+":");
				System.out.print("\n"+entry.getKey()+":");
			}
			pw.print("\t"+entry.getValue());
			System.out.print("\t"+entry.getValue());

			i++;
		}
		pw.print("\n");
		pw.print("\n");
		System.out.print("\n");
		System.out.print("\n");

	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Pipeline pipeline = new Pipeline();
		pipeline.disassembly("sample.txt");
		pipeline.simulation(256);
	}

}
