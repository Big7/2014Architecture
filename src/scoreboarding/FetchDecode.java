package scoreboarding;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import simulator.Instructions;
import simulator.MIPSsim;

public class FetchDecode {

	private static FetchDecode ifunit= null;
	boolean stall=false;
	boolean jump = false;
	Instructions waiting = null;
	Instructions executed = null;
	Queue<Instructions> pre_issue;
	Map<Integer,Instructions> instr_list;
	int new_issue =0;

	private FetchDecode(Map<Integer,Instructions> instr_list){
		this.instr_list = instr_list;
		pre_issue = new LinkedList<Instructions>();
	}

	public static FetchDecode getInstance(){
		return ifunit;
	}

	public static FetchDecode getInstance(Map<Integer,Instructions> instr_list){
		if(ifunit==null){
			ifunit = new FetchDecode(instr_list);
		}
		return ifunit;
	}

	public int IF(int pc) {
		int i=0;
		if(stall){
			if(executed!=null){
				stall = false;
				Instructions exe = executed;
				executed = null;
				switch(exe.getName()){
				case "J":
				case "JR":
					Instructions ins = instr_list.get(pc);
					switch(ins.getName()){
					case "J":
						executed = ins;
						pc = ins.op_reg[0];
						stall=true;
						return pc;
					case "JR":
						executed = ins;
						pc = MIPSsim.Register[ins.op_reg[0]];
						stall=true;
						return pc;
					case "BEQ":
					case "BLTZ":
					case "BGTZ":
						waiting = ins;
						stall = true;
						return pc;
					case "BREAK":
						executed = ins;
						stall = true;
						return 0;
					default:
						if(pre_issue.size()<4){
							pre_issue.add(ins);
							pc+=4;
						}
						break;
					}

				case "BREAK":
					return 0;

				}
			}else if(waiting!=null && !branchHazards(waiting)){
				stall = false;
				return pc;
			}

		}
		if(!stall&&waiting!=null){
			executed = waiting;
			waiting = null;
			stall = false;
			jump = true;
			switch(executed.getName()){
			case "BEQ":
				System.out.println(executed.op_reg[0]+"   "+executed.op_reg[1]);
				if(MIPSsim.Register[executed.op_reg[0]]==MIPSsim.Register[executed.op_reg[1]]){
					pc = pc+4+executed.offset;
					return pc;
				}else{
					return pc+4;
				}
			case "BLTZ":
				if(MIPSsim.Register[executed.op_reg[0]]<0){
					pc = pc+4+executed.offset;
					return pc;
				}else{
					return pc+4;
				}
			case "BGTZ":
				if(MIPSsim.Register[executed.op_reg[0]]>0){
					pc = pc+4+executed.offset;
					return pc;
				}else{
					return pc+4;
				}
			}
		}
		if(jump){
			executed = null;
			jump =false;
		}
		while(!stall&&i<2&&pre_issue.size()+new_issue<4){
			Instructions ins = instr_list.get(pc);
			System.out.println(pc+"**********"+ins.getPrint_disasb());
			switch(ins.getName()){
			case "J":
				executed = ins;
				pc = ins.op_reg[0];
				stall=true;
				return pc;
			case "JR":
				executed = ins;
				pc = MIPSsim.Register[ins.op_reg[0]];
				stall=true;
				return pc;
			case "BEQ":
			case "BLTZ":
			case "BGTZ":
				waiting = ins;
				stall = true;
				return pc;
			case "BREAK":
				executed = ins;
				stall = true;
				return 0;
			default:
				if(pre_issue.size()<4){
					pre_issue.add(ins);
					pc+=4;
				}
				break;
			}
			i++;

		}

		return pc;

	}

