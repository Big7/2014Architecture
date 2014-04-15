


public class Instructions {
	String line;
	String name;
	int address;
	int category;

	String rest;//除了前6位剩下的26位
	int[] op_reg = new int[3];
	int immediate,offset;
	String print_disasb;
	//instructions, category 1 or 2
	public Instructions(String line, int category, String name,int address){
		this.line = line;
		this.category = category;
		this.name = name;
		this.address = address;
		this.rest = line.substring(6);
		init();
	}

	//data, category 3
	public Instructions(String line, int category,int address){
		this.line = line;
		this.category = category;
		this.address = address;
		int num = parseData(line.toCharArray());
		MIPSsim.Data.put(address,num);
		this.print_disasb = ""+num;
	}

	public void init(){
		switch(name){
		case "ADD":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);//rd
			setPrint_disasb ("ADD R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "SUB":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb ("SUB R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "MUL":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("MUL R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "AND":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("AND R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "OR":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("OR R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "XOR":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("XOR R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "NOR":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("NOR R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;

		case "SLT":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("SLT R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			break;
		case "ADDI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("ADDI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			break;

		case "ANDI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("ANDI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			break;

		case "ORI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("ORI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			break;

		case "XORI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("XORI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			break;

		case "J":
			op_reg[0]=4*Integer.parseInt(rest, 2);//左移两位，即乘4
			setPrint_disasb("J #"+op_reg[0]);
			break;

		case "JR":
			op_reg[0]=Integer.parseInt(rest.substring(0,5), 2);
			setPrint_disasb("JR R"+op_reg[0]);

			break;

		case "BEQ":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			offset=4*Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("BEQ R"+op_reg[0]+", R"+op_reg[1]+", #"+offset);

			break;

		case "BLTZ":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			offset=4*Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("BLTZ R"+op_reg[0]+", #"+offset);

			break;

		case "BGTZ":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			offset=4*Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("BGTZ R"+op_reg[0]+", #"+offset);

			break;

		case "BREAK":
			MIPSsim.END = true;
			setPrint_disasb("BREAK");
			break;

		case "SW":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rs
			offset=Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("SW R"+op_reg[1]+", "+offset+"(R"+op_reg[0]+")");

			break;

		case "LW":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//base
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			offset=Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("LW R"+op_reg[1]+", "+offset+"(R"+op_reg[0]+")");

			break;

		case "SLL":
			op_reg[0]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[1]=Integer.parseInt(rest.substring(10,15), 2);//rd
			op_reg[2]=Integer.parseInt(rest.substring(15,20), 2);//sa
			setPrint_disasb("SLL R"+op_reg[1]+", R"+op_reg[0]+", #"+op_reg[2]);

			break;

		case "SRL":
			op_reg[0]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[1]=Integer.parseInt(rest.substring(10,15), 2);//rd
			op_reg[2]=Integer.parseInt(rest.substring(15,20), 2);//sa
			setPrint_disasb("SRL R"+op_reg[1]+", R"+op_reg[0]+", R"+op_reg[2]);

			break;

		case "SRA":
			op_reg[0]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[1]=Integer.parseInt(rest.substring(10,15), 2);//rd
			op_reg[2]=Integer.parseInt(rest.substring(15,20), 2);//sa
			setPrint_disasb("SRA R"+op_reg[1]+", R"+op_reg[0]+", R"+op_reg[2]);

			break;

		case "NOP":
			setPrint_disasb("NOP");
			break;

		default:
			setPrint_disasb("UNAVAILABLE");
			break;
		}

	}

	public int execute(int add){
		MIPSsim.cycle++;
		switch(name){
		case "ADD":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);//rd
			setPrint_disasb ("ADD R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]+MIPSsim.Register[op_reg[1]];
			return add+4;


		case "SUB":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb ("SUB R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]-MIPSsim.Register[op_reg[1]];
			return add+4;


		case "MUL":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("MUL R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]*MIPSsim.Register[op_reg[1]];
			return add+4;


		case "AND":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("AND R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]&MIPSsim.Register[op_reg[1]];
			return add+4;


		case "OR":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("OR R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]|MIPSsim.Register[op_reg[1]];
			return add+4;


		case "XOR":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("XOR R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]^MIPSsim.Register[op_reg[1]];
			return add+4;


		case "NOR":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("NOR R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=~(MIPSsim.Register[op_reg[0]]^MIPSsim.Register[op_reg[1]]);
			return add+4;


		case "SLT":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			op_reg[2]=Integer.parseInt(rest.substring(10,15), 2);
			setPrint_disasb("SLT R"+op_reg[2]+", R"+op_reg[0]+", R"+op_reg[1]);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]<MIPSsim.Register[op_reg[1]]?1:0;
			return add+4;
		case "ADDI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("ADDI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			MIPSsim.Register[op_reg[1]]=MIPSsim.Register[op_reg[0]]+immediate;
			return add+4;

		case "ANDI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("ANDI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]&immediate;
			return add+4;

		case "ORI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("ORI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]|immediate;
			return add+4;

		case "XORI":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);
			immediate=Integer.parseInt(rest.substring(10,26), 2);//iv
			setPrint_disasb("XORI R"+op_reg[1]+", R"+op_reg[0]+", #"+immediate);
			MIPSsim.Register[op_reg[2]]=MIPSsim.Register[op_reg[0]]^immediate;
			return add+4;

		case "J":
			op_reg[0]=4*Integer.parseInt(rest, 2);//左移两位，即乘4
			setPrint_disasb("J #"+op_reg[0]);
			//MIPSsim.instr_list.get(op_reg[0]).execute(pw);
			//			for(int i=0;i<MIPSsim.instr.size()-MIPSsim.Data.size();i++){
			//				Instructions ins = MIPSsim.instr.get(i);
			//				if(ins.getAddress()==op_reg[0]){
			//					ins.execute();
			//					//MIPSsim.cycle.put(ins.address, ins);
			//					break;
			//				}
			//			}
			return op_reg[0];

		case "JR":
			op_reg[0]=Integer.parseInt(rest.substring(0,5), 2);
			setPrint_disasb("JR R"+op_reg[0]);
			//MIPSsim.instr_list.get(MIPSsim.Register[op_reg[0]]).execute(pw);
			return MIPSsim.Register[op_reg[0]];

		case "BEQ":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			offset=4*Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("BEQ R"+op_reg[0]+", R"+op_reg[1]+", #"+offset);
			if(MIPSsim.Register[op_reg[0]]==MIPSsim.Register[op_reg[1]])
				return this.address+4+offset;
			else return add+4;


		case "BLTZ":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			offset=4*Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("BLTZ R"+op_reg[0]+", #"+offset);
			if(MIPSsim.Register[op_reg[0]]<0)
				return this.address+4+offset;
			else return add+4;

		case "BGTZ":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			offset=4*Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("BGTZ R"+op_reg[0]+", #"+offset);
			if(MIPSsim.Register[op_reg[0]]>0)
				return this.address+4+offset;
			else return add+4;

		case "BREAK":
			MIPSsim.END = true;
			setPrint_disasb("BREAK");
			return 0;

		case "SW":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//rs
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rs
			offset=Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("SW R"+op_reg[1]+", "+offset+"(R"+op_reg[0]+")");
			MIPSsim.Data.put(offset+MIPSsim.Register[op_reg[0]],MIPSsim.Register[op_reg[1]]);
			return add+4;

		case "LW":
			op_reg[0]=Integer.parseInt(rest.substring(0, 5), 2);//base
			op_reg[1]=Integer.parseInt(rest.substring(5,10), 2);//rt
			offset=Integer.parseInt(rest.substring(10,26), 2);//offset
			setPrint_disasb("LW R"+op_reg[1]+", "+offset+"(R"+op_reg[0]+")");
			MIPSsim.Register[op_reg[1]] = MIPSsim.Data.get(offset+MIPSsim.Register[op_reg[0]]);
			return add+4;

		case "SLL":
			op_reg[0]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[1]=Integer.parseInt(rest.substring(10,15), 2);//rd
			op_reg[2]=Integer.parseInt(rest.substring(15,20), 2);//sa
			setPrint_disasb("SLL R"+op_reg[1]+", R"+op_reg[0]+", #"+op_reg[2]);
			MIPSsim.Register[op_reg[1]] = MIPSsim.Register[op_reg[0]]<<op_reg[2];
			return add+4;

		case "SRL":
			op_reg[0]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[1]=Integer.parseInt(rest.substring(10,15), 2);//rd
			op_reg[2]=Integer.parseInt(rest.substring(15,20), 2);//sa
			setPrint_disasb("SRL R"+op_reg[1]+", R"+op_reg[0]+", R"+op_reg[2]);
			MIPSsim.Register[op_reg[1]] = MIPSsim.Register[op_reg[0]]>>>op_reg[2];
			return add+4;

		case "SRA":
			op_reg[0]=Integer.parseInt(rest.substring(5,10), 2);//rt
			op_reg[1]=Integer.parseInt(rest.substring(10,15), 2);//rd
			op_reg[2]=Integer.parseInt(rest.substring(15,20), 2);//sa
			setPrint_disasb("SRA R"+op_reg[1]+", R"+op_reg[0]+", R"+op_reg[2]);
			MIPSsim.Register[op_reg[1]] = MIPSsim.Register[op_reg[0]]>>op_reg[2];
			return add+4;

		case "NOP":
			setPrint_disasb("NOP");
			return add+4;


		default:
			MIPSsim.END = true;
			return 0;

		}
	}

	//有符号整数二进制转换十进制
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

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public String getPrint_disasb() {
		return print_disasb;
	}

	public void setPrint_disasb(String print_disasb) {
		this.print_disasb = print_disasb;
	}

	@Override
	public String toString(){
		return getLine()+"\t"+getAddress()+"\t"+getPrint_disasb();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print(1<<2);
	}

}
