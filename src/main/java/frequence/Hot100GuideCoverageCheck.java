package frequence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hot 100逐题详解覆盖检查。
 *
 * <p>验证每个Q编号Java题目恰好在一份package专题文档中完整讲解，并检查文档相对链接。
 */
public class Hot100GuideCoverageCheck {

    private static final Path ROOT = Paths.get("src/main/java/frequence");
    private static final Pattern JAVA_QUESTION = Pattern.compile("^Q(\\d+).*\\.java$");
    private static final Pattern GUIDE_HEADING = Pattern.compile("(?m)^## Q(\\d+)\\s");
    private static final Pattern MARKDOWN_LINK = Pattern.compile("\\[[^]]*]\\(([^)#]+)(?:#[^)]*)?\\)");
    private static final int EXPECTED_QUESTION_COUNT = 119;
    private static final int EXPECTED_HOT_100_COUNT = 100;
    private static final int EXPECTED_EXTENSION_COUNT = 19;

    public static void main(String[] args) throws Exception {
        Set<Integer> expected = scanJavaQuestionNumbers();
        Map<Integer, List<Path>> documented = scanGuideQuestionNumbers();

        List<Integer> missing = new ArrayList<>();
        List<Integer> duplicated = new ArrayList<>();
        for (Integer number : expected) {
            List<Path> paths = documented.get(number);
            if (paths == null) {
                missing.add(number);
            } else if (paths.size() != 1) {
                duplicated.add(number);
            }
        }

        Set<Integer> unknown = new HashSet<>(documented.keySet());
        unknown.removeAll(expected);
        check(missing.isEmpty(), "Missing question guides: " + missing);
        check(duplicated.isEmpty(), "Questions explained more than once: " + duplicated);
        check(unknown.isEmpty(), "Guide headings without Java question files: " + unknown);
        check(expected.size() == EXPECTED_QUESTION_COUNT,
                "Expected 119 unique questions, actual=" + expected.size());
        checkStructure();
        checkImportantTopicIndex();
        checkLinks();

        System.out.println("Hot 100 guide coverage check passed: " + expected.size()
                + " unique questions, no duplicate explanations, all links valid.");
    }

    private static Set<Integer> scanJavaQuestionNumbers() throws IOException {
        Set<Integer> ans = new HashSet<>();
        Files.walk(ROOT).filter(Files::isRegularFile).forEach(path -> {
            Matcher matcher = JAVA_QUESTION.matcher(path.getFileName().toString());
            if (matcher.matches()) {
                ans.add(Integer.parseInt(matcher.group(1)));
            }
        });
        return ans;
    }

    private static Map<Integer, List<Path>> scanGuideQuestionNumbers() throws IOException {
        Map<Integer, List<Path>> ans = new HashMap<>();
        Files.walk(ROOT)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().contains("逐题详解.md"))
                .filter(path -> !path.getFileName().toString().equals("Hot100逐题详解索引.md"))
                .forEach(path -> {
                    String source = readUnchecked(path);
                    Matcher matcher = GUIDE_HEADING.matcher(source);
                    while (matcher.find()) {
                        int number = Integer.parseInt(matcher.group(1));
                        ans.computeIfAbsent(number, ignored -> new ArrayList<>()).add(path);
                    }
                });
        return ans;
    }

    private static void checkLinks() throws IOException {
        List<String> invalid = new ArrayList<>();
        Files.walk(ROOT)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith("逐题详解.md")
                        || path.getFileName().toString().equals("Hot100逐题详解索引.md"))
                .forEach(path -> {
                    Matcher matcher = MARKDOWN_LINK.matcher(readUnchecked(path));
                    while (matcher.find()) {
                        String target = matcher.group(1);
                        if (target.startsWith("http://") || target.startsWith("https://")
                                || target.startsWith("mailto:")) {
                            continue;
                        }
                        Path resolved = path.getParent().resolve(target).normalize();
                        if (!Files.exists(resolved)) {
                            invalid.add(path + " -> " + target);
                        }
                    }
                });
        check(invalid.isEmpty(), "Invalid Markdown links: " + invalid);
    }

    private static void checkStructure() throws IOException {
        StringBuilder all = new StringBuilder();
        Files.walk(ROOT)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().contains("逐题详解.md"))
                .filter(path -> !path.getFileName().toString().equals("Hot100逐题详解索引.md"))
                .forEach(path -> all.append(readUnchecked(path)).append('\n'));

        check(count(all, "### 核心思想与题目要求") == EXPECTED_QUESTION_COUNT,
                "Every question must have a core-idea section");
        check(count(all, "### 算法流程与正确性依据") == EXPECTED_QUESTION_COUNT,
                "Every question must have an algorithm section");
        check(count(all, "### 标准 Java 代码") == EXPECTED_QUESTION_COUNT,
                "Every question must have a Java-code section");
        check(count(all, "### 易错点") == EXPECTED_QUESTION_COUNT,
                "Every question must have an error section");
        check(count(all, "### 复杂度与面试记忆") == EXPECTED_QUESTION_COUNT,
                "Every question must have a complexity section");
        check(count(all, "### 项目文件") == EXPECTED_QUESTION_COUNT,
                "Every question must link to project files");
        check(countMatches(all, Pattern.compile("(?m)^## Q\\d+ .*【Hot 100】$"))
                        == EXPECTED_HOT_100_COUNT,
                "Expected 100 Hot 100 question headings");
        check(countMatches(all, Pattern.compile("(?m)^## Q\\d+ .*【扩展】$"))
                        == EXPECTED_EXTENSION_COUNT,
                "Expected 19 extension question headings");
    }

    private static void checkImportantTopicIndex() throws IOException {
        String index = read(ROOT.resolve("Hot100逐题详解索引.md"));
        check(index.contains("[K 路归并专题](array/kWayMerge/note.md)"),
                "Main index must link the K-way merge guide");
        check(index.contains("[数组版实现](array/kWayMerge/MergeKSortedArrays.java)"),
                "Main index must link the array K-way merge implementation");
        check(index.contains("[数组版对数器](array/kWayMerge/MergeKSortedArraysComparator.java)"),
                "Main index must link the array K-way merge comparator");
        check(index.contains("[链表版 Q23](LinkedList/Q23_MergeKSortedLists.java)"),
                "Main index must link Q23 as the linked-list K-way merge implementation");
    }

    private static int count(CharSequence source, String target) {
        int ans = 0;
        int from = 0;
        while ((from = source.toString().indexOf(target, from)) >= 0) {
            ans++;
            from += target.length();
        }
        return ans;
    }

    private static int countMatches(CharSequence source, Pattern pattern) {
        int ans = 0;
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            ans++;
        }
        return ans;
    }

    private static String readUnchecked(Path path) {
        try {
            return read(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
