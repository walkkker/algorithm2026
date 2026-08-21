package frequence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据frequence现有Java源码生成按package分类的逐题详解基础文档。
 *
 * <p>生成内容直接复用项目中已经长期维护的Javadoc、错误注释和AC实现，保证文档和源码同步。
 * 本类只负责文档构建，不参与算法运行。
 */
public class Hot100GuideGenerator {

    private static final Path ROOT = Paths.get("src/main/java/frequence");
    private static final Pattern QUESTION_FILE = Pattern.compile("^Q(\\d+).*\\.java$");

    private static final Set<Integer> HOT_100 = new HashSet<>(Arrays.asList(
            1, 2, 3, 4, 5, 11, 15, 17, 19, 20, 21, 22, 23, 24, 25, 31, 32, 33, 34, 35,
            39, 41, 42, 45, 46, 48, 49, 51, 53, 54, 55, 56, 62, 64, 70, 72, 73, 74, 75, 76,
            78, 79, 84, 94, 98, 101, 102, 104, 105, 108, 114, 118, 121, 124, 128, 131, 136,
            138, 139, 141, 142, 146, 148, 152, 153, 155, 160, 169, 189, 198, 199, 200, 206,
            207, 208, 215, 226, 230, 234, 236, 238, 239, 240, 279, 283, 287, 295, 300, 322,
            347, 394, 416, 437, 438, 543, 560, 739, 763, 994, 1143
    ));

    private static final Map<String, Topic> TOPICS = new LinkedHashMap<>();
    private static final Map<Integer, String> TITLE_OVERRIDES = new HashMap<>();
    private static final Map<Integer, String> DESCRIPTION_OVERRIDES = new HashMap<>();

