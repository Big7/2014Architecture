import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*On my honor, I have neither given nor received unauthorized aid on this assignment*/
public class MIPSsim {
	static int address=256;
	int[] Register = new int[32];
	HashMap<Integer,Integer> Data = new HashMap<Integer,Integer>();
	HashMap<String,String> Instruction1 = new HashMap<String,String>();
	HashMap<String,String> Instruction2 = new HashMap<String,String>();
	List<Instructions> instr = new ArrayList<Instructions>();
	
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
		boolean end = false;
		String identification="";
		while((line = br.readLine()) != null){
			//System.out.println(line);
			if(!end){
				if(line.substring(0,2).equals("01")){
					identification = Instruction1.get(line.substring(2, 6));
					Instructions ins = new Instructions(line,identification,address);
					instr.add(ins);
					System.out.println(identification);
					if(identification.equals("BREAK")){
						end = true;
					}
				}else{
					identification = Instruction2.get(line.substring(2, 6));
					Instructions ins = new Instructions(line,identification,address);
					instr.add(ins);
				}
			}else{
				Data.put(address,parseData(line.toCharArray()));
			}
			
			address+=4;
			
			
		}
		br.close();
		fr.close();
		
		FileWriter fw = new FileWriter("disassembly.txt");
		Instructions ins ;
		for(int i=0;i<instr.size();i++){
			ins = instr.get(i);
			fw.write(ins.getLine()+"\t"+ins.getAddress()+"\t"+ins.getName()+"\n");
		}
		
		fw.flush();
		fw.close();
	}
	
	public int parseData(char[] line) {
		// TODO Auto-generated method stub
		int res=0;
		int[] word=new int[32];
		
		int j=0;
		for(int i=31;i>-1;i--)
		{
			if(line[i]=='1')
			{
				word[j]=1;
			} else {
				word[j]=0;
			}
			j++;
		}
		
		if(line[0]=='1')
		{
			for(j=0;j<31;j++)
			{
				if(word[j]==0)
				{
					res=res+(int)Math.pow(2, j);
				}
				else {
					
				}
			}
			res=res+1;
		}
		else {
			for(j=0;j<31;j++)
			{
				if(word[j]==1)
				{
					res=res+(int)Math.pow(2, j);
				}
				else {
					
				}
			}
		}
		if(line[0]=='1')
		{
			res=-res;
		}
		return res;
	}

	public void simulation(){
		
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MIPSsim a = new MIPSsim();
		a.disassembly("sample.txt");
//		int num = a.parseData("11111111111111111111111111111100".toCharArray());
//		System.out.println(num);
	}

}
