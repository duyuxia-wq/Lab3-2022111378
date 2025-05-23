// 黑盒测试 等价类和边界值方法
// 条件 两个输入 输入单词只有字母
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Lab3b_qBWTest {

    Graph graph;

    @BeforeEach
    void setUp() {
        // 初始化图结构
        ReadFile File = new ReadFile();
        File.transformFile("test_text/graph_bt.txt");
        graph = new Graph("result/data.txt");
        graph.buildGraph();

        // 将图的顶点表和数量传递给 Lab1_main，用于测试其静态方法
        Lab1_main.head = graph.head;
        Lab1_main.vertexNum = graph.buildGraph(); // 假设vertexNum是图的有效顶点数
    }

    // 测试正常情况：输入两个存在的字母单词
    @Test
    void testValidBridgeWords() {
        String result = Lab1_main.queryBridgeWords("The", "carefully");
        // 预期返回值可能是桥接词、0 或 -1，根据图内容确定
        assertNotNull(result);
        assertFalse(result.equals("-3") || result.equals("-4") || result.equals("-5"));
    }

    // 测试无效：少于两个单词
    @Test
    void testLessThanTwoWords() {
        String result = Lab1_main.queryBridgeWords("the");
        assertEquals("-3", result);
    }

    // 测试无效：多于两个单词
    @Test
    void testMoreThanTwoWords() {
        String result = Lab1_main.queryBridgeWords("and", "wrote", "the");
        assertEquals("-4", result);
    }

    // 测试无效：包含非字母字符
    @Test
    void testInvalidCharacters() {
        String result = Lab1_main.queryBridgeWords("and1", "the");
        assertEquals("-5", result);
    }
}