    static {
        topic("", "哈希", "哈希表把需要反复查找的信息预先建立索引，用额外空间换取平均O(1)查询。",
                "先确定查询关系，再决定先查询后登记还是先登记后查询；涉及连续性时只从序列起点扩展。");
        topic("双指针", "双指针", "两个指针共同缩小搜索空间，关键是证明每次移动排除的区域不可能包含答案。",
                "排序或单调性提供移动方向；固定一侧、移动另一侧；收集答案后处理重复值。");
        topic("双指针/SlidingWindow", "滑动窗口", "右指针扩张获得信息，左指针在约束被破坏或答案已满足时收缩。",
                "先加right，再按条件移动left，最后在窗口合法时更新答案；明确窗口是[left,right]。");
        topic("双指针/同向读写双指针_原地稳定压缩", "原地稳定压缩", "read扫描输入，write维护有效结果边界；输出扩张时改为反向写入。",
                "始终定义[0,write)的含义；覆盖前证明write不会越过尚未读取的位置。");
        topic("array", "数组", "数组题重点是前缀信息、区间合并、原地映射和从边界反向处理。",
                "先写结果或状态的精确定义，再决定排序、前缀和、原地哈希或反向写入。");
        topic("matrix", "矩阵", "矩阵题需要同时管理行列边界，并把二维操作拆成可验证的一维过程。",
                "先明确行列范围；原地标记要保护第一行第一列；分圈必须处理单行和单列退化情况。");
        topic("LinkedList", "链表", "链表算法的数组区间对应断开的子链表，所有操作都要明确新头、新尾和后继。",
                "先保存next，再改指针；使用dummy统一头节点变化；递归返回值必须说明头尾语义。");
        topic("cache", "缓存设计", "哈希表负责O(1)定位节点，双向链表负责O(1)移动和淘汰。",
                "LRU维护访问顺序；LFU再增加频次桶和minFreq，同频次内部仍按LRU淘汰。");
        topic("BinaryTree", "二叉树", "树题先定义递归函数对一棵子树返回什么，再组合左右子树信息。",
                "base case必须符合返回值定义；递归前保存仍需访问的指针；全局答案与向上返回值不能混淆。");
        topic("Graph", "图论", "图论题先明确节点、边和访问状态，再选择DFS、BFS、拓扑序或Trie。",
                "连通块用DFS/BFS；最短时间扩散用分层BFS；依赖关系用入度表和零入度队列。");
        topic("Backtracking", "回溯", "回溯就是遍历选择树：每层枚举选择，递归后恢复现场。",
                "明确路径、选择列表、终止条件；收集可变容器时保存快照；base case收集后立即return。");
        topic("BinarySearch", "二分查找", "二分的本质是依据确定的判定条件排除一半搜索空间。",
                "先定义搜索区间和边界语义；每个分支必须说明排除了什么；候选mid不能错误丢弃。");
        topic("Stack", "栈", "栈保存尚未匹配、尚未结算或需要等待右侧信息的元素。",
                "入栈前明确栈内单调性或语法层级；弹栈时确定当前元素、左边界和右边界各自角色。");
        topic("Heap", "堆", "堆只维护当前最需要被访问或淘汰的极值，适合Top K和动态中位数。",
                "Top K用大小为K的门槛堆；中位数用左大根堆和右小根堆并维护规模差不超过1。");
        topic("Greedy", "贪心", "贪心每一步做局部最优选择，必须证明该选择不会损失全局最优解。",
                "先写可达范围或边界不变量；不要只凭直觉选最大值，要说明交换论证或分层含义。");
        topic("dp", "动态规划", "动态规划把原问题拆成可复用子问题，状态定义决定转移、初始化和遍历顺序。",
                "先写dp含义，再列最后一步来源；空间优化必须保证读取的仍是上一阶段状态。");
        topic("dp/multidimensional", "多维动态规划", "多维DP的维度分别描述两个前缀、棋盘坐标或区间边界。",
                "i、j是长度还是下标必须先确定；根据依赖方向安排遍历顺序；空前缀通常需要额外一行一列。");
        topic("dp/stock", "股票状态机", "股票DP按每天结束时的业务状态建模，买卖行为是状态之间的转移边。",
                "cash/hold是基础状态；次数、冷冻期和手续费分别增加维度、状态或边权。");
        topic("permutation", "排列", "排列题围绕字典序直接后继、选择树和阶乘分块展开。",
                "生成全部排列用回溯；找下一个排列处理最长降序后缀；第k个排列使用阶乘数制。");
        topic("skill", "技巧", "技巧题利用输入值域或数学性质，把数组本身转换为计数器、分区或函数图。",
                "先识别题目额外保证，再选择位运算、摩尔投票、原地哈希、三向分区或Floyd判环。");
        topic("substringandsubsequence", "子串与子序列", "子串要求连续，子序列只保持相对顺序；二者决定滑动窗口、前缀和或DP。",
                "先判断是否连续；单次子序列匹配用双指针，多次查询可预处理位置，多序列最优问题使用DP。");

        title(2, "两数相加");
        title(21, "合并两个有序链表");
        title(24, "两两交换链表中的节点");
        title(42, "接雨水");
        title(48, "旋转图像");
        title(53, "最大子数组和");
        title(54, "螺旋矩阵");
        title(56, "合并区间");
        title(73, "矩阵置零");
        title(76, "最小覆盖子串");
        title(141, "环形链表");
        title(142, "环形链表II");
        title(160, "相交链表");
        title(206, "反转链表");
        title(234, "回文链表");
        title(238, "除自身以外数组的乘积");
        title(438, "找到字符串中所有字母异位词");

        describe(206,
                "反转链表的核心是让每个节点的next从指向后继改为指向前驱。\n\n"
                        + "1. 初始化`pre = null`、`cur = head`。\n"
                        + "2. 修改`cur.next`之前，必须先用`next`保存原后继。\n"
                        + "3. 执行`cur.next = pre`完成当前节点反转，再同步推进`pre`和`cur`。\n"
                        + "4. 循环结束时`cur == null`，`pre`就是反转后的新头节点。\n\n"
                        + "循环不变量：`pre`及其左侧是已经完成反转的链表，`cur`是尚未处理部分的头节点。"
                        + "每轮只把`cur`从未处理区移动到已处理区，因此不会丢失或重复节点。\n\n"
                        + "最致命的错误是先执行`cur.next = pre`再读取原来的`cur.next`，这会丢失后续链表。"
                        + "时间复杂度为`O(N)`，额外空间复杂度为`O(1)`。");
        describe(2,
                "两数相加把逆序链表视为从低位到高位排列的十进制数字，可以像手算加法一样同步遍历。\n\n"
                        + "每轮计算`sum = x + y + carry`，当前位为`sum % 10`，新进位为`sum / 10`。"
                        + "两个链表长度可以不同，因此公共部分结束后还要处理较长链表；全部节点处理完后若`carry != 0`，"
                        + "必须追加一个新节点。dummy节点用于统一结果链表头部的创建。\n\n"
                        + "循环不变量：结果链表已经保存所有已消费低位的正确结果，`carry`只表示向当前未处理位的进位。"
                        + "最常见错误是在循环体完成计算后忘记推进输入链表或结果`tail`，从而导致死循环或覆盖结果节点。"
                        + "时间复杂度为`O(max(M,N))`，额外结果空间为`O(max(M,N))`。");
        describe(21,
                "合并两个有序链表与归并排序的merge阶段相同：比较两个当前头节点，把较小节点接到结果链表尾部。\n\n"
                        + "使用dummy和tail维护结果链表；每接入一个节点，都要推进被选中的输入指针以及`tail`。"
                        + "当其中一个链表为空时，另一个链表整体已经有序，可以一次连接到`tail.next`。\n\n"
                        + "循环不变量：`dummy.next`到`tail`已经有序，并且其中包含两个输入链表所有已消费节点；"
                        + "两个当前指针分别指向各自未消费部分的最小节点。时间复杂度为`O(M+N)`，额外空间为`O(1)`。"
                        + "致命错误是只移动输入指针或只移动tail，两个动作缺一都会破坏链表结构。");
        describe(22,
                "括号生成不是先生成全部字符串再验证，而是在回溯过程中只进入仍可能形成合法答案的分支。\n\n"
                        + "状态由当前位置`index`、已使用左括号数`left`和右括号数`right`组成。"
                        + "任意前缀必须满足`right <= left`，同时`left <= n`、`right <= n`。"
                        + "当`left < n`时可以放左括号；当`right < left`时可以放右括号；长度达到`2*n`时收集答案并立即返回。\n\n"
                        + "由于路径长度固定，使用`char[]`按下标覆盖即可，不需要像`List`路径那样remove恢复现场。"
                        + "算法只生成合法前缀，输出规模由第n个卡特兰数决定；除结果外递归栈与路径空间为`O(N)`。");
        describe(48,
                "旋转图像可以按同心环原地处理。对于每一层，枚举上边除最后一个顶点之外的位置，"
                        + "同时完成左到上、下到左、右到下、上到右的四点循环交换。\n\n"
                        + "一层边长为`len`时只需要处理`len - 1`组四元位置；如果处理`len`组，四个角会重复移动。"
                        + "处理完当前环后，左上边界向内收缩、右下边界向内收缩。题目保证是`N*N`矩阵，"
                        + "因此不需要像螺旋矩阵那样单独处理矩形的单行或单列。\n\n"
                        + "每个元素恰好移动一次，时间复杂度为`O(N^2)`，额外空间为`O(1)`。"
                        + "也可以使用先转置再逐行反转的等价写法，面试时通常更短。");
        describe(94,
                "中序遍历的访问顺序固定为左子树、当前节点、右子树。递归写法直接按照这个语义组织代码。\n\n"
                        + "非递归写法使用栈模拟递归调用：只要`cur != null`就持续压入左链；走到空节点后弹栈并访问，"
                        + "随后转向该节点的右子树。循环条件必须是`cur != null || !stack.isEmpty()`，否则可能遗漏栈中节点。\n\n"
                        + "栈内保存的是左子树已经进入、但自身尚未访问的节点。每个节点入栈和出栈各一次，"
                        + "时间复杂度为`O(N)`，递归或显式栈空间复杂度为`O(H)`。二叉搜索树的中序遍历结果有序。"
                        + "如要求严格`O(1)`额外空间，可进一步使用Morris遍历。");
        describe(148,
                "排序链表优先使用归并排序，因为链表可以通过改指针完成merge，不需要数组归并的辅助数组。\n\n"
                        + "快慢指针找到中点后，必须执行`slow.next = null`断开链表；分别递归排序左右子链表，"
                        + "最后按Q21的方式合并两个有序链表。递归base case必须包含单节点`head.next == null`，"
                        + "否则链表无法继续缩小并会无限递归。\n\n"
                        + "递归函数定义为：接收一条已经断开的链表头，返回这条链表排序后的新头。"
                        + "时间复杂度为`O(N log N)`，递归栈为`O(log N)`；自底向上归并可以进一步做到`O(1)`额外空间。"
                        + "数组区间在链表中的对应物就是断开的子链表。");
        describe(160,
                "两个相交的单链表从相交节点开始共享同一段后缀，因此先消除长度差，再同步前进即可找到第一个公共节点。\n\n"
                        + "先分别遍历得到长度差`lenA - lenB`；长度较长的指针先走绝对长度差步，随后两个指针同步移动，"
                        + "第一次满足引用相同`cur1 == cur2`的位置就是交点。比较的是节点引用，不能比较节点值。\n\n"
                        + "第一次统计长度会把两个工作指针移动到null，因此对齐前必须重新从`headA`和`headB`赋值；"
                        + "这是该写法最容易遗漏的步骤。时间复杂度为`O(M+N)`，额外空间为`O(1)`。"
                        + "另一种等价写法是两个指针到达末尾后切换到另一条链表头，使它们各走`M+N`步。");
        describe(226,
                "翻转二叉树要求对每个节点交换左右子树，可以使用递归后序处理，也可以用BFS逐层交换。\n\n"
                        + "递归函数定义为：翻转以`cur`为根的整棵子树并返回其根节点。处理当前节点前先保存原`left`和"
                        + "原`right`引用，再令`cur.left = process(originalRight)`、`cur.right = process(originalLeft)`。\n\n"
                        + "不能先覆盖`cur.left`后再把新的`cur.left`当成原左子树使用，否则原左子树入口已经丢失。"
                        + "递归结束后，每个节点的两个子树均已翻转且位置互换，因此整棵树正确翻转。"
                        + "时间复杂度为`O(N)`，递归栈空间为`O(H)`。");
    }

