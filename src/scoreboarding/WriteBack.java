package scoreboarding;

import java.io.PrintWriter;

import simulator.Instructions;
import simulator.MIPSsim;

public class WriteBack {
	private static WriteBack wb =null;
	Instructions post_MEM;
	Instructions old_post_MEM;//保存此轮会被删除的
	int dstRid0,result0;//pre_MEM
	int dstRid1,result1;//post_MEM
	int dstRid2,result2;//post_ALU2

	public static WriteBack getInstance(){
		if(wb==null){
			wb = new WriteBack();
		}
		return wb;
	}

	public void alu2_wb(FunctionalUnit alu2){
		if(alu2.post_ALU2!=null){
			alu2.old_post_ALU2 = alu2.post_ALU2;
			dstRid2 = alu2.dstRid;
			result2 = alu2.result;
			Pipeline.Register[dstRid2] = result2;
			System.out.println(dstRid2 +":"+result2);
			alu2.post_ALU2 = null;

			Pipeline.result[dstRid2]=null;
			System.out.println("result "+dstRid2+" null");
			alu2.Busy=false;
			if("alu2".equals(Pipeline.alu1.Qj))Pipeline.alu1.Rj=true;
			if("alu2".equals(Pipeline.alu1.Qk))Pipeline.alu1.Rk=true;

		}else{
			alu2.old_post_ALU2 = null;
		}


	}

	public void alu1_wb(FunctionalUnit alu1){
		if(post_MEM!=null){
			old_post_MEM = post_MEM;//保存此轮会被删除的
		}else{
			old_post_MEM = null;
		}

		if(post_MEM!=null){
			Pipeline.Register[dstRid1] = result1;
			Pipeline.result[dstRid1]=null;
			for(String f:Pipeline.result){
				System.out.print(f+",");
			}
			post_MEM=null;
			alu1.Busy = false;
			if("alu1".equals(Pipeline.alu2.Qj))Pipeline.alu2.Rj=true;
			if("alu1".equals(Pipeline.alu2.Qk))Pipeline.alu2.Rk=true;

		}
		dstRid0 = alu1.dstRid;
		result0 = alu1.result;
		if(alu1.pre_MEM!=null){
			switch(alu1.pre_MEM.getName()){
			case"LW":
				post_MEM = alu1.pre_MEM;
				dstRid1 = dstRid0;
				result1 = result0;
				alu1.pre_MEM = null;
				break;
			case"SW":
				MIPSsim.Data.put(dstRid0,result0);
				Pipeline.result[alu1.pre_MEM.dst]=null;
				alu1.pre_MEM = null;
				System.out.println("SWresult "+dstRid0+" null");
				break;

			}
		}
	}

	public void print(PrintWriter pw){
		System.out.print("Post-MEM Queue: ");
		pw.print("Post-MEM Queue: ");
		if(post_MEM!=null){
			System.out.print("["+post_MEM.getPrint_disasb()+"]\n");
			pw.print("["+post_MEM.getPrint_disasb()+"]\n");
		}else{
			System.out.print("\n");
			pw.print("\n");
		}
	}

}
