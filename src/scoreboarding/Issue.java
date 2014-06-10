package scoreboarding;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import simulator.Instructions;

public class Issue {
	private static Issue issue = null;

	Queue<Instructions> pre_ALU1;
	Queue<Instructions> pre_ALU2;

	private Issue(){
		pre_ALU1 = new LinkedList<Instructions>();
		pre_ALU2 = new LinkedList<Instructions>();
	}

	public static Issue getInstance(){
		if(issue==null){
			issue = new Issue();
		}
		return issue;
	}

	public void check(FunctionalUnit alu1,FunctionalUnit alu2) {

		int a=0,b = 0;
		List<Instructions> list = new ArrayList<Instructions>();
		List<Instructions> del = new ArrayList<Instructions>();

		Iterator<Instructions> ltIns = FetchDecode.getInstance().pre_issue.iterator();

		while(ltIns.hasNext() && a+b<2){
			Instructions ins = ltIns.next();
			if(ins.getName().equals("SW")||ins.getName().equals("LW")){
				if(!alu1.Busy && !hazards(list,ins) && pre_ALU1.size()<2 && a<2){
					alu1.update(ins);
					pre_ALU1.add(ins);
					a++;
					del.add(ins);
					//System.out.println("pre_issue:"+FetchDecode.getInstance().pre_issue.size());
					//FetchDecode.getInstance().pre_issue.remove(ins);

					//System.out.println("remove:"+FetchDecode.getInstance().pre_issue.size());
				}
			}else{
				if(!alu2.Busy && !hazards(list,ins)  && pre_ALU2.size()<2 && b<2){
					alu2.update(ins);
					pre_ALU2.add(ins);
					b++;
					del.add(ins);
					//					System.out.println("pre_issue:"+FetchDecode.getInstance().pre_issue.size());
					//					FetchDecode.getInstance().pre_issue.remove(ins);
					//					System.out.println("remove:"+FetchDecode.getInstance().pre_issue.size());
				}
			}
			list.add(ins);
		}
		FetchDecode.getInstance().new_issue = a+b;
		FetchDecode.getInstance().pre_issue.removeAll(del);

	}

	private boolean hazards(List<Instructions> list, Instructions ins) {
		boolean flag = false;
		for(String f:Pipeline.result){
			System.out.print(f+",");
		}
		//WAW,RAW
		if(Pipeline.result[ins.dst]!=null||(ins.s1>-1&& Pipeline.result[ins.s1]!=null)||(ins.s2>-1&& Pipeline.result[ins.s2]!=null)){
			System.out.println(ins.getPrint_disasb()+"hazards1"+Pipeline.result[ins.dst]);

			return true;
		}

		//WAR
		for(Instructions in:list){
			if(ins.dst==in.dst||ins.dst==in.s1||ins.dst==in.s2||(ins.s1>-1&&ins.s1==in.dst)||(ins.s2>-1&&ins.s2==in.dst)){
				System.out.println(ins.getPrint_disasb()+"hazards2"+in.getPrint_disasb());
				return true;
			}
		}

		for(Instructions in:Issue.getInstance().pre_ALU1){
			if((ins.dst==in.dst||ins.dst==in.s1||ins.dst==in.s2)||(ins.s1>-1&&ins.s1==in.dst)||(ins.s2>-1 &&ins.s2==in.dst)){
				System.out.println(ins.getPrint_disasb()+"hazards3"+in.getPrint_disasb());
				return true;
			}
		}
		for(Instructions in:Issue.getInstance().pre_ALU2){
			if((ins.dst==in.dst||ins.dst==in.s1||ins.dst==in.s2)||(ins.s1>-1&&ins.s1==in.dst)||(ins.s2>-1 &&ins.s2==in.dst)){
				System.out.println(ins.getPrint_disasb()+"hazards4"+in.getPrint_disasb());
				return true;
			}
		}
		List<Instructions> preIns = new ArrayList<Instructions>();
		if(Pipeline.alu1.pre_MEM!=null)preIns.add(Pipeline.alu1.pre_MEM);
		if(Pipeline.alu2.post_ALU2!=null)preIns.add(Pipeline.alu2.post_ALU2);
		if(WriteBack.getInstance().post_MEM!=null)preIns.add(WriteBack.getInstance().post_MEM);
		if(WriteBack.getInstance().old_post_MEM!=null)preIns.add(WriteBack.getInstance().old_post_MEM);
		if(Pipeline.alu2.old_post_ALU2!=null)preIns.add(Pipeline.alu2.old_post_ALU2);

		for(Instructions in:preIns){
			if((ins.dst==in.dst)||(ins.s1>-1&&ins.s1==in.dst)||(ins.s2>-1 &&ins.s2==in.dst)){
				System.out.println(ins.getPrint_disasb()+"hazards5"+in.getPrint_disasb());
				return true;
			}
		}
		return flag;
	}

	public void print(String alu,PrintWriter pw){
		int i=0;
		if("alu1".compareTo(alu)==0){
			System.out.print("Pre-ALU1 Queue:\n");
			pw.print("Pre-ALU1 Queue:\n");
			for(Instructions is:pre_ALU1){
				System.out.print("\tEntry "+i+": ["+is.getPrint_disasb()+"]\n");
				pw.print("\tEntry "+i+": ["+is.getPrint_disasb()+"]\n");
				i++;
			}
			for(;i<2;i++){
				System.out.print("\tEntry "+i+": \n");
				pw.print("\tEntry "+i+": \n");
			}
		}
		i=0;
		if("alu2".compareTo(alu)==0){
			System.out.print("Pre-ALU2 Queue:\n");
			pw.print("Pre-ALU2 Queue:\n");
			for(Instructions is:pre_ALU2){
				System.out.print("\tEntry "+i+": ["+is.getPrint_disasb()+"]\n");
				pw.print("\tEntry "+i+": ["+is.getPrint_disasb()+"]\n");
				i++;
			}
			for(;i<2;i++){
				System.out.print("\tEntry "+i+": \n");
				pw.print("\tEntry "+i+": \n");
			}
		}
	}
}
