package scoreboarding;

import java.io.PrintWriter;

import simulator.Instructions;
import simulator.MIPSsim;

public class FunctionalUnit {
	public String name;
	public boolean Busy;
	public String Op;
	public int Fi,Fj,Fk;
	public String Qj, Qk;//FU for registers Fj, Fk
	public boolean Rj, Rk;//indicates when Fj, Fk are ready

	Instructions pre_MEM ;//alu1
	Instructions post_ALU2;//alu2
	Instructions old_post_ALU2;//保存此轮会被删除的
	int dstRid,result;
	public FunctionalUnit(String name) {
		this.name = name;
		Busy = false;
	}


	public void update(Instructions ins) {
		Busy=true;
		Op=ins.getName();//SW or LW
		Fi=ins.dst;//dst
		Fj=ins.s1;//src
		Fk=ins.s2;//src
		if(Fj>-1){
			Qj=Pipeline.result[Fj];
			Rj=(Qj==null);
		}else{
			Rj=true;
		}
		if(Fk>-1){
			Qk=Pipeline.result[Fk];
			Rk=(Qk==null);
		}else{
			Rk=true;
		}
		Pipeline.result[Fi]=name;
		System.out.println("debug:result"+Fi+"  "+name);
	}

	public void caculate() {
		//Read operands:the instruction waits until all operands become available
		//RAW
		switch(this.name){
		case "alu1":
			if(Rj && Rk){
				pre_MEM= Issue.getInstance().pre_ALU1.poll();
				if(pre_MEM!=null){
					System.out.println(pre_MEM.getPrint_disasb());
					execute(pre_MEM);
				}
				if(Fj>-1)Rj=false;
				if(Fk>-1)Rk=false;
				this.Busy= false;
			}
			break;
		case "alu2":
			if(Rj && Rk){
				post_ALU2= Issue.getInstance().pre_ALU2.poll();
				if(post_ALU2!=null){
					System.out.println("***********"+post_ALU2.getPrint_disasb());
					execute(post_ALU2);
				}
				if(Fj>-1)Rj=false;
				if(Fk>-1)Rk=false;
				this.Busy= false;
			}
			break;
		}

	}
	public void execute(Instructions ins){
		switch(ins.getName()){
		case "ADD":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]+MIPSsim.Register[ins.s2];
			return;


		case "SUB":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]-MIPSsim.Register[ins.s2];
			return;


		case "MUL":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]*MIPSsim.Register[ins.s2];
			return;



		case "AND":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]&MIPSsim.Register[ins.s2];
			return;

		case "OR":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]|MIPSsim.Register[ins.s2];
			return;


		case "XOR":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]^MIPSsim.Register[ins.s2];
			return;

		case "NOR":
			dstRid = ins.dst;
			result=~(MIPSsim.Register[ins.s1]^MIPSsim.Register[ins.s2]);
			return;

		case "SLT":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]<MIPSsim.Register[ins.s2]?1:0;
			return;

		case "ADDI":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]+ins.immediate;
			return;

		case "ANDI":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]&ins.immediate;
			return;

		case "ORI":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]|ins.immediate;
			return;

		case "XORI":
			dstRid = ins.dst;
			result=MIPSsim.Register[ins.s1]^ins.immediate;
			return;

		case "SW":
			dstRid = ins.offset+MIPSsim.Register[ins.s2];
			result=MIPSsim.Register[ins.dst];//store
			return;

		case "LW":
			dstRid = ins.dst;
			result= MIPSsim.Data.get(ins.offset+MIPSsim.Register[ins.s2]);//load
			return;

		case "SLL":
			dstRid = ins.dst;
			result = MIPSsim.Register[ins.s1]<<MIPSsim.Register[ins.s2];
			return;

		default:
			MIPSsim.END = true;
			return;

		}
	}
	public void print(PrintWriter pw){
		switch(this.name){
		case "alu1":
			System.out.print("Pre-MEM Queue: ");
			pw.print("Pre-MEM Queue: ");
			if(pre_MEM!=null){
				System.out.print("["+pre_MEM.getPrint_disasb()+"]\n");
				pw.print("["+pre_MEM.getPrint_disasb()+"]\n");
			}else{
				System.out.print("\n");
				pw.print("\n");
			}
			break;
		case "alu2":
			System.out.print("Post-ALU2 Queue: ");
			pw.print("Post-ALU2 Queue: ");
			if(post_ALU2!=null){
				System.out.print("["+post_ALU2.getPrint_disasb()+"]\n");
				pw.print("["+post_ALU2.getPrint_disasb()+"]\n");
			}else{
				System.out.print("\n");
				pw.print("\n");
			}
			break;
		}


	}
}