    public static void main(String[] args) throws Exception {
        Map<Integer, List<Path>> byNumber = scanQuestionFiles();
        Map<Integer, Path> primary = choosePrimaryFiles(byNumber);
        Map<String, List<Question>> grouped = groupQuestions(primary, byNumber);
        writeTopicGuides(grouped);
        writeIndex(grouped);
        System.out.println("Generated " + primary.size() + " unique question guides in "
                + grouped.size() + " packages.");
    }

    private static Map<Integer, List<Path>> scanQuestionFiles() throws IOException {
        Map<Integer, List<Path>> ans = new HashMap<>();
        Files.walk(ROOT).filter(Files::isRegularFile).forEach(path -> {
            Matcher matcher = QUESTION_FILE.matcher(path.getFileName().toString());
            if (matcher.matches()) {
                int number = Integer.parseInt(matcher.group(1));
                ans.computeIfAbsent(number, ignored -> new ArrayList<>()).add(path);
            }
        });
        return ans;
    }

    private static Map<Integer, Path> choosePrimaryFiles(Map<Integer, List<Path>> byNumber) {
        Map<Integer, Path> ans = new HashMap<>();
        for (Map.Entry<Integer, List<Path>> entry : byNumber.entrySet()) {
            List<Path> paths = entry.getValue();
            Collections.sort(paths, Comparator.comparing(Path::toString));
            Path selected = paths.get(0);
            for (Path path : paths) {
                String value = path.toString();
                if (!value.contains("Reference") && !value.contains("Wrong")
                        && !value.contains("Template") && !value.contains("ByLexicographicOrder")) {
                    selected = path;
                    break;
                }
            }
            if (entry.getKey() == 2) {
                selected = ROOT.resolve("LinkedList/Q2.java");
            } else if (entry.getKey() == 46) {
                selected = ROOT.resolve("Backtracking/Q46_Permutations.java");
            } else if (entry.getKey() == 460) {
                selected = ROOT.resolve("cache/Q460_LFUCache.java");
            }
            ans.put(entry.getKey(), selected);
        }
        return ans;
    }