	private boolean branchHazards(Instructions ins) {
		boolean flag = false;
		List<Instructions> list = new ArrayList<Instructions>();

		//System.out.println(ins.s1+"#####"+ins.s2);
		//WAW
		if(Pipeline.result[ins.s1]!=null ){
			//System.out.println(ins.s1+"******"+Pipeline.result[ins.s1]);
			return true;
		}
		if( ins.s2>-1 && Pipeline.result[ins.s2]!=null){
			//			System.out.println(ins.s2+"********"+Pipeline.result[ins.s2]);
			return true;
		}
		//WAR
		for(Instructions in:pre_issue){
			if(ins.s1==in.dst||ins.s1==in.s1||ins.s1==in.s2){
				return true;
			}
			if(ins.s2>-1 &&(ins.s2==in.dst||ins.s2==in.s1||ins.s2==in.s2)){
				return true;
			}
		}
		for(Instructions in:Issue.getInstance().pre_ALU1){
			if(ins.s1==in.dst||ins.s1==in.s1||ins.s1==in.s2){
				return true;
			}
			if(ins.s2>-1 &&(ins.s2==in.dst||ins.s2==in.s1||ins.s2==in.s2)){
				return true;
			}
		}
		for(Instructions in:Issue.getInstance().pre_ALU2){
			if(ins.s1==in.dst||ins.s1==in.s1||ins.s1==in.s2){
				return true;
			}
			if(ins.s2>-1 &&(ins.s2==in.dst||ins.s2==in.s1||ins.s2==in.s2)){
				return true;
			}
		}
		if(Pipeline.alu1.pre_MEM!=null &&(ins.s1==Pipeline.alu1.pre_MEM.dst||ins.s1==Pipeline.alu1.pre_MEM.s1||ins.s1==Pipeline.alu1.pre_MEM.s2)){
			return true;
		}
		if(Pipeline.alu1.pre_MEM!=null &&ins.s2>-1 &&(ins.s2==Pipeline.alu1.pre_MEM.dst||ins.s2==Pipeline.alu1.pre_MEM.s1||ins.s2==Pipeline.alu1.pre_MEM.s2)){
			return true;
		}
		if(Pipeline.alu2.post_ALU2!=null &&(ins.s1==Pipeline.alu2.post_ALU2.dst||ins.s1==Pipeline.alu2.post_ALU2.s1||ins.s1==Pipeline.alu2.post_ALU2.s2)){
			return true;
		}
		if(Pipeline.alu2.post_ALU2!=null && ins.s2>-1 &&(ins.s2==Pipeline.alu2.post_ALU2.dst||ins.s2==Pipeline.alu2.post_ALU2.s1||ins.s2==Pipeline.alu2.post_ALU2.s2)){
			return true;
		}
		if(WriteBack.getInstance().post_MEM!=null && (ins.s1==WriteBack.getInstance().post_MEM.dst||ins.s1==WriteBack.getInstance().post_MEM.s1||ins.s1==WriteBack.getInstance().post_MEM.s2)){
			return true;
		}
		if(WriteBack.getInstance().post_MEM!=null && ins.s2>-1 &&(ins.s2==WriteBack.getInstance().post_MEM.dst||ins.s2==WriteBack.getInstance().post_MEM.s1||ins.s2==WriteBack.getInstance().post_MEM.s2)){
			return true;
		}
		System.out.println("false brachhazard");
		return flag;
	}

	public void print(PrintWriter pw){
		System.out.print("IF Unit:\n");
		pw.print("IF Unit:\n");
		System.out.print("\tWaiting Instruction: ");
		if(waiting!=null)System.out.print("["+waiting.getPrint_disasb()+"]");
		System.out.print("\n");
		pw.print("\tWaiting Instruction: ");
		if(waiting!=null)pw.print("["+waiting.getPrint_disasb()+"]");
		pw.print("\n");
		System.out.print("\tExecuted Instruction: ");
		if(executed!=null)System.out.print("["+executed.getPrint_disasb()+"]");
		System.out.print("\n");
		pw.print("\tExecuted Instruction: ");
		if(executed!=null)pw.print("["+executed.getPrint_disasb()+"]");
		pw.print("\n");
		System.out.print("Pre-Issue Queue:\n");
		pw.print("Pre-Issue Queue:\n");
		int i=0;
		for(Instructions is:pre_issue){
			System.out.print("\tEntry "+i+": ["+is.getPrint_disasb()+"]\n");
			pw.print("\tEntry "+i+": ["+is.getPrint_disasb()+"]\n");
			i++;
		}
		for(;i<4;i++){
			System.out.print("\tEntry "+i+": \n");
			pw.print("\tEntry "+i+": \n");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "cc";
		int i =0;
		switch(a){
		case "ss":
		case "aa":
			i+=1;
			break;
		case "bb":
			i+=10;
			break;
		default:
			break;
		}
		System.out.println(i);
	}


}
