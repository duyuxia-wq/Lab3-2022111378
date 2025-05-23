import java.io.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/** 读取原始文件，并将其转化为标准文件data.txt */
class ReadFile {

	// 将传入路径的文件内容转换格式后输出到 data.txt 文件中
	public void transformFile(String string) {
		InputStream is = null;
		OutputStream os = null;

		try {
			// 打开输入流，从指定路径读取文件
			is = new FileInputStream(string);
			// 打开输出流，写入到 data.txt 文件中
			os = new FileOutputStream("result/data.txt");

			// 获取输入流中可读取的字节数（即整个文件的字节长度）
			int size = is.available();

			char str;          // 用于存储读取的字符
			boolean flag = true; // 用于控制连续空格的写入（防止多空格）

			// 遍历每一个字符
			for (int i = 0; i < size; i++) {
				str = (char) is.read();  // 读取一个字节并转换成字符

				// 如果是小写字母，直接写入
				if (str >= 'a' && str <= 'z') {
					os.write(str);
					flag = true;  // 恢复写空格权限
				}
				// 如果是大写字母，转为小写后写入
				else if (str >= 'A' && str <= 'Z') {
					os.write((char) (str + 32));  // 大写转小写
					flag = true;  // 恢复写空格权限
				}
				// 其他非字母字符处理（如空格、标点等）
				else {
					if (flag && str == ' ') {
						os.write(str);   // 只允许写入第一个空格
						flag = false;    // 之后遇到空格将跳过
					} else {
						if (flag) {
							os.write(' '); // 遇到其他符号时插入一个空格
							flag = false;  // 防止连续空格写入
						}
						// 若 flag 为 false，即刚写入过空格，当前字符也非字母，则不写入
					}
				}
			}
		} catch (IOException e) {
			// 处理读写过程中的异常
			e.printStackTrace();
		} finally {
			// 关闭输入流
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 关闭输出流
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

// 图的边结点类
class Edge {
	/** 邻接节点序号 */
	protected int verAdj;
	/** 邻接节点名称 */
	protected String Name;
	/** 边的权值 */
	protected int cost;
	/** 下一个边节点（邻接节点） */
	protected Edge link;
}

// 顶点表中的结点类
class Vertex {
	/** 节点序号 */
	protected int verName;
	/** 节点名称 **/
	protected String Name;
	/** 边链表的头指针 */
	protected Edge adjacent;
	/** 节点PR值 */
	protected double pageRank;
}

/** 以邻接表的形式存储图结构 */
class Graph {
	/** 顶点表，存储所有顶点的引用 */
	public Vertex[] head;

	/** 实际有效的顶点数量 */
	private int vertexNum;

	/** 输入文件路径 */
	private String str;

	/** 构造函数，保存文件路径 */
	public Graph(String string) {
		this.str = string;
	}

	/** 构建图结构：从文件中读取文本数据构建有向图 */
	public int buildGraph() {
		InputStream is = null;
		try {
			is = new FileInputStream(this.str);  // 打开输入流读取文件
			int size = is.available();           // 获取文件大小（字节数）
			this.vertexNum = size;               // 初始假设最大顶点数等于字节数
			this.head = new Vertex[this.vertexNum];  // 初始化顶点表数组

			// 初始化所有顶点对象，并设置默认值
			for (int i = 0; i < this.vertexNum; i++) {
				head[i] = new Vertex();        // 分配一个顶点对象
				head[i].verName = i;           // 给顶点编号
				head[i].adjacent = null;       // 邻接链表初始化为空
				head[i].Name = "!";            // 标记为空的顶点（未赋值）
				head[i].pageRank = 1.0 / this.vertexNum;
			}

			int charnum = this.vertexNum;      // 记录剩余未读取字符数量
			StringBuffer string1 = new StringBuffer();  // 用于暂存读取到的单词
			char str1 = (char) is.read();      // 读取首字符

			// 跳过文件开头的空格
			while (str1 == ' ') {
				str1 = (char) is.read();
				charnum--;
			}

			charnum--;  // 当前字符已读取

			// 读取第一个单词
			while (str1 != ' ') {
				string1.append(str1);
				str1 = (char) is.read();
				charnum--;
			}

			int num = 0;  // 当前有效顶点编号
			head[num++].Name = string1.toString();  // 第一个单词作为顶点加入图中

			// 读取后续单词对（string1 -> string2）
			while (charnum > 0) {
				StringBuffer string2 = new StringBuffer();
				str1 = (char) is.read();
				charnum--;

				// 读取一个单词，直到遇到空格或文件末尾
				while (str1 != ' ' && charnum >= 0) {
					string2.append(str1);
					str1 = (char) is.read();
					charnum--;
				}

				// 在顶点表中查找 string1 所在顶点
				for (int j = 0; j < this.vertexNum; j++) {
					if (head[j].Name.equals(string1.toString())) {
						Edge q = head[j].adjacent;  // 当前顶点的邻接表头
						Edge qr = null;

						// 查找是否已经存在 string1 -> string2 的边
						while (q != null) {
							if (q.Name.equals(string2.toString())) {
								q.cost += 1;  // 已存在，权重+1
								break;
							}
							qr = q;
							q = q.link;
						}

						// 若不存在该边，创建新的边
						if (q == null) {
							Edge p = new Edge();
							p.Name = string2.toString();  // 设置目标顶点名称
							p.cost = 1;                   // 初始权重为 1
							p.link = null;

							// 将新边插入邻接表头
							if (qr == null) {
								head[j].adjacent = p;
							} else {
								p.link = head[j].adjacent;
								head[j].adjacent = p;
							}

							// 在顶点表中查找 string2 对应顶点编号
							int n = findVertex(string2.toString());
							if (n == -1) {
								p.verAdj = num;  // 未找到则分配新编号
								head[num++].Name = string2.toString();  // 添加新顶点
							} else {
								p.verAdj = n;  // 若已存在，直接链接
							}
						}
						break;
					}
				}

				// 准备下一轮：string2 成为下一个起始单词
				string1 = string2;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭文件流
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 统计实际使用的顶点个数
		int a = 0;
		for (int i = 0; i < vertexNum && !head[i].Name.equals("!"); i++) {
			a++;
		}
		this.vertexNum = a;  // 更新有效顶点数
		return this.vertexNum;
	}

	/** 使用 GraphViz 工具可视化有向图 */
	public void showDirectedGraph() {
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());  // 添加 DOT 起始

		for (int i = 0; i < this.vertexNum; i++) {
			Edge p = this.head[i].adjacent;
			while (p != null) {
				// 生成格式: A->B[label=权重];
				gv.addln("\"" + this.head[i].Name + "\" -> \"" + p.Name + "\" [label=" + p.cost + "];");
				// System.out.println(this.head[i].Name + "->" + p.Name + "[label=" + p.cost + "];");
				p = p.link;
			}
		}

		gv.addln(gv.end_graph());  // 添加 DOT 结束

		try {
			FileWriter fw = new FileWriter(Lab1_main.pathtext + "/result/out.dot");  // 输出路径可自定义
			fw.write(gv.getDotSource());  // 写入DOT源码
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 输出为图像文件（.gif 格式）
		String type = "png";
		File out = new File(Lab1_main.pathtext + "/result/out." + type);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	/** 查找顶点名为 str 的顶点编号，未找到返回 -1 */
	public int findVertex(String str) {
		for (int i = 0; i < this.vertexNum; i++) {
			if (head[i].Name.equals(str)) {
				return i;
			}
			if (head[i].Name.equals("!")) {
				break;
			}
		}
		return -1;
	}

	/** 打印图的邻接表结构 */
	public void printGraph() {
		for (int i = 0; i < this.vertexNum; i++) {
			if (!head[i].Name.equals("!")) {
				System.out.print(head[i].Name + ":");
				Edge p = head[i].adjacent;
				while (p != null) {
					System.out.print(p.Name + "(" + p.cost + ")" + " ");
					p = p.link;
				}
				System.out.print("\n");
			} else {
				break;
			}
		}
	}
}


class BFS {
	/** 指向顶点表的引用 */
	private Vertex[] head;
	/* 当前顶点的个数  */
	private int vertexNum;

	BFS(Vertex[] H, int num) {
		this.head = H;
		this.vertexNum = num;
	}


	// 私有函数，广度优先遍历寻找桥接词
	private String[] broadFirstSearch(boolean[] isVisited, int i, String destination) {
		LinkedList<Integer> queue = new LinkedList<>();
		ArrayList<String> bridgeWords = new ArrayList<>();

		// 首先找到word1的所有直接邻居
		Edge p = head[i].adjacent;
		while (p != null) {
			// 检查这个邻居是否有指向word2的边
			Edge q = head[p.verAdj].adjacent;
			while (q != null) {
				if (q.Name.equals(destination)) {
					bridgeWords.add(head[p.verAdj].Name);
					break;
				}
				q = q.link;
			}
			p = p.link;
		}

		if (bridgeWords.isEmpty()) {
			String[] result = new String[1];
			result[0] = "0";
			return result;
		} else {
			return bridgeWords.toArray(new String[0]);
		}
	}

	// 对外公开函数，广度优先遍历
	String[] broadFirstSearch(String word1, String word2) {
		// 检查word1和word2是否存在
		int word1Index = -1;
		for (int i = 0; i < this.vertexNum; i++) {
			if (head[i].Name.equals(word1)) {
				word1Index = i;
				break;
			}
			if (head[i].Name.equals("!")) {
				break;
			}
		}

		if (word1Index == -1) {
			String[] result = new String[1];
			result[0] = "-1";
			return result;
		}

		boolean word2Exists = false;
		for (int i = 0; i < this.vertexNum; i++) {
			if (head[i].Name.equals(word2)) {
				word2Exists = true;
				break;
			}
			if (head[i].Name.equals("!")) {
				break;
			}
		}

		if (!word2Exists) {
			String[] result = new String[1];
			result[0] = "-1";
			return result;
		}

		boolean[] isVisited = new boolean[this.vertexNum];
		return broadFirstSearch(isVisited, word1Index, word2);
	}

}

class Node {
	int id;
	int d;
}

class Dij {
	private static int INF = 9999;
	ArrayList<Integer>[] prev;
	int size;
	Node[] dist;
	PriorityQueue<Node> queue = new PriorityQueue<Node>();
	boolean[] flag;

	@SuppressWarnings("unchecked") // ***
	public Dij(int vertexNum) {
		size = vertexNum;
		prev = new ArrayList[size];
		flag = new boolean[size];
		dist = new Node[size];
		// System.out.println(dist.length);
		for (int i = 0; i < size; i++) {
			prev[i] = new ArrayList<Integer>();
			prev[i].add(-1);
			flag[i] = false;
			dist[i] = new Node();
			dist[i].id = i;
			dist[i].d = INF;
		}
		queue = new PriorityQueue<Node>(size, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return n1.d - n2.d;
			}
		});
	}

	public void dijkstra(int s, Vertex[] head) {
		// System.out.println(G.head.length);
		dist[s].d = 0;
		queue.add(dist[s]);
		while (queue.peek() != null) { // 队列不为空
			Node temp = queue.poll(); // 移除并返回优先级队列头部的元素
			Edge p = head[temp.id].adjacent;
			int u = temp.id;

			if (flag[u])
				continue;
			flag[u] = true;

			while (p != null) {
				int tempid = p.verAdj;
				int tempcost = p.cost;
				if (!flag[tempid]) {
					if (dist[tempid].d > dist[u].d + tempcost) {
						dist[tempid].d = dist[u].d + tempcost;
						prev[tempid].clear();
						prev[tempid].add(u);
						queue.add(dist[tempid]);
					} else if (dist[tempid].d == dist[u].d + tempcost) {
						prev[tempid].add(u);
					}
				}
				p = p.link;
			}
		}
	}

	public ArrayList<ArrayList<Integer>> findpath(int beg, int end) {
		ArrayList<ArrayList<Integer>> childPaths = null;
		ArrayList<ArrayList<Integer>> midPaths = new ArrayList<>();
		if (beg != end) {
			for (int i = 0; i < prev[end].size(); i++) {
				childPaths = findpath(beg, prev[end].get(i));
				for (int j = 0; j < childPaths.size(); j++) {
					childPaths.get(j).add(end);
				}
				if (midPaths.size() == 0) {
					midPaths = childPaths;
				} else {
					midPaths.addAll(childPaths);
				}
			}
		} else {
			ArrayList<Integer> temp = new ArrayList<>(1);
			temp.add(beg);
			midPaths.add(temp);
		}
		return midPaths;
	}
}



public class Lab1_main {
	public static final String pathtext = System.getProperty("user.dir");
	/** 指向顶点表的引用 */
	public static Vertex[] head;
	/* 当前顶点的个数 */
	public static int vertexNum;
	private static String[] str;
	private static boolean flag = false;
	public static ArrayList<ArrayList<Integer>> waypoint;

	public static void main(String args[]) {
		Login loginframe = new Login();
		loginframe.run();
		// loginframe.setVisible(true);
		// System.out.println(System.getProperty("user.dir"));
	}

	/** 查询桥接词 */
	/*
	public static String queryBridgeWords(String word1, String word2) {
		StringBuffer str_ = new StringBuffer();
		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();

		// 检查 word1 和 word2 是否存在于图中
		boolean word1Exists = false;
		boolean word2Exists = false;

		for (int i = 0; i < vertexNum; i++) {
			if (head[i].Name.equals(word1)) {
				word1Exists = true;
			}
			if (head[i].Name.equals(word2)) {
				word2Exists = true;
			}
			if (word1Exists && word2Exists) {
				break;
			}
		}

		// 如果任意一个单词不存在，直接返回 -1
		if (!word1Exists || !word2Exists) {
			return "-1";
		}

		BFS bfsSearch = new BFS(head, vertexNum);
		str = bfsSearch.broadFirstSearch(word1, word2);

		if (str[0].equals("-1")) {
			str_.append("-1");
		} else if (str[0].equals("0")) {
			str_.append("0");
		} else {
			for (int i = 0; i < str.length && str[i] != null; i++) {
				str_.append(str[i] + " ");
			}
		}
		return str_.toString();
	}
	*/

	// 统一入口，处理参数个数等有效性检查
	public static String queryBridgeWords(String... words) {
		// 检查参数个数
		if (words == null || words.length < 2) {
			return "-3"; // 少于2个单词
		} else if (words.length > 2) {
			return "-4"; // 多于2个单词
		}

		String word1 = words[0];
		String word2 = words[1];

		// 检查是否为 null 或空字符串
		if (word1 == null || word2 == null || word1.trim().isEmpty() || word2.trim().isEmpty()) {
			return "-3"; // 少于2个有效单词
		}

		// 检查是否包含非字母字符
		if (!word1.matches("[a-zA-Z]+") || !word2.matches("[a-zA-Z]+")) {
			return "-5"; // 有非字母字符
		}

		return queryBridgeWords(word1, word2); // 调用实际处理逻辑
	}

	// 实际逻辑：处理2个合法单词
	private static String queryBridgeWords(String word1, String word2) {
		StringBuffer str_ = new StringBuffer();

		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();

		// 检查 word1 和 word2 是否存在于图中
		boolean word1Exists = false;
		boolean word2Exists = false;

		for (int i = 0; i < vertexNum; i++) {
			if (head[i].Name.equals(word1)) {
				word1Exists = true;
			}
			if (head[i].Name.equals(word2)) {
				word2Exists = true;
			}
			if (word1Exists && word2Exists) {
				break;
			}
		}

		// 如果任意一个单词不存在，返回 -1
		if (!word1Exists || !word2Exists) {
			return "-1"; // 单词不存在
		}

		BFS bfsSearch = new BFS(head, vertexNum);
		str = bfsSearch.broadFirstSearch(word1, word2);

		if (str[0].equals("-1")) {
			str_.append("-1"); // 无桥接词
		} else if (str[0].equals("0")) {
			str_.append("0"); // 自环
		} else {
			for (int i = 0; i < str.length && str[i] != null; i++) {
				str_.append(str[i] + " ");
			}
		}
		return str_.toString().trim();
	}


	/** 根据bridge word 生成新文本 */
	public static String generateNewText(String inputText) {
		String[] str1 = inputText.split(" ");
		// boolean Flag = false;
		/*
		 * if(str1[0].charAt(0) >= 'A'&& str1[0].charAt(0) <= 'Z'){ Flag = true;
		 * }
		 */
		String[] str2 = new String[2 * str1.length];
		int j = 0;
		flag = true;
		int i;
		for (i = 0; i < str1.length - 1; i++) {
			queryBridgeWords(str1[i].toLowerCase(), str1[i + 1].toLowerCase());
			if (str[0].equals("0") || str[0].equals("-1")) {
				str2[j++] = str1[i];
				// str2[j++] = str1[i+1];
			} else {
				str2[j++] = str1[i];
				str2[j++] = str[0];
			}
		}
		flag = false;
		str2[j] = str1[i];
		StringBuffer string = new StringBuffer();
		for (i = 0; i < str2.length && str2[i] != null; i++) {
			string.append(str2[i]);
			string.append(" ");
		}
		return string.toString();
	}

	/** 计算两个单词之间的最短路径 */
	public static String calcShortestPath(String word1, String word2) {
		StringBuffer ans = new StringBuffer();
		int w1 = -1;
		int w2 = -1;
		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();
		for (int i = 0; i < vertexNum; i++) {
			if (word1.equals(head[i].Name))
				w1 = i;
			if (word2.equals(head[i].Name))
				w2 = i;
		}
		if (w1 == -1 || w2 == -1)
			return "文本中不存在输入的某个词";
		Dij D = new Dij(vertexNum);
		D.dijkstra(w1, head);
		int INF = 9999;
		if (D.dist[w2].d == INF) {
			ans.append("不可达");
			return ans.toString();
		}
		waypoint = new ArrayList<ArrayList<Integer>>();
		waypoint = D.findpath(w1, w2);
		for (int i = 0; i < waypoint.size(); i++) {
			for (int j = 0; j < waypoint.get(i).size() - 1; j++) {
				ans.append(head[waypoint.get(i).get(j)].Name);
				ans.append("->");
			}
			ans.append(head[waypoint.get(i).get(waypoint.get(i).size() - 1)].Name);
			ans.append("  " + "(length: " + D.dist[w2].d + ")\n");
		}
		return ans.toString();
	}

	/** 计算一个单词到其他之间的最短路径 */
	public static String calcShortestPath_Oneword(String word) {
		int wd = -1;
		StringBuffer ans = new StringBuffer();
		word = word.toLowerCase();
		for (int i = 0; i < vertexNum; i++) {
			if (word.equals(head[i].Name))
				wd = i;
		}
		if (wd == -1) {
			return "文本中不存在输入的词";
		}
		Dij D = new Dij(vertexNum);
		D.dijkstra(wd, head);
		for (int i = 0; i < vertexNum; i++) {
			if (i == wd) {
				continue;
			}
			ans.append(word + "==>" + head[i].Name + ":\n");
			ans.append(calcShortestPath(word, head[i].Name));
			ans.append("\n");
		}
		return ans.toString();
	}

	/* 计算PR值 */
	public static Double calPageRank(String word) {
		int wd = -1;
		word = word.toLowerCase();

		for (int i = 0; i < vertexNum; i++) {
			if (word.equals(head[i].Name)) {
				wd = i;
				break;
			}
		}

		if (wd == -1) {
			return -1.0;
		}

		double d = 0.85; // 阻尼系数
		double threshold = 1e-6; // 收敛阈值
		int maxIter = 200; // 最大迭代次数

		double[] newPR = new double[vertexNum];
		double[] oldPR = new double[vertexNum];

		// 初始化每个节点的 PageRank 值
		for (int i = 0; i < vertexNum; i++) {
			oldPR[i] = 1.0 / vertexNum;
		}

		// 开始迭代
		for (int iter = 0; iter < maxIter; iter++) {
			// 重置新值
			for (int i = 0; i < vertexNum; i++) {
				newPR[i] = (1 - d) / vertexNum;
			}

			double zeroOutDegreePR = 0.0;

			// 计算新的 PageRank 值
			for (int i = 0; i < vertexNum; i++) {
				Edge edge = head[i].adjacent;

				// 统计当前节点出度
				int outDegree = 0;
				Edge temp = edge;
				while (temp != null) {
					outDegree++;
					temp = temp.link;
				}

				if (outDegree == 0) {
					zeroOutDegreePR += oldPR[i];
				} else {
					// 将当前节点的 PR 值传播给它指向的每一个节点
					while (edge != null) {
						newPR[edge.verAdj] += d * (oldPR[i] / outDegree);
						edge = edge.link;
					}
				}
			}

			// 处理出度为 0 的节点的 PR 值
			for (int i = 0; i < vertexNum; i++) {
				newPR[i] += d * (zeroOutDegreePR / vertexNum);
			}

			// 检查是否已经收敛
			double diff = 0.0;
			for (int i = 0; i < vertexNum; i++) {
				diff += Math.abs(newPR[i] - oldPR[i]);
				oldPR[i] = newPR[i]; // 更新旧值用于下一轮迭代
			}

			if (diff < threshold) {
				break;
			}
		}

		// 最终将计算结果写回每个节点的 pageRank
		for (int i = 0; i < vertexNum; i++) {
			head[i].pageRank = oldPR[i];
		}

		// 返回指定节点的 PageRank
		return head[wd].pageRank;
	}


	/** 随机游走 */
	/*
	public static String randomWalk() {
		Random rand = new Random();
		int r = rand.nextInt(vertexNum);
		StringBuffer string = new StringBuffer();
		int[][] isVisited = new int[vertexNum][vertexNum];
		for (int i = 0; i < vertexNum; i++) {
			for (int j = 0; j < vertexNum; j++) {
				isVisited[i][j] = 0;
			}
		}
		try {
			string.append(head[r].Name);
			string.append(" ");
			Edge p = head[r].adjacent;
			while (p != null) {
				// System.out.print(head[p.verAdj].Name+" ");
				string.append(head[p.verAdj].Name);
				string.append(" ");
				if (isVisited[r][p.verAdj] == 0)
				{
					isVisited[r][p.verAdj] = 1;
					r = p.verAdj;
					p = head[p.verAdj].adjacent;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			string.append("0");
		}
		return string.toString();
	}
	*/

	public static String randomWalk() {
		Random rand = new Random();
		int r = rand.nextInt(vertexNum);
		StringBuffer string = new StringBuffer();
		int[][] isVisited = new int[vertexNum][vertexNum];
		for (int i = 0; i < vertexNum; i++) {
			for (int j = 0; j < vertexNum; j++) {
				isVisited[i][j] = 0;
			}
		}
		try {
			string.append(head[r].Name);
			string.append(" ");

			while (true) {
				Edge p = head[r].adjacent;
				ArrayList<Edge> unvisitedEdges = new ArrayList<>();
				// 找出所有未访问过的邻接边
				while (p != null) {
					if (isVisited[r][p.verAdj] == 0) {
						unvisitedEdges.add(p);
					}
					p = p.link;
				}

				if (unvisitedEdges.isEmpty()) {
					// 没有未访问过的邻接边，停止随机游走
					break;
				}

				// 随机选择一条未访问过的邻接边
				int randomIndex = rand.nextInt(unvisitedEdges.size());
				Edge chosenEdge = unvisitedEdges.get(randomIndex);

				string.append(head[chosenEdge.verAdj].Name);
				string.append(" ");

				// 标记该路径已被访问
				isVisited[r][chosenEdge.verAdj] = 1;
				// 更新当前节点为邻接边指向的节点
				r = chosenEdge.verAdj;
			}
		} catch (Exception e) {
			string.append("0");
		}
		return string.toString();
	}

}