    private static Map<String, List<Question>> groupQuestions(Map<Integer, Path> primary,
                                                               Map<Integer, List<Path>> allFiles)
            throws IOException {
        Map<String, List<Question>> unordered = new HashMap<>();
        for (Map.Entry<Integer, Path> entry : primary.entrySet()) {
            Path path = entry.getValue();
            String group = normalize(ROOT.relativize(path.getParent()).toString());
            String source = read(path);
            String description = DESCRIPTION_OVERRIDES.get(entry.getKey());
            if (description == null) {
                description = cleanJavadocs(source);
            }
            Question question = new Question(entry.getKey(), title(entry.getKey(), path), path, source,
                    description, extractWarnings(source), extractComplexity(source),
                    allFiles.get(entry.getKey()));
            unordered.computeIfAbsent(group, ignored -> new ArrayList<>()).add(question);
        }
        Map<String, List<Question>> ans = new LinkedHashMap<>();
        for (String group : TOPICS.keySet()) {
            if (unordered.containsKey(group)) {
                ans.put(group, unordered.get(group));
            }
        }
        for (List<Question> questions : ans.values()) {
            Collections.sort(questions, Comparator.comparingInt(q -> q.number));
        }
        return ans;
    }

    private static void writeTopicGuides(Map<String, List<Question>> grouped) throws IOException {
        for (Map.Entry<String, List<Question>> entry : grouped.entrySet()) {
            Topic topic = TOPICS.get(entry.getKey());
            if (topic == null) {
                throw new IllegalStateException("Missing topic configuration: " + entry.getKey());
            }
            Path directory = entry.getKey().isEmpty() ? ROOT : ROOT.resolve(entry.getKey());
            Path output = directory.resolve(outputName(entry.getKey(), topic.name));
            StringBuilder md = new StringBuilder();
            md.append("# Hot 100 ").append(topic.name).append("逐题详解\n\n")
                    .append("> 本文按项目现有package整理。`【Hot 100】`是主线题，`【扩展】`是同模型扩展题。\n\n")
                    .append("## 专题核心\n\n").append(topic.core).append("\n\n")
                    .append("**统一记忆：** ").append(topic.memory).append("\n\n")
                    .append("## 题目清单\n\n");
            boolean hasHot100 = entry.getValue().stream().anyMatch(q -> HOT_100.contains(q.number));
            if (!hasHot100) {
                md.append("> 本专题当前题目均为Hot 100之外的扩展题，用于补全同类模型。\n\n");
            }
            for (Question q : entry.getValue()) {
                md.append("- Q").append(q.number).append(' ').append(q.title).append(' ')
                        .append(HOT_100.contains(q.number) ? "`【Hot 100】`" : "`【扩展】`").append("\n");
            }
            for (Question q : entry.getValue()) {
                appendQuestion(md, q, topic, output.getParent());
            }
            write(output, md.toString());
        }
    }

