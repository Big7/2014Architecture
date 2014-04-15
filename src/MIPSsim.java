import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*On my honor, I have neither given nor received unauthorized aid on this assignment*/
public class MIPSsim {
	static boolean END = false;
	static int address=256;
	static int[] Register = new int[32];
	static Map<Integer,Integer> Data = new LinkedHashMap<Integer,Integer>();
	//for disassemble
	static Map<Integer,Instructions> instr_list = new LinkedHashMap<Integer, Instructions>();
	//for simulation
	static int cycle = 0;
	static List<Instructions> instr_cycle = new ArrayList<Instructions>();
	static String Info;
	HashMap<String,String> Instruction1 = new HashMap<String,String>();
	HashMap<String,String> Instruction2 = new HashMap<String,String>();


	public MIPSsim(){
		for(int i=0;i<32;i++){
			Register[i]= 0;
		}

		Instruction1.put("0000","J");
		Instruction1.put("0001","JR");
		Instruction1.put("0010","BEQ");
		Instruction1.put("0011","BLTZ");
		Instruction1.put("0100","BGTZ");
		Instruction1.put("0101","BREAK");
		Instruction1.put("0110","SW");
		Instruction1.put("0111","LW");
		Instruction1.put("1000","SLL");
		Instruction1.put("1001","SRL");
		Instruction1.put("1010","SRA");
		Instruction1.put("1011","NOP");

		Instruction2.put("0000","ADD");
		Instruction2.put("0001", "SUB");
		Instruction2.put("0010", "MUL");
		Instruction2.put("0011", "AND");
		Instruction2.put("0100", "OR");
		Instruction2.put("0101", "XOR");
		Instruction2.put("0110", "NOR");
		Instruction2.put("0111", "SLT");
		Instruction2.put("1000", "ADDI");
		Instruction2.put("1001", "ANDI");
		Instruction2.put("1010", "ORI");
		Instruction2.put("1011", "XORI");

	}

	public void disassembly(String inputFile) throws Exception{
		FileReader fr = new FileReader(new File(inputFile));
		BufferedReader br = new BufferedReader(fr);
		String line;

		String identification="";
		while((line = br.readLine()) != null){
			//System.out.println(line);
			if(!END){
				if(line.substring(0,2).equals("01")){
					identification = Instruction1.get(line.substring(2, 6));
					Instructions ins = new Instructions(line,1,identification,address);
					//instr.add(ins);
					instr_list.put(address, ins);
					System.out.println(identification);
				}else{
					identification = Instruction2.get(line.substring(2, 6));
					Instructions ins = new Instructions(line,2,identification,address);
					//instr.add(ins);
					instr_list.put(address, ins);
					System.out.println(identification);
				}
			}else{
				Instructions ins = new Instructions(line,3,address);
				//instr.add(ins);
				instr_list.put(address, ins);
			}

			address+=4;

		}
		br.close();
		fr.close();

		FileWriter fw = new FileWriter("disassembly.txt");

		for(int add: instr_list.keySet()){
			fw.write(instr_list.get(add).toString()+"\n");
			System.out.print(instr_list.get(add).toString()+"\n");
		}

		fw.flush();
		fw.close();
	}



	public void simulation(int add) throws Exception {
		END=false;
		PrintWriter pw=new PrintWriter("simulation.txt");
		Instructions ins ;
		while(!END||add!=0){
			ins = instr_list.get(add);
			add = ins.execute(add);
			print(pw,ins);
		}
		pw.close();

	}


	private void print(PrintWriter pw,Instructions ins) {
		// TODO Auto-generated method stub
		pw.print("--------------------"+"\n");
		System.out.print("--------------------"+"\n");
		pw.print("Cycle:"+MIPSsim.cycle+"\t"+ins.address+"\t"+ins.getPrint_disasb()+"\n");
		System.out.print("Cycle:"+MIPSsim.cycle+"\t"+ins.address+"\t"+ins.getPrint_disasb()+"\n");
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
		MIPSsim a = new MIPSsim();
		a.disassembly("sample.txt");
		a.simulation(256);

	}

}
