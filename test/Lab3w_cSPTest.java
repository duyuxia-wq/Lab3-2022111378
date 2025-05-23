import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Lab3w_cSPTest {

    Graph graph;

    @BeforeEach
    void setUp() {
        // 初始化图结构
        ReadFile File = new ReadFile();
        File.transformFile("test_text/graph_wt.txt");
        graph = new Graph("result/data.txt");
        graph.buildGraph();

        // 将图的顶点表和数量传递给 Lab1_main，用于测试其静态方法
        Lab1_main.head = graph.head;
        Lab1_main.vertexNum = graph.buildGraph(); // 假设vertexNum是图的有效顶点数
    }

    // 基本路径1: 正常路径，两单词直接相连
    @Test
    void testPath1_DirectConnection() {
        // 准备图数据，确保顶点A和B存在且直接相连
        // 路径: A -> B
        String result = Lab1_main.calcShortestPath("be", "able");
        assertEquals("be->able  (length: 1)\n", result);
    }

    // 基本路径2: 正常路径，两单词通过多个中间节点相连
    @Test
    void testPath2_MultipleIntermediateNodes() {
        String result = Lab1_main.calcShortestPath("able", "accept");
        assertEquals("able->to->accept  (length: 2)\n", result);
    }

    // 基本路径3: 正常路径，两顶点通过多条路径相连
    @Test
    void testPath3_DifferentPathsWithCycle() {
        String result = Lab1_main.calcShortestPath("be", "to");
        assertEquals("be->able->to  (length: 2)\n" +
                "be->happy->to  (length: 2)\n" +
                "be->strong->to  (length: 2)\n", result);
    }

    // 基本路径4: 两单词不可达
    @Test
    void testPath4_DisconnectedComponents() {
        String result = Lab1_main.calcShortestPath("accept", "help");
        assertEquals("不可达", result);
    }

    // 基本路径5: 第2个单词不存在（且第1个单词存在）
    @Test
    void testPath5_SecondVertexMissing() {
        String result = Lab1_main.calcShortestPath("be", "what");
        assertEquals("文本中不存在输入的某个词", result);
    }

    // 基本路径6: 第1个单词不存在（且第2个单词存在）
    @Test
    void testPath6_FirstVertexMissing() {
        String result = Lab1_main.calcShortestPath("what", "be");
        assertEquals("文本中不存在输入的某个词", result);
    }

    // 基本路径7: 两个单词都不存在
    @Test
    void testPath7_BothVerticesMissing() {
        String result = Lab1_main.calcShortestPath("how", "where");
        assertEquals("文本中不存在输入的某个词", result);
    }

    // 基本路径8: 第1个单词不存在（且第2个单词存在）
    @Test
    void testPath8_FirstVertexMissing() {
        String result = Lab1_main.calcShortestPath("when", "be");
        assertEquals("文本中不存在输入的某个词", result);
    }

    // 基本路径9: 两单词不可达
    @Test
    void testPath9_DisconnectedComponents() {
        String result = Lab1_main.calcShortestPath("accept", "healthy");
        assertEquals("不可达", result);
    }
}