    private static void appendQuestion(StringBuilder md, Question q, Topic topic, Path outputDir) {
        md.append("\n---\n\n## Q").append(q.number).append(' ').append(q.title).append(' ')
                .append(HOT_100.contains(q.number) ? "【Hot 100】" : "【扩展】").append("\n\n")
                .append("### 核心思想与题目要求\n\n");
        if (q.javadoc.isEmpty()) {
            md.append(topic.core).append("本题需要先明确状态或指针区间的精确定义，再依据该定义推进算法。\n");
        } else {
            md.append(q.javadoc).append("\n");
        }
        md.append("\n### 算法流程与正确性依据\n\n")
                .append("1. 根据题意确定输入中可利用的单调性、值域、结构关系或可复用子问题。\n")
                .append("2. 使用本专题模型组织状态：").append(topic.memory).append("\n")
                .append("3. 每次更新后维持上述定义；只有在不变量仍成立时才移动指针、覆盖数据或进入下一状态。\n")
                .append("4. 最终从定义直接读取答案，而不是根据样例猜测返回位置。\n\n")
                .append("正确性的关键是：每一步只排除已经证明不可能成为答案的选择，或者完整枚举状态定义允许的所有前驱，因此不会漏解。\n\n")
                .append("### 标准 Java 代码\n\n```java\n")
                .append(q.source.trim()).append("\n```\n\n")
                .append("### 易错点\n\n");
        if (q.warnings.isEmpty()) {
            md.append("- 不要在没有写清状态、区间或返回值定义前直接套模板。\n")
                    .append("- 边界条件必须与主循环使用同一套下标语义。\n");
        } else {
            for (String warning : q.warnings) {
                md.append("- ").append(warning).append("\n");
            }
        }
        md.append("\n### 复杂度与面试记忆\n\n");
        if (q.complexity.isEmpty()) {
            md.append("复杂度需要按照代码中每个指针、节点、状态或边的实际访问次数推导；不能仅根据算法名称下结论。\n\n");
        } else {
            for (String complexity : q.complexity) {
                md.append("- ").append(complexity).append("\n");
            }
            md.append('\n');
        }
        md
                .append("> 记忆模板：").append(topic.memory).append("\n\n")
                .append("### 项目文件\n\n")
                .append("- [主实现](").append(relative(outputDir, q.path)).append(")\n");
        for (Path path : q.allFiles) {
            if (!path.equals(q.path)) {
                md.append("- [版本对比：").append(path.getFileName()).append("](")
                        .append(relative(outputDir, path)).append(")\n");
            }
        }
    }

    private static void writeIndex(Map<String, List<Question>> grouped) throws IOException {
        List<Question> all = new ArrayList<>();
        for (List<Question> value : grouped.values()) {
            all.addAll(value);
        }
        Collections.sort(all, Comparator.comparingInt(q -> q.number));
        long hotCount = all.stream().filter(q -> HOT_100.contains(q.number)).count();
        StringBuilder md = new StringBuilder("# Hot 100 与扩展题逐题详解索引\n\n")
                .append("- Hot 100主线题：").append(hotCount).append("道\n")
                .append("- 项目扩展题：").append(all.size() - hotCount).append("道\n")
                .append("- 唯一题号总数：").append(all.size()).append("道\n")
                .append("- 重要非编号专题：1个\n\n")
                .append("## 专题入口\n\n");
        for (Map.Entry<String, List<Question>> entry : grouped.entrySet()) {
            Topic topic = TOPICS.get(entry.getKey());
            String name = outputName(entry.getKey(), topic.name);
            String link = entry.getKey().isEmpty() ? name : entry.getKey() + "/" + name;
            md.append("- [").append(topic.name).append("专题](").append(link).append(")：")
                    .append(entry.getValue().size()).append("道\n");
        }
        md.append("- [K 路归并专题](array/kWayMerge/note.md)：数组版与链表版的统一归并模型\n");
        md.append("\n## 全部题目\n\n| 题号 | 类型 | 专题 | Java文件 |\n|---|---|---|---|\n");
        for (Question q : all) {
            String group = normalize(ROOT.relativize(q.path.getParent()).toString());
            Topic topic = TOPICS.get(group);
            md.append("| Q").append(q.number).append(' ').append(q.title).append(" | ")
                    .append(HOT_100.contains(q.number) ? "Hot 100" : "扩展")
                    .append(" | ").append(topic.name).append(" | [源码](")
                    .append(normalize(ROOT.relativize(q.path).toString())).append(") |\n");
        }
        md.append("\n## 重要非编号专题\n\n")
                .append("### K 路归并\n\n")
                .append("统一掌握小根堆与平衡两两归并，将同一模型迁移到数组和链表。\n\n")
                .append("- [K 路归并专题](array/kWayMerge/note.md)\n")
                .append("- [数组版实现](array/kWayMerge/MergeKSortedArrays.java)\n")
                .append("- [数组版对数器](array/kWayMerge/MergeKSortedArraysComparator.java)\n")
                .append("- [链表版 Q23](LinkedList/Q23_MergeKSortedLists.java)\n");
        write(ROOT.resolve("Hot100逐题详解索引.md"), md.toString());
    }

    private static String cleanJavadocs(String source) {
        StringBuilder ans = new StringBuilder();
        Matcher matcher = Pattern.compile("/\\*\\*(.*?)\\*/", Pattern.DOTALL).matcher(source);
        int blocks = 0;
        while (matcher.find() && blocks < 8) {
            String body = matcher.group(1);
            if (body.contains("Your ") || body.contains("Definition for a ")) {
                continue;
            }
            StringBuilder block = new StringBuilder();
            for (String line : body.split("\\R")) {
                String cleaned = line.trim().replaceFirst("^\\* ?", "")
                        .replace("<p>", "").replace("<b>", "**").replace("</b>", "**")
                        .replaceAll("\\{@code ([^}]*)}", "`$1`")
                        .replaceAll("\\{@link ([^}]*)}", "`$1`")
                        .replace("<pre>", "").replace("</pre>", "")
                        .replace("<ul>", "").replace("</ul>", "")
                        .replace("<ol>", "").replace("</ol>", "")
                        .replaceAll("<li>", "- ").replace("</li>", "");
                if (!cleaned.startsWith("@") && !cleaned.isEmpty()) {
                    block.append(cleaned).append('\n');
                }
            }
            if (block.length() > 0) {
                if (ans.length() > 0) {
                    ans.append("\n");
                }
                ans.append(block);
                blocks++;
            }
        }
        return ans.toString().trim();
    }

    private static List<String> extractComplexity(String source) {
        List<String> ans = new ArrayList<>();
        for (String line : source.split("\\R")) {
            String cleaned = line.trim().replaceFirst("^(//|/\\*+|\\*)\\s*", "")
                    .replace("*/", "").replace("{@code ", "`").replace("}", "`").trim();
            if ((cleaned.contains("时间复杂度") || cleaned.contains("空间复杂度")
                    || cleaned.matches(".*\\bO\\([^)]*\\).*"))
                    && cleaned.length() < 220 && !ans.contains(cleaned) && ans.size() < 8) {
                ans.add(cleaned);
            }
        }
        return ans;
    }

    private static List<String> extractWarnings(String source) {
        List<String> ans = new ArrayList<>();
        for (String line : source.split("\\R")) {
            String cleaned = line.trim().replaceFirst("^(//|/\\*+|\\*)\\s*", "")
                    .replace("*/", "").trim();
            if ((cleaned.contains("TODO") || cleaned.contains("错误") || cleaned.contains("注意"))
                    && cleaned.length() > 4 && ans.size() < 12) {
                ans.add(cleaned);
            }
        }
        return ans;
    }

    private static String title(int number, Path path) {
        String override = TITLE_OVERRIDES.get(number);
        if (override != null) {
            return override;
        }
        String file = path.getFileName().toString().replaceFirst("\\.java$", "");
        return file.replaceFirst("^Q\\d+_?", "").replace('_', ' ').trim();
    }

    private static String outputName(String group, String topicName) {
        if (group.isEmpty()) {
            return "哈希专题逐题详解.md";
        }
        return "Hot100" + topicName + "逐题详解.md";
    }

    private static String relative(Path from, Path to) {
        return normalize(from.relativize(to).toString());
    }

    private static String normalize(String value) {
        return value.replace('\\', '/');
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static void write(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    }

    private static void topic(String path, String name, String core, String memory) {
        TOPICS.put(path, new Topic(name, core, memory));
    }

    private static void title(int number, String title) {
        TITLE_OVERRIDES.put(number, title);
    }

    private static void describe(int number, String description) {
        DESCRIPTION_OVERRIDES.put(number, description);
    }

    private static class Topic {
        final String name;
        final String core;
        final String memory;

        Topic(String name, String core, String memory) {
            this.name = name;
            this.core = core;
            this.memory = memory;
        }
    }

    private static class Question {
        final int number;
        final String title;
        final Path path;
        final String source;
        final String javadoc;
        final List<String> warnings;
        final List<String> complexity;
        final List<Path> allFiles;

        Question(int number, String title, Path path, String source, String javadoc,
                 List<String> warnings, List<String> complexity, List<Path> allFiles) {
            this.number = number;
            this.title = title;
            this.path = path;
            this.source = source;
            this.javadoc = javadoc;
            this.warnings = warnings;
            this.complexity = complexity;
            this.allFiles = allFiles;
        }
    }
}
